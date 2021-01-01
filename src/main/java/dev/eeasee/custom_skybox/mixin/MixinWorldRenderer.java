package dev.eeasee.custom_skybox.mixin;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.render.SkyBoxRendering;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Inject(method = "renderSky", at = @At("RETURN"), cancellable = true)
    private void injectRenderSky_AfterAll(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.textureManager, matrixStack, NEW_END_SKY, SkyBoxRendering.SkyBoxRenderPhase.AFTER_ALL
        );
    }

    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEndSky(Lnet/minecraft/client/util/math/MatrixStack;)V",
            shift = At.Shift.AFTER
    ))
    private void injectRenderSky_EndSky(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.textureManager, matrixStack, NEW_END_SKY, SkyBoxRendering.SkyBoxRenderPhase.THE_END
        );
    }


    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableTexture()V",
            ordinal = 0, shift = At.Shift.AFTER
    ))
    private void injectRenderSky_BeforeOverworldSky(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.textureManager, matrixStack, NEW_END_SKY, SkyBoxRendering.SkyBoxRenderPhase.BEFORE_OVERWORLD_SKY
        );
    }


    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableFog()V",
            ordinal = 0, shift = At.Shift.AFTER
    ))
    private void injectRenderSky_BeforeOverworldDawnFog(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.textureManager, matrixStack, NEW_END_SKY, SkyBoxRendering.SkyBoxRenderPhase.BEFORE_DAWN_FOG
        );
    }


    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableTexture()V",
            ordinal = 0, shift = At.Shift.BEFORE
    ))
    private void injectRenderSky_BeforeOverworldSunAndMoon(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.textureManager, matrixStack, NEW_END_SKY, SkyBoxRendering.SkyBoxRenderPhase.BEFORE_SUN_AND_MOON
        );
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyDarknessHeight()D"))
    private double redirectedSkyDarknessHeight(ClientWorld clientWorld) {
        return CustomSkyBoxMod.configs.enableDarkenedSky ? clientWorld.getSkyDarknessHeight() : -Double.MAX_VALUE;
    }
}
