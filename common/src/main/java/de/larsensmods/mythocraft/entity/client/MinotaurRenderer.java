package de.larsensmods.mythocraft.entity.client;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.monster.MinotaurEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MinotaurRenderer extends MobRenderer<MinotaurEntity, MinotaurModel<MinotaurEntity>> {

    public MinotaurRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new MinotaurModel<>(pContext.bakeLayer(MinotaurModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MinotaurEntity minotaurEntity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/minotaur.png");
    }
}
