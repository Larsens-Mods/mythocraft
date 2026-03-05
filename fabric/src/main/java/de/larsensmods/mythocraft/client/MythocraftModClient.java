package de.larsensmods.mythocraft.client;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.client.*;
import de.larsensmods.mythocraft.entity.client.cyclops.BlacksmithCyclopsModel;
import de.larsensmods.mythocraft.entity.client.cyclops.BlacksmithCyclopsRenderer;
import de.larsensmods.mythocraft.entity.client.cyclops.CyclopsModel;
import de.larsensmods.mythocraft.entity.client.cyclops.CyclopsRenderer;
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
        EntityRendererRegistry.register(MythEntities.CYCLOPS.get(), CyclopsRenderer::new);
        EntityRendererRegistry.register(MythEntities.BLACKSMITH_CYCLOPS.get(), BlacksmithCyclopsRenderer::new);
    }

    private void registerEntityModelLayers(){
        EntityModelLayerRegistry.registerModelLayer(SatyrModel.LAYER_LOCATION, SatyrModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(PegasusModel.LAYER_LOCATION, PegasusModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NemeanLionModel.LAYER_LOCATION, NemeanLionModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(CyclopsModel.LAYER_LOCATION, CyclopsModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(BlacksmithCyclopsModel.LAYER_LOCATION, BlacksmithCyclopsModel::createBodyLayer);
    }
}
