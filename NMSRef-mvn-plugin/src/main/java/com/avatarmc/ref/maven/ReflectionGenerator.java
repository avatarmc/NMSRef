package com.avatarmc.ref.maven;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.util.TraceSignatureVisitor;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Primitives;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A version-independent reflection generator.
 *
 * Classes to generate mappings for must be loadable.
 */
@RequiredArgsConstructor
public class ReflectionGenerator {
    /**
     * The file path to store mapping files.
     *
     * These files can be rather large (several MB), make sure to keep it out of
     * the hands of git.
     */
    private final File mappingsDirectory;

    /**
     * The url to download the mappings from.
     */
    private final URL spigot2srgURL;
    private final URL mcpURL;

    /**
     * The rCOMMON target package
     */
    private final String targetPackage;

    /**
     * The path to the server package. (Example: net.minecraft.server.v1_9_R1).
     */
    private final String mcPackage;

    private final ClassLoader classLoader;

    private final File SOURCE_DIR;
    private final Set<String> excludes;

    // Classes to generate reflectors for
    private static final Set<String> CLASS_WHITELIST = readWhiteList();

    private static Set<String> readWhiteList() {
        try (InputStream is = ReflectionGenerator.class
                .getResourceAsStream("class-names.list")) {
            return ListReader.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    private static final class Mapping {
        private final String fromField;
        private final MCPMapping mapping;
    }

    @Data
    private static class Member {
        private final String name;
    }

    private static class MethodMember extends Member {
        private final String desc;
        private final String signature;

        public MethodMember(String name, String desc, String signature) {
            super(name);
            this.desc = desc;
            this.signature = signature;
        }
    }

    protected Class<?> loadClass(String name) throws ClassNotFoundException {
        return Class.forName(mcPackage + "." + name, false, classLoader);
    }

    public void run() throws Exception {
        Multimap<String, Mapping> mappings = readMappings();

        Map<String, Collection<Mapping>> map = mappings.asMap();
        for (Map.Entry<String, Collection<Mapping>> entry : map.entrySet()) {
            Class<?> c = loadClass(entry.getKey());

            if (!Modifier.isPublic(c.getModifiers())) {
                // Skip non public classes
                continue;
            }

            System.out.println("Generating " + entry.getKey());

            List<Member> fieldNames = readMemberOrder(c);

            Map<String, MCPMapping> mapping = entry.getValue().stream()
                    .collect(
                            Collectors.toMap(m -> m.fromField, m -> m.mapping));
            String className = "Ref" + c.getSimpleName();

            File file = new File(SOURCE_DIR, targetPackage.replace('.', '/')
                    + "/" + className + ".java");

            File dir = file.getParentFile();
            if (!dir.exists() && !dir.mkdirs()) {
                System.err.println("Failed to create parent directories");
            }

            try (FileOutputStream os = new FileOutputStream(file);
                    OutputStreamWriter out = new OutputStreamWriter(os,
                            StandardCharsets.UTF_8)) {
                doWrite(c, fieldNames, mapping, className, out);
            }
        }
    }

    private void doWrite(Class<?> c, List<Member> fieldNames,
            Map<String, MCPMapping> mapping, String className,
            OutputStreamWriter out) throws IOException, NoSuchFieldException {
        out.append("package ").append(targetPackage).append(";\n");

        out.append("\n");
        out.append("import com.avatarmc.ref.AbstractRefBase;\n");

        // Import that is not actually used to not get an unused-suppression
        // warning
        out.append("import java.lang.System;\n");
        out.append("\n");

        out.append("// @" + "generated\n");
        out.append("@javax.annotation.G" + "enerated(\"")
                .append(getClass().getName()).append("\")\n");

        // TODO: Make sure we do not import anything incorrectly
        out.append("@SuppressWarnings(\"unused\")\n");
        out.append("public class ").append(className).append(" extends ");

        Class<?> superClass = c.getSuperclass();
        while (superClass != null
                && !Modifier.isPublic(superClass.getModifiers())) {
            // Don't use protected super classes
            superClass = superClass.getSuperclass();
        }

        if (superClass != null
                && superClass.getName().startsWith("net.minecraft")) {
            // TODO: Respect the class whitelist here
            // && CLASS_WHITELIST.contains(superClass.getSimpleName())) {
            out.append("Ref").append(superClass.getSimpleName());
        } else {
            out.append("AbstractRefBase");
        }

        out.append(" {").append('\n');

        out.append("    private static final ")
                .append("java.lang.invoke.MethodHandles.Lookup lookup = ")
                .append("getLookup(").append(cleanupTypeString(c.getName()))
                .append(".class);\n");

        Map<String, Method> methodsBySignature = new HashMap<>();
        for (Method method : c.getDeclaredMethods()) {
            methodsBySignature.put(
                    method.getName() + Type.getMethodDescriptor(method),
                    method);
        }

        Set<String> found = new HashSet<>();
        for (Member fieldName : fieldNames) {
            String origName = fieldName.getName();

            MCPMapping mapped;

            if (fieldName instanceof MethodMember) {
                MethodMember methodMember = (MethodMember) fieldName;
                String key = origName + methodMember.desc;
                Method method = methodsBySignature.get(key);

                if (key.contains("SWITCH_TABLE")) {
                    continue;
                } else if (method == null) {
                    System.err.println("Missing method in reflection: " + key);
                    continue;
                } else if (method.isBridge() || method.isSynthetic()) {
                    // Do not generate reflection for generic bridge methods
                    continue;
                }
                if (findDeclaringClass(method) != c) {
                    // Skip inherited methods
                    continue;
                }

                // Remove dupes :/
                if (!found.add(key.split("\\)")[0])) {
                    continue;
                }

                // Do not generate methods to compiler generated things
                if (origName.contains("access$")) {
                    continue;
                }

                if ((mapped = mapping.get(key)) == null) {
                    // No mapping available
                    mapped = new MethodMapping(origName, null, null);
                    if (!key.contains("org/bukkit")) { // Duh
                        System.err.println(key + " not in mappings");
                    }
                }

                writeForMethod(c, out, key, mapped, method,
                        methodMember.signature);
            } else {
                if ((mapped = mapping.get(origName)) == null) {
                    // No mapping available
                    mapped = new FieldMapping(origName, null);
                }

                if (origName.contains("$VALUES")
                        || origName.contains("SWITCH_TABLE")) {
                    // TODO: Move to the other skips
                    continue;
                }

                Field field = c.getDeclaredField(origName);

                // Skip weird things
                if (c.getName().endsWith(".World")) {
                    if (origName.equals("tileEntityList")) {
                        // Paper doesn't have this?
                        continue;
                    }
                } else if (c.getName().endsWith(".Item")) {
                    if (origName.equals("j")) {
                        // PIT issues?
                        continue;
                    }
                } else if (c.getName().endsWith(".ContainerAnvil")) {
                    if (origName.equals("player") && field.getType().getName()
                            .endsWith(".PlayerInventory")) {
                        // MCP name clashes with spigot
                        continue;
                    }
                }

                writeForField(c, out, mapped, field);
            }
        }

        out.append("}\n");
    }

    private Class<?> findDeclaringClass(Method method) {
        Class<?> c = method.getDeclaringClass();
        Class<?> s = c.getSuperclass();

        while (s != null) {
            try {
                s.getDeclaredMethod(method.getName(),
                        method.getParameterTypes());

                // Has this method, update declaring class
                c = s;
            } catch (NoSuchMethodException | SecurityException e) {
                // Ignore
            }

            s = s.getSuperclass();
        }

        return c;
    }

    private void writeForMethod(Class<?> c, OutputStreamWriter out, String key,
            MCPMapping mapped, Method m,
            String signature) throws IOException {
        // If one of the parameter types is not public, we have to skip :(
        for (Class<?> param : m.getParameterTypes()) {
            if (!Modifier.isPublic(param.getModifiers())) {
                out.write("    // Skipping due to private parameter: " + m
                        + "\n");
                return;
            }
        }

        boolean isStatic = Modifier.isStatic(m.getModifiers());

        String[] paramNames = Arrays.stream(m.getParameters())
                .map(Parameter::getName).toArray(n -> new String[n]);
        if (mapped instanceof MethodMapping) {
            MethodMapping methodMapping = (MethodMapping) mapped;
            String[] realNames = methodMapping.getParameterNames();
            if (realNames != null) {
                for (int i = 0; i < realNames.length; i++) {
                    paramNames[i] = realNames[i];
                }
            }
        }

        key = key.replace('(', '$').replace(')', '$').replace('[', 'A')
                .replace(';', 'E').replace('/', 'P');

        StringBuilder body = new StringBuilder();

        // Generic parameters <A, B> ...
        if (signature != null) {
            TraceSignatureVisitor sigVisitor = new TraceSignatureVisitor(0);
            new SignatureReader(signature).accept(sigVisitor);

            String declaration = sigVisitor.getDeclaration();

            if (declaration.charAt(0) == '<')
                body.append(declaration, 0, declaration.indexOf("("))
                        .append(' ');
        }

        // name(self
        body.append(cleanupTypeString(m.getGenericReturnType().getTypeName()))
                .append(' ').append(mapped.getName())
                .append('(');

        boolean firstArg = true;
        if (!isStatic) {
            firstArg = false;
            body.append(cleanupTypeString(c.getName())).append(" self");
        }

        // Params
        {
            java.lang.reflect.Type[] paramTypes = m.getGenericParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (!firstArg) {
                    body.append(", ");
                }
                firstArg = false;

                String param = cleanupTypeString(paramTypes[j].getTypeName());

                // replace T[] with T...
                if (m.isVarArgs() && (j == paramTypes.length - 1)) {
                    param = param.replaceFirst("\\[\\]$", "...");
                }

                body.append(param).append(' ').append(paramNames[j]);
            }
        }
        body.append(')');

        // Exceptions
        {
            java.lang.reflect.Type[] exceptions = m.getGenericExceptionTypes();
            if (exceptions.length > 0) {
                body.append(" throws ");
                for (int k = 0; k < exceptions.length; k++) {
                    if (exceptions[k] instanceof Class) {
                        body.append(((Class<?>) exceptions[k]).getName());
                    } else {
                        body.append(exceptions[k].toString());
                    }

                    if (k < (exceptions.length - 1)) {
                        body.append(',');
                    }
                }
            }
        }

        // No need to generate a method handle for public methods
        if (Modifier.isPublic(m.getModifiers())) {
            writeCommentBlock(out, mapped);

            if (m.isAnnotationPresent(Deprecated.class)) {
                out.write("    @Deprecated\n");
            }
            out.append("    public static ").append(body).append(" { ");

            if (m.getReturnType() != Void.TYPE) {
                out.append("return ");
            }

            if (isStatic) {
                out.append(cleanupTypeString(c.getName())).append('.');
            } else {
                out.append("self.");
            }

            out.append(m.getName()).append("(");

            boolean first = true;
            for (String param : paramNames) {
                if (!first) {
                    out.append(", ");
                }
                first = false;

                out.append(param);
            }

            out.append("); }\n");

            return;
        }

        // Declare interface
        out.append("    private interface Method").append(key).append(" { ")
                .append(body).append(";").append(" }\n");
        out.append("    private static final java.lang.invoke.MethodHandle")
                .append(" MH_").append(key).append(" = ")
                .append(isStatic ? "staticM" : "m")
                .append("ethodAccess(lookup, ")
                .append(cleanupTypeString(c.getName())).append(".class, \"")
                .append(m.getName()).append("\", Method")
                .append(key).append(".class);\n");

        writeCommentBlock(out, mapped);

        out.append("    public static ").append(body).append(" { try { ");

        if (m.getReturnType() != Void.TYPE) {
            out.append("return (")
                    .append(cleanupTypeString(
                            m.getGenericReturnType().getTypeName()))
                    .append(") ");
        }

        firstArg = true;
        out.append("MH_").append(key).append(".invokeExact(");

        if (!isStatic) {
            out.append("self");
            firstArg = false;
        }

        for (String param : paramNames) {
            if (!firstArg) {
                out.append(", ");
            }
            firstArg = false;

            out.append(param);
        }

        out.append(");").append(" } catch(Throwable e) { ")
                .append("throw sneakyThrow(e);").append(" }}\n");
    }

    private void writeForField(Class<?> c, Writer out, MCPMapping mapped,
            Field f) throws IOException {
        String typeString = f.getGenericType().getTypeName();

        // TODO: Primitive accessors
        if (f.getType().isPrimitive()) {
            typeString = Primitives.wrap(f.getType()).getSimpleName();
        }

        typeString = cleanupTypeString(typeString);

        {
            Class<?> type = f.getType();
            while (type.isArray()) {
                type = type.getComponentType();
            }

            if (!Modifier.isPublic(type.getModifiers())) {
                out.write("    // Type not public: " + typeString + "\n");
                typeString = "Object";
            }
        }

        if (typeString.contains("IDynamicTexture")) {
            // Client shit
            return;
        }

        writeCommentBlock(out, mapped);

        int modifiers = f.getModifiers();
        boolean isStatic = Modifier.isStatic(modifiers);
        boolean isFinal = Modifier.isFinal(modifiers);

        out.append("    public static final ").append(isStatic ? "Static" : "")
                .append(isFinal ? "Getter<" : "Accessor<").append(typeString);

        if (!isStatic) {
            out.append(", ").append(cleanupTypeString(c.getName()));
        }

        out.append("> ").append(mapped.getName()).append(" = ");

        if (isStatic) {
            out.append("staticAccess");
        } else {
            out.append("access");
        }

        if (isFinal) {
            out.append("Final");
        }

        out.append("(").append("lookup, ")
                .append(cleanupTypeString(c.getName())).append(".class")
                .append(", ")
                .append("\"").append(f.getName()).append("\"").append(");");

        out.append('\n');
    }

    private String cleanupTypeString(String typeString) {
        // WTF?
        typeString = typeString.replaceAll(
                "\\.DataWatcher\\.net\\.minecraft\\.server\\.[^\\.]*", "");
        typeString = typeString.replaceAll("java\\.lang\\.", "");
        typeString = typeString.replaceAll("\\$", ".");
        return typeString;
    }

    private List<Member> readMemberOrder(Class<?> c) throws IOException {
        // Extract the order in which the fields are defined directly from the
        // .class files.
        List<Member> fields = new ArrayList<>();
        try (InputStream is = c.getClassLoader().getResourceAsStream(
                c.getName().replace('.', '/') + ".class")) {
            ClassReader reader = new ClassReader(is);

            reader.accept(new ClassVisitor(Opcodes.ASM5) {
                @Override
                public FieldVisitor visitField(int access, String name,
                        String desc, String signature, Object value) {
                    fields.add(new Member(name));
                    return null;
                }

                @Override
                public MethodVisitor visitMethod(int access, String name,
                        String desc, String signature,
                        String[] exceptions) {
                    // Skip (static-) constructors
                    if (name.contains("<")) {
                        return null;
                    }

                    // Skip 'obvious' methods
                    switch (name) {
                    case "equals":
                    case "hashCode":
                    case "values":
                    case "valueOf":
                        return null;
                    default:
                    }

                    fields.add(new MethodMember(name, desc, signature));
                    return null;
                }

            }, 0);
        }

        return fields;
    }

    private void writeCommentBlock(Writer out, MCPMapping mapped)
            throws IOException {
        String description = mapped.getDescription();
        if (description == null || "".equals(description)) {
            return;
        }

        out.write("    /**");
        out.write("\n    * ");
        out.write(description.replace("\\n", "\n    * ")
                .replace("@link #", "@link#").replace("@link ", "@link Ref")
                .replace("@link#", "@link #"));
        out.write("\n    */\n");
    }

    private Multimap<String, Mapping> readMappings() throws IOException {
        Multimap<String, Mapping> mappings = LinkedListMultimap.create();

        File spigot2srg = new File(mappingsDirectory, "spigot2srg.srg");
        File srg2field = new File(mappingsDirectory, "srg2field.csv");
        File srg2method = new File(mappingsDirectory, "srg2method.csv");
        File srg2param = new File(mappingsDirectory, "srg2param.csv");
        File mcp = new File(mappingsDirectory, "mcp.zip");

        if (!mappingsDirectory.exists() && !mappingsDirectory.mkdirs()) {
            throw new IOException("Failed to create parent dirs");
        }

        if (!spigot2srg.exists()) {
            downloadFile(spigot2srgURL, spigot2srg);
        }
        if (!mcp.exists()) {
            downloadFile(mcpURL, mcp);
        }
        if (!srg2field.exists() || !srg2method.exists()
                || !srg2param.exists()) {
            try (FileInputStream fis = new FileInputStream(mcp);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    ZipInputStream zis = new ZipInputStream(bis)) {
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    String name = ze.getName();
                    if (name.endsWith("fields.csv")) {
                        writeTo(srg2field, zis);
                    } else if (name.endsWith("methods.csv")) {
                        writeTo(srg2method, zis);
                    } else if (name.endsWith("params.csv")) {
                        writeTo(srg2param, zis);
                    }
                }
            }
        }

        Map<String, MethodMapping> methods = readMethodMappings(srg2method,
                srg2param);
        Map<String, FieldMapping> fields = readFieldMappings(srg2field);

        File mappingLog = new File(mappingsDirectory, "map.log");
        try (InputStream is = new FileInputStream(spigot2srg);
                FileWriter log = new FileWriter(mappingLog);
                Scanner s = new Scanner(is)) {
            while (s.hasNext()) {
                String kind = s.next();
                String from, to;
                String fromClass;
                List<String> fromParts, toParts;

                switch (kind) {
                case "MD:":
                    from = s.next();
                    String fromType = s.next();
                    to = s.next();
                    s.next(); // To type

                    // Skip unmapped or non-server mappings
                    if (!from.contains("net/minecraft/server")) {
                        break;
                    }

                    fromParts = Splitter.on('/').splitToList(from);
                    toParts = Splitter.on('/').splitToList(to);

                    fromClass = fromParts.get(fromParts.size() - 2);
                    String fromMethod = fromParts.get(fromParts.size() - 1);
                    String toMethod = toParts.get(toParts.size() - 1);

                    // Skip java magic
                    if (toMethod.contains("access$")
                            || toMethod.contains("$VALUES")) {
                        break;
                    }

                    // Skip non white-listed classes
                    if (!CLASS_WHITELIST.contains(fromClass)
                            || excludes.contains(fromClass)) {
                        break;
                    }

                    if ("func_70109_d".equals(toMethod)) {
                        toMethod = "func_143022_a";
                    }

                {
                    MethodMapping mapping = methods.get(toMethod);

                    String fromKey = fromMethod + fromType;
                    fromKey = fromKey.replace("net/minecraft/server",
                            mcPackage.replace('.', '/'));

                    log.write("METHOD " + fromClass + " | " + fromKey + " / "
                            + toMethod);
                    if (mapping == null) {
                        System.err
                                .println("MISSING METHOD MAPPING " + toMethod);
                        log.write(" => MISSING \n");
                        continue;
                    }
                    log.write(" => " + mapping.getName() + "\n");

                    mappings.put(fromClass, new Mapping(fromKey, mapping));
                }
                    break;
                case "CL:":
                    // Ignore class mappings
                    s.nextLine();
                    break;
                case "FD:":
                    from = s.next();
                    to = s.next();

                    // Skip unmapped or non-server mappings
                    if (!from.contains("net/minecraft/server")) {
                        break;
                    }

                    fromParts = Splitter.on('/').splitToList(from);
                    toParts = Splitter.on('/').splitToList(to);

                    fromClass = fromParts.get(fromParts.size() - 2);
                    String fromField = fromParts.get(fromParts.size() - 1);
                    String toField = toParts.get(toParts.size() - 1);

                    // Skip non white-listed classes
                    if (!CLASS_WHITELIST.contains(fromClass)
                            || excludes.contains(fromClass)) {
                        break;
                    }

                    // Skip java magic
                    if (toField.contains("$VALUES")) {
                        break;
                    }

                {
                    log.write("METHOD " + fromClass + " | " + fromField + " / "
                            + fromField);
                    FieldMapping mapping = fields.get(toField);
                    if (mapping == null) {
                        System.err.println("MISSING FIELD MAPPING " + toField);
                        log.write(" => MISSING \n");
                        continue;
                    }

                    log.write(" => " + mapping.getName() + "\n");

                    mappings.put(fromClass, new Mapping(fromField, mapping));
                }
                    break;
                default:
                    throw new IllegalStateException(
                            "Unexpected mapping type: " + kind);
                }
            }
        }

        return mappings;
    }

