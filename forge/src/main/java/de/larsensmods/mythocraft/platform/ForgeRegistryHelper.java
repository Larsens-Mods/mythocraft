package de.larsensmods.mythocraft.platform;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.platform.services.IRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID );

    public static void finishRegistration(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @Override
    public Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return ITEMS.register(itemID, () -> factory.apply(properties));
    }

}
