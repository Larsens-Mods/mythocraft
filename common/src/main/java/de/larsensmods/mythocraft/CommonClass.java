package de.larsensmods.mythocraft;

import de.larsensmods.lmcc.api.entity.AttributeRegistry;
import de.larsensmods.lmcc.api.entity.SpawnPlacementsRegistry;
import de.larsensmods.mythocraft.block.MythBlocks;
import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.BlacksmithCyclopsEntity;
import de.larsensmods.mythocraft.entity.friendly.PegasusEntity;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.entity.monster.CyclopsEntity;
import de.larsensmods.mythocraft.entity.monster.MinotaurEntity;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import de.larsensmods.mythocraft.item.MythArmorMaterials;
import de.larsensmods.mythocraft.item.MythCreativeTabs;
import de.larsensmods.mythocraft.item.MythItems;
import de.larsensmods.mythocraft.platform.Services;
import de.larsensmods.mythocraft.world.level.MythChunkGenerators;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;

//Common entrypoint class
public class CommonClass {

    /**
     * Common initialization method called by all supported loaders.
     */
    public static void init() {
        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        if (Services.PLATFORM.isModLoaded("mythocraft")) {
            Constants.LOG.info("Hello to mythocraft");
        }

        MythEntities.registerEntityTypes();
        MythArmorMaterials.registerArmorMaterials();
        MythBlocks.registerBlocks();
        MythItems.registerItems();
        MythCreativeTabs.registerCreativeTabs();
        MythChunkGenerators.registerChunkGenerators();

        createEntityAttributes();
        registerSpawnPlacements();
    }

    private static void createEntityAttributes(){
        AttributeRegistry.register(MythEntities.SATYR, () -> SatyrEntity.createAttributes().build());
        AttributeRegistry.register(MythEntities.PEGASUS, () -> PegasusEntity.createAttributes().build());
        AttributeRegistry.register(MythEntities.NEMEAN_LION, () -> NemeanLionEntity.createAttributes().build());
        AttributeRegistry.register(MythEntities.CYCLOPS, () -> CyclopsEntity.createAttributes().build());
        AttributeRegistry.register(MythEntities.BLACKSMITH_CYCLOPS, () -> BlacksmithCyclopsEntity.createAttributes().build());
        AttributeRegistry.register(MythEntities.MINOTAUR, () -> MinotaurEntity.createAttributes().build());
    }

    private static void registerSpawnPlacements(){
        SpawnPlacementsRegistry.register(MythEntities.SATYR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SatyrEntity::checkSatyrSpawnRules);
        SpawnPlacementsRegistry.register(MythEntities.PEGASUS, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PegasusEntity::checkPegasusSpawnRules);
        SpawnPlacementsRegistry.register(MythEntities.NEMEAN_LION, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NemeanLionEntity::checkAnyLightMonsterSpawnRules);
        SpawnPlacementsRegistry.register(MythEntities.CYCLOPS, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CyclopsEntity::checkAnyLightMonsterSpawnRules);
        SpawnPlacementsRegistry.register(MythEntities.BLACKSMITH_CYCLOPS, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlacksmithCyclopsEntity::checkMobSpawnRules);
        SpawnPlacementsRegistry.register(MythEntities.MINOTAUR, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MinotaurEntity::checkAnyLightMonsterSpawnRules);
    }
}
