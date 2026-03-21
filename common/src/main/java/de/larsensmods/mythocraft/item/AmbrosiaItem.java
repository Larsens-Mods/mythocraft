package de.larsensmods.mythocraft.item;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.block.LabyrinthPortalBlock;
import de.larsensmods.mythocraft.block.MythBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AmbrosiaItem extends Item {
    public AmbrosiaItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if(LabyrinthPortalBlock.VALID_PORTAL_DIMENSIONS.contains(level.dimension())){
            if(blockstate.is(MythBlocks.LABYRINTH_BARRIER_ROCK.get())){
                if(isValidPortalShape(level, blockpos, Direction.Axis.X)){
                    BlockState portalState = MythBlocks.LABYRINTH_PORTAL.get().defaultBlockState().setValue(LabyrinthPortalBlock.AXIS, Direction.Axis.Z);
                    BlockPos.betweenClosed(blockpos.relative(Direction.UP, 1), blockpos.relative(Direction.UP, 2)).forEach((pos) -> level.setBlock(pos, portalState, 18));
                }else if(isValidPortalShape(level, blockpos, Direction.Axis.Z)){
                    BlockState portalState = MythBlocks.LABYRINTH_PORTAL.get().defaultBlockState().setValue(LabyrinthPortalBlock.AXIS, Direction.Axis.X);
                    BlockPos.betweenClosed(blockpos.relative(Direction.UP, 1), blockpos.relative(Direction.UP, 2)).forEach((pos) -> level.setBlock(pos, portalState, 18));
                }
            }
        }
        return super.useOn(pContext);
    }

    private boolean isValidPortalShape(Level level, BlockPos basePos, Direction.Axis axis) {
        for(int x = basePos.getX() - (axis == Direction.Axis.X ? 0 : 1); x <= basePos.getX() + (axis == Direction.Axis.X ? 0 : 1); x++) {
            for(int y = basePos.getY(); y <= basePos.getY() + 3; y++) {
                for(int z = basePos.getZ() - (axis == Direction.Axis.Z ? 0 : 1); z <= basePos.getZ() + (axis == Direction.Axis.Z ? 0 : 1); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if(x == basePos.getX() && z == basePos.getZ() && (y == basePos.getY() + 1 || y == basePos.getY() + 2)) {
                        if(!level.getBlockState(pos).is(Blocks.AIR) && !level.getBlockState(pos).is(MythBlocks.LABYRINTH_PORTAL.get())) {
                            return false;
                        }
                    }else{
                        if(!level.getBlockState(pos).is(MythBlocks.LABYRINTH_BARRIER_ROCK.get())) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
