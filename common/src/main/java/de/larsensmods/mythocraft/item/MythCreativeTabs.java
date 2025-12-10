package de.larsensmods.mythocraft.item;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class MythCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static Supplier<CreativeModeTab> MYTH_ITEMS;

    public static void registerCreativeTabs(){
        MYTH_ITEMS = CREATIVE_TABS.register("myth_items", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemGroup.mythocraft.myth_items"))
                .icon(() -> new ItemStack(MythItems.NEMEAN_LION_PELT.get()))
                .displayItems((params, output) -> {
                    output.accept(MythItems.AMBROSIA.get());
                    output.accept(MythItems.NEMEAN_LION_PELT.get());

                    output.accept(MythItems.SATYR_SPAWN_EGG.get());
                })
                .build());

        CREATIVE_TABS.register();
    }

}
