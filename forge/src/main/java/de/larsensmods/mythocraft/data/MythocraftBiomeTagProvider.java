package de.larsensmods.mythocraft.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
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
    }
}
