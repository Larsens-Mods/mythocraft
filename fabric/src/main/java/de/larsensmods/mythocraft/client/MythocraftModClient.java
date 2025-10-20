package de.larsensmods.mythocraft.client;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.client.SatyrModel;
import de.larsensmods.mythocraft.entity.client.SatyrRenderer;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.entity.EntityType;

@Environment(EnvType.CLIENT)
public class MythocraftModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.registerEntityRenderers();
        this.registerEntityModelLayers();
    }

    @SuppressWarnings("unchecked")
    private void registerEntityRenderers(){
        EntityRendererRegistry.register((EntityType<SatyrEntity>) MythEntities.SATYR.get(), SatyrRenderer::new);
    }

    private void registerEntityModelLayers(){
        EntityModelLayerRegistry.registerModelLayer(SatyrModel.LAYER_LOCATION, SatyrModel::createBodyLayer);
    }
}
