package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.item.MythItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class MythocraftRecipeProvider extends FabricRecipeProvider {

    public MythocraftRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> exporter) {
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
