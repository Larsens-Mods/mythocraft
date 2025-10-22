package de.larsensmods.mythocraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.entity.friendly.SatyrVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SatyrRenderer extends MobRenderer<SatyrEntity, SatyrModel<SatyrEntity>> {

    private static final Map<SatyrVariant, ResourceLocation> TEXTURE_MAP = Util.make(Maps.newEnumMap(SatyrVariant.class), map -> {
        map.put(SatyrVariant.VARIANT_0, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/satyr/satyr1.png"));
        map.put(SatyrVariant.VARIANT_1, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/satyr/satyr2.png"));
    });
    private static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/satyr/satyr1.png");

    public SatyrRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SatyrModel<>(pContext.bakeLayer(SatyrModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SatyrEntity satyrEntity) {
        return TEXTURE_MAP.getOrDefault(satyrEntity.getVariant(), DEFAULT_TEXTURE);
    }

    @Override
    public void render(@NotNull SatyrEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()){
            pPoseStack.scale(0.6f, 0.6f, 0.6f);
        }else{
            pPoseStack.scale(0.9f, 0.9f, 0.9f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
