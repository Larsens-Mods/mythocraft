package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.items.MythItems;
import de.larsensmods.mythocraft.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

//Common entrypoint class
public class CommonClass {

    /**
     * Common initialization method called by all supported loaders.
     */
    public static void init() {
        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        if (Services.PLATFORM.isModLoaded("mythocraft")) {
            Constants.LOG.info("Hello to mythocraft");
        }

        MythItems.registerItems();
    }
}
