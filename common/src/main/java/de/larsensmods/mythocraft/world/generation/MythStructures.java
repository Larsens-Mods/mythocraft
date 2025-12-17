package de.larsensmods.mythocraft.world.generation;

import de.larsensmods.mythocraft.data.MythocraftBiomeTags;
import de.larsensmods.mythocraft.data.MythocraftStructureTemplatePools;
import de.larsensmods.mythocraft.data.MythocraftStructures;
import de.larsensmods.mythocraft.entity.MythEntities;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.Map;

public class MythStructures {

    public static void bootstrap(BootstrapContext<Structure> structureBootstrapContext) {
        HolderGetter<Biome> biomeHolderGetter = structureBootstrapContext.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> structureTemplatePoolHolderGetter = structureBootstrapContext.lookup(Registries.TEMPLATE_POOL);

        structureBootstrapContext.register(MythocraftStructures.GREEK_TEMPLE, new JigsawStructure(
                new Structure.StructureSettings(
                        biomeHolderGetter.getOrThrow(MythocraftBiomeTags.HAS_GREEK_TEMPLE),
                        Map.of(
                                MobCategory.MONSTER, new StructureSpawnOverride(
                                        StructureSpawnOverride.BoundingBoxType.STRUCTURE,
                                        WeightedRandomList.create(new MobSpawnSettings.SpawnerData(MythEntities.NEMEAN_LION.get(), 1, 1, 1))
                                )
                        ),
                        GenerationStep.Decoration.SURFACE_STRUCTURES,
                        TerrainAdjustment.NONE
                ),
                structureTemplatePoolHolderGetter.getOrThrow(MythocraftStructureTemplatePools.GREEK_TEMPLE),
                1,
                ConstantHeight.ZERO,
                false,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES
        ));
    }

}
