package de.larsensmods.mythocraft.entity.friendly;

import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.entity.MythEntities;
import de.larsensmods.mythocraft.entity.ai.goal.BlacksmithCraftingGoal;
import de.larsensmods.mythocraft.entity.ai.goal.PickupFromChestGoal;
import de.larsensmods.mythocraft.entity.ai.goal.PutIntoChestGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BlacksmithCyclopsEntity extends AgeableMob implements InventoryCarrier {

    private static final Block CHEST_BLOCK_TYPE = Blocks.TRAPPED_CHEST;
    private static final Function<Item, Boolean> PICKUP_VALIDATOR = (item) -> {
        for(BlacksmithCyclopsRecipe recipe : BlacksmithCyclopsRecipe.RECIPES){
            if(recipe.ingredients().containsKey(item)){
                return true;
            }
        }
        return false;
    };
    private static final Function<Item, Boolean> PUT_DOWN_VALIDATOR = (item) -> !PICKUP_VALIDATOR.apply(item) && !item.equals(Items.AIR);

    private final SimpleContainer inventory = new SimpleContainer(4);

    private BlockPos currentAnvil = null;
    private BlockPos currentChest = null;

    private int craftingFinishTick = 0;
    private Runnable craftingFinished = null;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState smithingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int smithingAnimationTimeout = 0;

    private final int scanTickOffset = this.random.nextInt(100);

    public BlacksmithCyclopsEntity(EntityType<? extends AgeableMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BlacksmithCraftingGoal(this, this, () -> this.currentAnvil));
        this.goalSelector.addGoal(3, new PickupFromChestGoal(this, this, () -> this.currentChest, PICKUP_VALIDATOR));
        this.goalSelector.addGoal(4, new PutIntoChestGoal(this, this, () -> this.currentChest, PUT_DOWN_VALIDATOR));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide()){
            this.setupAnimStates();
        }else{
            if(this.tickCount % 100 == this.scanTickOffset){
                this.scanForAnvils();
                this.scanForChests();
            }
            if(this.craftingFinished != null && this.craftingFinishTick <= this.tickCount){
                this.craftingFinished.run();
                this.craftingFinished = null;
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
        if(this.craftingFinishTick > this.tickCount){
            if(this.smithingAnimationTimeout <= 0){
                this.smithingAnimationTimeout = 55;
                this.smithingAnimationState.start(this.tickCount);
            }else{
                this.smithingAnimationTimeout--;
            }
        }else{
            this.smithingAnimationState.stop();
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return MythEntities.BLACKSMITH_CYCLOPS.get().create(serverLevel);
    }


    @Override
    public @NotNull SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.writeInventoryToTag(pCompound, this.registryAccess());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readInventoryFromTag(pCompound, this.registryAccess());
    }

    public void scanForAnvils(){
        //TODO: Search from center outwards to find the closest anvil
        if (this.currentAnvil == null || !(this.blockPosition().distSqr(this.currentAnvil) < 48) || !this.level().getBlockState(this.currentAnvil).is(BlockTags.ANVIL)) {
            this.currentAnvil = null;
            for (int xRel = -24; xRel <= 24; xRel++) {
                for (int yRel = -8; yRel <= 8; yRel++) {
                    for (int zRel = -24; zRel <= 24; zRel++) {
                        BlockPos checkPos = this.blockPosition().offset(xRel, yRel, zRel);
                        if (this.level().getBlockState(checkPos).is(BlockTags.ANVIL)) {
                            this.currentAnvil = checkPos;
                        }
                    }
                }
            }
        }
    }

    public void scanForChests(){
        //TODO: Search from center outwards to find the closest chest
        if (this.currentChest == null || !(this.blockPosition().distSqr(this.currentChest) < 48) || !this.level().getBlockState(this.currentChest).is(CHEST_BLOCK_TYPE)) {
            this.currentChest = null;
            for (int xRel = -24; xRel <= 24; xRel++) {
                for (int yRel = -8; yRel <= 8; yRel++) {
                    for (int zRel = -24; zRel <= 24; zRel++) {
                        BlockPos checkPos = this.blockPosition().offset(xRel, yRel, zRel);
                        if (this.level().getBlockState(checkPos).is(CHEST_BLOCK_TYPE)) {
                            this.currentChest = checkPos;
                        }
                    }
                }
            }
        }
    }

    public void startCrafting(BlacksmithCyclopsRecipe recipe, Runnable onCraftComplete){
        if(!recipe.canUse(this)){
            Constants.LOG.error("Tried to start crafting with BlacksmithCyclopsEntity.startCrafting without having the required items.");
            return;
        }
        this.craftingFinishTick = this.tickCount + recipe.craftingTicks();
        this.craftingFinished = () -> {
            recipe.changeItems(this);
            onCraftComplete.run();
        };
    }

    //Utility methods

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8)
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
}
