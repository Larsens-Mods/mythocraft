package de.larsensmods.mythocraft;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class MythocraftMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        this.createEntityAttributes();
    }

    private void createEntityAttributes(){
        FabricDefaultAttributeRegistry.register(MythEntities.SATYR.get(), SatyrEntity.createAttributes().build());
    }
}
