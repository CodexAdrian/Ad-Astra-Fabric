package net.mrscauthd.beyond_earth.world.chunk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.mrscauthd.beyond_earth.mixin.ChunkGeneratorAccessor;
import net.mrscauthd.beyond_earth.mixin.NoiseChunkGeneratorAccessor;
import net.mrscauthd.beyond_earth.world.WorldSeed;

public class PlanetChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<PlanetChunkGenerator> CODEC = RecordCodecBuilder
            .create((
                    instance) -> method_41042(instance)
                            .and(instance.group(
                                    RegistryOps.createRegistryCodec(Registry.NOISE_WORLDGEN)
                                            .forGetter((generator) -> ((NoiseChunkGeneratorAccessor) generator)
                                                    .getNoiseRegistry()),
                                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(
                                            (generator) -> ((ChunkGeneratorAccessor) generator).getPopulationSource()),
                                    Codec.LONG.fieldOf("seed").stable().forGetter(
                                            (generator) -> ((NoiseChunkGeneratorAccessor) generator).getSeed()),
                                    ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings")
                                            .forGetter((generator) -> generator.settings)))
                            .apply(instance, instance.stable(PlanetChunkGenerator::new)));

    public PlanetChunkGenerator(Registry<StructureSet> noiseRegistry,
            Registry<DoublePerlinNoiseSampler.NoiseParameters> structuresRegistry, BiomeSource biomeSource, long seed,
            RegistryEntry<ChunkGeneratorSettings> settings) {
        super(noiseRegistry, structuresRegistry, biomeSource, seed == 0 ? WorldSeed.getSeed() : seed, settings);
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        NoiseChunkGeneratorAccessor noiseAccessor = (NoiseChunkGeneratorAccessor) this;
        ChunkGeneratorAccessor chunkAccessor = (ChunkGeneratorAccessor) this;
        return new PlanetChunkGenerator(chunkAccessor.getStructureSet(), noiseAccessor.getNoiseRegistry(),
                this.biomeSource.withSeed(seed), seed, this.settings);
    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
        BlockState bedrock = Blocks.BEDROCK.getDefaultState();

        BlockPos.Mutable pos = new BlockPos.Mutable();

        int x;
        int y;
        int z;

        // Generate the Bedrock Layer.
        if (!defaultBlock.isAir()) {
            for (x = 0; x < 16; x++) {
                for (z = 0; z < 16; z++) {
                    chunk.setBlockState(pos.set(x, this.getMinimumY(), z), bedrock, false);
                }
            }
        }

        // Generate lava on the Bedrock Layer.
        if (!defaultBlock.isAir()) {
            for (x = 0; x < 16; x++) {
                for (z = 0; z < 16; z++) {
                    for (y = 1; y < 9; y++) {
                        if (chunk.getBlockState(new BlockPos(x, this.getMinimumY() + y, z)).isAir()) {
                            chunk.setBlockState(pos.set(x, this.getMinimumY() + y, z), Blocks.LAVA.getDefaultState(),
                                    false);
                        }
                    }
                }
            }
        }

        super.buildSurface(region, structures, chunk);
    }
}