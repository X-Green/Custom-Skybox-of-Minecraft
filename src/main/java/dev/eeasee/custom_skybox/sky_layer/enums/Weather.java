package dev.eeasee.custom_skybox.sky_layer.enums;

public enum  Weather {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    public final String key;

    Weather(String key) {
        this.key = key;
    }
}
