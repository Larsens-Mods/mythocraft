package de.larsensmods.mythocraft.entity;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.lmcc.api.registry.DeferredSupplier;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MythEntities {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Constants.MOD_ID);

    public static DeferredSupplier<EntityType<SatyrEntity>> SATYR;

    public static void registerEntityTypes(){
        SATYR = ENTITY_TYPES.register("satyr", () -> EntityType.Builder.of(SatyrEntity::new, MobCategory.CREATURE).sized(0.65f, 2.4f).build("satyr"));

        ENTITY_TYPES.register();
    }

}
