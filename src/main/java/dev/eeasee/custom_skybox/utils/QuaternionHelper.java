package dev.eeasee.custom_skybox.utils;

import net.minecraft.util.math.Quaternion;

public class QuaternionHelper {
    public static Quaternion hamiltonProduct(Quaternion p, Quaternion q) {
        float f = p.getB();
        float g = p.getC();
        float h = p.getD();
        float i = p.getA();
        float j = q.getB();
        float k = q.getC();
        float l = q.getD();
        float m = q.getA();
        float b = i * j + f * m + g * l - h * k;
        float c = i * k - f * l + g * m + h * j;
        float d = i * l + f * k - g * j + h * m;
        float a = i * m - f * j - g * k - h * l;
        return new Quaternion(b, c, d, a);
    }
}
