package de.larsensmods.mythocraft.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class MythocraftStructureTagProvider extends FabricTagProvider<Structure> {

    public MythocraftStructureTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.STRUCTURE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(MythocraftStructureTags.IS_LABYRINTH_PORTAL)
                .addOptional(MythocraftStructures.LABYRINTH_PORTAL.location()).addOptional(MythocraftStructures.LABYRINTH_PORTAL_SANDSTONE.location());
    }

}
