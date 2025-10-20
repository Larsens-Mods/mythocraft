package de.larsensmods.mythocraft.platform.services;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IRegistryHelper {

    <T extends Entity> Supplier<EntityType<T>> registerEntityType(String entityID, Supplier<EntityType<T>> typeSupplier);
    Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties);

}
