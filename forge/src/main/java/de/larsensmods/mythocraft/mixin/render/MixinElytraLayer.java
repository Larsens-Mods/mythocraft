package de.larsensmods.mythocraft.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.IForgeElytraLayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraLayer.class)
public class MixinElytraLayer<T extends LivingEntity, M extends EntityModel<T>> implements IForgeElytraLayer<T> {

    @Shadow @Final private ElytraModel<T> elytraModel;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V"), cancellable = true)
    private void mythocraft$modRenderMethod(PoseStack par1, MultiBufferSource par2, int par3, T par4, float par5, float par6, float par7, float par8, float par9, float par10, CallbackInfo ci){
        ItemStack itemstack = par4.getItemBySlot(EquipmentSlot.CHEST);
        ResourceLocation resourcelocation;
        if (par4 instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)par4;
            PlayerSkin playerskin = abstractclientplayer.getSkin();
            if (playerskin.elytraTexture() != null) {
                resourcelocation = playerskin.elytraTexture();
            } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                resourcelocation = playerskin.capeTexture();
            } else {
                resourcelocation = this.getElytraTexture(itemstack, par4);
            }
        } else {
            resourcelocation = this.getElytraTexture(itemstack, par4);
        }

        this.elytraModel.setupAnim(par4, par5, par6, par8, par9, par10);
        VertexConsumer vertexconsumer = mythocraft$replaceGetArmorFoilBuffer(par4, par2, mythocraft$replaceArmorCutoutNoCull(par4, resourcelocation), itemstack.hasFoil());
        //RENDER TO BUFFER START
        //this.elytraModel.renderToBuffer(par1, vertexconsumer, par3, OverlayTexture.NO_OVERLAY);
        if(!(par4 instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            this.elytraModel.renderToBuffer(par1, vertexconsumer, par3, OverlayTexture.NO_OVERLAY);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            this.elytraModel.renderToBuffer(par1, vertexconsumer, par3, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(FastColor.as8BitChannel(MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY), -1));
        }else{
            this.elytraModel.renderToBuffer(par1, vertexconsumer, par3, OverlayTexture.NO_OVERLAY);
        }
        //RENDER TO BUFFER END
        par1.popPose();
        ci.cancel();
    }

    @Unique
    private RenderType mythocraft$replaceArmorCutoutNoCull(LivingEntity livingEntity, ResourceLocation location) {
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return RenderType.armorCutoutNoCull(location);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return RenderType.entityTranslucent(location);
        }else{
            return RenderType.armorCutoutNoCull(location);
        }
    }

    @Unique
    private VertexConsumer mythocraft$replaceGetArmorFoilBuffer(LivingEntity livingEntity, MultiBufferSource bufferSource, RenderType renderType, boolean hasFoil) {
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, hasFoil);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return ItemRenderer.getFoilBufferDirect(bufferSource, renderType, hasFoil, hasFoil);
        }else{
            return ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, hasFoil);
        }
    }

    //DOESN'T WORK FOR SOME REASON
    /*@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getArmorFoilBuffer(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;Z)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
    private VertexConsumer mythocraft$enableTransparency(MultiBufferSource bufferSource, RenderType renderType, boolean hasFoil, Operation<VertexConsumer> original, @Local(argsOnly = true) T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return original.call(bufferSource, renderType, hasFoil);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return ItemRenderer.getFoilBufferDirect(bufferSource, renderType, hasFoil, hasFoil);
        }else{
            return original.call(bufferSource, renderType, hasFoil);
        }
    }*/

    /*@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;armorCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType mythocraft$modRenderType(ResourceLocation location, Operation<RenderType> original, @Local(argsOnly = true) T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return original.call(location);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return RenderType.entityTranslucent(location);
        }else{
            return original.call(location);
        }
    }*/

    /*@WrapOperation(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ElytraModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;II)V"))
    private void mythocraft$modRendering(ElytraModel<T> instance, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, Operation<Void> original, @Local(argsOnly = true) T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            original.call(instance, poseStack, vertexConsumer, packedLight, packedOverlay);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            instance.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color(FastColor.as8BitChannel(MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY), -1));
        }else{
            original.call(instance, poseStack, vertexConsumer, packedLight, packedOverlay);
        }
    }*/

}
