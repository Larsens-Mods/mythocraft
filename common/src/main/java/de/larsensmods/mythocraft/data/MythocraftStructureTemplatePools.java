package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class MythocraftStructureTemplatePools {

    public static ResourceKey<StructureTemplatePool> GREEK_TEMPLE = ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "greek_temple"));
    public static ResourceKey<StructureTemplatePool> CYCLOPS_CAVE = ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cyclops_cave/cyclops_cave"));
    public static ResourceKey<StructureTemplatePool> CYCLOPS_PEN = ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cyclops_cave/pen"));
    public static ResourceKey<StructureTemplatePool> CYCLOPS_FORGE = ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "cyclops_forge"));
    public static ResourceKey<StructureTemplatePool> LABYRINTH_PORTAL = ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth_portal_stonebricks"));
    public static ResourceKey<StructureTemplatePool> LABYRINTH_PORTAL_SANDSTONE = ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth_portal_sandstone"));

}
