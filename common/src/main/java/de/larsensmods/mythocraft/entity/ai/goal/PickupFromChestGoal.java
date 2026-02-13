package de.larsensmods.mythocraft.entity.ai.goal;

import de.larsensmods.mythocraft.data.MythocraftBlockTags;
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

public class PickupFromChestGoal extends Goal {

    private final PathfinderMob mob;
    private final InventoryCarrier carrier;
    private final Supplier<BlockPos> chestPosSupplier;
    private final Function<Item, Boolean> pickupValidator;

    public PickupFromChestGoal(PathfinderMob mob, InventoryCarrier carrier, Supplier<BlockPos> chestPosSupplier, Function<Item, Boolean> pickupValidator) {
        this.mob = mob;
        this.carrier = carrier;
        this.chestPosSupplier = chestPosSupplier;
        this.pickupValidator = pickupValidator;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos chestPos = chestPosSupplier.get();
        if(chestPos == null || !mob.level().getBlockState(chestPos).is(MythocraftBlockTags.IS_CHEST)){
            return false;
        }
        boolean inventoryNotFull = false;
        for(int i = 0; i < carrier.getInventory().getContainerSize() - 1; i++){ // Keep one slot free
            if(carrier.getInventory().getItem(i).isEmpty()){
                inventoryNotFull = true;
                break;
            }
        }
        BlockEntity rawEntity = mob.level().getBlockEntity(chestPos);
        if(!(rawEntity instanceof ChestBlockEntity chestEntity)){
            return false;
        }
        for(int i = 0; i < chestEntity.getContainerSize(); i++){
            if(pickupValidator.apply(chestEntity.getItem(i).getItem())){
                return inventoryNotFull;
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
                for(int i = 0; i < chestEntity.getContainerSize(); i++){
                    ItemStack item = chestEntity.getItem(i);
                    if (pickupValidator.apply(item.getItem()) && this.carrier.getInventory().canAddItem(item)) {
                        ItemStack removed = chestEntity.removeItem(i, item.getCount());
                        this.carrier.getInventory().addItem(removed);
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
