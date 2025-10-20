package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.platform.ForgeRegistryHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
public class MythocraftMod {

    public MythocraftMod() {
        Constants.LOG.info("Hello Forge world!");

        Constants.LOG.info("Setting up forge overrides!");
        setupItemOverrides();

        CommonClass.init();

        ForgeRegistryHelper.finishRegistration(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setupItemOverrides(){
        ForgeRegistryHelper.addOverride("satyr_spawn_egg", (Function<Item.Properties, Item>) properties -> new ForgeSpawnEggItem(MythEntities.SATYR, 0x978F88, 0xF7D1AB, properties));
    }
}
