package de.larsensmods.mythocraft.client.mixincore;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.client.RenderTypes;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;

public class HumanoidArmorLayerMixinCore {

    public static <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> void renderModelInjectCore(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, A model, boolean withGlint, float r, float g, float b, String armorSuffix, Operation<Void> original, ResourceLocation textureLocation, T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            original.call(instance, poseStack, buffer, packedLight, armorItem, model, withGlint, r, g, b, armorSuffix);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderTypes.armorTranslucentNoCull(textureLocation));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY);
        }else{
            original.call(instance, poseStack, buffer, packedLight, armorItem, model, withGlint, r, g, b, armorSuffix);
        }
    }

}
