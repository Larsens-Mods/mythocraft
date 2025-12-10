package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import de.larsensmods.mythocraft.world.generation.MythocraftEntitySpawns;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

public class MythocraftMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        this.createEntityAttributes();
        this.registerSpawnPlacements();

        MythocraftEntitySpawns.addSpawns();
    }

    private void createEntityAttributes(){
        FabricDefaultAttributeRegistry.register(MythEntities.SATYR.get(), SatyrEntity.createAttributes().build());
        FabricDefaultAttributeRegistry.register(MythEntities.NEMEAN_LION.get(), NemeanLionEntity.createAttributes().build());
    }

    private void registerSpawnPlacements(){
        SpawnPlacements.register(MythEntities.SATYR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SatyrEntity::checkSatyrSpawnRules);
    }
}
