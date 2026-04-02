package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class MythocraftStructureTags {

    public static final TagKey<Structure> IS_LABYRINTH_PORTAL = TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "is_labyrinth_portal"));

}
