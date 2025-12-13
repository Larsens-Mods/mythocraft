package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MythocraftRecipeProvider extends RecipeProvider {

    public MythocraftRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, MythItems.NEMEAN_COAT.get(), 1)
                .pattern("N N")
                .pattern("NSN")
                .pattern("NSN")
                .define('N', MythItems.NEMEAN_LION_PELT.get())
                .define('S', Items.STRING)
                .unlockedBy("nemean_kill", has(MythItems.NEMEAN_LION_PELT.get()))
                .save(recipeOutput);
    }
}
