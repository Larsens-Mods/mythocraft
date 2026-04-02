package de.larsensmods.mythocraft.world.level;

import com.mojang.serialization.MapCodec;
import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;

import java.util.function.Supplier;

public class MythChunkGenerators {

    private static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, Constants.MOD_ID);

    public static Supplier<MapCodec<LabyrinthChunkGenerator>> LABYRINTH;

    public static void registerChunkGenerators(){
        LABYRINTH = CHUNK_GENERATORS.register("labyrinth", () -> LabyrinthChunkGenerator.CODEC);

        CHUNK_GENERATORS.register();
    }

}
