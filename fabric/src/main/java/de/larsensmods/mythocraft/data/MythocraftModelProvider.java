package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.block.MythBlocks;
import de.larsensmods.mythocraft.item.MythItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;

public class MythocraftModelProvider extends FabricModelProvider {

    public MythocraftModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        blockStateModelGenerator.createTrivialCube(MythBlocks.LABYRINTH_BARRIER_ROCK.get());
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(MythItems.AMBROSIA.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(MythItems.NEMEAN_LION_PELT.get(), ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(MythItems.NEMEAN_COAT.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(MythItems.HADES_HELM.get(), ModelTemplates.FLAT_ITEM);

        itemModelGenerator.generateFlatItem(MythItems.SATYR_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.generateFlatItem(MythItems.PEGASUS_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.generateFlatItem(MythItems.NEMEAN_LION_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.generateFlatItem(MythItems.CYCLOPS_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.generateFlatItem(MythItems.BLACKSMITH_CYCLOPS_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
        itemModelGenerator.generateFlatItem(MythItems.MINOTAUR_SPAWN_EGG.get(), new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/template_spawn_egg")), Optional.empty()));
    }
}
