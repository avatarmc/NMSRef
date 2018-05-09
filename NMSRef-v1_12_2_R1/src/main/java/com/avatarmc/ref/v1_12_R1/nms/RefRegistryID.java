package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
public class RefRegistryID extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.RegistryID.class);
    public static final StaticGetter<Object> EMPTY = staticAccessFinal(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "a");
    public static final Accessor<Object[], net.minecraft.server.v1_12_R1.RegistryID<?>> values = access(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "b");
    public static final Accessor<int[], net.minecraft.server.v1_12_R1.RegistryID<?>> intKeys = access(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "c");
    public static final Accessor<Object[], net.minecraft.server.v1_12_R1.RegistryID<?>> byId = access(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "d");
    public static final Accessor<Integer, net.minecraft.server.v1_12_R1.RegistryID<?>> nextFreeIndex = access(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "e");
    public static final Accessor<Integer, net.minecraft.server.v1_12_R1.RegistryID<?>> mapSize = access(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "f");
    public static <K> int getId(net.minecraft.server.v1_12_R1.RegistryID<K> self, K arg0) { return self.getId(arg0); }
    public static <K> K get(net.minecraft.server.v1_12_R1.RegistryID<K> self, int idIn) { return self.fromId(idIn); }
    private interface Methodc$I$I { int getValue(net.minecraft.server.v1_12_R1.RegistryID<?> self, int arg0); }
    private static final java.lang.invoke.MethodHandle MH_c$I$I = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "c", Methodc$I$I.class);
    public static int getValue(net.minecraft.server.v1_12_R1.RegistryID<?> self, int arg0) { try { return (int) MH_c$I$I.invokeExact(self, arg0); } catch(Throwable e) { throw sneakyThrow(e); }}
    /**
    * Adds the given object while expanding this map
    */
    public static <K> int add(net.minecraft.server.v1_12_R1.RegistryID<K> self, K objectIn) { return self.c(objectIn); }
    private interface Methodc$$I { int nextId(net.minecraft.server.v1_12_R1.RegistryID<?> self); }
    private static final java.lang.invoke.MethodHandle MH_c$$I = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "c", Methodc$$I.class);
    public static int nextId(net.minecraft.server.v1_12_R1.RegistryID<?> self) { try { return (int) MH_c$$I.invokeExact(self); } catch(Throwable e) { throw sneakyThrow(e); }}
    private interface Methodd$I$V { void grow(net.minecraft.server.v1_12_R1.RegistryID<?> self, int capacity); }
    private static final java.lang.invoke.MethodHandle MH_d$I$V = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "d", Methodd$I$V.class);
    /**
    * Rehashes the map to the new capacity
    */
    public static void grow(net.minecraft.server.v1_12_R1.RegistryID<?> self, int capacity) { try { MH_d$I$V.invokeExact(self, capacity); } catch(Throwable e) { throw sneakyThrow(e); }}
    /**
    * Puts the provided object value with the integer key.
    */
    public static <K> void put(net.minecraft.server.v1_12_R1.RegistryID<K> self, K objectIn, int intKey) { self.a(objectIn, intKey); }
    private interface Methodd$LjavaPlangPObjectE$I { <K> int hashObject(net.minecraft.server.v1_12_R1.RegistryID<K> self, K obectIn); }
    private static final java.lang.invoke.MethodHandle MH_d$LjavaPlangPObjectE$I = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "d", Methodd$LjavaPlangPObjectE$I.class);
    public static <K> int hashObject(net.minecraft.server.v1_12_R1.RegistryID<K> self, K obectIn) { try { return (int) MH_d$LjavaPlangPObjectE$I.invokeExact(self, obectIn); } catch(Throwable e) { throw sneakyThrow(e); }}
    private interface Methodb$LjavaPlangPObjectEI$I { <K> int getIndex(net.minecraft.server.v1_12_R1.RegistryID<K> self, K objectIn, int arg1); }
    private static final java.lang.invoke.MethodHandle MH_b$LjavaPlangPObjectEI$I = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "b", Methodb$LjavaPlangPObjectEI$I.class);
    public static <K> int getIndex(net.minecraft.server.v1_12_R1.RegistryID<K> self, K objectIn, int arg1) { try { return (int) MH_b$LjavaPlangPObjectEI$I.invokeExact(self, objectIn, arg1); } catch(Throwable e) { throw sneakyThrow(e); }}
    private interface Methode$I$I { int findEmpty(net.minecraft.server.v1_12_R1.RegistryID<?> self, int arg0); }
    private static final java.lang.invoke.MethodHandle MH_e$I$I = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistryID.class, "e", Methode$I$I.class);
    public static int findEmpty(net.minecraft.server.v1_12_R1.RegistryID<?> self, int arg0) { try { return (int) MH_e$I$I.invokeExact(self, arg0); } catch(Throwable e) { throw sneakyThrow(e); }}
    public static <K> java.util.Iterator<K> iterator(net.minecraft.server.v1_12_R1.RegistryID<K> self) { return self.iterator(); }
    public static int size(net.minecraft.server.v1_12_R1.RegistryID<?> self) { return self.b(); }
}
