package com.avatarmc.ref.v1_12_R1.nms;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
@SuppressWarnings("unused")
public class RefMutableBlockPosition extends RefBlockPosition {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition.class);
    /**
    * None
    */
    public static net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition setPos(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition self, int xIn, int yIn, int zIn) { return self.c(xIn, yIn, zIn); }
    public static net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition setPos(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition self, double xIn, double yIn, double zIn) { return self.c(xIn, yIn, zIn); }
    public static net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition setPos(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition self, net.minecraft.server.v1_12_R1.BaseBlockPosition vec) { return self.g(vec); }
    public static net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition move(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition self, net.minecraft.server.v1_12_R1.EnumDirection facing) { return self.c(facing); }
    public static net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition move(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition self, net.minecraft.server.v1_12_R1.EnumDirection facing, int n) { return self.c(facing, n); }
    public static void setY(net.minecraft.server.v1_12_R1.BlockPosition.MutableBlockPosition self, int yIn) { self.p(yIn); }
}
