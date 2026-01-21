package de.larsensmods.mythocraft.event;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.client.NemeanLionModel;
import de.larsensmods.mythocraft.entity.client.PegasusModel;
import de.larsensmods.mythocraft.entity.client.SatyrModel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SatyrModel.LAYER_LOCATION, SatyrModel::createBodyLayer);
        event.registerLayerDefinition(PegasusModel.LAYER_LOCATION, PegasusModel::createBodyLayer);
        event.registerLayerDefinition(NemeanLionModel.LAYER_LOCATION, NemeanLionModel::createBodyLayer);
    }

}
