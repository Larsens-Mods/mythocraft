package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class MythocraftItemModelProvider extends ItemModelProvider {

    public MythocraftItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MythItems.AMBROSIA.get());
        basicItem(MythItems.NEMEAN_LION_PELT.get());

        basicItem(MythItems.NEMEAN_COAT.get());

        withExistingParent(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(MythItems.SATYR_SPAWN_EGG.get())).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(MythItems.NEMEAN_LION_SPAWN_EGG.get())).getPath(), mcLoc("item/template_spawn_egg"));
    }

}
