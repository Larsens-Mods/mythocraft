package de.larsensmods.mythocraft.world.level;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.larsensmods.mythocraft.Constants;
import de.larsensmods.mythocraft.world.level.util.LabyrinthUtilFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class LabyrinthChunkGenerator extends ChunkGenerator {

    private static final int BASE_FLOOR_THICKNESS = 5;

    private static final Map<LabyrinthUtilFunctions.Shape, ResourceLocation> TILE_MAPPINGS = Map.of(
            LabyrinthUtilFunctions.Shape.EMPTY, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_0x"),
            LabyrinthUtilFunctions.Shape.DEAD_END, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_1x"),
            LabyrinthUtilFunctions.Shape.CURVE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_corner"),
            LabyrinthUtilFunctions.Shape.STRAIGHT, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_2x_straight"),
            LabyrinthUtilFunctions.Shape.THREE_WAY_JUNCTION, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_3x"),
            LabyrinthUtilFunctions.Shape.FOUR_WAY_JUNCTION, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "labyrinth/labyrinth_tile_4x")
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
        StructureTemplateManager structureTemplateManager = level.getServer().getStructureManager();
        byte tileType = LabyrinthUtilFunctions.calculateCellType(seed, chunk.getPos().x, chunk.getPos().z);
        LabyrinthUtilFunctions.Shape tileShape = LabyrinthUtilFunctions.getShape(tileType);
        ResourceLocation tileStructure = TILE_MAPPINGS.get(tileShape);
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
                        level.setBlock(new BlockPos(blockInfo.pos().getX() + chunk.getPos().getMinBlockX(), blockInfo.pos().getY() + baseY, blockInfo.pos().getZ() + chunk.getPos().getMinBlockZ()).offset(offset), blockInfo.state(), Block.UPDATE_NONE);
                    }
                }
            }
        }
        //TODO: Generate structure pieces for the labyrinth, find portals in overworld through 'level.getServer().getLevel(Level.OVERWORLD).findNearestMapStructure()'
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
