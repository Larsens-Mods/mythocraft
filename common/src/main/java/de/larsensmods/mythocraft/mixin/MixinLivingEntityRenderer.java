package de.larsensmods.mythocraft.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> {

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V"))
    private void mythocraft$renderToBufferRedirect(EntityModel<T> instance, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, @Local(argsOnly = true) T pEntity){
        if(!(instance instanceof PlayerModel<T>) || !(pEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            instance.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            instance.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha * MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY);
        }else{
            instance.renderToBuffer(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

}
