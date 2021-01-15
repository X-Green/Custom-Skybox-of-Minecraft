package dev.eeasee.custom_skybox;

import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.configs.ConfigIO;
import dev.eeasee.custom_skybox.resources.ResourceInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class CustomSkyBoxMod implements ModInitializer {
    public static final Identifier MOD_ID = new Identifier("eeasee_custom_skybox");

    public static ConfigHolder configs;

    @Override
    public void onInitialize() {
        configs = ConfigIO.readConfigFromFile();
        SimpleResourceReloadListener resourceReloadListener = new ResourceInitializer();

        ResourceManagerHelperImpl
                .get(ResourceType.CLIENT_RESOURCES)
                .registerReloadListener(resourceReloadListener);

    }
}
