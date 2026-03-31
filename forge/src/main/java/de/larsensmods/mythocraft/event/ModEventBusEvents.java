package de.larsensmods.mythocraft.event;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.client.MinotaurModel;
import de.larsensmods.mythocraft.entity.client.NemeanLionModel;
import de.larsensmods.mythocraft.entity.client.PegasusModel;
import de.larsensmods.mythocraft.entity.client.SatyrModel;
import de.larsensmods.mythocraft.entity.client.cyclops.BlacksmithCyclopsModel;
import de.larsensmods.mythocraft.entity.client.cyclops.CyclopsModel;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SatyrModel.LAYER_LOCATION, SatyrModel::createBodyLayer);
        event.registerLayerDefinition(PegasusModel.LAYER_LOCATION, PegasusModel::createBodyLayer);
        event.registerLayerDefinition(NemeanLionModel.LAYER_LOCATION, NemeanLionModel::createBodyLayer);
        event.registerLayerDefinition(CyclopsModel.LAYER_LOCATION, CyclopsModel::createBodyLayer);
        event.registerLayerDefinition(BlacksmithCyclopsModel.LAYER_LOCATION, BlacksmithCyclopsModel::createBodyLayer);
        event.registerLayerDefinition(MinotaurModel.LAYER_LOCATION, MinotaurModel::createBodyLayer);
    }

}
