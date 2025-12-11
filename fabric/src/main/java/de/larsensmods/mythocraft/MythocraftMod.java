package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.world.generation.MythocraftEntitySpawns;
import net.fabricmc.api.ModInitializer;

public class MythocraftMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        MythocraftEntitySpawns.addSpawns();
    }
}
