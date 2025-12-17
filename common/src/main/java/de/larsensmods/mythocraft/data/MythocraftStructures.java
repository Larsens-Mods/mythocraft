package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class MythocraftStructures {

    public static ResourceKey<Structure> GREEK_TEMPLE = ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "greek_temple"));

}
