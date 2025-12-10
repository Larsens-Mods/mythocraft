package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.world.generation.MythocraftEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class MythocraftMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        this.registerSpawnPlacements();

        MythocraftEntitySpawns.addSpawns();
    }

    private void registerSpawnPlacements(){
        SpawnPlacements.register(MythEntities.SATYR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SatyrEntity::checkSatyrSpawnRules);
    }
}
