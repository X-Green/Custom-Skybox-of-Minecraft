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

    public static void renderSky(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyBoxRenderPhase renderPhase) {
        for (ListIterator<SkyLayer> layers = renderPhase.getLayersIterator();
             layers.hasNext(); ) {
            SkyLayer skyLayer = layers.next();
            renderLayer(clientWorld, textureManager, matrixStack, skyLayer);
        }
    }

    private static void renderLayer(ClientWorld clientWorld, TextureManager textureManager, MatrixStack matrixStack, SkyLayer skyLayer) {

        RenderSystem.disableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        Identifier textureLocation = skyLayer.getSkyBoxTexture();

        if (textureLocation == null) {
            return;
        }

        for (int i = 0; i < 6; ++i) {
            textureManager.bindTexture(textureLocation);
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

    }

}
