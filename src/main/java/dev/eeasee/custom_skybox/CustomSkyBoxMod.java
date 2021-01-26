package dev.eeasee.custom_skybox;

import dev.eeasee.custom_skybox.resources.ResourceInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class CustomSkyBoxMod implements ModInitializer {
    public static final Identifier MOD_ID = new Identifier("eeasee_custom_skybox");


    @Override
    public void onInitialize() {
        SimpleResourceReloadListener resourceReloadListener = new ResourceInitializer();

        ResourceManagerHelperImpl.get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(resourceReloadListener);

    }
}
