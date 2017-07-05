package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
public class RefRegistrySimple extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.RegistrySimple.class);
    public static final StaticGetter<org.apache.logging.log4j.Logger> LOGGER = staticAccessFinal(lookup, net.minecraft.server.v1_12_R1.RegistrySimple.class, "a");
    /**
    * Objects registered on this registry.
    */
    public static final Getter<java.util.Map<?, ?>, net.minecraft.server.v1_12_R1.RegistrySimple<?, ?>> registryObjects = accessFinal(lookup, net.minecraft.server.v1_12_R1.RegistrySimple.class, "c");
    public static final Accessor<Object[], net.minecraft.server.v1_12_R1.RegistrySimple<?, ?>> values = access(lookup, net.minecraft.server.v1_12_R1.RegistrySimple.class, "b");
    private interface Methodb$$LjavaPutilPMapE { java.util.Map<?, ?> createUnderlyingMap(net.minecraft.server.v1_12_R1.RegistrySimple<?, ?> self); }
    private static final java.lang.invoke.MethodHandle MH_b$$LjavaPutilPMapE = methodAccess(lookup, net.minecraft.server.v1_12_R1.RegistrySimple.class, "b", Methodb$$LjavaPutilPMapE.class);
    /**
    * Creates the Map we will use to map keys to their registered values.
    */
    public static <K, V> java.util.Map<K, V> createUnderlyingMap(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self) { try { return (java.util.Map<K, V>) MH_b$$LjavaPutilPMapE.invokeExact(self); } catch(Throwable e) { throw sneakyThrow(e); }}
    public static <K, V> V getObject(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self, K name) { return self.get(name); }
    /**
    * Register an object on this registry.
    */
    public static <K, V> void putObject(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self, K key, V value) { self.a(key, value); }
    /**
    * Gets all the keys recognized by this registry.
    */
    public static <K, V> java.util.Set<K> getKeys(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self) { return self.keySet(); }
    public static <K, V> V getRandomObject(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self, java.util.Random random) { return self.a(random); }
    /**
    * Does this registry contain an entry for the given key?
    */
    public static <K, V> boolean containsKey(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self, K key) { return self.d(key); }
    public static <K, V> java.util.Iterator<V> iterator(net.minecraft.server.v1_12_R1.RegistrySimple<K, V> self) { return self.iterator(); }
}
