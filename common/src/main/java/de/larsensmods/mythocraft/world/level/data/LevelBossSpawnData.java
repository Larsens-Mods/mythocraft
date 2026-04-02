package de.larsensmods.mythocraft.world.level.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LevelBossSpawnData extends SavedData {

    public static final String NAME = "labyrinth_boss_spawn_data";

    private final Map<Long, CompoundTag> bossSpawners;

    public static SavedData.Factory<LevelBossSpawnData> factory(){
        return new SavedData.Factory<>(LevelBossSpawnData::new, LevelBossSpawnData::load, DataFixTypes.LEVEL);
    }

    public LevelBossSpawnData() {
        this(new HashMap<>());
    }

    private LevelBossSpawnData(Map<Long, CompoundTag> bossSpawners) {
        this.bossSpawners = bossSpawners;
    }

    public static LevelBossSpawnData load(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        Map<Long, CompoundTag> bossSpawners = new HashMap<>();
        CompoundTag tag = compoundTag.getCompound("LevelBossSpawnData");
        for(String posString : tag.getAllKeys()) {
            long position = Long.parseLong(posString);
            CompoundTag entityData = tag.getCompound(posString);
            bossSpawners.put(position, entityData);
        }
        return new LevelBossSpawnData(bossSpawners);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        for(Map.Entry<Long, CompoundTag> entry : bossSpawners.entrySet()){
            tag.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        compoundTag.put("LevelBossSpawnData", tag);
        return compoundTag;
    }

    @NotNull
    public Map<Long, CompoundTag> getOpenSpawners(){
        return Map.copyOf(bossSpawners);
    }

    public void addBossSpawner(BlockPos position, CompoundTag data){
        addBossSpawner(position.asLong(), data);
    }

    public void addBossSpawner(long position, CompoundTag data){
        bossSpawners.put(position, data);
        setDirty();
    }

    public void doneSpawning(long position){
        bossSpawners.remove(position);
        setDirty();
    }
}