package dev.eeasee.custom_skybox.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;

public class SkyBoxRendering {
    public static void renderSkyBox(TextureManager textureManager, MatrixStack matrixStack, Identifier[] textureArray) {
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
                    break;
                case 1:
                    // TOP
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180.0F));
                    break;
                case 2:
                    // SIDE 4
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
                    break;
                case 3:
                    // SIDE 1
                    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
                    break;
                case 4:
                    // SIDE 2
                    matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
                    break;
                case 5:
                    // SIDE 3
                    matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                    matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
                    break;

            }

            Matrix4f matrix4f = matrixStack.peek().getModel();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);

            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(255, 255, 255, 50).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1F).color(255, 255, 255, 50).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1F, 1F).color(255, 255, 255, 50).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1F, 0.0F).color(255, 255, 255, 50).next();

            tessellator.draw();
            matrixStack.pop();
            RenderSystem.depthMask(true);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }

    }

}
