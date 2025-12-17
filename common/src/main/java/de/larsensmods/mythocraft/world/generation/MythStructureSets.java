package de.larsensmods.mythocraft.world.generation;

import de.larsensmods.mythocraft.data.MythocraftStructureSets;
import de.larsensmods.mythocraft.data.MythocraftStructures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

public class MythStructureSets {

    public static void bootstrap(BootstrapContext<StructureSet> structureSetBootstrapContext) {
        HolderGetter<Structure> structureHolderGetter = structureSetBootstrapContext.lookup(Registries.STRUCTURE);

        structureSetBootstrapContext.register(MythocraftStructureSets.GREEK_TEMPLE, new StructureSet(
                structureHolderGetter.getOrThrow(MythocraftStructures.GREEK_TEMPLE),
                new RandomSpreadStructurePlacement(
                        20, 4, RandomSpreadType.LINEAR, 0
                )
        ));
    }

}
