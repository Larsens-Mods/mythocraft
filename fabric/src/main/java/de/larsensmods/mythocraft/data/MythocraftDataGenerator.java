package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.data.loot.MythocraftEntityLootProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MythocraftDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(MythocraftModelProvider::new);
        pack.addProvider(MythocraftEntityLootProvider::new);
        pack.addProvider(MythocraftRecipeProvider::new);
    }
}
