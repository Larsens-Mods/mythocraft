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
                        32, 12, RandomSpreadType.LINEAR, 83476973
                )
        ));
        structureSetBootstrapContext.register(MythocraftStructureSets.CYCLOPS_CAVE, new StructureSet(
                structureHolderGetter.getOrThrow(MythocraftStructures.CYCLOPS_CAVE),
                new RandomSpreadStructurePlacement(
                        32, 15, RandomSpreadType.LINEAR, 24976475
                )
        ));
        structureSetBootstrapContext.register(MythocraftStructureSets.CYCLOPS_FORGE, new StructureSet(
                structureHolderGetter.getOrThrow(MythocraftStructures.CYCLOPS_FORGE),
                new RandomSpreadStructurePlacement(
                        32, 16, RandomSpreadType.LINEAR, 98765432
                )
        ));
    }

}
