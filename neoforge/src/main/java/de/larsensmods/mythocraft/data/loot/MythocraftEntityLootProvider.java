package de.larsensmods.mythocraft.data.loot;

import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.friendly.PegasusEntity;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import de.larsensmods.mythocraft.entity.monster.NemeanLionEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class MythocraftEntityLootProvider extends EntityLootSubProvider {

    public MythocraftEntityLootProvider(HolderLookup.Provider lookupProvider) {
        super(FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return MythEntities.getEntityTypes().stream().map(Supplier::get);
    }

    @Override
    public void generate() {
        add(MythEntities.NEMEAN_LION.get(), NemeanLionEntity.getLootTableBuilder());
        add(MythEntities.PEGASUS.get(), PegasusEntity.getLootTableBuilder());
        add(MythEntities.SATYR.get(), SatyrEntity.getLootTableBuilder());
    }
}
