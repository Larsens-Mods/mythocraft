package de.larsensmods.mythocraft.client;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.client.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class MythocraftModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        this.registerEntityRenderers();
        this.registerEntityModelLayers();
    }

    private void registerEntityRenderers(){
        EntityRendererRegistry.register(MythEntities.SATYR.get(), SatyrRenderer::new);
        EntityRendererRegistry.register(MythEntities.PEGASUS.get(), PegasusRenderer::new);
        EntityRendererRegistry.register(MythEntities.NEMEAN_LION.get(), NemeanLionRenderer::new);
    }

    private void registerEntityModelLayers(){
        EntityModelLayerRegistry.registerModelLayer(SatyrModel.LAYER_LOCATION, SatyrModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PegasusModel.LAYER_LOCATION, PegasusModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NemeanLionModel.LAYER_LOCATION, NemeanLionModel::createBodyLayer);
    }
}
