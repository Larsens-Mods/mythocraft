package de.larsensmods.mythocraft.platform;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.platform.services.IRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    private static final Map<String, Function<?, ?>> OVERRIDES = new HashMap<>();

    public static void addOverride(String id, Function<?, ?> function) {
        OVERRIDES.put(id, function);
    }

    public static void finishRegistration(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        ITEMS.register(eventBus);
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntityType(String entityID, Supplier<EntityType<T>> entityTypeSupplier) {
        return ENTITY_TYPES.register(entityID, entityTypeSupplier);
    }

    @Override
    public Supplier<Item> registerItem(String itemID, Function<Item.Properties, Item> factory, Item.Properties properties) {
        if(OVERRIDES.containsKey(itemID)){
            return ITEMS.register(itemID, () -> ((Function<Item.Properties, Item>) OVERRIDES.get(itemID)).apply(properties));
        }
        return ITEMS.register(itemID, () -> factory.apply(properties));
    }

}
