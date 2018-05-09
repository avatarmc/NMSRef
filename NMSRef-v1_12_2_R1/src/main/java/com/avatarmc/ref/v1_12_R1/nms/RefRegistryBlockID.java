package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
public class RefRegistryBlockID extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.RegistryBlockID.class);
    public static final Getter<java.util.IdentityHashMap<?, Integer>, net.minecraft.server.v1_12_R1.RegistryBlockID> identityMap = accessFinal(lookup, net.minecraft.server.v1_12_R1.RegistryBlockID.class, "a");
    public static final Getter<java.util.List<?>, net.minecraft.server.v1_12_R1.RegistryBlockID> objectList = accessFinal(lookup, net.minecraft.server.v1_12_R1.RegistryBlockID.class, "b");
    public static <T> void put(net.minecraft.server.v1_12_R1.RegistryBlockID<T> self, T key, int value) { self.a(key, value); }
    public static <T> int get(net.minecraft.server.v1_12_R1.RegistryBlockID<T>  self, T key) { return self.getId(key); }
    public static <T> T getByValue(net.minecraft.server.v1_12_R1.RegistryBlockID<T>  self, int value) { return self.fromId(value); }
    public static <T> java.util.Iterator<T> iterator(net.minecraft.server.v1_12_R1.RegistryBlockID<T> self) { return self.iterator(); }
    public static int size(net.minecraft.server.v1_12_R1.RegistryBlockID self) { return self.a(); }
}
