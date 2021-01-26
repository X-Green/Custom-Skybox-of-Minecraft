package test;

import dev.eeasee.custom_skybox.utils.ValueNormalizer;
import net.minecraft.resource.ResourceManager;

public class Test {
    public static void main(String[] args) {
        String s = "aaa/bbb/c.d";
        String[] ss = s.split("/");
        String parentPath = s.substring(0, s.length() - ss[ss.length - 1].length() - 1);
        System.out.println(parentPath);
    }

    public static void onGameStart() {

    }

    public static void onResourceLoad(ResourceManager manager) {
        System.out.println("==================================");
        manager.findResources("sky", s -> true).forEach(System.out::println);
    }

    public static int getFadingAlpha(int dayTime) {
        int startIn = 0;
        int endIn = 1000;
        int startOut = 18000;
        int endOut = 19000;

        int fadingInStartEndDelta = ValueNormalizer.toNormalDaytime(endIn - startIn);
        int fadingInStartNowDelta = ValueNormalizer.toNormalDaytime(dayTime - startIn);

        if (fadingInStartNowDelta < fadingInStartEndDelta) {
            return (int) ((1 - ((float) (fadingInStartEndDelta - fadingInStartNowDelta)) / fadingInStartEndDelta) * 255);
        }


        int fadingOutStartEndDelta = ValueNormalizer.toNormalDaytime(endOut - startOut);
        int fadingOutStartNowDelta = ValueNormalizer.toNormalDaytime(dayTime - startOut);

        if (fadingOutStartNowDelta < fadingOutStartEndDelta) {
            return (int) (((float) (fadingOutStartEndDelta - fadingOutStartNowDelta)) / fadingOutStartEndDelta * 255);
        }

        if (fadingInStartNowDelta > fadingOutStartNowDelta) {
            // in between fading-out and fading-in
            return 0;
        } else {
            return 255;
        }
    }
}
