package dev.eeasee.custom_skybox.sky_layer;

import com.google.common.collect.ImmutableList;
import dev.eeasee.custom_skybox.sky_layer.enums.Blend;
import dev.eeasee.custom_skybox.sky_layer.enums.DimensionTypeEnum;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import dev.eeasee.custom_skybox.sky_layer.enums.Weather;
import net.minecraft.client.render.RenderPhase;
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
    }

    public final Identifier source;
    public final int[] fadeInOutTimes;
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
            return null;
        }

        private static Blend getBlend(Properties properties) {
            return null;
        }

        private static Vector3f getRotationAxis(Properties properties) {
            return null;
        }

        private static EnumSet<Weather> getWeathers(Properties properties) {
            return null;
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
}
