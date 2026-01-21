package de.larsensmods.mythocraft.event;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.client.NemeanLionRenderer;
import de.larsensmods.mythocraft.entity.client.PegasusRenderer;
import de.larsensmods.mythocraft.entity.client.SatyrRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventBusEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(MythEntities.SATYR.get(), SatyrRenderer::new);
        EntityRenderers.register(MythEntities.PEGASUS.get(), PegasusRenderer::new);
        EntityRenderers.register(MythEntities.NEMEAN_LION.get(), NemeanLionRenderer::new);
    }

}
