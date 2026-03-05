package de.larsensmods.mythocraft.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.larsensmods.mythocraft.client.mixincore.HumanoidArmorLayerMixinCore;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HumanoidArmorLayer.class)
public class MixinHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/HumanoidModel;ZFFFLjava/lang/String;)V"))
    private void mythocraft$renderModelWrap(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, A model, boolean withGlint, float red, float green, float blue, String armorSuffix, Operation<Void> original,  @Local(argsOnly = true) T livingEntity){
        ResourceLocation textureLocation = instance.getArmorLocation(armorItem, withGlint, armorSuffix);
        HumanoidArmorLayerMixinCore.renderModelInjectCore(instance, poseStack, buffer, packedLight, armorItem, model, withGlint, red, green, blue, armorSuffix, original, textureLocation, livingEntity);
    }

}
