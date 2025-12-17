package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.loot.MythocraftLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MythocraftDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //Models
        generator.addProvider(event.includeClient(), new MythocraftItemModelProvider(packOutput, existingFileHelper));

        //Datapack
        generator.addProvider(event.includeServer(), new MythocraftDatapackEntries(packOutput, lookupProvider));

        //LootTables
        generator.addProvider(event.includeServer(), new MythocraftLootTableProvider(packOutput, lookupProvider));

        //Crafting
        generator.addProvider(event.includeServer(), new MythocraftRecipeProvider(packOutput, lookupProvider));

        //Tags
        generator.addProvider(event.includeServer(), new MythocraftBiomeTagProvider(packOutput, lookupProvider));
    }

}
