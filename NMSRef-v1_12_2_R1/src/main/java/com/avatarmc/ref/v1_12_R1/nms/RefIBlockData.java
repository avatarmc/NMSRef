package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
@SuppressWarnings("unused")
public class RefIBlockData extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.IBlockData.class);
    /**
    * Get the names of all properties defined for this BlockState
    */
    public static java.util.Collection<net.minecraft.server.v1_12_R1.IBlockState<?>> getPropertyKeys(net.minecraft.server.v1_12_R1.IBlockData self) { return self.s(); }
    /**
    * Get the value of the given Property for this BlockState
    */
    public static <T extends java.lang.Comparable<T>> T getValue(net.minecraft.server.v1_12_R1.IBlockData self, net.minecraft.server.v1_12_R1.IBlockState<T> property) { return self.get(property); }
    /**
    * Get a version of this BlockState with the given Property now set to the given value
    */
    public static <T extends java.lang.Comparable<T>, V extends T> net.minecraft.server.v1_12_R1.IBlockData withProperty(net.minecraft.server.v1_12_R1.IBlockData self, net.minecraft.server.v1_12_R1.IBlockState<T> property, V value) { return self.set(property, value); }
    /**
    * "Create a version of this BlockState with the given property cycled to the next value in order. If the property was at the highest possible value, it is set to the lowest one instead."
    */
    public static <T extends java.lang.Comparable<T>> net.minecraft.server.v1_12_R1.IBlockData cycleProperty(net.minecraft.server.v1_12_R1.IBlockData self, net.minecraft.server.v1_12_R1.IBlockState<T> property) { return self.a(property); }
    /**
    * Get all properties of this BlockState. The returned Map maps from properties (IProperty) to the respective current value.
    */
    public static com.google.common.collect.ImmutableMap<net.minecraft.server.v1_12_R1.IBlockState<?>, Comparable<?>> getProperties(net.minecraft.server.v1_12_R1.IBlockData self) { return self.t(); }
    public static net.minecraft.server.v1_12_R1.Block getBlock(net.minecraft.server.v1_12_R1.IBlockData self) { return self.getBlock(); }
}
