package de.larsensmods.mythocraft.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MythocraftBiomeTagProvider extends BiomeTagsProvider {

    public MythocraftBiomeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider wrapperLookup) {
        tag(MythocraftBiomeTags.HAS_GREEK_TEMPLE)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT, Biomes.SNOWY_PLAINS);
        tag(MythocraftBiomeTags.HAS_CYCLOPS_CAVE)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.FOREST);
        tag(MythocraftBiomeTags.HAS_CYCLOPS_FORGE)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        tag(MythocraftBiomeTags.SPAWNS_PEGASUS)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);

        tag(MythocraftBiomeTags.HAS_LABYRINTH_STONE_PORTAL)
                .add(Biomes.MUSHROOM_FIELDS)
                .add(Biomes.JAGGED_PEAKS, Biomes.FROZEN_PEAKS, Biomes.STONY_PEAKS, Biomes.MEADOW, Biomes.CHERRY_GROVE, Biomes.GROVE, Biomes.SNOWY_SLOPES, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST)
                .add(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.SNOWY_TAIGA, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE)
                .add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.STONY_SHORE)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES)
                .add(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS);
        tag(MythocraftBiomeTags.HAS_LABYRINTH_SAND_PORTAL)
                .add(Biomes.BEACH, Biomes.SNOWY_BEACH, Biomes.DESERT);
    }
}
