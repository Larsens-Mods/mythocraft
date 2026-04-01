package de.larsensmods.mythocraft.world.level;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.data.MythLevels;
import de.larsensmods.mythocraft.data.MythocraftStructures;
import de.larsensmods.mythocraft.world.level.util.LabyrinthUtilFunctions;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LabyrinthChunkGenerator extends ChunkGenerator {

    private static final int BASE_FLOOR_THICKNESS = 5;

    private static final Map<LabyrinthUtilFunctions.Shape, ResourceLocation[]> TILE_MAPPINGS = Map.of(
            LabyrinthUtilFunctions.Shape.EMPTY, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_0x")},
            LabyrinthUtilFunctions.Shape.DEAD_END, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_1x_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_1x_2")},
            LabyrinthUtilFunctions.Shape.CURVE, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_2"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_3"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_4"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_5")},
            LabyrinthUtilFunctions.Shape.STRAIGHT, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_2"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_3"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_4"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_5")},
            LabyrinthUtilFunctions.Shape.THREE_WAY_JUNCTION, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_2"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_3"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_4")},
            LabyrinthUtilFunctions.Shape.FOUR_WAY_JUNCTION, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_2"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_3"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_4")}
    );
    private static final Map<LabyrinthUtilFunctions.Shape, ResourceLocation[]> PORTAL_TILE_MAPPINGS = Map.of(
            LabyrinthUtilFunctions.Shape.EMPTY, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_0x_portal")},
            LabyrinthUtilFunctions.Shape.DEAD_END, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_1x_portal_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_1x_portal_2")},
            LabyrinthUtilFunctions.Shape.CURVE, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_portal_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_curve_portal_2")},
            LabyrinthUtilFunctions.Shape.STRAIGHT, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_portal_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight_portal_2")},
            LabyrinthUtilFunctions.Shape.THREE_WAY_JUNCTION, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_portal_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_portal_2")},
            LabyrinthUtilFunctions.Shape.FOUR_WAY_JUNCTION, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_portal_1"), ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_portal_2")}
    );
    private static final Map<LabyrinthUtilFunctions.Shape, ResourceLocation[]> BOSS_TILE_MAPPINGS = Map.of(
            LabyrinthUtilFunctions.Shape.DEAD_END, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_1x_boss")},
            LabyrinthUtilFunctions.Shape.THREE_WAY_JUNCTION, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x_boss")},
            LabyrinthUtilFunctions.Shape.FOUR_WAY_JUNCTION, new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x_boss")}
    );
    private static final Map<Rotation, BlockPos> STRUCTURE_OFFSETS = Map.of(
            Rotation.NONE, BlockPos.ZERO,
            Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 15),
            Rotation.CLOCKWISE_180, new BlockPos(15, 0, 15),
            Rotation.CLOCKWISE_90, new BlockPos(15, 0, 0)
    );

    public static final MapCodec<LabyrinthChunkGenerator> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(LabyrinthChunkGeneratorSettings.CODEC.fieldOf("settings").forGetter((generator) -> generator.settings)).apply(instance, instance.stable(LabyrinthChunkGenerator::new)));
    private final LabyrinthChunkGeneratorSettings settings;

    public LabyrinthChunkGenerator(LabyrinthChunkGeneratorSettings settings) {
        super(new FixedBiomeSource(settings.getBiome()));
        this.settings = settings;
    }

    @Override
    protected @NotNull MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public void applyCarvers(@NotNull WorldGenRegion level, long seed, @NotNull RandomState random, @NotNull BiomeManager biomeManager, @NotNull StructureManager structureManager, @NotNull ChunkAccess chunk, GenerationStep.@NotNull Carving genStep) {
        if(level.getServer() == null) {
            Constants.LOG.warn("Could not get server from world gen region, skipping structure piece generation for the labyrinth");
            return;
        }
        ServerLevel overworld = level.getServer().overworld();
        ServerLevel labyrinthServerLevel = level.getServer().getLevel(MythLevels.LABYRINTH);
        Registry<Structure> structureRegistry = overworld.registryAccess().registryOrThrow(Registries.STRUCTURE);

        StructureTemplateManager structureTemplateManager = level.getServer().getStructureManager();
        byte tileType = LabyrinthUtilFunctions.calculateCellType(seed, chunk.getPos().x, chunk.getPos().z);
        LabyrinthUtilFunctions.Shape tileShape = LabyrinthUtilFunctions.getShape(tileType);

        boolean portalTile = false;

        double teleportScale = DimensionType.getTeleportationScale(level.dimensionType(), overworld.dimensionType());
        HolderSet<Structure> structureHolderSet = HolderSet.direct(structureRegistry.getHolderOrThrow(MythocraftStructures.LABYRINTH_PORTAL), structureRegistry.getHolderOrThrow(MythocraftStructures.LABYRINTH_PORTAL_SANDSTONE));
        BlockPos labyrinthOriginPos = chunk.getPos().getMiddleBlockPosition(overworld.getSeaLevel());
        BlockPos originPos = new BlockPos(
                (int) (labyrinthOriginPos.getX() * teleportScale),
                labyrinthOriginPos.getY(),
                (int) (labyrinthOriginPos.getZ() * teleportScale)
        );
        int searchRadius = (int) Math.ceil(1.2 * teleportScale);
        Pair<BlockPos, Holder<Structure>> result = findNearestMapStructureInLevel(
                overworld,
                structureHolderSet,
                originPos,
                searchRadius
        );
        if(result != null){
            BlockPos overworldMinPos = new BlockPos((int) (chunk.getPos().getMinBlockX() * teleportScale), overworld.getMinBuildHeight(), (int) (chunk.getPos().getMinBlockZ() * teleportScale));
            BlockPos overworldMaxPos = new BlockPos((int) (chunk.getPos().getMaxBlockX() * teleportScale), overworld.getMaxBuildHeight(), (int) (chunk.getPos().getMaxBlockZ() * teleportScale));

            if(BlockPos.min(overworldMinPos, result.getFirst()).equals(overworldMinPos) && BlockPos.max(overworldMaxPos, result.getFirst()).equals(overworldMaxPos)){
                portalTile = true;
            }
        }

        ResourceLocation tileStructure;

        boolean isBossTile = !portalTile
                && BOSS_TILE_MAPPINGS.containsKey(tileShape)
                && BOSS_TILE_MAPPINGS.get(tileShape).length > 0
                && LabyrinthUtilFunctions.canBeBossTile(seed, chunk.getPos().x, chunk.getPos().z);
        if(isBossTile){
            tileStructure = BOSS_TILE_MAPPINGS.get(tileShape)[LabyrinthUtilFunctions.getTileVariant(seed, chunk.getPos().x, chunk.getPos().z, BOSS_TILE_MAPPINGS.get(tileShape).length)];
        }else{
            tileStructure = portalTile
                    ? PORTAL_TILE_MAPPINGS.get(tileShape)[LabyrinthUtilFunctions.getTileVariant(seed, chunk.getPos().x, chunk.getPos().z, PORTAL_TILE_MAPPINGS.get(tileShape).length)]
                    : TILE_MAPPINGS.get(tileShape)[LabyrinthUtilFunctions.getTileVariant(seed, chunk.getPos().x, chunk.getPos().z, TILE_MAPPINGS.get(tileShape).length)];
        }

        if(tileStructure != null){
            StructureTemplate structure = structureTemplateManager.get(tileStructure).orElse(null);
            if(structure != null){
                int baseY = chunk.getMinBuildHeight() + BASE_FLOOR_THICKNESS;
                Set<Block> set = new HashSet<>();
                for(StructureTemplate.StructureBlockInfo blockInfo : structure.palettes.getFirst().blocks()){
                    set.add(blockInfo.state().getBlock());
                }
                Rotation rotation = LabyrinthUtilFunctions.calcRequiredRotation(tileShape, tileType);
                BlockPos offset = STRUCTURE_OFFSETS.get(rotation);
                StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(rotation);
                for(Block block : set){
                    for(StructureTemplate.StructureBlockInfo blockInfo : structure.filterBlocks(BlockPos.ZERO, settings, block)){
                        BlockPos pos = new BlockPos(blockInfo.pos().getX() + chunk.getPos().getMinBlockX(), blockInfo.pos().getY() + baseY, blockInfo.pos().getZ() + chunk.getPos().getMinBlockZ()).offset(offset);
                        level.setBlock(pos, blockInfo.state(), Block.UPDATE_NONE);
                        if(level.getBlockEntity(pos) != null && blockInfo.nbt() != null){
                            Objects.requireNonNull(level.getBlockEntity(pos)).loadWithComponents(blockInfo.nbt(), level.registryAccess());
                        }
                    }
                }
                if(labyrinthServerLevel == null){
                    Constants.LOG.warn("Could not spawn entities, labyrinthServerLevel is null!");
                    return;
                }
                for(StructureTemplate.StructureEntityInfo entityInfo : structure.entityInfoList){
                    Vec3 spawnPos = entityInfo.pos;
                    switch(rotation){
                        case CLOCKWISE_90 -> spawnPos = new Vec3(16 - spawnPos.z, spawnPos.y, spawnPos.x);
                        case CLOCKWISE_180 -> spawnPos = new Vec3(16 - spawnPos.x, spawnPos.y, 16 - spawnPos.z);
                        case COUNTERCLOCKWISE_90 -> spawnPos = new Vec3(spawnPos.z, spawnPos.y, 16 - spawnPos.x);
                    }
                    CompoundTag entityData = entityInfo.nbt;
                    String entityID = entityData.getString("id");
                    EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.tryParse(entityID));
                    Entity entity = type.spawn(labyrinthServerLevel, new BlockPos((int) (spawnPos.x + chunk.getPos().getMinBlockX()), (int) (spawnPos.y + baseY), (int) (spawnPos.z + chunk.getPos().getMinBlockZ())), MobSpawnType.STRUCTURE);
                    if(entity != null) {
                        entityData.putUUID("UUID", entity.getUUID());
                        ListTag pos = entityData.getList("Pos", ListTag.TAG_DOUBLE);
                        pos.set(0, DoubleTag.valueOf(entity.position().x));
                        pos.set(1, DoubleTag.valueOf(entity.position().y));
                        pos.set(2, DoubleTag.valueOf(entity.position().z));
                        entityData.put("Pos", pos);
                        entity.load(entityData);
                        Constants.LOG.debug("Spawned entity of type {} with id {} at {}", entity.getType(), entity.getStringUUID(), entity.position());
                    }else{
                        Constants.LOG.warn("Failed to spawn entity of type {} at position {}", entityID, spawnPos);
                    }
                }
            }
        }
    }

    @Nullable
    private Pair<BlockPos, Holder<Structure>> findNearestMapStructureInLevel(ServerLevel pLevel, HolderSet<Structure> pStructure, BlockPos pPos, int searchRadius) {
        ChunkGeneratorStructureState chunkgeneratorstructurestate = pLevel.getChunkSource().getGeneratorState();
        Map<StructurePlacement, Set<Holder<Structure>>> map = new Object2ObjectArrayMap<>();
        for(Holder<Structure> holder : pStructure) {
            for(StructurePlacement structureplacement : chunkgeneratorstructurestate.getPlacementsForStructure(holder)) {
                map.computeIfAbsent(structureplacement, (placement) -> new ObjectArraySet<>()).add(holder);
            }
        }
        if (map.isEmpty()) {
            return null;
        } else {
            Pair<BlockPos, Holder<Structure>> pair2 = null;
            double d2 = Double.MAX_VALUE;
            StructureManager structuremanager = pLevel.structureManager();
            List<Map.Entry<StructurePlacement, Set<Holder<Structure>>>> list = new ArrayList<>(map.size());
            for(Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : map.entrySet()) {
                StructurePlacement placement = entry.getKey();
                if (placement instanceof RandomSpreadStructurePlacement) {
                    list.add(entry);
                }
            }
            if (!list.isEmpty()) {
                int i = SectionPos.blockToSectionCoord(pPos.getX());
                int j = SectionPos.blockToSectionCoord(pPos.getZ());
                for(int k = 0; k <= searchRadius; ++k) {
                    boolean flag = false;
                    for(Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry1 : list) {
                        RandomSpreadStructurePlacement randomspreadstructureplacement = (RandomSpreadStructurePlacement)entry1.getKey();
                        Pair<BlockPos, Holder<Structure>> pair1 = getNearestGeneratedStructure(entry1.getValue(), pLevel, structuremanager, i, j, k, chunkgeneratorstructurestate.getLevelSeed(), randomspreadstructureplacement);
                        if (pair1 != null) {
                            flag = true;
                            double d1 = pPos.distSqr(pair1.getFirst());
                            if (d1 < d2) {
                                d2 = d1;
                                pair2 = pair1;
                            }
                        }
                    }
                    if (flag) {
                        return pair2;
                    }
                }
            }
            return pair2;
        }
    }

    @Nullable
    private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, int pX, int pY, int pZ, long pSeed, RandomSpreadStructurePlacement pSpreadPlacement) {
        int i = pSpreadPlacement.spacing();
        for(int j = -pZ; j <= pZ; ++j) {
            boolean flag = j == -pZ || j == pZ;
            for(int k = -pZ; k <= pZ; ++k) {
                boolean flag1 = k == -pZ || k == pZ;
                if (flag || flag1) {
                    int l = pX + i * j;
                    int i1 = pY + i * k;
                    ChunkPos chunkpos = pSpreadPlacement.getPotentialStructureChunk(pSeed, l, i1);
                    Pair<BlockPos, Holder<Structure>> pair = getStructureGeneratingAt(pStructureHoldersSet, pLevel, pStructureManager, pSpreadPlacement, chunkpos);
                    if (pair != null) {
                        return pair;
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, StructurePlacement pPlacement, ChunkPos pChunkPos) {
        for(Holder<Structure> holder : pStructureHoldersSet) {
            StructureCheckResult structurecheckresult = pStructureManager.checkStructurePresence(pChunkPos, holder.value(), pPlacement, false);
            if (structurecheckresult != StructureCheckResult.START_NOT_PRESENT) {
                if (structurecheckresult == StructureCheckResult.START_PRESENT) {
                    return Pair.of(pPlacement.getLocatePos(pChunkPos), holder);
                }

                if(structurecheckresult == StructureCheckResult.CHUNK_LOAD_NEEDED) {
                    return Pair.of(pChunkPos.getMiddleBlockPosition(pLevel.getMinBuildHeight()), holder);
                }
                Constants.LOG.warn("{}:getStructureGeneratingAt() detected technically unreachable state for structurecheckresult {}", LabyrinthChunkGenerator.class.getSimpleName(), structurecheckresult);
            }
        }
        return null;
    }

    @Override
    public void buildSurface(@NotNull WorldGenRegion worldGenRegion, @NotNull StructureManager structureManager, @NotNull RandomState randomState, @NotNull ChunkAccess chunkAccess) {}

    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion worldGenRegion) {}

    @Override
    public int getGenDepth() {
        return 256;
    }

    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Blender blender, @NotNull RandomState randomState, @NotNull StructureManager structureManager, @NotNull ChunkAccess chunkAccess) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        Heightmap oceanFloorHeightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap worldSurfaceHeightmap = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

        for(int i = 0; i < Math.min(chunkAccess.getHeight(), BASE_FLOOR_THICKNESS); i++){
            BlockState blockState = Blocks.BEDROCK.defaultBlockState();
            int y = chunkAccess.getMinBuildHeight() + i;
            for(int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunkAccess.setBlockState(mutableBlockPos.set(x, y, z), blockState, false);
                    oceanFloorHeightmap.update(x, y, z, blockState);
                    worldSurfaceHeightmap.update(x, y, z, blockState);
                }
            }
        }
        return CompletableFuture.completedFuture(chunkAccess);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinY() {
        return 0;
    }

    @Override
    public int getBaseHeight(int i, int i1, Heightmap.@NotNull Types types, @NotNull LevelHeightAccessor levelHeightAccessor, @NotNull RandomState randomState) {
        return levelHeightAccessor.getMinBuildHeight() + BASE_FLOOR_THICKNESS;
    }

    @Override
    public @NotNull NoiseColumn getBaseColumn(int i, int i1, @NotNull LevelHeightAccessor levelHeightAccessor, @NotNull RandomState randomState) {
        return new NoiseColumn(levelHeightAccessor.getMinBuildHeight(), new BlockState[]{Blocks.BEDROCK.defaultBlockState(), Blocks.BEDROCK.defaultBlockState(), Blocks.BEDROCK.defaultBlockState(), Blocks.BEDROCK.defaultBlockState(), Blocks.BEDROCK.defaultBlockState()});
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> list, @NotNull RandomState randomState, @NotNull BlockPos blockPos) {}

    @Override
    public int getSpawnHeight(@NotNull LevelHeightAccessor pLevel) {
        return pLevel.getMinBuildHeight() + Math.min(pLevel.getHeight(), BASE_FLOOR_THICKNESS);
    }
}
