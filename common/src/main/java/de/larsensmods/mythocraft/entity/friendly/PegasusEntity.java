package de.larsensmods.mythocraft.entity.friendly;

import de.larsensmods.mythocraft.entity.MythEntities;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.IntFunction;

public class PegasusEntity extends Animal implements VariantHolder<PegasusEntity.Variant>, FlyingAnimal, OwnableEntity, Saddleable, PlayerRideableJumping {

    private static final int FLAG_START_FLYING = 1;
    private static final int FLAG_TAMED = 2;
    private static final int FLAG_SADDLED = 4;
    private static final int FLAG_FLYING = 8;
    private static final int FLAG_GRASSING = 16;
    private static final int FLAG_REARING = 32;
    private static final int FLAG_GLIDING = 64;

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(PegasusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(PegasusEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> LAST_GLIDING_CALC = SynchedEntityData.defineId(PegasusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> LAST_GLIDING_VELOCITY = SynchedEntityData.defineId(PegasusEntity.class, EntityDataSerializers.FLOAT);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState grassingAnimationState = new AnimationState();
    public final AnimationState rearingAnimationState = new AnimationState();
    public final AnimationState glidingAnimationState = new AnimationState();
    public final AnimationState flyingAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private int glidingAnimationTimeout = -1;
    private int flyingAnimationTimeout = -1;

    private int grassingCounter = 0;
    private int rearingCounter = 0;

    private int fallingTicks = 0;
    private Vec3 serverTickedDeltaMove = Vec3.ZERO;
    private Vec3 lastPos = Vec3.ZERO;

    @Nullable
    private UUID owner;
    private int temper;

    public PegasusEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(VARIANT, 0);
        pBuilder.define(FLAGS, (byte) 0);
        pBuilder.define(LAST_GLIDING_CALC, 0);
        pBuilder.define(LAST_GLIDING_VELOCITY, Float.NEGATIVE_INFINITY);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("variant", this.getVariantID());
        pCompound.putBoolean("tamed", this.isTamed());
        pCompound.putBoolean("saddled", this.isSaddled());
        pCompound.putInt("temper", this.getTemper());
        if(this.getOwnerUUID() != null){
            pCompound.putUUID("owner", this.getOwnerUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(VARIANT, pCompound.getInt("variant"));
        this.setTamed(pCompound.getBoolean("tamed"));
        this.setSaddled(pCompound.getBoolean("saddled"));
        this.setTemper(pCompound.getInt("temper"));
        if(pCompound.hasUUID("owner")){
            this.setOwnerUUID(pCompound.getUUID("owner"));
        }
    }

    private boolean getFlag(int flag){
        return (this.entityData.get(FLAGS) & flag) != 0;
    }

    private void setFlag(int flag, boolean value){
        byte b = this.entityData.get(FLAGS);
        if(value){
            this.entityData.set(FLAGS, (byte) (b | flag));
        }else{
            this.entityData.set(FLAGS, (byte) (b & ~flag));
        }
    }

    private void setStartFlying(boolean startFlying){
        this.setFlag(FLAG_START_FLYING, startFlying);
    }

    private boolean getStartFlying() {
        return this.getFlag(FLAG_START_FLYING);
    }

    private void setLastGlidingCalc(int lastGlidingCalc){
        this.entityData.set(LAST_GLIDING_CALC, lastGlidingCalc);
    }

    private int getLastGlidingCalc() {
        return this.entityData.get(LAST_GLIDING_CALC);
    }

    private void setLastGlidingVelocity(float lastGlidingVelocity){
        this.entityData.set(LAST_GLIDING_VELOCITY, lastGlidingVelocity);
    }

    private float getLastGlidingVelocity(){
        return this.entityData.get(LAST_GLIDING_VELOCITY);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PegasusRunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0F, PegasusEntity.class));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25F, stack -> stack.is(ItemTags.HORSE_TEMPT_ITEMS), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.HORSE_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        PegasusEntity otherPegasus = (PegasusEntity) otherParent;
        PegasusEntity child = MythEntities.PEGASUS.get().create(level);
        if(child != null){
            int i = this.random.nextInt(9);
            Variant variant;
            if (i < 4) {
                variant = this.getVariant();
            } else if (i < 8) {
                variant = otherPegasus.getVariant();
            } else {
                variant = Util.getRandom(Variant.values(), this.random);
            }
            child.setVariant(variant);
        }
        return child;
    }

    @Override
    protected void dropAllDeathLoot(@NotNull ServerLevel pLevel, @NotNull DamageSource pDamageSource) {
        super.dropAllDeathLoot(pLevel, pDamageSource);
        if(this.isSaddled()){
            this.spawnAtLocation(new ItemStack(Items.SADDLE));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()){
            this.setupAnimStates();
        }else {
            if (!this.onGround() && this.getControllingPassenger() == null && this.getDeltaMovement().y < 0) {
                this.fallingTicks++;
                if (this.fallingTicks >= 10) {
                    this.setGliding(true);
                    this.setRearing(false);
                    this.setGrassing(false);
                    this.calculateGlidingMovements();
                }
            } else {
                this.fallingTicks = 0;
            }
            this.serverTickedDeltaMove = this.position().subtract(this.lastPos);
            this.lastPos = this.position();
        }
    }

    private void calculateGlidingMovements(){
        Vec3 movement = this.getDeltaMovement();
        if(movement.y < -0.25){
            Vec3 calcMovement = new Vec3(movement.x * 1.1, movement.y * 0.9, movement.z * 1.1);
            this.setDeltaMovement(calcMovement);
            this.setLastGlidingVelocity((float) calcMovement.y);
            this.setLastGlidingCalc(this.tickCount);
        }
    }

    private void setupAnimStates(){
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = 3 * 20;
            this.idleAnimationState.start(this.tickCount);
        }else{
            this.idleAnimationTimeout--;
        }

        if(this.isGrassing()){
            this.grassingAnimationState.startIfStopped(this.tickCount);
        }else{
            this.grassingAnimationState.stop();
        }

        if(this.isRearing()){
            this.rearingAnimationState.startIfStopped(this.tickCount);
        }else{
            this.rearingAnimationState.stop();
        }

        if(this.isGliding()){
            if(this.glidingAnimationTimeout <= 0){
                this.glidingAnimationTimeout = 30;
                this.glidingAnimationState.start(this.tickCount);
            }else{
                this.glidingAnimationTimeout--;
            }
        }else{
            this.glidingAnimationTimeout = -1;
            this.glidingAnimationState.stop();
        }

        if(this.isFlying()){
            if(this.flyingAnimationTimeout <= 0){
                this.flyingAnimationTimeout = 20;
                this.flyingAnimationState.start(this.tickCount);
            }else{
                this.flyingAnimationTimeout--;
            }
        }else{
            this.flyingAnimationTimeout = -1;
            this.flyingAnimationState.stop();
        }
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        if (pFallDistance > 1.0F) {
            this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }

        int i = this.calculateFallDamage(pFallDistance, pMultiplier);
        if (i <= 0) {
            return false;
        } else {
            this.hurt(pSource, i);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(pSource, i);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        if((this.isGliding() || this.isFlying() || this.getLastGlidingCalc() + 3 > this.tickCount) && this.getLastGlidingVelocity() > -0.375){
            this.setGliding(false);
            this.setFlying(false);
            return 0;
        }else {
            double safeFallDist = this.getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
            double calcFallDist = pFallDistance - safeFallDist;
            this.setGliding(false);
            this.setFlying(false);
            return Mth.ceil((calcFallDist * pDamageMultiplier) * this.getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HORSE_DEATH;
    }

    @Nullable
    protected SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.HORSE_HURT;
    }

    protected SoundEvent getAngrySound() {
        return SoundEvents.HORSE_ANGRY;
    }

    public boolean isTamed(){
        return this.getFlag(FLAG_TAMED);
    }

    public void setTamed(boolean tamed){
        this.setFlag(FLAG_TAMED, tamed);
    }

    public boolean isGrassing(){
        return this.getFlag(FLAG_GRASSING);
    }

    public void setGrassing(boolean grassing){
        this.setFlag(FLAG_GRASSING, grassing);
    }

    public boolean isRearing(){
        return this.getFlag(FLAG_REARING);
    }

    public void setRearing(boolean rearing){
        if(rearing){
            setGrassing(false);
        }
        this.setFlag(FLAG_REARING, rearing);
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int pTemper) {
        this.temper = pTemper;
    }

    public void modifyTemper(int pAddedTemper) {
        int i = Mth.clamp(this.getTemper() + pAddedTemper, 0, this.getMaxTemper());
        this.setTemper(i);
    }

    public int getMaxTemper() {
        return 100;
    }

    @Override
    public boolean handleLeashAtDistance(@NotNull Entity pLeashHolder, float pDistance) {
        if(pDistance > 6f && this.isGrassing()){
            this.setGrassing(false);
        }
        return true;
    }

    public void setFlying(boolean flying){
        this.setFlag(FLAG_FLYING, flying);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround() && this.getFlag(FLAG_FLYING);
    }

    public void setGliding(boolean gliding){
        this.setFlag(FLAG_GLIDING, gliding);
    }

    public boolean isGliding(){
        return !this.onGround() && this.getFlag(FLAG_GLIDING);
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return this.owner;
    }

    public void setOwnerUUID(@Nullable UUID owner){
        this.owner = owner;
    }

    @Override
    public void onPlayerJump(int i) {}

    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int i) {
        if(!this.isGliding()) {
            this.playJumpSound();
        }
        if(this.isSaddled() && !this.isFlying() && !this.getStartFlying()){
            this.startFlying();
        }else if(this.isSaddled() && this.isFlying()){
            this.startGliding();
        }
    }

    @Override
    public void handleStopJump() {}

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTamed();
    }

    @Override
    public void equipSaddle(@NotNull ItemStack itemStack, @Nullable SoundSource soundSource) {
        this.setSaddled(true);
    }

    @Override
    public boolean isSaddled() {
        return this.getFlag(FLAG_SADDLED);
    }

    public void setSaddled(boolean saddled){
        this.setFlag(FLAG_SADDLED, saddled);
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle() && this.isSaddled() || this.isGrassing() || this.isRearing();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(!this.level().isClientSide && this.isAlive()){
            if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0F);
            }

            if (!this.isGrassing() && !this.isVehicle() && this.random.nextInt(300) == 0 && this.level().getBlockState(this.blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
                this.setGrassing(true);
                this.grassingCounter = 1;
            }

            if (this.isGrassing() && this.grassingCounter++ > 40) {
                this.grassingCounter = 0;
                this.setGrassing(false);
            }
            if(this.isRearing() && this.rearingCounter++ > 32) {
                this.rearingCounter = 0;
                this.setRearing(false);
            }
        }
    }

