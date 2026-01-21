package de.larsensmods.mythocraft.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.PegasusEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PegasusRenderer extends MobRenderer<PegasusEntity, PegasusModel<PegasusEntity>> {

    private static final Map<PegasusEntity.Variant, ResourceLocation> TEXTURE_MAP = Util.make(Maps.newEnumMap(PegasusEntity.Variant.class), map -> {
        map.put(PegasusEntity.Variant.WHITE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_white.png"));
        map.put(PegasusEntity.Variant.BLACK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_black.png"));
        map.put(PegasusEntity.Variant.BROWN, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_brown.png"));
        map.put(PegasusEntity.Variant.CHESTNUT, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_chestnut.png"));
        map.put(PegasusEntity.Variant.CREAMY, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_creamy.png"));
        map.put(PegasusEntity.Variant.DARK_BROWN, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_darkbrown.png"));
        map.put(PegasusEntity.Variant.GRAY, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_gray.png"));
    });
    private static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/pegasus/pegasus_white.png");

    public PegasusRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PegasusModel<>(pContext.bakeLayer(PegasusModel.LAYER_LOCATION)), 0.75f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PegasusEntity pegasusEntity) {
        return TEXTURE_MAP.getOrDefault(pegasusEntity.getVariant(), DEFAULT_TEXTURE);
    }

    @Override
    public void render(@NotNull PegasusEntity pEntity, float pEntityYaw, float pPartialTicks, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()){
            pPoseStack.scale(0.6f, 0.6f, 0.6f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
