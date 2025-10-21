package de.larsensmods.mythocraft.event;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.client.SatyrModel;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SatyrModel.LAYER_LOCATION, SatyrModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(MythEntities.SATYR.get(), SatyrEntity.createAttributes().build());
    }
}
