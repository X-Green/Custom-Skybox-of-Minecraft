package dev.eeasee.custom_skybox.sky_layer.enums;

import dev.eeasee.custom_skybox.sky_layer.SkyLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public enum SkyBoxRenderPhase {
    THE_END,
    BEFORE_OVERWORLD_SKY,
    BEFORE_DAWN_FOG,
    BEFORE_SUN_AND_MOON,
    AFTER_ALL;


    private final List<SkyLayer> skyLayersRenderedInThisPhase = new ArrayList<>();

    SkyBoxRenderPhase() {
    }

    public void clearLayers() {
        this.skyLayersRenderedInThisPhase.clear();
    }

    /**
     * Layer with larger priority shall be placed front; Newly added layer shall
     * be placed behind existing layers with the same priority.
     */
    public void addLayer(SkyLayer layer) {
        if (this.skyLayersRenderedInThisPhase.size() == 0) {
            this.skyLayersRenderedInThisPhase.add(layer);
            return;
        }

        for (int i = this.skyLayersRenderedInThisPhase.size() - 1; i >= 0; i--) {
            if (this.skyLayersRenderedInThisPhase.get(i).priority >= layer.priority) {
                this.skyLayersRenderedInThisPhase.add(i + 1, layer);
                return;
            }
        }

        this.skyLayersRenderedInThisPhase.add(layer);
    }

    public ListIterator<SkyLayer> getLayersIterator() {
        return this.skyLayersRenderedInThisPhase.listIterator();
    }
}