    private interface MCPMapping {
        String getName();

        String getDescription();
    }

    @Data
    private static final class MethodMapping implements MCPMapping {
        private final String name;
        private final String description;
        private final String[] parameterNames;
    }

    private Map<String, MethodMapping> readMethodMappings(File srg2method,
            File srg2param) throws IOException {
        ImmutableMap.Builder<String, MethodMapping> out;
        out = ImmutableMap.builder();
        try (Scanner methods = new Scanner(srg2method);
                Scanner params = new Scanner(srg2param)) {
            // Skip first lines
            methods.nextLine();
            params.nextLine();

            List<String> currentParameters = new ArrayList<>();
            String nextParameter = null;
            String nextParameterFunction = null;
            String currentID;

            while (methods.hasNext()) {
                currentParameters.clear();

                String methodName;
                String methodNiceName;
                String methodDescription;

                {
                    Iterator<String> line = Splitter.on(',').limit(4)
                            .split(methods.nextLine()).iterator();
                    methodName = line.next();
                    methodNiceName = line.next();
                    line.next(); // Side
                    methodDescription = line.next();
                }

                // Extract id
                currentID = Splitter.on('_').splitToList(methodName).get(1);

                if (nextParameterFunction == null
                        || nextParameterFunction.equals(currentID)) {
                    // Read parameters
                    if (nextParameterFunction != null) {
                        currentParameters.add(nextParameter);
                        nextParameterFunction = null;
                    }

                    while (true) {
                        Iterator<String> line = Splitter.on(',').limit(4)
                                .split(params.nextLine()).iterator();
                        String parameterID = line.next();
                        String parameterName = line.next();
                        line.next(); // Side

                        String parameterMethodID = Splitter.on('_')
                                .splitToList(parameterID).get(1);

                        if (!parameterMethodID.equals(currentID)) {
                            nextParameter = parameterName;
                            nextParameterFunction = parameterMethodID;
                            break;
                        }

                        currentParameters.add(parameterName);
                    }
                }

                out.put(methodName,
                        new MethodMapping(methodNiceName, methodDescription,
                                currentParameters.toArray(
                                        new String[currentParameters.size()])));
            }
        }
        return out.build();
    }

    @Data
    private static final class FieldMapping implements MCPMapping {
        private final String name;
        private final String description;
    }

    private Map<String, FieldMapping> readFieldMappings(File srg2field)
            throws IOException {
        ImmutableMap.Builder<String, FieldMapping> out;
        out = ImmutableMap.builder();

        try (Scanner s = new Scanner(srg2field)) {
            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                Iterator<String> it = Splitter.on(',').limit(4).split(line)
                        .iterator();

                String key = it.next();
                String name = it.next();
                it.next(); // side
                String description = it.next();
                out.put(key, new FieldMapping(name, description));
            }
        }

        return out.build();
    }

    private void downloadFile(URL url, File file) throws IOException {
        try (InputStream is = url.openStream()) {
            writeTo(file, is);
        }
    }

    private void writeTo(File file, InputStream is)
            throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            ByteStreams.copy(is, os);
        }
    }
}
