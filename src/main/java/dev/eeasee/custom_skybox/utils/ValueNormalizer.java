package dev.eeasee.custom_skybox.utils;

public class ValueNormalizer {
    public static float toNormalDegree(float f) {
        while (f <= -180.0F) {
            f += 360.0F;
        }
        while (f > 180.0F) {
            f -= 360.0F;
        }
        return f;
    }

    public static int toNormalDaytime(int i) {
        while (i <0) {
            i += 24000;
        }
        while (i > 24000) {
            i -= 24000;
        }
        return i;
    }
}
