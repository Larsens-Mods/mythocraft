package de.larsensmods.mythocraft.world.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class LabyrinthChunkGeneratorSettings {

    private static final Codec<LabyrinthChunkGeneratorSettings> CODEC_RAW = RecordCodecBuilder.create((instance) -> instance.group(Biome.CODEC.fieldOf("biome").forGetter((settings) -> settings.biome)).apply(instance, LabyrinthChunkGeneratorSettings::new));
    public static final Codec<LabyrinthChunkGeneratorSettings> CODEC = CODEC_RAW.stable();
    private final Holder<Biome> biome;

    public LabyrinthChunkGeneratorSettings(Holder<Biome> biome) {
        this.biome = biome;
    }

    public Holder<Biome> getBiome() {
        return biome;
    }
}
