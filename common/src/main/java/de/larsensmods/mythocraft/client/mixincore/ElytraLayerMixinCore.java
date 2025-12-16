package de.larsensmods.mythocraft.client.mixincore;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class ElytraLayerMixinCore {

    public static <T extends LivingEntity> VertexConsumer enableTransparency(MultiBufferSource bufferSource, RenderType renderType, boolean hasFoil, Operation<VertexConsumer> original, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return original.call(bufferSource, renderType, hasFoil);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return ItemRenderer.getFoilBufferDirect(bufferSource, renderType, hasFoil, hasFoil);
        }else{
            return original.call(bufferSource, renderType, hasFoil);
        }
    }

    public static <T extends LivingEntity> RenderType modRenderType(ResourceLocation location, Operation<RenderType> original, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            return original.call(location);
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            return RenderType.entityTranslucent(location);
        }else{
            return original.call(location);
        }
    }

    public static <T extends LivingEntity> void modRendering(ElytraModel<T> instance, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, Operation<Void> original, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            original.call(instance, poseStack, vertexConsumer, packedLight, packedOverlay);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            instance.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, FastColor.ARGB32.color(FastColor.as8BitChannel(MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY), -1));
        }else{
            original.call(instance, poseStack, vertexConsumer, packedLight, packedOverlay);
        }
    }

}
