package de.larsensmods.mythocraft.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class MythocraftBiomeTagProvider extends FabricTagProvider<Biome> {

    public MythocraftBiomeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(MythocraftBiomeTags.HAS_GREEK_TEMPLE)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.DESERT, Biomes.SNOWY_PLAINS);
        tag(MythocraftBiomeTags.SPAWNS_PEGASUS)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
    }
}
