package de.larsensmods.mythocraft.platform;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties) {
        Item item = Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, itemID), factory.apply(properties));
        return () -> item;
    }

}
