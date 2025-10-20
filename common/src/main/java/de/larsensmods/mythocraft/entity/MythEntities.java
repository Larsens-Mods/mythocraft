package de.larsensmods.mythocraft.entity;

import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.platform.Services;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class MythEntities {

    public static Supplier<EntityType<SatyrEntity>> SATYR;

    public static void registerEntityTypes(){
        SATYR = Services.REGISTRY.registerEntityType("satyr", () -> EntityType.Builder.of(SatyrEntity::new, MobCategory.CREATURE).sized(0.6f, 1.8f).build("satyr"));
    }

}
