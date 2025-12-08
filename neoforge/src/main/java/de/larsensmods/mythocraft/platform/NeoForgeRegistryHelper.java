package de.larsensmods.mythocraft.platform;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.platform.services.IRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, Constants.MOD_ID);

    public static void finishRegistration(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @Override
    public Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties) {
        return ITEMS.register(itemID, () -> factory.apply(properties));
    }

}
