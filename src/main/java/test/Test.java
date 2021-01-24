package test;

import com.google.common.collect.ImmutableMap;
import dev.eeasee.custom_skybox.mixin.IMixinReloadableResourceManagerImpl;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class Test {
    public static void main(String[] args) {
        ImmutableMap.Builder<String, Biome> stringBiomeMapBuilder = ImmutableMap.builder();
        for (Biome biome : Biome.BIOMES) {
            stringBiomeMapBuilder.put(Registry.BIOME.getId(biome).getPath(), biome);
        }
        System.out.println(stringBiomeMapBuilder.build());
    }

    public static void onGameStart() {

    }

    public static void onResourceLoad(ResourceManager manager) {
        System.out.println("==================================");
        manager.findResources("sky", s -> true).forEach(System.out::println);
    }
}
