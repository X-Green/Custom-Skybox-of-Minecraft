package dev.eeasee.custom_skybox.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import dev.eeasee.custom_skybox.utils.ValueNormalizer;
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

public class SkyBoxRendering {

    private static final Identifier CUSTOM_END_SKY = new Identifier(
            "eeasee_custom_skybox", "sky/the_end/sky.png"
    );

    private static final Identifier CUSTOM_OVERWORLD_SKY = new Identifier(
            "eeasee_custom_skybox", "sky/overworld/sky.png"
    );

    private static final Identifier CUSTOM_NETHER_SKY = new Identifier(
            "eeasee_custom_skybox", "sky/the_nether/sky.png"
    );

    private static final Quaternion TOP = Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F);
    private static final Quaternion SIDE_1 = QuaternionHelper.hamiltonProduct(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F), Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
    private static final Quaternion SIDE_2 = Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F);
    private static final Quaternion SIDE_3 = QuaternionHelper.hamiltonProduct(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F), Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
    private static final Quaternion SIDE_4 = QuaternionHelper.hamiltonProduct(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F), Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

    private static final float[][][] VERTEX_COORDINATES = {
            {{-1, -1, -1}, {-1, -1, 1}, {1, -1, 1}, {1, -1, -1}},   // BOTTOM
            {{-1, 1, 1}, {-1, 1, -1}, {1, 1, -1}, {1, 1, 1}},       // TOP
            {{1, 1, 1}, {1, -1, 1}, {-1, -1, 1}, {-1, 1, 1}},       // SOUTH
            {{-1, 1, 1}, {-1, -1, 1}, {-1, -1, -1}, {-1, 1, -1}},   // WEST
            {{-1, 1, -1}, {-1, -1, -1,}, {1, -1, -1}, {1, 1, -1}},  // NORTH
            {{1, 1, -1}, {1, -1, -1}, {1, -1, 1}, {1, 1, 1}}        // EAST
    };

    private static final float[][] TEXTURE_LOC = {
            {0F, 1.0F / 3.0F, 0F, 0.5F},
            {1.0F / 3.0F, 2.0F / 3.0F, 0F, 0.5F},
            {2.0F / 3.0F, 1F, 0F, 0.5F},
            {0F, 1.0F / 3.0F, 0.5F, 1},
            {1.0F / 3.0F, 2.0F / 3.0F, 0.5F, 1},
            {2.0F / 3.0F, 1F, 0.5F, 1}
    };

    public static void renderSkyBox(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyBoxRenderPhase renderPhase) {

        Identifier texture = null;
        texture = CUSTOM_END_SKY;
        //Identifier texture = renderPhase.CustomSkyBoxTextureProvider.apply(CustomSkyBoxMod.configs, clientWorld);
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

            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

            float[] texture_loc = TEXTURE_LOC[i];
            float texture_x1, texture_x2, texture_y1, texture_y2;
            texture_x1 = texture_loc[0];
            texture_x2 = texture_loc[1];
            texture_y1 = texture_loc[2];
            texture_y2 = texture_loc[3];

            float[][] vertex_four = VERTEX_COORDINATES[i];

            bufferBuilder.vertex(matrix4f, vertex_four[0][0], vertex_four[0][1], vertex_four[0][2]).texture(texture_x1, texture_y1).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, vertex_four[1][0], vertex_four[1][1], vertex_four[1][2]).texture(texture_x1, texture_y2).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, vertex_four[2][0], vertex_four[2][1], vertex_four[2][2]).texture(texture_x2, texture_y2).color(255, 255, 255, 255).next();
            bufferBuilder.vertex(matrix4f, vertex_four[3][0], vertex_four[3][1], vertex_four[3][2]).texture(texture_x2, texture_y1).color(255, 255, 255, 255).next();

            tessellator.draw();
            matrixStack.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }

        /*
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

         */

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
        return ValueNormalizer.toNormalDegree(degree);
    }

}
