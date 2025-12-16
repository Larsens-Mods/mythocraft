package de.larsensmods.mythocraft.mixin.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.larsensmods.mythocraft.MythConfigValues;
import de.larsensmods.mythocraft.client.RenderTypes;
import de.larsensmods.mythocraft.client.mixincore.HumanoidArmorLayerMixinCore;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class MixinHumanoidArmorLayer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    //DOES NOT WORK SOMEHOW
    /*@WrapOperation(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/Model;ILnet/minecraft/resources/ResourceLocation;)V"))
    private void mythocraft$renderModelInject(HumanoidArmorLayer<T, M, A> instance, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Model model, int dyeColor, ResourceLocation textureLocation, Operation<Void> original, @Local(argsOnly = true) T livingEntity){
        HumanoidArmorLayerMixinCore.renderModelInjectCore(instance, poseStack, bufferSource, packedLight, model, dyeColor, textureLocation, original, livingEntity);
    }*/

    @Inject(method = "renderArmorPiece", at = @At(value = "HEAD"), cancellable = true)
    private void mythocraft$substituteRenderMethod(PoseStack p_117119_, MultiBufferSource p_117120_, T p_117121_, EquipmentSlot p_117122_, int p_117123_, A p_117124_, CallbackInfo ci){
        HumanoidArmorLayer<T, M, A> instance = (HumanoidArmorLayer<T, M, A>)(Object) this;
        ItemStack itemstack = p_117121_.getItemBySlot(p_117122_);
        Item var9 = itemstack.getItem();
        if (var9 instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == p_117122_) {
                instance.getParentModel().copyPropertiesTo(p_117124_);
                instance.setPartVisibility(p_117124_, p_117122_);
                A model = (A) ForgeHooksClient.getArmorModel(p_117121_, itemstack, p_117122_, p_117124_);
                boolean flag = instance.usesInnerModel(p_117122_);
                ArmorMaterial armormaterial = armoritem.getMaterial().value();
                int i = itemstack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemstack, -6265536)) : -1;

                for(ArmorMaterial.Layer armormaterial$layer : armormaterial.layers()) {
                    int j = armormaterial$layer.dyeable() ? i : -1;
                    ResourceLocation texture = ForgeHooksClient.getArmorTexture(p_117121_, itemstack, p_117122_, armormaterial$layer, flag);
                    //START RENDER MODEL CALL
                    if(!(p_117121_ instanceof Player player) || !(player.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get()))){
                        mythocraft$originalCall(p_117119_, p_117120_, p_117123_, model, j, texture);
                        continue;
                    }
                    if(!Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.is(Minecraft.getInstance().player)){
                        VertexConsumer vertexconsumer = p_117120_.getBuffer(RenderTypes.armorTranslucentNoCull(texture));
                        model.renderToBuffer(p_117119_, vertexconsumer, p_117123_, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(FastColor.as8BitChannel(MythConfigValues.HADES_HELM_RENDER_TRANSPARENCY), j));
                    }else{
                        mythocraft$originalCall(p_117119_, p_117120_, p_117123_, model, j, texture);
                    }
                    //END RENDER MODEL CALL
                }

                ArmorTrim armortrim = itemstack.get(DataComponents.TRIM);
                if (armortrim != null) {
                    instance.renderTrim(armoritem.getMaterial(), p_117119_, p_117120_, p_117123_, armortrim, model, flag);
                }

                if (itemstack.hasFoil()) {
                    instance.renderGlint(p_117119_, p_117120_, p_117123_, model);
                }
            }
        }
        ci.cancel();
    }

    @Unique
    private void mythocraft$originalCall(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Model model, int dyeColor, ResourceLocation textureLocation) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.armorCutoutNoCull(textureLocation));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, dyeColor);
    }

}
