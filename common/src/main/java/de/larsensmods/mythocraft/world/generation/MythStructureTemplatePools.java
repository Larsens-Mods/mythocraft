package de.larsensmods.mythocraft.world.generation;

import com.mojang.datafixers.util.Pair;
import de.larsensmods.mythocraft.data.MythocraftStructureTemplatePools;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.List;

public class MythStructureTemplatePools {

    public static void bootstrap(BootstrapContext<StructureTemplatePool> structureTemplatePoolBootstrapContext) {
        HolderGetter<StructureTemplatePool> structureTemplatePoolHolderGetter = structureTemplatePoolBootstrapContext.lookup(Registries.TEMPLATE_POOL);

        structureTemplatePoolBootstrapContext.register(MythocraftStructureTemplatePools.GREEK_TEMPLE, new StructureTemplatePool(
                structureTemplatePoolHolderGetter.getOrThrow(Pools.EMPTY),
                List.of(
                        Pair.of(StructurePoolElement.single("mythocraft:greek_temple"), 1)
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }

}
