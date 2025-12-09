package de.larsensmods.mythocraft.item;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.lmcc.api.wrappers.item.WrappedSpawnEggItem;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.MythEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MythItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);

    public static Supplier<Item> AMBROSIA;

    public static Supplier<Item> SATYR_SPAWN_EGG;

    public static void registerItems(){
        AMBROSIA = ITEMS.register("ambrosia", () -> new Item(new Item.Properties()));

        SATYR_SPAWN_EGG = ITEMS.register("satyr_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.SATYR, 0x978F88, 0xF7D1AB, new Item.Properties()));

        ITEMS.register();
    }

}
