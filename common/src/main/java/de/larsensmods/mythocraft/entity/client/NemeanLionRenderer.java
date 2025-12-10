package de.larsensmods.mythocraft.entity.client;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class NemeanLionRenderer extends MobRenderer<NemeanLionEntity, NemeanLionModel<NemeanLionEntity>> {

    public NemeanLionRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new NemeanLionModel<>(pContext.bakeLayer(NemeanLionModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull NemeanLionEntity nemeanLionEntity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/nemean_lion.png");
    }
}
