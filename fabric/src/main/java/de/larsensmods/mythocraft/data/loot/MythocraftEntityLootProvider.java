package de.larsensmods.mythocraft.data.loot;

import de.larsensmods.mythocraft.data.MythocraftLootTables;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class MythocraftEntityLootProvider extends SimpleFabricLootTableProvider {

    public MythocraftEntityLootProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, LootContextParamSets.ENTITY);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(MythocraftLootTables.NEMEAN_LION_LOOT, NemeanLionEntity.getLootTableBuilder());
    }
}
