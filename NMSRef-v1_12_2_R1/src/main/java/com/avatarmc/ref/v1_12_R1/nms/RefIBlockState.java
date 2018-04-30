package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
@SuppressWarnings("unused")
public class RefIBlockState extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.IBlockState.class);
    public static String getName(net.minecraft.server.v1_12_R1.IBlockState<?> self) { return self.a(); }
    public static <T extends Comparable<T>> java.util.Collection<T> getAllowedValues(net.minecraft.server.v1_12_R1.IBlockState<T> self) { return self.c(); }
    /**
    * The class of the values of this property
    */
    public static <T extends Comparable<T>> Class<T> getValueClass(net.minecraft.server.v1_12_R1.IBlockState<T> self) { return self.b(); }
    public static <T extends Comparable<T>> com.google.common.base.Optional<T> parseValue(net.minecraft.server.v1_12_R1.IBlockState<T> self, String value) { return self.b(value); }
    /**
    * Get the name for the given value.
    */
    public static <T extends Comparable<T>> String getName(net.minecraft.server.v1_12_R1.IBlockState<T> self, T value) { return self.a(value); }
}
