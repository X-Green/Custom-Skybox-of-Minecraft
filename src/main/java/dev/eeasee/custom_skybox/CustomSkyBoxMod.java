package dev.eeasee.custom_skybox;

import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.configs.ConfigIO;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class CustomSkyBoxMod implements ModInitializer {
    public static final Identifier MOD_ID = new Identifier("eeasee_custom_skybox");

    public static ConfigHolder configs;

    @Override
    public void onInitialize() {
        configs = ConfigIO.readConfigFromFile();
    }
}
