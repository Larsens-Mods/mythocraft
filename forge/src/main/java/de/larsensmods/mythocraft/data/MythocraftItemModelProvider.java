package de.larsensmods.mythocraft.data;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

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
        basicItem(MythItems.HADES_HELM.get());

        withExistingParent(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(MythItems.SATYR_SPAWN_EGG.get())).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(MythItems.PEGASUS_SPAWN_EGG.get())).getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(MythItems.NEMEAN_LION_SPAWN_EGG.get())).getPath(), mcLoc("item/template_spawn_egg"));
    }

}
