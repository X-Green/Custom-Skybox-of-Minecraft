package dev.eeasee.custom_skybox.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.utils.Degree;
import dev.eeasee.custom_skybox.utils.QuaternionHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class SkyBoxRendering {

    private static final Identifier CUSTOM_END_SKY = new Identifier(
            "eeasee_custom_skybox", "texture/end_sky/sky.png"
    );

    private static final Identifier CUSTOM_OVERWORLD_SKY = new Identifier(
            "eeasee_custom_skybox", "texture/overworld_sky/sky.png"
    );

    private static final Identifier CUSTOM_NETHER_SKY = new Identifier(
            "eeasee_custom_skybox", "texture/nether_sky/sky.png"
    );

    private static final Quaternion TOP = Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F);
    private static final Quaternion SIDE_1 = QuaternionHelper.hamiltonProduct(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F), Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
    private static final Quaternion SIDE_2 = Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F);
    private static final Quaternion SIDE_3 = QuaternionHelper.hamiltonProduct(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F), Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
    private static final Quaternion SIDE_4 = QuaternionHelper.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F), Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

    public static void renderSkyBox(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyBoxRenderPhase renderPhase) {

        Identifier texture = renderPhase.CustomSkyBoxTextureProvider.apply(CustomSkyBoxMod.configs, clientWorld);
        if (texture == null) {
            return;
        }

        float rotation = skyBoxRotationDegree(clientWorld);

        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for (int i = 0; i < 6; ++i) {
            textureManager.bindTexture(texture);
            matrixStack.push();

            float x1, x2, y1, y2;

            switch (i) {
                case 0:
                    // BOTTOM
                    x1 = 0;
                    x2 = 1.0F / 3.0F;
                    y1 = 0;
                    y2 = 0.5F;
                    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(rotation));
                    break;
                case 1:
                    // TOP
                    x1 = 1.0F / 3.0F;
                    x2 = 2.0F / 3.0F;
                    y1 = 0;
                    y2 = 0.5F;
                    matrixStack.multiply(TOP);
                    matrixStack.multiply(Vector3f.NEGATIVE_Z.getDegreesQuaternion(rotation));
                    break;
                case 2:
                    // SOUTH
                    x1 = 2.0F / 3.0F;
                    x2 = 1;
                    y1 = 0;
                    y2 = 0.5F;
                    matrixStack.multiply(SIDE_4);
                    matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(rotation));
                    break;
                case 3:
                    // WEST
                    x1 = 0;
                    x2 = 1.0F / 3.0F;
                    y1 = 0.5F;
                    y2 = 1;
                    matrixStack.multiply(SIDE_1);
                    matrixStack.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(rotation));
                    break;
                case 4:
                    // NORTH
                    x1 = 1.0F / 3.0F;
                    x2 = 2.0F / 3.0F;
                    y1 = 0.5F;
                    y2 = 1;
                    matrixStack.multiply(SIDE_2);
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
                    break;
                case 5:
                    // EAST
                    x1 = 2.0F / 3.0F;
                    x2 = 1;

                    y1 = 0.5F;
                    y2 = 1;
                    matrixStack.multiply(SIDE_3);
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(rotation));
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

            float layer = 0.0F;

            bufferBuilder.vertex(matrix4f, -100.0F - layer, -100.0F - layer, -100.0F - layer).texture(x1, y1).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, -100.0F - layer, -100.0F - layer, 100.0F + layer).texture(x1, y2).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F + layer, -100.0F - layer, 100.0F + layer).texture(x2, y2).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F + layer, -100.0F - layer, -100.0F - layer).texture(x2, y1).color(255, 255, 255, 255).next();

            tessellator.draw();
            matrixStack.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }

        {
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.depthMask(false);


            matrixStack.push();


            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);


            textureManager.bindTexture(new Identifier("minecraft", "textures/item/apple.png"));

            float layer = -50F;

            bufferBuilder.vertex(matrix4f, -100.0F - layer, -100.0F - 1.0F, -100.0F - layer).texture(0, 0).color(255, 255, 255, 100).next();
            bufferBuilder.vertex(matrix4f, -100.0F - layer, -100.0F - 1.0F, 100.0F + layer).texture(0, 1).color(255, 255, 255, 100).next();
            bufferBuilder.vertex(matrix4f, 100.0F + layer, -100.0F - 1.0F, 100.0F + layer).texture(1, 1).color(255, 255, 255, 100).next();
            bufferBuilder.vertex(matrix4f, 100.0F + layer, -100.0F - 1.0F, -100.0F - layer).texture(1, 0).color(255, 255, 255, 100).next();

            tessellator.draw();
            matrixStack.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();

        }

    }

    private static float skyBoxRotationDegree(World world) {
        int rotationCyclesInSingleDay;
        int skyBoxNoonTime;
        ConfigHolder configs = CustomSkyBoxMod.configs;
        switch (world.getDimension().getType().getRawId() + 1) {
            case 0: // Nether
                skyBoxNoonTime = configs.skyBoxNoonTimeNether;
                rotationCyclesInSingleDay = configs.rotationCyclesInSingleNetherDay;
                break;
            case 1: // Overworld
                skyBoxNoonTime = configs.skyBoxNoonTimeOverworld;
                rotationCyclesInSingleDay = configs.rotationCyclesInSingleOverworldDay;
                break;
            case 2: // TheEnd
                skyBoxNoonTime = configs.skyBoxNoonTimeTheEnd;
                rotationCyclesInSingleDay = configs.rotationCyclesInSingleTheEndDay;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        long daytime = world.getTimeOfDay();
        daytime -= skyBoxNoonTime;
        float degree = ((float) (daytime * rotationCyclesInSingleDay * 360)) / 24000.0F;
        return Degree.toNormalDegree(degree);
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

        public final BiFunction<ConfigHolder, ClientWorld, Identifier> CustomSkyBoxTextureProvider;

        SkyBoxRenderPhase(BiFunction<ConfigHolder, ClientWorld, Identifier> infoToCustomSkyBoxTexture) {
            this.CustomSkyBoxTextureProvider = infoToCustomSkyBoxTexture;
        }
    }
}
