package de.larsensmods.mythocraft.entity;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.lmcc.api.registry.DeferredSupplier;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.friendly.PegasusEntity;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Set;

public class MythEntities {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Constants.MOD_ID);

    public static DeferredSupplier<EntityType<SatyrEntity>> SATYR;
    public static DeferredSupplier<EntityType<PegasusEntity>> PEGASUS;
    public static DeferredSupplier<EntityType<NemeanLionEntity>> NEMEAN_LION;

    public static void registerEntityTypes(){
        SATYR = ENTITY_TYPES.register("satyr", () -> EntityType.Builder.of(SatyrEntity::new, MobCategory.CREATURE).sized(0.65f, 2.4f).build("satyr"));
        PEGASUS = ENTITY_TYPES.register("pegasus", () -> EntityType.Builder.of(PegasusEntity::new, MobCategory.CREATURE).sized(1.3964844F, 1.6F).eyeHeight(1.52F).passengerAttachments(1.44375F).clientTrackingRange(10).build("pegasus"));
        NEMEAN_LION = ENTITY_TYPES.register("nemean_lion", () -> EntityType.Builder.of(NemeanLionEntity::new, MobCategory.MONSTER).sized(1.2f, 1.5f).build("nemean_lion"));

        ENTITY_TYPES.register();
    }

    public static Set<DeferredSupplier<? extends EntityType<?>>> getEntityTypes(){
        return Set.of(
                SATYR,
                PEGASUS,
                NEMEAN_LION
        );
    }

}
