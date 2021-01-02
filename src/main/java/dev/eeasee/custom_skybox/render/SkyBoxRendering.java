package dev.eeasee.custom_skybox.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigHolder;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

public class SkyBoxRendering {
    private static final Identifier[] CUSTOM_END_SKY = new Identifier[]{
            new Identifier("eeasee_custom_skybox", "texture/end_sky/1.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/2.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/3.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/4.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/5.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/6.png")
    };
    private static final Identifier[] CUSTOM_OVERWORLD_SKY = new Identifier[]{
            new Identifier("eeasee_custom_skybox", "texture/overworld_sky/1.png"),
            new Identifier("eeasee_custom_skybox", "texture/overworld_sky/2.png"),
            new Identifier("eeasee_custom_skybox", "texture/overworld_sky/3.png"),
            new Identifier("eeasee_custom_skybox", "texture/overworld_sky/4.png"),
            new Identifier("eeasee_custom_skybox", "texture/overworld_sky/5.png"),
            new Identifier("eeasee_custom_skybox", "texture/overworld_sky/6.png")
    };
    private static final Identifier[] CUSTOM_NETHER_SKY = new Identifier[]{
            new Identifier("eeasee_custom_skybox", "texture/nether_sky/1.png"),
            new Identifier("eeasee_custom_skybox", "texture/nether_sky/2.png"),
            new Identifier("eeasee_custom_skybox", "texture/nether_sky/3.png"),
            new Identifier("eeasee_custom_skybox", "texture/nether_sky/4.png"),
            new Identifier("eeasee_custom_skybox", "texture/nether_sky/5.png"),
            new Identifier("eeasee_custom_skybox", "texture/nether_sky/6.png")
    };

    public static void renderSkyBox(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyBoxRenderPhase renderPhase) {

        Identifier[] textureArray = renderPhase.CustomSkyBoxTextureProvider.apply(CustomSkyBoxMod.configs, clientWorld);
        if (textureArray == null) {
            return;
        }

        float rotation;


        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for (int i = 0; i < 6; ++i) {
            textureManager.bindTexture(textureArray[i]);
            matrixStack.push();

            switch (i) {
                case 0:
                    // BOTTOM
                    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(60.0F));
                    break;
                case 1:
                    // TOP
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
                    matrixStack.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(60.0F));
                    break;
                case 2:
                    // SIDE 4
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

                    matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(60.0F));

                    break;
                case 3:
                    // SIDE 1
                    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

                    matrixStack.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(60.0F));

                    break;
                case 4:
                    // SIDE 2
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));

                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(60.0F));

                    break;
                case 5:
                    // SIDE 3
                    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));

                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(60.0F));

                    break;

            }

            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1F).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1F, 1F).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1F, 0.0F).color(255, 255, 255, 255).next();

            tessellator.draw();
            matrixStack.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }

    }

    public enum SkyBoxRenderPhase {
        THE_END((configHolder, world) -> configHolder.enableTheEndCustomSkyBox ? CUSTOM_END_SKY : null),
        BEFORE_OVERWORLD_SKY((configHolder, world) -> {
            // this phase can only be called in overworld, so dimension judgement is unnecessary.
            return (configHolder.enableOverworldCustomSkyBox
                    && configHolder.overworldOcclusionLevel == OverworldOcclusionLevel.ALLOW_BLUE_SKY)
                    ? CUSTOM_OVERWORLD_SKY : null;
        }),
        BEFORE_DAWN_FOG((configHolder, world) -> {
            // this phase can only be called in overworld, so dimension judgement is unnecessary.
            return (configHolder.enableOverworldCustomSkyBox
                    && configHolder.overworldOcclusionLevel == OverworldOcclusionLevel.ALLOW_DAWN_FOG)
                    ? CUSTOM_OVERWORLD_SKY : null;
        }),
        BEFORE_SUN_AND_MOON((configHolder, world) -> {
            // this phase can only be called in overworld, so dimension judgement is unnecessary.
            return (configHolder.enableOverworldCustomSkyBox
                    && configHolder.overworldOcclusionLevel == OverworldOcclusionLevel.ALLOW_SUN_AND_MOON)
                    ? CUSTOM_OVERWORLD_SKY : null;
        }),
        AFTER_ALL((configHolder, world) -> {
            if (world.dimension.isNether() && configHolder.enableNetherCustomSkyBox) {
                return CUSTOM_NETHER_SKY;
            }
            if (world.dimension.canPlayersSleep() &&
                    (configHolder.enableOverworldCustomSkyBox) &&
                    (configHolder.overworldOcclusionLevel == OverworldOcclusionLevel.COVER_EVERYTHING)) {
                return CUSTOM_OVERWORLD_SKY;
            }
            return null;
        });

        public final BiFunction<ConfigHolder, ClientWorld, Identifier[]> CustomSkyBoxTextureProvider;

        SkyBoxRenderPhase(BiFunction<ConfigHolder, ClientWorld, Identifier[]> infoToCustomSkyBoxTexture) {
            this.CustomSkyBoxTextureProvider = infoToCustomSkyBoxTexture;
        }
    }

}
