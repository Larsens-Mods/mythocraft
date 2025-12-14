package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.item.MythItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class MythocraftModelProvider extends FabricModelProvider {

    public MythocraftModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(MythItems.AMBROSIA.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(MythItems.NEMEAN_LION_PELT.get(), ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(MythItems.NEMEAN_COAT.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(MythItems.HADES_HELM.get(), ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(MythItems.SATYR_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.generateFlatItem(MythItems.NEMEAN_LION_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
    }
}
