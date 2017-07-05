package com.avatarmc.ref.v1_9_R1.nms;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
public class RefRegistryMaterials extends RefRegistrySimple {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_9_R1.RegistryMaterials.class);
    /**
    * The backing store that maps Integers to objects.
    */
    public static final Getter<net.minecraft.server.v1_9_R1.RegistryID<?>, net.minecraft.server.v1_9_R1.RegistryMaterials<?, ?>> underlyingIntegerMap = accessFinal(lookup, net.minecraft.server.v1_9_R1.RegistryMaterials.class, "a");
    /**
    * A BiMap of objects (key) to their names (value).
    */
    public static final Getter<java.util.Map<?, ?>, net.minecraft.server.v1_9_R1.RegistryMaterials<?, ?>> inverseObjectRegistry = accessFinal(lookup, net.minecraft.server.v1_9_R1.RegistryMaterials.class, "b");
    public static <K, V> void register(net.minecraft.server.v1_9_R1.RegistryMaterials<K, V> self, int id, K key, V value) { self.a(id, key, value); }
    /**
    * Gets the name we use to identify the given object.
    */
    public static <K, V> K getNameForObject(net.minecraft.server.v1_9_R1.RegistryMaterials<K, V> self, V value) { return self.b(value); }
    /**
    * Gets the integer ID we use to identify the given object.
    */
    public static <K, V> int getIDForObject(net.minecraft.server.v1_9_R1.RegistryMaterials<K, V> self, V value) { return self.a(value); }
    /**
    * Gets the object identified by the given ID.
    */
    public static <K, V> V getObjectById(net.minecraft.server.v1_9_R1.RegistryMaterials<K, V> self, int id) { return self.getId(id); }
}
