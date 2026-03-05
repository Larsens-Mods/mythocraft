package de.larsensmods.mythocraft.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class MythocraftLootTableProvider extends LootTableProvider {

    public MythocraftLootTableProvider(PackOutput packOutput) {
        super(packOutput, Set.of(), List.of(
                new SubProviderEntry(
                        MythocraftEntityLootProvider::new,
                        LootContextParamSets.ENTITY
                )
        ));
    }

}
