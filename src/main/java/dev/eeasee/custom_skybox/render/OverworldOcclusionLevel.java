package dev.eeasee.custom_skybox.render;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Language;

public enum OverworldOcclusionLevel {
    ALLOW_BLUE_SKY          ("dev.eeasee.custom_skybox.option.overworld_occlusion_level.allow_blue_sky"),
    ALLOW_DAWN_FOG          ("dev.eeasee.custom_skybox.option.overworld_occlusion_level.allow_dawn_fog"),
    ALLOW_SUN_AND_MOON      ("dev.eeasee.custom_skybox.option.overworld_occlusion_level.allow_sun_and_moon"),
    COVER_EVERYTHING        ("dev.eeasee.custom_skybox.option.overworld_occlusion_level.cover_everything");

    public final String desc;


    OverworldOcclusionLevel(String desc) {
        this.desc = desc;
    }

    public OverworldOcclusionLevel getByIndex(int index) {
        return OverworldOcclusionLevel.values()[index];
    }
}