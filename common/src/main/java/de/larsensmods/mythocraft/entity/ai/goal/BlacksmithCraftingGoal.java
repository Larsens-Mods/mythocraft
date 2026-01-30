package de.larsensmods.mythocraft.entity.ai.goal;

import de.larsensmods.mythocraft.entity.friendly.BlacksmithCyclopsEntity;
import de.larsensmods.mythocraft.entity.friendly.BlacksmithCyclopsRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Supplier;

public class BlacksmithCraftingGoal extends Goal {

    private final BlacksmithCyclopsEntity cyclops;
    private final InventoryCarrier carrier;
    private final Supplier<BlockPos> anvilPosSupplier;

    private boolean isCrafting = false;
    private boolean startedCrafting = false;

    public BlacksmithCraftingGoal(BlacksmithCyclopsEntity cyclops, InventoryCarrier carrier, Supplier<BlockPos> anvilPosSupplier) {
        this.cyclops = cyclops;
        this.carrier = carrier;
        this.anvilPosSupplier = anvilPosSupplier;

        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos anvilPos = anvilPosSupplier.get();
        if(anvilPos == null || !cyclops.level().getBlockState(anvilPos).is(BlockTags.ANVIL)){
            return false;
        }
        for(BlacksmithCyclopsRecipe recipe : BlacksmithCyclopsRecipe.RECIPES){
            if(recipe.canUse(carrier)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        boolean taskDone = false;
        if(this.cyclops.getNavigation().isDone() && !startedCrafting){
            BlockPos anvilPos = anvilPosSupplier.get();
            if(anvilPos.distSqr(this.cyclops.blockPosition()) > 3.5){
                return false;
            }
            for(BlacksmithCyclopsRecipe recipe : BlacksmithCyclopsRecipe.RECIPES){
                if(recipe.canUse(carrier)){
                    isCrafting = true;
                    startedCrafting = true;
                    cyclops.startCrafting(recipe, () -> isCrafting = false);
                }
            }
        }
        if(startedCrafting && !isCrafting){
            taskDone = true;
        }
        return super.canContinueToUse() && !taskDone;
    }

    @Override
    public void start() {
        this.startedCrafting = false;
        this.isCrafting = false;
        Vec3 anvilPos = this.anvilPosSupplier.get().getCenter();
        this.cyclops.getNavigation().moveTo(anvilPos.x, anvilPos.y, anvilPos.z, 1.0);
    }

    @Override
    public void stop() {
        this.cyclops.getNavigation().stop();
    }
}