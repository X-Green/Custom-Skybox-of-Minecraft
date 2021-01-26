package dev.eeasee.custom_skybox.resources;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.sky_layer.SkyLayerManager;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ResourceInitializer implements SimpleResourceReloadListener {
    @Override
    public CompletableFuture load(ResourceManager manager, Profiler profiler, Executor executor) {
        return CompletableFuture.runAsync(
                () -> this.onResourceLoad(manager, profiler, executor)
        );
    }

    @Override
    public CompletableFuture<Void> apply(Object data, ResourceManager manager, Profiler profiler, Executor executor) {
        return CompletableFuture.runAsync(()->{});
    }

    @Override
    public Identifier getFabricId() {
        return CustomSkyBoxMod.MOD_ID;
    }

    private void onResourceLoad(ResourceManager manager, Profiler profiler, Executor executor) {

        SkyLayerManager.updateSkies(manager);
    }
}
