package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;

public class MythocraftItemModelProvider extends ItemModelProvider {

    public MythocraftItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(MythItems.AMBROSIA.get());

        withExistingParent(Objects.requireNonNull(((DeferredHolder<Item, Item>) MythItems.SATYR_SPAWN_EGG).getId()).getPath(), mcLoc("item/template_spawn_egg"));
    }

}
