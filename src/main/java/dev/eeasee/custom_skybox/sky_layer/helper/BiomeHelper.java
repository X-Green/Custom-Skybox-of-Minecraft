package dev.eeasee.custom_skybox.sky_layer.helper;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

import java.util.Map;

public class BiomeHelper {
    public static final Map<String, Biome> STRING_BIOME_MAP;

    static {
        ImmutableMap.Builder<String, Biome> stringBiomeMapBuilder = ImmutableMap.builder();
        for (Biome biome : Biome.BIOMES) {
            stringBiomeMapBuilder.put(Registry.BIOME.getId(biome).getPath(), biome);
        }
        STRING_BIOME_MAP = stringBiomeMapBuilder.build();
    }
}
