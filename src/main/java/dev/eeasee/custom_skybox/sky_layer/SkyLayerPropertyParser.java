package dev.eeasee.custom_skybox.sky_layer;

import dev.eeasee.custom_skybox.sky_layer.enums.Blend;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import dev.eeasee.custom_skybox.sky_layer.enums.Weather;
import dev.eeasee.custom_skybox.utils.ValueNormalizer;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

class SkyLayerPropertyParser {

    static Identifier getSource(Properties properties, Identifier propertiesLocation, String parentPath, ResourceManager resourceManager) {
        String rawString = properties.getProperty("source");

        if (rawString == null) {
            Collection<Identifier> sources = resourceManager.findResources(parentPath, s -> s.endsWith(".png"));
            if (sources.isEmpty()) {
                throw new SkyLayer.SkyLayerParseException("Find No Source Texture");
            } else {
                return sources.iterator().next();
            }
        } else {
            String sourcePath = (rawString.charAt(0) == '.') ? parentPath.concat(rawString.substring(1)) : rawString;
            return new Identifier(propertiesLocation.getNamespace(), sourcePath);
        }
    }

    static int[] getFadeInOutTimes(Properties properties) {
        String[] originals = new String[4];
        int[] output = new int[4];
        originals[0] = properties.getProperty("startFadeIn", "");
        originals[1] = properties.getProperty("endFadeIn", "");
        originals[2] = properties.getProperty("startFadeOut", "");
        originals[3] = properties.getProperty("endFadeOut", "");
        boolean useDefaultFadeOut = false;
        for (int i = 0; i < 4; i++) {
            String[] strings = originals[i].split(":");
            if (strings.length != 2) {
                if (i == 2) {
                    useDefaultFadeOut = true;
                } else {
                    throw new SkyLayer.SkyLayerParseException("Getting Fade In And Out Times");
                }
            } else {
                output[i] = ValueNormalizer.toNormalDaytime(Integer.parseInt(strings[0]) * 1000 +
                        (int) (Integer.parseInt(strings[1]) * (1000.0F / 60.0F) + 0.5) + 18000);
            }
            if (useDefaultFadeOut) {
                output[2] = ValueNormalizer.toNormalDaytime(output[3] - (output[1] - output[0]));
            }
        }
        return output;
    }

    static Blend getBlend(Properties properties) {
        //todo: add more blends as Optifine does.
        return Blend.ADD;
    }

    static Vector3f getRotationAxis(Properties properties) {
        String[] original = properties.getProperty("axis", "0.0 0.0 1.0").split(" ");
        if (original.length != 3) {
            throw new SkyLayer.SkyLayerParseException("Getting Rotation Axis");
        }
        float x = Float.parseFloat(original[0]);
        float y = Float.parseFloat(original[1]);
        float z = Float.parseFloat(original[2]);
        return new Vector3f(x, y, z);
    }

    static EnumSet<Weather> getWeathers(Properties properties) {
        String[] original = properties.getProperty("weather", "clear").split(" ");
        EnumSet<Weather> weatherSet = EnumSet.noneOf(Weather.class);
        for (String s : original) {
            if (!Weather.WeatherEnumHelper.STRING_TO_WEATHER_ENUM.containsKey(s)) {
                throw new SkyLayer.SkyLayerParseException("Getting Weathers");
            }
            weatherSet.add(Weather.WeatherEnumHelper.STRING_TO_WEATHER_ENUM.get(s));
        }
        if (weatherSet.isEmpty()) {
            weatherSet.add(Weather.CLEAR);
        }
        return weatherSet;
    }

    static Set<Biome> getBiomes(Properties properties) {
        String input = properties.getProperty("biomes");
        if (input == null) {
            return Biome.BIOMES;
        }

        String[] original = input.split(" ");

        //todo: To find out how Optifine defines a BIOME LIST!
        return Biome.BIOMES;
    }

    static Predicate<Integer> getHeightPredicate(Properties properties) {
        return (i) -> true;
        //todo: To find out how Optifine defines a HEIGHT RANGE!
    }

    static EnumSet<SkyBoxRenderPhase> getRenderPhases(Properties properties, SkyBoxRenderPhase defaultPhase) {
        String[] original = properties.getProperty("dimensionAndPhase", defaultPhase.getID()).split(" ");
        EnumSet<SkyBoxRenderPhase> output = EnumSet.noneOf(SkyBoxRenderPhase.class);
        SkyBoxRenderPhase phase;
        for (String s : original) {
            phase = SkyBoxRenderPhase.PhaseEnumHelper.STRING_TO_RENDER_PHASE_MAP.get(s);
            if (phase == null) {
                throw new SkyLayer.SkyLayerParseException("Getting Render Phases");
            }
            output.add(phase);
        }
        return output;
    }

    static Identifier getScriptLocation(Properties properties) {
        return null;
        //todo: Add Scripts
    }
}
