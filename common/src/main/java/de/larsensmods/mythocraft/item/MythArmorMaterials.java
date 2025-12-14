package de.larsensmods.mythocraft.item;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.lmcc.api.registry.DeferredSupplier;
import de.larsensmods.mythocraft.Constants;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;

public class MythArmorMaterials {

    private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, Constants.MOD_ID);

    public static DeferredSupplier<ArmorMaterial> NEMEAN;
    public static DeferredSupplier<ArmorMaterial> HADES;

    public static void registerArmorMaterials(){
        NEMEAN = ARMOR_MATERIALS.register("nemean", () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 3);
                    map.put(ArmorItem.Type.LEGGINGS, 6);
                    map.put(ArmorItem.Type.CHESTPLATE, 8);
                    map.put(ArmorItem.Type.HELMET, 3);
                    map.put(ArmorItem.Type.BODY, 11);
                }),
                25,
                SoundEvents.ARMOR_EQUIP_LEATHER,
                () -> Ingredient.of(MythItems.NEMEAN_LION_PELT.get()),
                List.of(
                        new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "nemean"))
                ),
                4.0f,
                0.1f
        ));
        HADES = ARMOR_MATERIALS.register("hades", () -> new ArmorMaterial(
                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                    map.put(ArmorItem.Type.BOOTS, 2);
                    map.put(ArmorItem.Type.LEGGINGS, 5);
                    map.put(ArmorItem.Type.CHESTPLATE, 6);
                    map.put(ArmorItem.Type.HELMET, 3);
                    map.put(ArmorItem.Type.BODY, 5);
                }),
                25,
                SoundEvents.ARMOR_EQUIP_IRON,
                () -> Ingredient.of(Items.IRON_BLOCK),
                List.of(
                        new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hades"))
                ),
                3f,
                0f
        ));

        ARMOR_MATERIALS.register();
    }

}
