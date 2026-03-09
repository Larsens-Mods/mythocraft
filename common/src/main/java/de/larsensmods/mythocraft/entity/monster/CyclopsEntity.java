package de.larsensmods.mythocraft.entity.monster;

import com.mojang.datafixers.util.Pair;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.MythocraftStructures;
import de.larsensmods.mythocraft.entity.friendly.SatyrEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class CyclopsEntity extends Monster {

    private static final EntityDataAccessor<Integer> ATTACK_ANIM_TICKS = SynchedEntityData.defineId(CyclopsEntity.class, EntityDataSerializers.INT);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState rockThrowAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private boolean animationStartedInCycle = false;

    public CyclopsEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = XP_REWARD_LARGE;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_ANIM_TICKS, -1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CyclopsAttackGoal(this, 1.25, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(4, new EnterCaveGoal(this, 1.0));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(CyclopsEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, SatyrEntity.class, true));
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()){
            this.setupAnimStates();
        }else{
            int attackAnimTicks = this.getAttackAnimTicks();
            if(attackAnimTicks >= 0){
                this.setAttackAnimTicks(attackAnimTicks - 1);
            }
        }
    }

    private void setupAnimStates(){
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = 3 * 20;
            this.idleAnimationState.start(this.tickCount);
        }else{
            this.idleAnimationTimeout--;
        }
        int attackAnimTicks = this.getAttackAnimTicks();
        if(attackAnimTicks >= 15 && !this.animationStartedInCycle){
            this.attackAnimationState.start(this.tickCount);
            this.animationStartedInCycle = true;
        }else if(attackAnimTicks >= 0){
            if(attackAnimTicks < 15) {
                this.animationStartedInCycle = false;
            }
            if(attackAnimTicks == 0){
                this.attackAnimationState.stop();
            }
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pEntity) {
        this.setAttackAnimTicks(20);
        return super.doHurtTarget(pEntity);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public void handleDamageEvent(@NotNull DamageSource pDamageSource) {
        //Prevent fire-related damages since cyclopses are immune to them
        if(pDamageSource.is(DamageTypes.IN_FIRE) || pDamageSource.is(DamageTypes.ON_FIRE) || pDamageSource.is(DamageTypes.LAVA) || pDamageSource.is(DamageTypes.FIREBALL)){
            return;
        }
        super.handleDamageEvent(pDamageSource);
    }

    private void setAttackAnimTicks(int attackAnimTicks){
        this.entityData.set(ATTACK_ANIM_TICKS, attackAnimTicks);
    }

    private int getAttackAnimTicks() {
        return this.entityData.get(ATTACK_ANIM_TICKS);
    }

    //Utility methods

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.ATTACK_KNOCKBACK, 2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public static LootTable.Builder getLootTableBuilder(){
        return LootTable.lootTable()
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .setBonusRolls(ConstantValue.exactly(0))
                        .add(LootItem.lootTableItem(Items.SPIDER_EYE))
                );
    }

    static class EnterCaveGoal extends RandomStrollGoal {

        public EnterCaveGoal(PathfinderMob pMob, double pSpeedModifier) {
            this(pMob, pSpeedModifier, 120);
        }

        public EnterCaveGoal(PathfinderMob pMob, double pSpeedModifier, int pInterval) {
            this(pMob, pSpeedModifier, pInterval, true);
        }

        public EnterCaveGoal(PathfinderMob pMob, double pSpeedModifier, int pInterval, boolean pCheckNoActionTime) {
            super(pMob, pSpeedModifier, pInterval, pCheckNoActionTime);
        }

        @Nullable
        protected Vec3 getPosition() {
            if(this.mob.level() instanceof ServerLevel level){
                Registry<Structure> registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
                HolderSet<Structure> holderset = getHolders(MythocraftStructures.CYCLOPS_CAVE, registry).orElse(null);
                if(holderset == null){
                    Constants.LOG.warn("Could not find structure holder for Cyclops Cave structure!");
                    return DefaultRandomPos.getPos(this.mob, 10, 7);
                }
                Pair<BlockPos, Holder<Structure>> pair = level.getChunkSource().getGenerator().findNearestMapStructure(level, holderset, this.mob.blockPosition(), 4, false);
                if(pair == null){
                    return DefaultRandomPos.getPos(this.mob, 10, 7);
                }
                BlockPos structurePos = pair.getFirst();
                return Vec3.atBottomCenterOf(structurePos);
            }else {
                return DefaultRandomPos.getPos(this.mob, 10, 7);
            }
        }

        private Optional<? extends HolderSet<Structure>> getHolders(ResourceKey<Structure> pStructure, Registry<Structure> pStructureRegistry) {
            Objects.requireNonNull(pStructureRegistry);
            return pStructureRegistry.getHolder(pStructure).map(HolderSet::direct);
        }
    }

    static class CyclopsAttackGoal extends MeleeAttackGoal {
        public CyclopsAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        }
    }

}
