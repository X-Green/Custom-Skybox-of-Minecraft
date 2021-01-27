package dev.eeasee.custom_skybox.sky_layer;

import dev.eeasee.custom_skybox.sky_layer.enums.Blend;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import dev.eeasee.custom_skybox.sky_layer.enums.Weather;
import dev.eeasee.custom_skybox.utils.ValueNormalizer;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

public class SkyLayer {

    static SkyLayer of(Identifier skyPropertiesLocation, ResourceManager resourceManager) {

        SkyBoxRenderPhase defaultPhase;
        String[] nodes = StringUtils.split(skyPropertiesLocation.getPath(), '/');
        if (nodes.length <= 1) {
            SkyLayerManager.reportSkyLayerRenderingException("Invalid location of sky.properties! @" + skyPropertiesLocation.toString());
        }
        String sub = nodes[nodes.length - 2];
        switch (sub) {
            case "the_nether":
                defaultPhase = SkyBoxRenderPhase.THE_NETHER;
                break;
            case "the_end":
                defaultPhase = SkyBoxRenderPhase.THE_END;
                break;
            default:
                defaultPhase = SkyBoxRenderPhase.OVERWORLD;
        }
        String parentPath = skyPropertiesLocation.getPath().substring(0, skyPropertiesLocation.getPath().length() - nodes[nodes.length - 1].length() - 1);

        try {
            InputStream inputStream = resourceManager.getResource(skyPropertiesLocation).getInputStream();
            Properties properties = new Properties();
            properties.load(inputStream);

            Identifier source = SkyLayerPropertyParser.getSource(properties, skyPropertiesLocation, parentPath, resourceManager);
            int[] fadeInOutTimes = SkyLayerPropertyParser.getFadeInOutTimes(properties);
            Blend blendMode = SkyLayerPropertyParser.getBlend(properties);
            boolean rotate = Boolean.parseBoolean(properties.getProperty("rotate", "true"));
            float rotationSpeed = Float.parseFloat(properties.getProperty("speed", "1.0"));
            Vector3f rotationAxis = SkyLayerPropertyParser.getRotationAxis(properties);
            EnumSet<Weather> weathers = SkyLayerPropertyParser.getWeathers(properties);
            Set<Biome> biomes = SkyLayerPropertyParser.getBiomes(properties);
            Predicate<Integer> heightPredicate = SkyLayerPropertyParser.getHeightPredicate(properties);
            float transitionTime = Float.parseFloat(properties.getProperty("transition", "1.0"));
            EnumSet<SkyBoxRenderPhase> renderPhases = SkyLayerPropertyParser.getRenderPhases(properties, defaultPhase);
            int priority = Integer.parseInt(properties.getProperty("priority", "0"));
            float distance = Float.parseFloat(properties.getProperty("distance", "1"));
            Identifier script = SkyLayerPropertyParser.getScriptLocation(properties);

            SkyLayer layer = new SkyLayer(source, fadeInOutTimes, blendMode, rotate, rotationSpeed, rotationAxis, weathers, biomes, heightPredicate, transitionTime, renderPhases, priority, distance, script);

            return layer;
        } catch (SkyLayerParseException e) {
            throw new SkyLayerParseException(skyPropertiesLocation.toString(), e.section);
        } catch (IOException e) {
            throw new SkyLayerParseException(skyPropertiesLocation.toString(), "Reading sky.properties: " + e.getMessage());
        }
    }

    private final Identifier source;
    private final int[] fadeInOutTimes;
    private final Blend blendMode;
    private final boolean rotate;
    private final float rotationSpeed;
    private final Vector3f rotationAxis;
    private final EnumSet<Weather> weathers;
    private final Set<Biome> biomes;
    private final Predicate<Integer> heightPredicate;
    private final float transitionTime;
    private final EnumSet<SkyBoxRenderPhase> renderPhases;
    private final int priority;
    private final float distance;
    private final Identifier script;

    public float getDistanceFactor() {
        return distance;
    }

    public boolean isRotate() {
        return rotate;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public Vector3f getRotationAxis() {
        return rotationAxis;
    }

    EnumSet<SkyBoxRenderPhase> getRenderPhases() {
        return renderPhases;
    }

    public int getPriority() {
        return priority;
    }

    public Identifier getScript() {
        return script;
    }

    private SkyLayer(Identifier source, int[] fadeInOutTimes, Blend blendMode, boolean rotate, float rotationSpeed, Vector3f rotationAxis, EnumSet<Weather> weathers, Set<Biome> biomes, Predicate<Integer> heightPredicate, float transitionTime, EnumSet<SkyBoxRenderPhase> renderPhases, int priority, float distance, Identifier script) {
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
        this.distance = distance;
        this.script = script;
    }

    public Identifier getSkyBoxTexture() {
        return this.source;
    }

    public int getFadingAlpha(int dayTime) {
        int startIn = fadeInOutTimes[0];
        int endIn = fadeInOutTimes[1];
        int startOut = fadeInOutTimes[2];
        int endOut = fadeInOutTimes[3];

        int fadingInStartEndDelta = ValueNormalizer.toNormalDaytime(endIn - startIn);
        int fadingInStartNowDelta = ValueNormalizer.toNormalDaytime(dayTime - startIn);

        if (fadingInStartNowDelta < fadingInStartEndDelta) {
            // now is fading in
            return (int) ((1 - ((float) (fadingInStartEndDelta - fadingInStartNowDelta)) / fadingInStartEndDelta) * 255);
        }

        int fadingOutStartEndDelta = ValueNormalizer.toNormalDaytime(endOut - startOut);
        int fadingOutStartNowDelta = ValueNormalizer.toNormalDaytime(dayTime - startOut);

        if (fadingOutStartNowDelta < fadingOutStartEndDelta) {
            // now is fading out
            return (int) (((float) (fadingOutStartEndDelta - fadingOutStartNowDelta)) / fadingOutStartEndDelta * 255);
        }

        if (fadingInStartNowDelta > fadingOutStartNowDelta) {
            // in between fading-out and fading-in
            return 0;
        } else {
            return 255;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("Sky Layer: {")
                .append("source:").append(source.toString())
                .append(", fadeIn: ").append(fadeInOutTimes[0]).append("->").append(fadeInOutTimes[1])
                .append(", fadeOut: ").append(fadeInOutTimes[2]).append("->").append(fadeInOutTimes[3])
                .append(", rotate: ").append(rotate)
                .append(", rotate speed: ").append(rotationSpeed)
                .append(", rotate axis: ").append(rotationAxis)
                .append(", weather: ").append(weathers)
                //.append(", biome: ").append(biomes)
                .append(", transition time: ").append(transitionTime)
                .append(", render phases: ").append(renderPhases)
                .append(", priority: ").append(priority)
                .append(", script: ").append(script == null ? "None" : script.toString())
                .toString();
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
