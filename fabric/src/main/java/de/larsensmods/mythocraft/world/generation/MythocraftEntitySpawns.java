package de.larsensmods.mythocraft.world.generation;

import de.larsensmods.mythocraft.entity.MythEntities;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;

public class MythocraftEntitySpawns {

    public static void addSpawns() {
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST), MobCategory.CREATURE, MythEntities.SATYR.get(), 20, 1, 2);
    }

}
