package de.larsensmods.mythocraft.item;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.platform.Services;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class MythItems {

    public static Supplier<Item> AMBROSIA;

    public static Supplier<Item> SATYR_SPAWN_EGG;

    public static void registerItems(){
        AMBROSIA = Services.REGISTRY.registerItem("ambrosia", Item::new, new Item.Properties());

        SATYR_SPAWN_EGG = Services.REGISTRY.registerItem("satyr_spawn_egg", properties ->
                new SpawnEggItem((EntityType<SatyrEntity>) MythEntities.SATYR.get(), 0x978F88, 0xF7D1AB, properties), new Item.Properties());
    }

}
