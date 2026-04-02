package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class MythocraftBiomeTags {

    public static final TagKey<Biome> HAS_GREEK_TEMPLE = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "has_greek_temple"));
    public static final TagKey<Biome> HAS_CYCLOPS_CAVE = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "has_cyclops_cave"));
    public static final TagKey<Biome> HAS_CYCLOPS_FORGE = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "has_cyclops_forge"));
    public static final TagKey<Biome> SPAWNS_PEGASUS = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "spawns_pegasus"));
    public static final TagKey<Biome> HAS_LABYRINTH_STONE_PORTAL = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth_stone_portal"));
    public static final TagKey<Biome> HAS_LABYRINTH_SAND_PORTAL = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth_sand_portal"));

}
