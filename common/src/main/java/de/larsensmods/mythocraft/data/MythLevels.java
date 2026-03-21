package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class MythLevels {

    public static final ResourceKey<Level> LABYRINTH = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth"));

}
