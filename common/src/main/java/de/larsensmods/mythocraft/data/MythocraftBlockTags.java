package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class MythocraftBlockTags {
    public static final TagKey<Block> IS_CHEST = TagKey.create(Registries.BLOCK, new ResourceLocation(Constants.MOD_ID, "is_chest"));
}
