package de.larsensmods.mythocraft.mixin.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class MixinHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    /*@WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/client/model/Model;ZFFFLnet/minecraft/resources/ResourceLocation;)V"))
    private void mythocraft$renderModelWrap(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, Model model, boolean withGlint, float red, float green, float blue, ResourceLocation textureLocation, Operation<Void> original, @Local(argsOnly = true) T livingEntity){
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            original.call(instance, poseStack, buffer, packedLight, armorItem, model, withGlint, red, green, blue, textureLocation);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(textureLocation));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY);
        }else{
            original.call(instance, poseStack, buffer, packedLight, armorItem, model, withGlint, red, green, blue, textureLocation);
        }
    }*/

    @Inject(method = "renderArmorPiece", at = @At(value = "HEAD"), cancellable = true)
    private void mythocraft$substituteRenderMethod(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int packedLight, A p_model, CallbackInfo ci){
        HumanoidArmorLayer<T, M, A> instance = (HumanoidArmorLayer<T, M, A>)(Object) this;
        ItemStack itemstack = livingEntity.getItemBySlot(slot);
        Item $$9 = itemstack.getItem();
        if ($$9 instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == slot) {
                instance.getParentModel().copyPropertiesTo(p_model);
                instance.setPartVisibility(p_model, slot);
                A model = (A) ForgeHooksClient.getArmorModel(livingEntity, itemstack, slot, p_model);
                boolean flag = instance.usesInnerModel(slot);
                if (armoritem instanceof DyeableLeatherItem) {
                    int i = ((DyeableLeatherItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    //instance.renderModel(poseStack, buffer, packedLight, armoritem, model, flag, f, f1, f2, instance.getArmorResource(livingEntity, itemstack, slot, (String)null));
                    mythocraft$renderModelWrap(poseStack, buffer, packedLight, armoritem, model, flag, f, f1, f2, instance.getArmorResource(livingEntity, itemstack, slot, null), livingEntity);
                    //instance.renderModel(poseStack, buffer, packedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, instance.getArmorResource(livingEntity, itemstack, slot, "overlay"));
                    mythocraft$renderModelWrap(poseStack, buffer, packedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, instance.getArmorResource(livingEntity, itemstack, slot, "overlay"), livingEntity);
                } else {
                    //instance.renderModel(poseStack, buffer, packedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, instance.getArmorResource(livingEntity, itemstack, slot, (String)null));
                    mythocraft$renderModelWrap(poseStack, buffer, packedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, instance.getArmorResource(livingEntity, itemstack, slot, null), livingEntity);
                }

                ArmorTrim.getTrim(livingEntity.level().registryAccess(), itemstack).ifPresent((p_289638_) -> instance.renderTrim(armoritem.getMaterial(), poseStack, buffer, packedLight, p_289638_, model, flag));
                if (itemstack.hasFoil()) {
                    instance.renderGlint(poseStack, buffer, packedLight, model);
                }
            }
        }
        ci.cancel();
    }

    @Unique
    private void mythocraft$renderModelWrap(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, Model model, boolean withGlint, float red, float green, float blue, ResourceLocation textureLocation, T livingEntity) {
        if(!(livingEntity instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
            mythocraft$renderModel(poseStack, buffer, packedLight, armorItem, model, withGlint, red, green, blue, textureLocation);
            return;
        }
        if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(textureLocation));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY);
        }else{
            mythocraft$renderModel(poseStack, buffer, packedLight, armorItem, model, withGlint, red, green, blue, textureLocation);
        }
    }

    @Unique
    private void mythocraft$renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, Model model, boolean withGlint, float red, float green, float blue, ResourceLocation textureLocation) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.armorCutoutNoCull(textureLocation));
        model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

}
