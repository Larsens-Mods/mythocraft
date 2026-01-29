package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class MythocraftStructureSets {

    public static ResourceKey<StructureSet> GREEK_TEMPLE = ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "greek_temple"));
    public static ResourceKey<StructureSet> CYCLOPS_CAVE = ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cyclops_cave"));
    public static ResourceKey<StructureSet> CYCLOPS_FORGE = ResourceKey.create(Registries.STRUCTURE_SET, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cyclops_forge"));

}
