package dev.eeasee.custom_skybox.configs;

import dev.eeasee.custom_skybox.render.OverworldOcclusionLevel;

public class ConfigHolder {
    public boolean enableNetherCustomSkyBox = false;
    public boolean enableOverworldCustomSkyBox = false;
    public boolean enableEndCustomSkyBox = false;

    public OverworldOcclusionLevel overworldOcclusionLevel = OverworldOcclusionLevel.COVER_EVERYTHING;

    public boolean enableDarkenedSky = true;
}
