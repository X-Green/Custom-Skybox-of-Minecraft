package test;

import dev.eeasee.custom_skybox.utils.QuaternionHelper;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> stringList =new ArrayList<>();
        addLayer(stringList, "A");
        addLayer(stringList, "AA");
        addLayer(stringList, "AAA");
        addLayer(stringList, "B");
        addLayer(stringList, "BB");
        addLayer(stringList, "BBB");
        addLayer(stringList, "C");
        addLayer(stringList, "CC");
        addLayer(stringList, "CCC");
        System.out.println(stringList);
    }

    public static void addLayer(List<String> list, String s) {
        if (list.size() == 0) {
            list.add(s);
            return;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).length() >= s.length()) {
                list.add(i + 1, s);
                return;
            }
        }
        list.add(0,s);
    }
}
