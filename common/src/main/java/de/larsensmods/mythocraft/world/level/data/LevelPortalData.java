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
import java.util.Optional;

public class LevelPortalData extends SavedData {

    public static final String NAME = "labyrinth_portal_data";

    private final Map<Long, Long> portalBasePositions;

    public static SavedData.Factory<LevelPortalData> factory(){
        return new SavedData.Factory<>(LevelPortalData::new, LevelPortalData::load, DataFixTypes.LEVEL);
    }

    public LevelPortalData() {
        this(new HashMap<>());
    }

    private LevelPortalData(Map<Long, Long> portalBasePositions) {
        this.portalBasePositions = portalBasePositions;
    }

    public static LevelPortalData load(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        Map<Long, Long> portalBasePositions = new HashMap<>();
        CompoundTag tag = compoundTag.getCompound("LevelPortalData");
        for(String key : tag.getAllKeys()){
            long chunkPos = Long.parseLong(key);
            long portalBasePos = tag.getLong(key);
            portalBasePositions.put(chunkPos, portalBasePos);
        }
        return new LevelPortalData(portalBasePositions);
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        for(Map.Entry<Long, Long> entry : portalBasePositions.entrySet()){
            tag.putLong(String.valueOf(entry.getKey()), entry.getValue());
        }
        compoundTag.put("LabyrinthPortalBases", tag);
        return compoundTag;
    }

    @NotNull
    public Optional<Long> getPortalBaseForChunk(long chunkPos){
        return Optional.ofNullable(portalBasePositions.get(chunkPos));
    }

    public void clearPortalBaseForChunk(@NotNull ChunkPos chunkPos){
        clearPortalBaseForChunk(chunkPos.toLong());
    }

    public void clearPortalBaseForChunk(long chunkPos){
        portalBasePositions.remove(chunkPos);
        setDirty();
    }

    public void setPortalBaseForChunk(@NotNull ChunkPos chunkPos, @NotNull BlockPos portalBasePos){
        setPortalBaseForChunk(chunkPos.toLong(), portalBasePos.asLong());
    }

    public void setPortalBaseForChunk(long chunkPos, long portalBasePos){
        portalBasePositions.put(chunkPos, portalBasePos);
        setDirty();
    }
}
