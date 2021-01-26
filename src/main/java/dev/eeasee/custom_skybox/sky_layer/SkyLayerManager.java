package dev.eeasee.custom_skybox.sky_layer;

import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class SkyLayerManager {
    public static void updateSkies(ResourceManager resourceManager) {
        for (SkyBoxRenderPhase phase : SkyBoxRenderPhase.values()) {
            phase.clearLayers();
        }

        Set<Identifier> skyConfigs = new HashSet<>();
        skyConfigs.addAll(resourceManager.findResources("custom_sky", s -> s.endsWith(".properties")));
        skyConfigs.addAll(resourceManager.findResources("optifine/sky", s -> s.endsWith(".properties")));
        for (Identifier skyPropertiesLocation : skyConfigs) {
            SkyLayer skyLayer = SkyLayer.of(skyPropertiesLocation, resourceManager);
            for (SkyBoxRenderPhase phase : skyLayer.getRenderPhases()) {
                phase.addLayer(skyLayer);
            }
            System.out.println(skyLayer);
        }
    }

    static void reportSkyLayerRenderingException(String reason) {
        //todo: REPORT EXCEPTION.
    }
}
