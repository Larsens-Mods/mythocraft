package de.larsensmods.mythocraft.item;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

import java.util.function.Supplier;

public class MythItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);

    public static Supplier<Item> AMBROSIA;

    public static Supplier<Item> SATYR_SPAWN_EGG;

    public static void registerItems(){
        AMBROSIA = ITEMS.register("ambrosia", () -> new Item(new Item.Properties()));

        SATYR_SPAWN_EGG = Services.REGISTRY.registerItem("satyr_spawn_egg", properties ->
                new SpawnEggItem((EntityType<SatyrEntity>) MythEntities.SATYR.get(), 0x978F88, 0xF7D1AB, properties), new Item.Properties());

        ITEMS.register();
    }

}
