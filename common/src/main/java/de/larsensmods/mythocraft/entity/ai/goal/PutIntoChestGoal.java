package de.larsensmods.mythocraft.entity.ai.goal;

import de.larsensmods.mythocraft.data.MythocraftBlockTags;
import de.larsensmods.mythocraft.util.ChestUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class PutIntoChestGoal extends Goal {

    private final PathfinderMob mob;
    private final InventoryCarrier carrier;
    private final Supplier<BlockPos> chestPosSupplier;
    private final Function<Item, Boolean> putDownValidator;

    public PutIntoChestGoal(PathfinderMob mob, InventoryCarrier carrier, Supplier<BlockPos> chestPosSupplier, Function<Item, Boolean> putDownValidator) {
        this.mob = mob;
        this.carrier = carrier;
        this.chestPosSupplier = chestPosSupplier;
        this.putDownValidator = putDownValidator;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos chestPos = chestPosSupplier.get();
        if(chestPos == null || !mob.level().getBlockState(chestPos).is(MythocraftBlockTags.IS_CHEST)){
            return false;
        }
        BlockEntity rawEntity = mob.level().getBlockEntity(chestPos);
        if(!(rawEntity instanceof ChestBlockEntity chestEntity)){
            return false;
        }
        ChestBlockEntity otherHalf = ChestUtilities.getOtherHalf(chestEntity);
        boolean hasSpaceInChest = false;
        for(int i = 0; i < chestEntity.getContainerSize(); i++){
            if(chestEntity.getItem(i).isEmpty()){
                hasSpaceInChest = true;
                break;
            }
        }
        if(!hasSpaceInChest && otherHalf != null){
            for(int i = 0; i < otherHalf.getContainerSize(); i++){
                if(otherHalf.getItem(i).isEmpty()){
                    hasSpaceInChest = true;
                    break;
                }
            }
        }
        if(hasSpaceInChest){
            for(int i = 0; i < carrier.getInventory().getContainerSize(); i++){
                if(putDownValidator.apply(carrier.getInventory().getItem(i).getItem())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        boolean taskDone = false;
        if(this.mob.getNavigation().isDone()){
            BlockPos chestPos = chestPosSupplier.get();
            if(chestPos.distSqr(this.mob.blockPosition()) > 3.5){
                return false;
            }
            BlockEntity rawEntity = mob.level().getBlockEntity(chestPos);
            if(rawEntity instanceof ChestBlockEntity chestEntity){
                ChestBlockEntity otherHalf = ChestUtilities.getOtherHalf(chestEntity);
                for(int i = 0; i < carrier.getInventory().getContainerSize(); i++){
                    ItemStack item = carrier.getInventory().getItem(i);
                    if(putDownValidator.apply(item.getItem())){
                        ItemStack toInsert = carrier.getInventory().removeItem(i, item.getCount());
                        for(int j = 0; j < chestEntity.getContainerSize(); j++){
                            if(chestEntity.getItem(j).isEmpty()){
                                chestEntity.setItem(j, toInsert);
                                toInsert = ItemStack.EMPTY;
                                break;
                            }
                        }
                        if(otherHalf != null && !toInsert.isEmpty()){
                            for(int j = 0; j < otherHalf.getContainerSize(); j++){
                                if(otherHalf.getItem(j).isEmpty()){
                                    otherHalf.setItem(j, toInsert);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            taskDone = true;
        }
        return super.canContinueToUse() && !taskDone;
    }

    @Override
    public void start() {
        Vec3 chestPos = this.chestPosSupplier.get().getCenter();
        this.mob.getNavigation().moveTo(chestPos.x, chestPos.y, chestPos.z, 1.0);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }
}
