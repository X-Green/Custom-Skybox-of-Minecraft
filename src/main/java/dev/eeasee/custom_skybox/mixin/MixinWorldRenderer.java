package dev.eeasee.custom_skybox.mixin;

import dev.eeasee.custom_skybox.render.SkyBoxRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
    private static final Identifier[] NEW_END_SKY = new Identifier[]{
            new Identifier("eeasee_custom_skybox", "texture/end_sky/1.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/2.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/3.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/4.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/5.png"),
            new Identifier("eeasee_custom_skybox", "texture/end_sky/6.png")
    };

    @Final
    @Shadow
    private TextureManager textureManager;

    @Inject(method = "renderSky", at = @At("RETURN"))
    private void injectRenderSky(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(this.textureManager, matrixStack, NEW_END_SKY);
    }

    /*
    @Redirect(method = "renderSky", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;renderEndSky(Lnet/minecraft/client/util/math/MatrixStack;)V"
    ))
    private void renderSkyBox(WorldRenderer worldRenderer, MatrixStack matrixStack) {
        SkyBoxRendering.renderSkyBox(this.textureManager, matrixStack, NEW_END_SKY);
    }

     */

}
