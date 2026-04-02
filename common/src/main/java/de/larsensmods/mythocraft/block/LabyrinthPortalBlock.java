package de.larsensmods.mythocraft.block;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.MythLevels;
import de.larsensmods.mythocraft.world.level.data.LevelPortalData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class LabyrinthPortalBlock extends Block implements Portal {

    public static final Set<ResourceKey<Level>> VALID_PORTAL_DIMENSIONS = Set.of(MythLevels.LABYRINTH, Level.OVERWORLD);

    public static final EnumProperty<Direction.Axis> AXIS;
    protected static final VoxelShape X_AXIS_AABB;
    protected static final VoxelShape Z_AXIS_AABB;

    public LabyrinthPortalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return pState.getValue(AXIS).equals(Direction.Axis.Z) ? Z_AXIS_AABB : X_AXIS_AABB;
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState pState, Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        Direction.Axis direction$axis = pFacing.getAxis();
        Direction.Axis direction$axis1 = pState.getValue(AXIS);
        boolean flag = direction$axis1 != direction$axis && direction$axis.isHorizontal();
        return !flag && !pFacingState.is(this) && !(new PortalShape(pLevel, pCurrentPos, direction$axis1)).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, Entity pEntity) {
        if (pEntity.canUsePortal(false)) {
            pEntity.setAsInsidePortal(this, pPos);
        }
    }

    @Override
    public int getPortalTransitionTime(@NotNull ServerLevel pLevel, @NotNull Entity pEntity) {
        int ticks;
        if (pEntity instanceof Player player) {
            ticks = Math.max(1, pLevel.getGameRules().getInt(player.getAbilities().invulnerable ? GameRules.RULE_PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.RULE_PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        } else {
            ticks = 0;
        }

        return ticks;
    }

    @Override
    @Nullable
    public DimensionTransition getPortalDestination(ServerLevel currentLevel, @NotNull Entity entity, @NotNull BlockPos portalPos) {
        ResourceKey<Level> resourcekey = currentLevel.dimension() == MythLevels.LABYRINTH ? Level.OVERWORLD : MythLevels.LABYRINTH;
        ServerLevel destinationLevel = currentLevel.getServer().getLevel(resourcekey);
        if (destinationLevel == null) {
            return null;
        } else {
            boolean isLabyrinth = destinationLevel.dimension() == MythLevels.LABYRINTH;
            WorldBorder border = destinationLevel.getWorldBorder();
            double teleportScale = DimensionType.getTeleportationScale(currentLevel.dimensionType(), destinationLevel.dimensionType());
            BlockPos resultingPos = border.clampToBounds(entity.getX() * teleportScale, entity.getY(), entity.getZ() * teleportScale);
            return this.findNearestCounterpart(destinationLevel, entity, portalPos, resultingPos, isLabyrinth, border, teleportScale);
        }
    }

    @Nullable
    private DimensionTransition findNearestCounterpart(ServerLevel inLevel, Entity pEntity, BlockPos entryPortalPos, BlockPos exitPos, boolean isDestinationLabyrinth, WorldBorder destinationWorldBorder, double teleportScale) {
        BlockPos exitPortalBasePos = null;
        LevelPortalData portalData = inLevel.getDataStorage().computeIfAbsent(LevelPortalData.factory(), LevelPortalData.NAME);
        ChunkAccess chunkAccess = inLevel.getChunk(exitPos);
        Optional<Long> dataPortalPos = portalData.getPortalBaseForChunk(chunkAccess.getPos().toLong());

        boolean findPortalPos = dataPortalPos.isEmpty();

        if(!findPortalPos) {
            exitPortalBasePos = BlockPos.of(dataPortalPos.get());
            if(!isValidPortalShape(inLevel, exitPortalBasePos, Direction.Axis.X) && !isValidPortalShape(inLevel, exitPortalBasePos, Direction.Axis.Z)){
                Constants.LOG.warn("Portal base position stored in LevelPortalData for chunk {} is not a valid portal shape anymore! Removing entry from data.", chunkAccess.getPos());
                portalData.clearPortalBaseForChunk(chunkAccess.getPos());
                findPortalPos = true;
                exitPortalBasePos = null;
            }
        }
        if(findPortalPos) {
            Set<BlockPos> barrierRockPositions = new HashSet<>();
            Set<BlockPos> labyrinthPortalPositions = new HashSet<>();
            BlockPos minPos, maxPos;
            if (isDestinationLabyrinth) {
                minPos = new BlockPos(chunkAccess.getPos().getMinBlockX() - 16, inLevel.getMinBuildHeight(), chunkAccess.getPos().getMinBlockZ() - 16);
                maxPos = new BlockPos(chunkAccess.getPos().getMaxBlockX() + 16, inLevel.getMaxBuildHeight(), chunkAccess.getPos().getMaxBlockZ() + 16);
            } else {
                ChunkPos entryChunkPos = inLevel.getChunk(entryPortalPos).getPos();
                minPos = new BlockPos((int) (entryChunkPos.getMinBlockX() * teleportScale) - 16, inLevel.getMinBuildHeight(), (int) (entryChunkPos.getMinBlockZ() * teleportScale) - 16);
                maxPos = new BlockPos((int) (entryChunkPos.getMaxBlockX() * teleportScale) + 16, inLevel.getMaxBuildHeight(), (int) (entryChunkPos.getMaxBlockZ() * teleportScale) + 16);
            }
            for(BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                if(inLevel.getBlockState(pos).is(MythBlocks.LABYRINTH_BARRIER_ROCK.get())) {
                    barrierRockPositions.add(pos.immutable());
                }else if(inLevel.getBlockState(pos).is(MythBlocks.LABYRINTH_PORTAL.get())) {
                    labyrinthPortalPositions.add(pos.immutable());
                }
            }
            boolean searchForShape = labyrinthPortalPositions.isEmpty();
            if(!searchForShape) {
                BlockPos pos = new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
                for(BlockPos portal : labyrinthPortalPositions) {
                    if(portal.getY() < pos.getY()){
                        pos = portal;
                    }
                }
                exitPortalBasePos = pos.below();
                if(!isValidPortalShape(inLevel, exitPortalBasePos, Direction.Axis.X)  && !isValidPortalShape(inLevel, exitPortalBasePos, Direction.Axis.Z)) {
                    exitPortalBasePos = null;
                    searchForShape = true;
                }
            }
            if(searchForShape) {
                for(BlockPos pos : barrierRockPositions) {
                    if(isValidPortalShape(inLevel, pos, Direction.Axis.X) || isValidPortalShape(inLevel, pos, Direction.Axis.Z)) {
                        exitPortalBasePos = pos;
                        break;
                    }
                }
                if(exitPortalBasePos == null){
                    Constants.LOG.warn("Could not find any portals or barrier rocks in correct portal shape for portal {} between {} and {}!", entryPortalPos, minPos, maxPos);
                    return null;
                }
            }
            portalData.setPortalBaseForChunk(chunkAccess.getPos(), exitPortalBasePos);
        }

        Direction.Axis frameOrientation;
        if(isValidPortalShape(inLevel, exitPortalBasePos, Direction.Axis.X)) {
            frameOrientation = Direction.Axis.X;
        }else if(isValidPortalShape(inLevel, exitPortalBasePos, Direction.Axis.Z)) {
            frameOrientation = Direction.Axis.Z;
        }else{
            throw new IllegalStateException("Portal shape is invalid after existence verification!");
        }

        if(frameOrientation == Direction.Axis.X) {
            BlockState portalState = MythBlocks.LABYRINTH_PORTAL.get().defaultBlockState().setValue(LabyrinthPortalBlock.AXIS, Direction.Axis.Z);
            BlockPos.betweenClosed(exitPortalBasePos.relative(Direction.UP, 1), exitPortalBasePos.relative(Direction.UP, 2)).forEach((pos) -> inLevel.setBlock(pos, portalState, Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE));
        }else{
            BlockState portalState = MythBlocks.LABYRINTH_PORTAL.get().defaultBlockState().setValue(LabyrinthPortalBlock.AXIS, Direction.Axis.X);
            BlockPos.betweenClosed(exitPortalBasePos.relative(Direction.UP, 1), exitPortalBasePos.relative(Direction.UP, 2)).forEach((pos) -> inLevel.setBlock(pos, portalState, Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE));
        }

        DimensionTransition.PostDimensionTransition postDimensionTransition = DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET);

        return getDimensionTransitionFromExit(pEntity, entryPortalPos, exitPortalBasePos, inLevel, postDimensionTransition);
    }

    private static DimensionTransition getDimensionTransitionFromExit(Entity pEntity, BlockPos entryPortalPos, BlockPos exitPortalBasePos, ServerLevel pLevel, DimensionTransition.PostDimensionTransition pPostDimensionTransition) {
        BlockState blockstate = pEntity.level().getBlockState(entryPortalPos);
        Direction.Axis direction;
        if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            direction = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
        } else {
            direction = Direction.Axis.X;
        }

        return createDimensionTransition(pLevel, exitPortalBasePos, direction, pEntity, pEntity.getDeltaMovement(), pEntity.getYRot(), pEntity.getXRot(), pPostDimensionTransition);
    }

    private static DimensionTransition createDimensionTransition(ServerLevel pLevel, BlockPos exitPortalBasePos, Direction.Axis pAxis, Entity pEntity, Vec3 pSpeed, float pYRot, float pXRot, DimensionTransition.PostDimensionTransition pPostDimensionTransition) {
        BlockPos blockpos = exitPortalBasePos.above();
        BlockState blockstate = pLevel.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        EntityDimensions entitydimensions = pEntity.getDimensions(pEntity.getPose());
        int i = pAxis == direction$axis ? 0 : 90;
        Vec3 vec3 = pAxis == direction$axis ? pSpeed : new Vec3(pSpeed.z, pSpeed.y, -pSpeed.x);
        double d2 = entitydimensions.width() / 2;
        double d3 = 0;
        double d4 = 0.5;
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec31 = new Vec3((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec32 = PortalShape.findCollisionFreePosition(vec31, pLevel, pEntity, entitydimensions);
        return new DimensionTransition(pLevel, vec32, vec3, pYRot + (float)i, pXRot, pPostDimensionTransition);
    }

    public static boolean isValidPortalShape(Level level, BlockPos basePos, Direction.Axis axis) {
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

    @Override
    public Portal.@NotNull Transition getLocalTransition() {
        return Transition.CONFUSION;
    }

    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(100) == 0) {
            pLevel.playLocalSound((double)pPos.getX() + (double)0.5F, (double)pPos.getY() + (double)0.5F, (double)pPos.getZ() + (double)0.5F, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5F, pRandom.nextFloat() * 0.4F + 0.8F, false);
        }

        for(int i = 0; i < 4; ++i) {
            double d0 = (double)pPos.getX() + pRandom.nextDouble();
            double d1 = (double)pPos.getY() + pRandom.nextDouble();
            double d2 = (double)pPos.getZ() + pRandom.nextDouble();
            double d3 = ((double)pRandom.nextFloat() - (double)0.5F) * (double)0.5F;
            double d4 = ((double)pRandom.nextFloat() - (double)0.5F) * (double)0.5F;
            double d5 = ((double)pRandom.nextFloat() - (double)0.5F) * (double)0.5F;
            int j = pRandom.nextInt(2) * 2 - 1;
            if (!pLevel.getBlockState(pPos.west()).is(this) && !pLevel.getBlockState(pPos.east()).is(this)) {
                d0 = (double)pPos.getX() + (double)0.5F + (double)0.25F * (double)j;
                d3 = pRandom.nextFloat() * 2.0F * (float)j;
            } else {
                d2 = (double)pPos.getZ() + (double)0.5F + (double)0.25F * (double)j;
                d5 = pRandom.nextFloat() * 2.0F * (float)j;
            }

            pLevel.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }

    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState) {
        return ItemStack.EMPTY;
    }

    /** @deprecated */
    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState pState, Rotation pRot) {
        switch (pRot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch (pState.getValue(AXIS)) {
                    case Z -> {
                        return pState.setValue(AXIS, Direction.Axis.X);
                    }
                    case X -> {
                        return pState.setValue(AXIS, Direction.Axis.Z);
                    }
                    default -> {
                        return pState;
                    }
                }
            default:
                return pState;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    static {
        AXIS = BlockStateProperties.HORIZONTAL_AXIS;
        X_AXIS_AABB = Block.box(0.0F, 0.0F, 6.0F, 16.0F, 16.0F, 10.0F);
        Z_AXIS_AABB = Block.box(6.0F, 0.0F, 0.0F, 10.0F, 16.0F, 16.0F);
    }

}
