package dev.eeasee.custom_skybox.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.eeasee.custom_skybox.sky_layer.SkyLayer;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

import java.util.ListIterator;

public class SkyBoxRendering {

    private static final byte[][][] VERTEX_COORDINATES = {
            {{-100, -100, -100}, {-100, -100, 100}, {100, -100, 100}, {100, -100, -100}},   // BOTTOM
            {{-100, 100, 100}, {-100, 100, -100}, {100, 100, -100}, {100, 100, 100}},       // TOP
            {{100, 100, 100}, {100, -100, 100}, {-100, -100, 100}, {-100, 100, 100}},       // SOUTH
            {{-100, 100, 100}, {-100, -100, 100}, {-100, -100, -100}, {-100, 100, -100}},   // WEST
            {{-100, 100, -100}, {-100, -100, -100,}, {100, -100, -100}, {100, 100, -100}},  // NORTH
            {{100, 100, -100}, {100, -100, -100}, {100, -100, 100}, {100, 100, 100}}        // EAST
    };

    private static final float[][] TEXTURE_LOC = {
            {0F, 1.0F / 3.0F, 0F, 0.5F},
            {1.0F / 3.0F, 2.0F / 3.0F, 0F, 0.5F},
            {2.0F / 3.0F, 1F, 0F, 0.5F},
            {0F, 1.0F / 3.0F, 0.5F, 1},
            {1.0F / 3.0F, 2.0F / 3.0F, 0.5F, 1},
            {2.0F / 3.0F, 1F, 0.5F, 1}
    };

    public static void renderSky(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyBoxRenderPhase renderPhase) {

        for (ListIterator<SkyLayer> layers = renderPhase.getLayersIterator();
             layers.hasNext(); ) {
            SkyLayer skyLayer = layers.next();
            renderLayer(clientWorld, textureManager, matrixStack, skyLayer);
        }

    }

    private static void renderLayer(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyLayer skyLayer) {

        int alpha = skyLayer.getFadingAlpha((int) clientWorld.getTimeOfDay());

        alpha = 255;


        if (alpha == 0) {
            return;
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();

        if (alpha == 255) {
            RenderSystem.defaultBlendFunc();
        } else {
            RenderSystem.defaultAlphaFunc();
        }

        RenderSystem.defaultAlphaFunc();
        RenderSystem.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        Identifier textureLocation = skyLayer.getSkyBoxTexture();

        textureManager.bindTexture(textureLocation);

        for (int i = 0; i < 6; ++i) {
            matrixStack.push();

            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

            float[] texture_loc = TEXTURE_LOC[i];
            float texture_x1, texture_x2, texture_y1, texture_y2;
            texture_x1 = texture_loc[0];
            texture_x2 = texture_loc[1];
            texture_y1 = texture_loc[2];
            texture_y2 = texture_loc[3];

            byte[][] vertex_four = VERTEX_COORDINATES[i];

            bufferBuilder.vertex(matrix4f, vertex_four[0][0], vertex_four[0][1], vertex_four[0][2]).texture(texture_x1, texture_y1).color(255, 255, 255, alpha).next();
            bufferBuilder.vertex(matrix4f, vertex_four[1][0], vertex_four[1][1], vertex_four[1][2]).texture(texture_x1, texture_y2).color(255, 255, 255, alpha).next();
            bufferBuilder.vertex(matrix4f, vertex_four[2][0], vertex_four[2][1], vertex_four[2][2]).texture(texture_x2, texture_y2).color(255, 255, 255, alpha).next();
            bufferBuilder.vertex(matrix4f, vertex_four[3][0], vertex_four[3][1], vertex_four[3][2]).texture(texture_x2, texture_y1).color(255, 255, 255, alpha).next();

            tessellator.draw();
            matrixStack.pop();
        }
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        /*
        {
            //Tessellator tessellator = Tessellator.getInstance();
            //BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultAlphaFunc();
            RenderSystem.depthMask(false);
            matrixStack.push();
            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            textureManager.bindTexture(new Identifier("minecraft", "textures/item/apple.png"));
            int aaaa;
            aaaa = (int) clientWorld.getTime() % 255;
            bufferBuilder.vertex(matrix4f, -150, 200, 150).texture(0, 0).color(255, 255, 255, aaaa).next();
            bufferBuilder.vertex(matrix4f, -150, 200, -150).texture(0, 1).color(255, 255, 255, aaaa).next();
            bufferBuilder.vertex(matrix4f, 150, 200, -150).texture(1, 1).color(255, 255, 255, aaaa).next();
            bufferBuilder.vertex(matrix4f, 150, 200, 150).texture(1, 0).color(255, 255, 255, aaaa).next();

            tessellator.draw();
            matrixStack.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }

         */
    }

}
