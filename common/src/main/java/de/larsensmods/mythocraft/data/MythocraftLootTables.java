package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class MythocraftLootTables {

    //ENTITIES
    public static ResourceKey<LootTable> NEMEAN_LION_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entities/nemean_lion"));
    public static ResourceKey<LootTable> SATYR_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entities/satyr"));
    public static ResourceKey<LootTable> PEGASUS_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entities/pegasus"));
    public static ResourceKey<LootTable> CYCLOPS_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entities/cyclops"));
    public static ResourceKey<LootTable> BLACKSMITH_CYCLOPS_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entities/blacksmith_cyclops"));
    public static ResourceKey<LootTable> MINOTAUR_LOOT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entities/minotaur"));

}
