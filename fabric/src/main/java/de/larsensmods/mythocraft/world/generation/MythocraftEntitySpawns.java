package de.larsensmods.mythocraft.world.generation;

import de.larsensmods.mythocraft.data.MythocraftBiomeTags;
import de.larsensmods.mythocraft.entity.MythEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;

public class MythocraftEntitySpawns {

    public static void addSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST), MobCategory.CREATURE, MythEntities.SATYR.get(), 20, 1, 2);
        BiomeModifications.addSpawn(BiomeSelectors.tag(MythocraftBiomeTags.SPAWNS_PEGASUS), MobCategory.CREATURE, MythEntities.PEGASUS.get(), 1, 2, 4);
    }

}
