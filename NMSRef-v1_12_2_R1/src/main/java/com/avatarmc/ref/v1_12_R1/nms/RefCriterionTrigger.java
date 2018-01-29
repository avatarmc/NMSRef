package com.avatarmc.ref.v1_12_R1.nms;

import com.avatarmc.ref.AbstractRefBase;

// @generated
@javax.annotation.Generated("com.avatarmc.ref.maven.ReflectionGenerator")
@SuppressWarnings("unused")
public class RefCriterionTrigger extends AbstractRefBase {
    private static final java.lang.invoke.MethodHandles.Lookup lookup = getLookup(net.minecraft.server.v1_12_R1.CriterionTrigger.class);
    public static net.minecraft.server.v1_12_R1.MinecraftKey getId(net.minecraft.server.v1_12_R1.CriterionTrigger<?> self) { return self.a(); }
    public static <T extends net.minecraft.server.v1_12_R1.CriterionInstance> void addListener(net.minecraft.server.v1_12_R1.CriterionTrigger<T>  self, net.minecraft.server.v1_12_R1.AdvancementDataPlayer arg0, net.minecraft.server.v1_12_R1.CriterionTrigger.a<T> arg1) { self.a(arg0, arg1); }
    public static <T extends net.minecraft.server.v1_12_R1.CriterionInstance> void removeListener(net.minecraft.server.v1_12_R1.CriterionTrigger<T>  self, net.minecraft.server.v1_12_R1.AdvancementDataPlayer arg0, net.minecraft.server.v1_12_R1.CriterionTrigger.a<T> arg1) { self.b(arg0, arg1); }
    public static void removeAllListeners(net.minecraft.server.v1_12_R1.CriterionTrigger<?> self, net.minecraft.server.v1_12_R1.AdvancementDataPlayer playerAdvancementsIn) { self.a(playerAdvancementsIn); }
    /**
    * Deserialize a ICriterionInstance of this trigger from the data in the JSON.
    */
    public static <T extends net.minecraft.server.v1_12_R1.CriterionInstance> T deserializeInstance(net.minecraft.server.v1_12_R1.CriterionTrigger<T> self, com.google.gson.JsonObject json, com.google.gson.JsonDeserializationContext context) { return self.a(json, context); }
}
