package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.item.MythItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class MythocraftRecipeProvider extends FabricRecipeProvider {

    public MythocraftRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MythItems.NEMEAN_COAT.get(), 1)
                .pattern("N N")
                .pattern("NSN")
                .pattern("NSN")
                .define('N', MythItems.NEMEAN_LION_PELT.get())
                .define('S', Items.STRING)
                .unlockedBy("nemean_kill", FabricRecipeProvider.has(MythItems.NEMEAN_LION_PELT.get()))
                .save(exporter);
    }
}
