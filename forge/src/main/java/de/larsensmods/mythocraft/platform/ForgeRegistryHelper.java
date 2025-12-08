package de.larsensmods.mythocraft.platform;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.platform.services.IRegistryHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    private static final Map<String, Function<?, ?>> OVERRIDES = new HashMap<>();

    public static void addOverride(String id, Function<?, ?> function) {
        OVERRIDES.put(id, function);
    }

    public static void finishRegistration(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @Override
    public Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties) {
        if(OVERRIDES.containsKey(itemID)){
            return ITEMS.register(itemID, () -> ((Function<Item.Properties, Item>) OVERRIDES.get(itemID)).apply(properties));
        }
        return ITEMS.register(itemID, () -> factory.apply(properties));
    }

}
