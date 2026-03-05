package de.larsensmods.mythocraft.entity.client.cyclops;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.monster.CyclopsEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CyclopsRenderer extends MobRenderer<CyclopsEntity, CyclopsModel<CyclopsEntity>> {

    public CyclopsRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CyclopsModel<>(pContext.bakeLayer(CyclopsModel.LAYER_LOCATION)), 0.7f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CyclopsEntity cyclopsEntity) {
        return new ResourceLocation(Constants.MOD_ID, "textures/entity/cyclops/cyclops.png");
    }
}
