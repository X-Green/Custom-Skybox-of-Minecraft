package dev.eeasee.custom_skybox.utils;

public class Degree {
    public static float toNormalDegree(float f) {
        while (f <= -180.0F) {
            f += 360.0F;
        }
        while (f > 180.0F) {
            f -= 360.0F;
        }
        return f;
    }
}
