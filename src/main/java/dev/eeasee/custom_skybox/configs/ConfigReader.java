package dev.eeasee.custom_skybox.configs;

import com.google.gson.Gson;

import java.io.File;

public class ConfigReader {
    public static final File CONFIG_FILE = new File("config/eeasee-custom-skybox.json");

    public static ConfigHolder readConfigsFromFile(File configFile) {
        System.out.println(configFile.getAbsolutePath());

        return null;
    }

    public static ConfigHolder readConfigFromFile() {
        return readConfigsFromFile(CONFIG_FILE);
    }
}
