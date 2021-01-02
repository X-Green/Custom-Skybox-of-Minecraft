package dev.eeasee.custom_skybox.configs;

import dev.eeasee.custom_skybox.render.OverworldOcclusionLevel;

public class ConfigHolder {
    public boolean enableNetherCustomSkyBox = false;
    public boolean enableOverworldCustomSkyBox = false;
    public boolean enableTheEndCustomSkyBox = false;

    public OverworldOcclusionLevel overworldOcclusionLevel = OverworldOcclusionLevel.COVER_EVERYTHING;

    public boolean enableDarkenedOverworldSkyUnderCertainLevel = true;

    public float skyBoxNoonTimeNether = 6000;
    public float skyBoxNoonTimeOverworld = 6000;
    public float skyBoxNoonTimeTheEnd = 6000;

    public int rotationCyclesInSingleNetherDay = 0;
    public int rotationCyclesInSingleOverworldDay = 1;
    public int rotationCyclesInSingleTheEndDay = 0;

}
