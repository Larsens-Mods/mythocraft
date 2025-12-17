package de.larsensmods.mythocraft.mixin.entity;

import de.larsensmods.mythocraft.item.MythItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MixinNearestAttackableTargetGoal extends TargetGoal {

    @Shadow protected LivingEntity target;
    @Shadow protected TargetingConditions targetConditions;

    @Inject(method = "findTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getNearestPlayer(Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;DDD)Lnet/minecraft/world/entity/player/Player;"), cancellable = true)
    private void injected(CallbackInfo ci){
        List<Player> targetablePlayers = new ArrayList<>();
        for(Player p : this.mob.level().players()){
            if(!p.getInventory().getArmor(Inventory.HELMET_SLOT_ONLY[0]).is(MythItems.HADES_HELM.get())){
                targetablePlayers.add(p);
            }
        }
        this.target = this.mob.level().getNearestEntity(targetablePlayers, this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
        ci.cancel();
    }

    //Constructor
    public MixinNearestAttackableTargetGoal(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
    }

}
