package dev.eeasee.custom_skybox.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ConfigIO {
    private static final File CONFIG_FILE = new File("config/eeasee-custom-skybox.json");

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final Logger LOGGER = LogManager.getLogger();

    private static ConfigHolder readConfigsFromFile(File configFile) {
        ConfigHolder config;
        try {
            config = GSON.fromJson(new FileReader(configFile), ConfigHolder.class);
        } catch (FileNotFoundException | JsonSyntaxException | JsonIOException e) {
            config = new ConfigHolder();
        }
        return config;
    }

    public static ConfigHolder readConfigFromFile() {
        return readConfigsFromFile(CONFIG_FILE);
    }

    private static void writeConfigToFile(ConfigHolder configs, File configFile) {
        configFile.getParentFile().mkdirs();
        String output = GSON.toJson(configs);

        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(configFile);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeConfigToFile(ConfigHolder configs) {
        writeConfigToFile(configs, CONFIG_FILE);
    }

}
