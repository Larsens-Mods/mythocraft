package de.larsensmods.mythocraft.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SatyrRenderer extends MobRenderer<SatyrEntity, SatyrModel<SatyrEntity>> {

    public SatyrRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SatyrModel<>(pContext.bakeLayer(SatyrModel.LAYER_LOCATION)), 0.3f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SatyrEntity satyrEntity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/satyr/satyr1.png");
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
