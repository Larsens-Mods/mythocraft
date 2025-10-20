package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;

public class MythocraftMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        this.createEntityAttributes();
    }

    @SuppressWarnings("unchecked")
    private void createEntityAttributes(){
        FabricDefaultAttributeRegistry.register((EntityType<SatyrEntity>) MythEntities.SATYR.get(), SatyrEntity.createAttributes().build());
    }
}
