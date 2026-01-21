package de.larsensmods.mythocraft.world.generation;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.MythocraftBiomeTags;
import de.larsensmods.mythocraft.entity.MythEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class MythocraftBiomeModifiers {

    public static ResourceKey<BiomeModifier> SPAWN_SATYR = registerKey("spawn_satyr");
    public static ResourceKey<BiomeModifier> SPAWN_PEGASUS = registerKey("spawn_pegasus");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_SATYR, new BiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(BiomeTags.IS_FOREST),
                List.of(new MobSpawnSettings.SpawnerData(MythEntities.SATYR.get(), 20, 1, 2))));
        context.register(SPAWN_PEGASUS, new BiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(MythocraftBiomeTags.SPAWNS_PEGASUS),
                List.of(new MobSpawnSettings.SpawnerData(MythEntities.PEGASUS.get(), 1, 2, 4))));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

}
