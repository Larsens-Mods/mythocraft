package de.larsensmods.mythocraft.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraLayer.class)
public class MixinElytraLayer<T extends LivingEntity, M extends EntityModel<T>> {

    /*@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getArmorFoilBuffer(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;ZZ)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer mythocraft$enableTransparency(MultiBufferSource buffer, RenderType renderType, boolean noEntity, boolean withGlint, Operation<VertexConsumer> original, @Local(argsOnly = true) T livingEntity){
        return ElytraLayerMixinCore.enableTransparency(buffer, renderType, noEntity, withGlint, original, livingEntity);
    }

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType mythocraft$modRenderType(ResourceLocation location, Operation<RenderType> original, @Local(argsOnly = true) T livingEntity){
        return ElytraLayerMixinCore.modRenderType(location, original, livingEntity);
    }

    @WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private void mythocraft$modRendering(ElytraModel<T> instance, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, Operation<Void> original, @Local(argsOnly = true) T livingEntity){
        ElytraLayerMixinCore.modRendering(instance, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha, original, livingEntity);
    }*/

    @Shadow
    @Final
    private ElytraModel<T> elytraModel;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER), cancellable = true)
    private void mythocraft$modRenderMethod(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci, @Local ItemStack itemStack, @Local ResourceLocation resourcelocation){
        VertexConsumer vertexconsumer = mythocraft$enableTransparency(buffer, mythocraft$modRenderType(resourcelocation, RenderType.armorCutoutNoCull(resourcelocation), livingEntity), false, itemStack.hasFoil(), livingEntity);
        mythocraft$modRendering(this.elytraModel, poseStack, vertexconsumer, packedLight, livingEntity);
        poseStack.popPose();
        ci.cancel();
    }

    @Unique
    private static <T extends LivingEntity> VertexConsumer mythocraft$enableTransparency(MultiBufferSource bufferSource, RenderType renderType, boolean noEntity, boolean withGlint, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, noEntity, withGlint);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return ItemRenderer.getFoilBufferDirect(bufferSource, renderType, noEntity, withGlint);
        }else{
            return ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, noEntity, withGlint);
        }
    }

    @Unique
    private static <T extends LivingEntity> RenderType mythocraft$modRenderType(ResourceLocation location, RenderType original, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return original;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return RenderType.entityTranslucent(location);
        }else{
            return original;
        }
    }

    @Unique
    private static <T extends LivingEntity> void mythocraft$modRendering(ElytraModel<T> instance, PoseStack poseStack, VertexConsumer buffer, int packedLight, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            instance.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            instance.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY);
        }else{
            instance.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

}
