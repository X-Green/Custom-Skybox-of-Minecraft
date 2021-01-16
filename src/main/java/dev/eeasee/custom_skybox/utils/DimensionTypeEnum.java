package dev.eeasee.custom_skybox.utils;

public enum DimensionTypeEnum {
    OVERWORLD("overworld"),
    THE_NETHER("the_nether"),
    THE_END("the_end");

    public final String id;

    DimensionTypeEnum(String id) {
        this.id = id;
    }
}
