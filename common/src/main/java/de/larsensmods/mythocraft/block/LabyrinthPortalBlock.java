package de.larsensmods.mythocraft.block;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.MythLevels;
import net.minecraft.BlockUtil;
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
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public DimensionTransition getPortalDestination(ServerLevel pLevel, @NotNull Entity pEntity, @NotNull BlockPos pPos) {
        ResourceKey<Level> resourcekey = pLevel.dimension() == MythLevels.LABYRINTH ? Level.OVERWORLD : MythLevels.LABYRINTH;
        ServerLevel serverlevel = pLevel.getServer().getLevel(resourcekey);
        if (serverlevel == null) {
            return null;
        } else {
            boolean isLabyrinth = serverlevel.dimension() == MythLevels.LABYRINTH;
            WorldBorder border = serverlevel.getWorldBorder();
            double teleportScale = DimensionType.getTeleportationScale(pLevel.dimensionType(), serverlevel.dimensionType());
            BlockPos resultingPos = border.clampToBounds(pEntity.getX() * teleportScale, pEntity.getY(), pEntity.getZ() * teleportScale);
            return this.findNearestCounterpart(serverlevel, pEntity, pPos, resultingPos, isLabyrinth, border);
        }
    }

    @Nullable
    private DimensionTransition findNearestCounterpart(ServerLevel pLevel, Entity pEntity, BlockPos pPos, BlockPos pExitPos, boolean pIsLabyrinth, WorldBorder pWorldBorder) {
        Optional<BlockPos> optional = pLevel.getPortalForcer().findClosestPortalPosition(pExitPos, pIsLabyrinth, pWorldBorder);
        BlockUtil.FoundRectangle blockutil$foundrectangle;
        DimensionTransition.PostDimensionTransition dimensiontransition$postdimensiontransition;
        if (optional.isPresent()) {
            BlockPos blockpos = optional.get();
            BlockState blockstate = pLevel.getBlockState(blockpos);
            blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(blockpos, blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (pos) -> pLevel.getBlockState(pos) == blockstate);
            dimensiontransition$postdimensiontransition = DimensionTransition.PLAY_PORTAL_SOUND.then((entity) -> entity.placePortalTicket(blockpos));
        } else {
            Direction.Axis direction$axis = pEntity.level().getBlockState(pPos).getOptionalValue(AXIS).orElse(Direction.Axis.X);
            Optional<BlockUtil.FoundRectangle> optional1 = pLevel.getPortalForcer().createPortal(pExitPos, direction$axis);
            if (optional1.isEmpty()) {
                Constants.LOG.error("Unable to create a portal, likely target out of worldborder");
                return null;
            }

            blockutil$foundrectangle = optional1.get();
            dimensiontransition$postdimensiontransition = DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET);
        }
        return getDimensionTransitionFromExit(pEntity, pPos, blockutil$foundrectangle, pLevel, dimensiontransition$postdimensiontransition);
    }

    private static DimensionTransition getDimensionTransitionFromExit(Entity pEntity, BlockPos pPos, BlockUtil.FoundRectangle pRectangle, ServerLevel pLevel, DimensionTransition.PostDimensionTransition pPostDimensionTransition) {
        BlockState blockstate = pEntity.level().getBlockState(pPos);
        Direction.Axis direction$axis;
        Vec3 vec3;
        if (blockstate.hasProperty(BlockStateProperties.HORIZONTAL_AXIS)) {
            direction$axis = blockstate.getValue(BlockStateProperties.HORIZONTAL_AXIS);
            BlockUtil.FoundRectangle blockutil$foundrectangle = BlockUtil.getLargestRectangleAround(pPos, direction$axis, 21, Direction.Axis.Y, 21, (pos) -> pEntity.level().getBlockState(pos) == blockstate);
            vec3 = pEntity.getRelativePortalPosition(direction$axis, blockutil$foundrectangle);
        } else {
            direction$axis = Direction.Axis.X;
            vec3 = new Vec3(0.5F, 0.0F, 0.0F);
        }

        return createDimensionTransition(pLevel, pRectangle, direction$axis, vec3, pEntity, pEntity.getDeltaMovement(), pEntity.getYRot(), pEntity.getXRot(), pPostDimensionTransition);
    }

    private static DimensionTransition createDimensionTransition(ServerLevel pLevel, BlockUtil.FoundRectangle pRectangle, Direction.Axis pAxis, Vec3 pOffset, Entity pEntity, Vec3 pSpeed, float pYRot, float pXRot, DimensionTransition.PostDimensionTransition pPostDimensionTransition) {
        BlockPos blockpos = pRectangle.minCorner;
        BlockState blockstate = pLevel.getBlockState(blockpos);
        Direction.Axis direction$axis = blockstate.getOptionalValue(BlockStateProperties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d0 = pRectangle.axis1Size;
        double d1 = pRectangle.axis2Size;
        EntityDimensions entitydimensions = pEntity.getDimensions(pEntity.getPose());
        int i = pAxis == direction$axis ? 0 : 90;
        Vec3 vec3 = pAxis == direction$axis ? pSpeed : new Vec3(pSpeed.z, pSpeed.y, -pSpeed.x);
        double d2 = (double)entitydimensions.width() / (double)2.0F + (d0 - (double)entitydimensions.width()) * pOffset.x();
        double d3 = (d1 - (double)entitydimensions.height()) * pOffset.y();
        double d4 = (double)0.5F + pOffset.z();
        boolean flag = direction$axis == Direction.Axis.X;
        Vec3 vec31 = new Vec3((double)blockpos.getX() + (flag ? d2 : d4), (double)blockpos.getY() + d3, (double)blockpos.getZ() + (flag ? d4 : d2));
        Vec3 vec32 = PortalShape.findCollisionFreePosition(vec31, pLevel, pEntity, entitydimensions);
        return new DimensionTransition(pLevel, vec32, vec3, pYRot + (float)i, pXRot, pPostDimensionTransition);
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
