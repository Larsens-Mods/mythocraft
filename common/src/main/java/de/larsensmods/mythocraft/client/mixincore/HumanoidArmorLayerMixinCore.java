package de.larsensmods.mythocraft.client.mixincore;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.client.RenderTypes;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class HumanoidArmorLayerMixinCore {

    public static <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> void renderModelInjectCore(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Model model, int dyeColor, ResourceLocation textureLocation, Operation<Void> original, @Local(argsOnly = true) T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            original.call(instance, poseStack, bufferSource, packedLight, model, dyeColor, textureLocation);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderTypes.armorTranslucentNoCull(textureLocation));
            model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(FastColor.as8BitChannel(MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY), dyeColor));
        }else{
            original.call(instance, poseStack, bufferSource, packedLight, model, dyeColor, textureLocation);
        }
    }

}
