package de.larsensmods.mythocraft.entity.client.cyclops;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.BlacksmithCyclopsEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlacksmithCyclopsRenderer extends MobRenderer<BlacksmithCyclopsEntity, BlacksmithCyclopsModel<BlacksmithCyclopsEntity>> {

    public BlacksmithCyclopsRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BlacksmithCyclopsModel<>(pContext.bakeLayer(BlacksmithCyclopsModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BlacksmithCyclopsEntity cyclopsEntity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/cyclops/blacksmith_cyclops.png");
    }
}