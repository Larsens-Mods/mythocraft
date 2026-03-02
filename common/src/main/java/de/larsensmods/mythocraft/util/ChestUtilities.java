package de.larsensmods.mythocraft.util;

import de.larsensmods.mythocraft.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.TrappedChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ChestUtilities {

    public static @Nullable ChestBlockEntity getOtherHalf(@NotNull ChestBlockEntity chestEntity) {
        if(chestEntity.getBlockState().getValue(TrappedChestBlock.TYPE) == ChestType.SINGLE){
            return null;
        }
        BlockPos chestPos = chestEntity.getBlockPos();
        Direction facing = chestEntity.getBlockState().getValue(ChestBlock.FACING);
        BlockPos otherHalfPos = null;
        if(chestEntity.getBlockState().getValue(ChestBlock.TYPE) == ChestType.LEFT){
            switch (facing){
                case EAST -> otherHalfPos = chestPos.south();
                case SOUTH -> otherHalfPos = chestPos.west();
                case WEST -> otherHalfPos = chestPos.north();
                case NORTH -> otherHalfPos = chestPos.east();
            }
        }else{
            switch (facing){
                case EAST -> otherHalfPos = chestPos.north();
                case SOUTH -> otherHalfPos = chestPos.east();
                case WEST -> otherHalfPos = chestPos.south();
                case NORTH -> otherHalfPos = chestPos.west();
            }
        }
        if(otherHalfPos != null) {
            BlockEntity otherRawEntity = Objects.requireNonNull(chestEntity.getLevel()).getBlockEntity(otherHalfPos);
            if (otherRawEntity instanceof ChestBlockEntity otherChestEntity) {
                return otherChestEntity;
            }else{
                Constants.LOG.error("Double chest at {} has no valid other half at {}", chestPos, otherHalfPos);
            }
        }
        return null;
    }

}
