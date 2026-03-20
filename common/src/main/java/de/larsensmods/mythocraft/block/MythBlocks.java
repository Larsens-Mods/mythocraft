package de.larsensmods.mythocraft.block;

import de.larsensmods.lmcc.api.registry.DeferredRegister;
import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class MythBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Constants.MOD_ID);

    public static Supplier<Block> LABYRINTH_BARRIER_ROCK;
    public static Supplier<Block> LABYRINTH_PORTAL;

    public static void registerBlocks(){
        LABYRINTH_BARRIER_ROCK = BLOCKS.register("labyrinth_barrier_rock", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BEDROCK)));
        LABYRINTH_PORTAL = BLOCKS.register("labyrinth_portal", () -> new LabyrinthPortalBlock(BlockBehaviour.Properties.of().noCollission().strength(-1.0f).sound(SoundType.GLASS).lightLevel((state) -> 8).pushReaction(PushReaction.DESTROY)));

        BLOCKS.register();
    }
}
