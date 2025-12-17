package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.loot.MythocraftLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MythocraftDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //Models
        generator.addProvider(true /*event.includeClient()*/, new MythocraftItemModelProvider(packOutput, existingFileHelper));

        //Datapack
        generator.addProvider(true /*event.includeServer()*/, new MythocraftDatapackEntries(packOutput, lookupProvider));

        //LootTables
        generator.addProvider(true /*event.includeServer()*/, new MythocraftLootTableProvider(packOutput, lookupProvider));

        //Crafting
        generator.addProvider(true /*event.includeServer()*/, new MythocraftRecipeProvider(packOutput, lookupProvider));

        //Tags
        generator.addProvider(true /*event.includeServer()*/, new MythocraftBiomeTagProvider(packOutput, lookupProvider));
    }

}
