package dev.eeasee.custom_skybox.sky_layer.enums;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public enum Weather {
    CLEAR("clear"),
    RAIN("rain"),
    THUNDER("thunder");

    public final String key;

    Weather(String key) {
        this.key = key;
    }

    public static class WeatherEnumHelper {
        public static final Map<String, Weather> STRING_TO_WEATHER_ENUM = ImmutableMap.of(
                CLEAR.key, CLEAR,
                RAIN.key, RAIN,
                THUNDER.key, THUNDER
        );
    }
}
