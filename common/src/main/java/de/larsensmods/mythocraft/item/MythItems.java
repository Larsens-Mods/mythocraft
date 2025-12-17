package de.larsensmods.mythocraft.item;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.lmcc.api.wrappers.item.WrappedSpawnEggItem;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.MythEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MythItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);

    public static Supplier<Item> AMBROSIA;
    public static Supplier<Item> NEMEAN_LION_PELT;

    public static Supplier<Item> NEMEAN_COAT;
    public static Supplier<Item> HADES_HELM;

    public static Supplier<Item> SATYR_SPAWN_EGG;
    public static Supplier<Item> NEMEAN_LION_SPAWN_EGG;

    public static void registerItems(){
        AMBROSIA = ITEMS.register("ambrosia", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(20).saturationModifier(0.5f).alwaysEdible().effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2), 1f).build())));
        NEMEAN_LION_PELT = ITEMS.register("nemean_lion_pelt", () -> new Item(new Item.Properties()));

        NEMEAN_COAT = ITEMS.register("nemean_coat", () -> new ArmorItem(MythArmorMaterials.NEMEAN, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(500)));
        HADES_HELM = ITEMS.register("hades_helm", () -> new ArmorItem(MythArmorMaterials.HADES, ArmorItem.Type.HELMET, new Item.Properties().durability(450)));

        SATYR_SPAWN_EGG = ITEMS.register("satyr_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.SATYR, 0x978F88, 0xF7D1AB, new Item.Properties()));
        NEMEAN_LION_SPAWN_EGG = ITEMS.register("nemean_lion_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.NEMEAN_LION, 0xDEA765, 0xB87B32, new Item.Properties()));

        ITEMS.register();
    }

}
