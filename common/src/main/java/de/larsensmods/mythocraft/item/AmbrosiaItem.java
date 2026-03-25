package de.larsensmods.mythocraft.item;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.block.LabyrinthPortalBlock;
import de.larsensmods.mythocraft.block.MythBlocks;
import de.larsensmods.mythocraft.data.MythLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
                boolean validPortal = false;
                if(LabyrinthPortalBlock.isValidPortalShape(level, blockpos, Direction.Axis.X)){
                    validPortal = true;
                    BlockState portalState = MythBlocks.LABYRINTH_PORTAL.get().defaultBlockState().setValue(LabyrinthPortalBlock.AXIS, Direction.Axis.Z);
                    BlockPos.betweenClosed(blockpos.relative(Direction.UP, 1), blockpos.relative(Direction.UP, 2)).forEach((pos) -> level.setBlock(pos, portalState, 18));
                }else if(LabyrinthPortalBlock.isValidPortalShape(level, blockpos, Direction.Axis.Z)){
                    validPortal = true;
                    BlockState portalState = MythBlocks.LABYRINTH_PORTAL.get().defaultBlockState().setValue(LabyrinthPortalBlock.AXIS, Direction.Axis.X);
                    BlockPos.betweenClosed(blockpos.relative(Direction.UP, 1), blockpos.relative(Direction.UP, 2)).forEach((pos) -> level.setBlock(pos, portalState, 18));
                }
                if(validPortal){
                    preGenOtherDim(blockpos, level, player);
                }
            }
        }
        return super.useOn(pContext);
    }

    private void preGenOtherDim(BlockPos portalBasePos, Level usedInLevel, Player user){
        if(!(usedInLevel instanceof ServerLevel serverLevel)){
            return;
        }
        MinecraftServer server = serverLevel.getServer();
        if(usedInLevel.dimension() == MythLevels.LABYRINTH) {
            ServerLevel overworld = server.overworld();
            double teleportScale = DimensionType.getTeleportationScale(overworld.dimensionType(), usedInLevel.dimensionType());
            ChunkPos portalChunkPos = overworld.getChunk(portalBasePos).getPos();
            BlockPos overworldMin = new BlockPos((int) (portalChunkPos.getMinBlockX() * teleportScale), overworld.getMinBuildHeight(), (int) (portalChunkPos.getMinBlockZ() * teleportScale));
            BlockPos overworldMax = new BlockPos((int) (portalChunkPos.getMaxBlockX() * teleportScale), overworld.getMaxBuildHeight(), (int) (portalChunkPos.getMaxBlockZ() * teleportScale));

            ChunkPos.rangeClosed(overworld.getChunk(overworldMin).getPos(), overworld.getChunk(overworldMax).getPos()).forEach((pos) -> overworld.getChunkSource().addRegionTicket(TicketType.UNKNOWN, pos, 1, pos));
        }else{
            ServerLevel labyrinth = Objects.requireNonNull(server.getLevel(MythLevels.LABYRINTH));
            WorldBorder border = labyrinth.getWorldBorder();
            double teleportScale = DimensionType.getTeleportationScale(usedInLevel.dimensionType(), labyrinth.dimensionType());
            BlockPos resultingPos = border.clampToBounds(portalBasePos.getX() * teleportScale, portalBasePos.getY(), portalBasePos.getZ() * teleportScale);
            labyrinth.getChunkSource().addRegionTicket(TicketType.UNKNOWN, labyrinth.getChunk(resultingPos).getPos(), 1, labyrinth.getChunk(resultingPos).getPos());
        }
    }
}
