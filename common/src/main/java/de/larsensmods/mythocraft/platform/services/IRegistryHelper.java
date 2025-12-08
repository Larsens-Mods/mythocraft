package de.larsensmods.mythocraft.platform.services;

import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IRegistryHelper {

    Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties);

}
