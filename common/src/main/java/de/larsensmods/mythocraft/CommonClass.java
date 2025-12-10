package de.larsensmods.mythocraft;

import de.larsensmods.lmcc.api.entity.AttributeRegistry;
import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import de.larsensmods.mythocraft.item.MythCreativeTabs;
import de.larsensmods.mythocraft.item.MythItems;
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

        MythEntities.registerEntityTypes();
        MythItems.registerItems();
        MythCreativeTabs.registerCreativeTabs();

        createEntityAttributes();
    }

    private static void createEntityAttributes(){
        AttributeRegistry.register(MythEntities.SATYR, () -> SatyrEntity.createAttributes().build());
        AttributeRegistry.register(MythEntities.NEMEAN_LION, () -> NemeanLionEntity.createAttributes().build());
    }
}
