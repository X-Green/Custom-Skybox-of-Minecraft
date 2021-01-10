package test;

import dev.eeasee.custom_skybox.utils.QuaternionHelper;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public class Test {
    public static void main(String[] args) {
        Quaternion q = new Quaternion(1.0F, 2.0F, 3.0F, 4.0F);
        Quaternion p = new Quaternion(5.0F, 6.0F, 7.0F, 8.0F);
        System.out.println(q);
        System.out.println(new Matrix4f(q));
        System.out.println(QuaternionHelper.hamiltonProduct(q, p));
        p.hamiltonProduct(q);
        System.out.println(p);
    }
}
