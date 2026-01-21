package de.larsensmods.mythocraft.event;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.client.NemeanLionModel;
import de.larsensmods.mythocraft.entity.client.PegasusModel;
import de.larsensmods.mythocraft.entity.client.SatyrModel;
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
    }

}
