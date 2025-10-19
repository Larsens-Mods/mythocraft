package de.larsensmods.mythocraft.items;

import de.larsensmods.mythocraft.platform.Services;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MythItems {

    public static Supplier<Item> AMBROSIA;

    public static void registerItems(){
        AMBROSIA = Services.REGISTRY.registerItem("ambrosia", Item::new, new Item.Properties());
    }

}
