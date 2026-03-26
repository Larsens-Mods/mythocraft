package de.larsensmods.mythocraft.item;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.lmcc.api.wrappers.item.WrappedSpawnEggItem;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.block.MythBlocks;
import de.larsensmods.mythocraft.entity.MythEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MythItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);

    public static Supplier<Item> AMBROSIA;
    public static Supplier<Item> NEMEAN_LION_PELT;
    public static Supplier<Item> MINOTAUR_HORN;

    public static Supplier<Item> NEMEAN_COAT;
    public static Supplier<Item> HADES_HELM;

    public static Supplier<Item> LABYRINTH_BARRIER_ROCK;

    public static Supplier<Item> SATYR_SPAWN_EGG;
    public static Supplier<Item> PEGASUS_SPAWN_EGG;
    public static Supplier<Item> NEMEAN_LION_SPAWN_EGG;
    public static Supplier<Item> CYCLOPS_SPAWN_EGG;
    public static Supplier<Item> BLACKSMITH_CYCLOPS_SPAWN_EGG;

    public static void registerItems(){
        AMBROSIA = ITEMS.register("ambrosia", () -> new AmbrosiaItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(20).saturationModifier(0.5f).alwaysEdible().effect(new MobEffectInstance(MobEffects.REGENERATION, 200, 2), 1f).build())));
        NEMEAN_LION_PELT = ITEMS.register("nemean_lion_pelt", () -> new Item(new Item.Properties()));
        MINOTAUR_HORN = ITEMS.register("minotaur_horn", () -> new Item(new Item.Properties()));

        NEMEAN_COAT = ITEMS.register("nemean_coat", () -> new ArmorItem(MythArmorMaterials.NEMEAN, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(500)));
        HADES_HELM = ITEMS.register("hades_helm", () -> new ArmorItem(MythArmorMaterials.HADES, ArmorItem.Type.HELMET, new Item.Properties().durability(450)));

        LABYRINTH_BARRIER_ROCK = ITEMS.register("labyrinth_barrier_rock", () -> new BlockItem(MythBlocks.LABYRINTH_BARRIER_ROCK.get(), new Item.Properties()));

        SATYR_SPAWN_EGG = ITEMS.register("satyr_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.SATYR, 0x978F88, 0xF7D1AB, new Item.Properties()));
        PEGASUS_SPAWN_EGG = ITEMS.register("pegasus_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.PEGASUS, 0xA6A6A6, 0x858585, new Item.Properties()));
        NEMEAN_LION_SPAWN_EGG = ITEMS.register("nemean_lion_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.NEMEAN_LION, 0xDEA765, 0xB87B32, new Item.Properties()));
        CYCLOPS_SPAWN_EGG = ITEMS.register("cyclops_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.CYCLOPS, 0xCFB198, 0x00AEF9, new Item.Properties()));
        BLACKSMITH_CYCLOPS_SPAWN_EGG = ITEMS.register("blacksmith_cyclops_spawn_egg", () -> new WrappedSpawnEggItem(MythEntities.BLACKSMITH_CYCLOPS, 0xCFB198, 0x23C700, new Item.Properties()));

        ITEMS.register();
    }

}
