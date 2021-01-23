package dev.eeasee.custom_skybox.sky_layer;

import com.google.common.collect.ImmutableList;
import dev.eeasee.custom_skybox.sky_layer.enums.Blend;
import dev.eeasee.custom_skybox.sky_layer.enums.DimensionTypeEnum;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import dev.eeasee.custom_skybox.sky_layer.enums.Weather;
import dev.eeasee.custom_skybox.utils.ValueNormalizer;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Predicate;

public class SkyLayer {

    private static final Logger LOGGER = LogManager.getLogger();

    public static List<SkyLayer> getSkies(ResourceManager resourceManager) {

        final String[] NAMESPACES = {"eeasee_custom_skybox", "minecraft"};
        final String[][] PATHS_ARRAY = {{"sky/overworld", "sky/the_nether", "sky/the_end"}, {"optifine/sky/world0"}};

        List<SkyLayer> skyProperties = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            String namespace = NAMESPACES[i];
            String[] paths = PATHS_ARRAY[i];
            for (int j = 0; j < paths.length; j++) {
                String path = paths[j];
                int skyIndex = 0;
                while (true) {
                    Identifier skyIdentifier = new Identifier(namespace, path + "/sky" + skyIndex + ".properties");
                    if (resourceManager.containsResource(skyIdentifier)) {
                        try {
                            SkyLayer skyLayer = SkyLayer.of(DimensionTypeEnum.values()[j], skyIdentifier, path, resourceManager);
                            skyProperties.add(skyLayer);
                        } catch (IOException e) {
                            LOGGER.error(e);
                        }
                        skyIndex++;
                    } else {
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(skyProperties);
    }

    private static SkyLayer of(DimensionTypeEnum dimensionType, Identifier resourceLocation, String parentPath, ResourceManager resourceManager) throws IOException {
        try {
            InputStream inputStream = resourceManager.getResource(resourceLocation).getInputStream();
            Properties properties = new Properties();
            properties.load(inputStream);

            Identifier source = SkyPropertyParser.getSource(properties, resourceLocation.getNamespace(), parentPath, resourceManager);
            int[] fadeInOutTimes = SkyPropertyParser.getFadeInOutTimes(properties);
            Blend blendMode = SkyPropertyParser.getBlend(properties);
            boolean rotate = Boolean.parseBoolean(properties.getProperty("rotate", "true"));
            float rotationSpeed = Float.parseFloat(properties.getProperty("speed", "1.0"));
            Vector3f rotationAxis = SkyPropertyParser.getRotationAxis(properties);
            EnumSet<Weather> weathers = SkyPropertyParser.getWeathers(properties);
            Set<Biome> biomes = SkyPropertyParser.getBiomes(properties);
            Predicate<Integer> heightPredicate = SkyPropertyParser.getHeightPredicate(properties);
            float transitionTime = Float.parseFloat(properties.getProperty("transition", "1.0"));
            EnumSet<SkyBoxRenderPhase> renderPhases = SkyPropertyParser.getRenderPhases(properties, dimensionType);
            int priority = Integer.parseInt(properties.getProperty("priority", "0"));
            Identifier script = SkyPropertyParser.getScriptLocation(properties);

            SkyLayer layer = new SkyLayer(source, fadeInOutTimes, blendMode, rotate, rotationSpeed, rotationAxis, weathers, biomes, heightPredicate, transitionTime, renderPhases, priority, script);
            for (SkyBoxRenderPhase phase : renderPhases) {
                phase.addLayer(layer);
            }
            return layer;
        } catch (SkyLayerParseException e) {
            throw new SkyLayerParseException(resourceLocation.toString(), e.section);
        }
    }

    private final Identifier source;
    private final int[] fadeInOutTimes;
    public final Blend blendMode;
    public final boolean rotate;
    public final float rotationSpeed;
    public final Vector3f rotationAxis;
    public final EnumSet<Weather> weathers;
    public final Set<Biome> biomes;
    public final Predicate<Integer> heightPredicate;
    public final float transitionTime;
    public final EnumSet<SkyBoxRenderPhase> renderPhases;
    public final int priority;
    public final Identifier script;

    private SkyLayer(Identifier source, int[] fadeInOutTimes, Blend blendMode, boolean rotate, float rotationSpeed, Vector3f rotationAxis, EnumSet<Weather> weathers, Set<Biome> biomes, Predicate<Integer> heightPredicate, float transitionTime, EnumSet<SkyBoxRenderPhase> renderPhases, int priority, Identifier script) {
        this.source = source;
        this.fadeInOutTimes = fadeInOutTimes;
        this.blendMode = blendMode;
        this.rotate = rotate;
        this.rotationSpeed = rotationSpeed;
        this.rotationAxis = rotationAxis;
        this.weathers = weathers;
        this.biomes = biomes;
        this.heightPredicate = heightPredicate;
        this.transitionTime = transitionTime;
        this.renderPhases = renderPhases;
        this.priority = priority;
        this.script = script;
    }

    public Identifier getSkyBoxTexture() {
        return this.source;
    }

    public int getFadingAlpha(int dayTime) {

        throw new UnsupportedOperationException();
    }

    private static class SkyPropertyParser {

        private static Identifier getSource(Properties properties, String namespace, String parentPath, ResourceManager resourceManager) {
            String rawString = properties.getProperty("source");
            String sourceString;
            if (rawString == null) {
                //resourceManager.findResources()
            } else {
                sourceString = (rawString.charAt(0) == '.') ? parentPath.concat(rawString.substring(1)) : rawString;
            }
            return null;
        }

        private static int[] getFadeInOutTimes(Properties properties) {
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
                        throw new SkyLayerParseException("Getting Fade In And Out Times");
                    }
                } else {
                    output[i] = ValueNormalizer.toNormalDaytime(Integer.parseInt(strings[0]) * 1000 +
                            (int) (Integer.parseInt(strings[1]) * (1000.0F / 60.0F) + 0.5) + 18000);
                }
                if (useDefaultFadeOut) {
                    output[2] = ValueNormalizer.toNormalDaytime(output[3] - output[2] + output[1]);
                }
            }
            return output;
        }

        private static Blend getBlend(Properties properties) {
            //todo: add more blends as Optifine does.
            return Blend.ADD;
        }

        private static Vector3f getRotationAxis(Properties properties) {
            String[] original = properties.getProperty("axis", "0.0 0.0 1.0").split(" ");
            if (original.length != 3) {
                throw new SkyLayerParseException("Getting Rotation Axis");
            }
            float x = Float.parseFloat(original[0]);
            float y = Float.parseFloat(original[1]);
            float z = Float.parseFloat(original[2]);
            return new Vector3f(x, y, z);
        }

        private static EnumSet<Weather> getWeathers(Properties properties) {
            String[] original = properties.getProperty("weather", "clear").split(" ");
            EnumSet<Weather> weatherSet = EnumSet.noneOf(Weather.class);
            for (String s : original) {
                if (!Weather.WeatherEnumHelper.STRING_TO_WEATHER_ENUM.containsKey(s)) {
                    throw new SkyLayerParseException("Getting Weathers");
                }
                weatherSet.add(Weather.WeatherEnumHelper.STRING_TO_WEATHER_ENUM.get(s));
            }
            if (weatherSet.isEmpty()) {
                weatherSet.add(Weather.CLEAR);
            }
            return weatherSet;
        }

        private static Set<Biome> getBiomes(Properties properties) {
            return null;
        }

        private static Predicate<Integer> getHeightPredicate(Properties properties) {
            return null;
        }

        private static EnumSet<SkyBoxRenderPhase> getRenderPhases(Properties properties, DimensionTypeEnum dimensionType) {
            return null;
        }

        private static Identifier getScriptLocation(Properties properties) {
            return null;
        }
    }

    public static class SkyLayerParseException extends RuntimeException {
        private String section = null;

        public SkyLayerParseException(String section) {
            this.section = section;
        }

        public SkyLayerParseException(String fileLoc, String section) {
            super("Exception in creating the skylayer @" + fileLoc + " ! Error in parsing [" + section + "]");
        }
    }
}
