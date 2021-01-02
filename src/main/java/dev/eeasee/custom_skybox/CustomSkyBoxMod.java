package dev.eeasee.custom_skybox;

import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.configs.ConfigIO;
import net.fabricmc.api.ModInitializer;

public class CustomSkyBoxMod implements ModInitializer {
    public static ConfigHolder configs;

    @Override
    public void onInitialize() {
        configs = ConfigIO.readConfigFromFile();
    }
}
