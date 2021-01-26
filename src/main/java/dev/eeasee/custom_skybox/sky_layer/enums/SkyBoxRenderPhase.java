package dev.eeasee.custom_skybox.sky_layer.enums;

import com.google.common.collect.ImmutableMap;
import dev.eeasee.custom_skybox.sky_layer.SkyLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public enum SkyBoxRenderPhase {
    BEFORE_OVERWORLD_SKY("before_overworld_sky"),
    BEFORE_DAWN_FOG("overworld_before_dawn_fog"),
    BEFORE_SUN_AND_MOON("overworld_before_sun_and_moon"),
    OVERWORLD("overworld"),
    THE_NETHER("the_nether"),
    THE_END("the_end");

    private final List<SkyLayer> skyLayersRenderedInThisPhase = new ArrayList<>();
    private final String id;

    SkyBoxRenderPhase(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
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

    public static class PhaseEnumHelper {
        public static final Map<String, SkyBoxRenderPhase> STRING_TO_RENDER_PHASE_MAP;

        static {
            ImmutableMap.Builder<String, SkyBoxRenderPhase> mb = new ImmutableMap.Builder<>();
            for (SkyBoxRenderPhase phase : SkyBoxRenderPhase.values()) {
                mb.put(phase.id, phase);
            }
            STRING_TO_RENDER_PHASE_MAP = mb.build();
        }
    }
}