    public void startRearing(){
        if(this.isEffectiveAi()) {
            this.setRearing(true);
            this.rearingCounter = 1;
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        boolean flag = !this.isBaby() && this.isTamed() && pPlayer.isSecondaryUseActive();
        if (!this.isVehicle() && !flag) {
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            if (!itemstack.isEmpty()) {
                if (this.isFood(itemstack)) {
                    return this.fedFood(pPlayer, itemstack);
                }

                if (!this.isTamed()) {
                    this.makeMad();
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }
        }
        if(!this.isVehicle() && !this.isBaby() && !pPlayer.isSecondaryUseActive()){
            this.doPlayerRide(pPlayer);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    protected void doPlayerRide(Player pPlayer) {
        this.setGrassing(false);
        this.setRearing(false);
        if (!this.level().isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
        }
    }

    public void tameByPlayer(@NotNull Player tamingPlayer){
        this.setOwnerUUID(tamingPlayer.getUUID());
        this.setTamed(true);
        if(tamingPlayer instanceof ServerPlayer serverPlayer){
            CriteriaTriggers.TAME_ANIMAL.trigger(serverPlayer, this);
        }

        this.level().broadcastEntityEvent(this, (byte) 7);
    }

    public void makeMad() {
        if (!this.isRearing()) {
            this.startRearing();
            this.makeSound(this.getAngrySound());
        }

    }

    public InteractionResult fedFood(Player pPlayer, ItemStack pStack) {
        boolean flag = this.handleEating(pPlayer, pStack);
        if (flag) {
            pStack.consume(1, pPlayer);
        }

        if (this.level().isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
    }

    protected boolean handleEating(Player pPlayer, ItemStack pStack) {
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        int j = 0;
        if (pStack.is(Items.WHEAT)) {
            f = 2.0F;
            i = 20;
            j = 3;
        } else if (pStack.is(Items.SUGAR)) {
            f = 1.0F;
            i = 30;
            j = 3;
        } else if (pStack.is(Blocks.HAY_BLOCK.asItem())) {
            f = 20.0F;
            i = 180;
        } else if (pStack.is(Items.APPLE)) {
            f = 3.0F;
            i = 60;
            j = 3;
        } else if (pStack.is(Items.GOLDEN_CARROT)) {
            f = 4.0F;
            i = 60;
            j = 5;
            if (!this.level().isClientSide && this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(pPlayer);
            }
        } else if (pStack.is(Items.GOLDEN_APPLE) || pStack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            f = 10.0F;
            i = 240;
            j = 10;
            if (!this.level().isClientSide && this.isTamed() && this.getAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(pPlayer);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isBaby() && i > 0) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0F), this.getRandomY() + (double)0.5F, this.getRandomZ(1.0F), 0.0F, 0.0F, 0.0F);
            if (!this.level().isClientSide) {
                this.ageUp(i);
                flag = true;
            }
        }

        if (j > 0 && (flag || !this.isTamed()) && this.getTemper() < this.getMaxTemper() && !this.level().isClientSide) {
            this.modifyTemper(j);
            flag = true;
        }

        if (flag) {
            if (!this.isSilent()) {
                SoundEvent soundevent = this.getEatingSound();
                if (soundevent != null) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), soundevent, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                }
            }
            this.gameEvent(GameEvent.EAT);
        }

        return flag;
    }

    @Override
    protected void tickRidden(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        Vec2 vec2 = this.getRiddenRotation(pPlayer);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if(this.isControlledByLocalInstance()){
            if (this.onGround()) {
                if(this.getStartFlying()){
                    this.executeRidersJump(pTravelVector);
                }
            } else if(this.isFlying()){
                //FLYING
                Vec3 move = this.getDeltaMovement();
                double verticalMovement = pTravelVector.y;
                this.setDeltaMovement(move.x, verticalMovement, move.z);
            } else if(this.isGliding()) {
                //GLIDING
                Vec3 move = this.getDeltaMovement();
                if(move.y < -0.25){
                    Vec3 calcMovement = new Vec3(move.x * 1.1, move.y * 0.9, move.z * 1.1);
                    this.setDeltaMovement(calcMovement);
                }
            }
        }
        if (!this.level().isClientSide()) {
            if (this.onGround()) {
                if(this.getStartFlying()){
                    this.setStartFlying(false);
                }else {
                    this.setFlying(false);
                }
            } else if(this.isFlying()){
                //FLYING
                this.setLastGlidingCalc(this.tickCount);
                this.setLastGlidingVelocity((float) pTravelVector.y);
            } else if(this.isGliding()) {
                //GLIDING
                Vec3 move = this.serverTickedDeltaMove;
                if(move.y < -0.25){
                    Vec3 calcMovement = new Vec3(move.x * 1.1, move.y * 0.9, move.z * 1.1);
                    this.setLastGlidingCalc(this.tickCount);
                    this.setLastGlidingVelocity((float) calcMovement.y);
                }
            }
        }
    }

    protected Vec2 getRiddenRotation(@NotNull LivingEntity pEntity) {
        return new Vec2(pEntity.getXRot() * 0.5F, pEntity.getYRot());
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        if (this.onGround() && !this.getStartFlying() && this.isRearing()) {
            return Vec3.ZERO;
        } else {
            float f = pPlayer.xxa * 0.5F;
            float f1 = pPlayer.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            if(this.isFlying()) {
                return new Vec3(f, pPlayer.getXRot() / -90.0, f1);
            }else{
                return new Vec3(f, 0.0, f1);
            }
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return (float) ((this.isFlying() || this.isGliding()) ? this.getAttributeValue(Attributes.FLYING_SPEED) : this.getAttributeValue(Attributes.MOVEMENT_SPEED));
    }

    private void startFlying(){
        this.setFlying(true);
        this.setGliding(false);
        this.setStartFlying(true);
    }

    private void startGliding(){
        this.setFlying(false);
        this.setGliding(true);
        this.setStartFlying(false);
    }

    protected void executeRidersJump(Vec3 pTravelVector) {
        double d0 = this.getJumpPower(1);
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d0, vec3.z);
        this.setFlying(true);
        this.hasImpulse = true;
        if (pTravelVector.z > 0.0) {
            float f = Mth.sin(this.getYRot() * (float) (Math.PI / 180.0));
            float f1 = Mth.cos(this.getYRot() * (float) (Math.PI / 180.0));
            this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f, 0.0, 0.4F * f1));
        }
    }

    protected void playJumpSound() {
        this.playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
    }

    protected void spawnTamingParticles(boolean pTamed) {
        ParticleOptions particleoptions = pTamed ? ParticleTypes.HEART : ParticleTypes.SMOKE;

        for (int i = 0; i < 7; i++) {
            double d0 = this.random.nextGaussian() * 0.02;
            double d1 = this.random.nextGaussian() * 0.02;
            double d2 = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particleoptions, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d0, d1, d2);
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 7) {
            this.spawnTamingParticles(true);
        } else if (pId == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, Entity.@NotNull MoveFunction pCallback) {
        super.positionRider(pPassenger, pCallback);
        if (pPassenger instanceof LivingEntity) {
            ((LivingEntity)pPassenger).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        if(this.isSaddled()){
            Entity passenger = this.getFirstPassenger();
            if(passenger instanceof Player p){
                return p;
            }
        }

        return super.getControllingPassenger();
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 pDirection, LivingEntity pPassenger) {
        double d0 = this.getX() + pDirection.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + pDirection.z;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (Pose pose : pPassenger.getDismountPoses()) {
            blockpos$mutableblockpos.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75;

            do {
                double d4 = this.level().getBlockFloorHeight(blockpos$mutableblockpos);
                if ((double)blockpos$mutableblockpos.getY() + d4 > d3) {
                    break;
                }

                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = pPassenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, (double)blockpos$mutableblockpos.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level(), pPassenger, aabb.move(vec3))) {
                        pPassenger.setPose(pose);
                        return vec3;
                    }
                }

                blockpos$mutableblockpos.move(Direction.UP);
            } while (!((double)blockpos$mutableblockpos.getY() < d3));
        }

        return null;
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity pLivingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector(
                this.getBbWidth(), pLivingEntity.getBbWidth(), this.getYRot() + (pLivingEntity.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F)
        );
        Vec3 vec31 = this.getDismountLocationInDirection(vec3, pLivingEntity);
        if (vec31 != null) {
            return vec31;
        } else {
            Vec3 vec32 = getCollisionHorizontalEscapeVector(
                    this.getBbWidth(), pLivingEntity.getBbWidth(), this.getYRot() + (pLivingEntity.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F)
            );
            Vec3 vec33 = this.getDismountLocationInDirection(vec32, pLivingEntity);
            return vec33 != null ? vec33 : this.position();
        }
    }

    //Variant handling

    private int getVariantID(){
        return this.entityData.get(VARIANT);
    }

    @Override
    public @NotNull Variant getVariant(){
        return Variant.byId(this.getVariantID() & 255);
    }

    @Override
    public void setVariant(@NotNull Variant pVariant){
        this.entityData.set(VARIANT, pVariant.getId() & 255);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        Variant variant = Util.getRandom(Variant.values(), this.random);
        this.setVariant(variant);
        if (pSpawnGroupData == null) {
            pSpawnGroupData = new AgeableMob.AgeableMobGroupData(false);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    public enum Variant implements StringRepresentable {
        WHITE(0, "white"),
        BLACK(1, "black"),
        BROWN(2, "brown"),
        CHESTNUT(3, "chestnut"),
        CREAMY(4, "creamy"),
        DARK_BROWN(5, "dark_brown"),
        GRAY(6, "gray");

        private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.CLAMP);

        final int id;
        private final String name;

        Variant(int pId, String pName) {
            this.id = pId;
            this.name = pName;
        }

        public int getId() {
            return this.id;
        }

        public static Variant byId(int pId) {
            return BY_ID.apply(pId);
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public static class PegasusRunAroundLikeCrazyGoal extends Goal {
        private final PegasusEntity pegasus;
        private final double speedModifier;
        private double posX;
        private double posY;
        private double posZ;

        public PegasusRunAroundLikeCrazyGoal(PegasusEntity pPegasus, double pSpeedModifier) {
            this.pegasus = pPegasus;
            this.speedModifier = pSpeedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!this.pegasus.isTamed() && this.pegasus.isVehicle()) {
                Vec3 vec3 = DefaultRandomPos.getPos(this.pegasus, 5, 4);
                if (vec3 == null) {
                    return false;
                } else {
                    this.posX = vec3.x;
                    this.posY = vec3.y;
                    this.posZ = vec3.z;
                    return true;
                }
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.pegasus.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        }

        @Override
        public boolean canContinueToUse() {
            return !this.pegasus.isTamed() && !this.pegasus.getNavigation().isDone() && this.pegasus.isVehicle();
        }

        @Override
        public void tick() {
            if (!this.pegasus.isTamed() && this.pegasus.getRandom().nextInt(this.adjustedTickDelay(50)) == 0) {
                Entity entity = this.pegasus.getFirstPassenger();
                if (entity == null) {
                    return;
                }

                if (entity instanceof Player player) {
                    int i = this.pegasus.getTemper();
                    int j = this.pegasus.getMaxTemper();
                    if (j > 0 && this.pegasus.getRandom().nextInt(j) < i) {
                        this.pegasus.tameByPlayer(player);
                        return;
                    }

                    this.pegasus.modifyTemper(5);
                }

                this.pegasus.ejectPassengers();
                this.pegasus.makeMad();
                this.pegasus.level().broadcastEntityEvent(this.pegasus, (byte)6);
            }
        }
    }

    //Utility methods

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    public static boolean checkPegasusSpawnRules(EntityType<? extends PegasusEntity> pPegasus, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return checkAnimalSpawnRules(pPegasus, pLevel, pSpawnType, pPos, pRandom);
    }

    public static LootTable.Builder getLootTableBuilder(){
        return LootTable.lootTable()
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .setBonusRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.FEATHER))
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .setBonusRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.LEATHER))
                );
    }
}
