package dev.eeasee.custom_skybox.mixin;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.render.SkyBoxRendering;
import dev.eeasee.custom_skybox.sky_layer.enums.SkyBoxRenderPhase;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {


    @Final
    @Shadow
    private TextureManager textureManager;

    @Shadow
    private ClientWorld world;

    @Inject(method = "renderSky", at = @At("RETURN"), cancellable = true)
    private void injectRenderSky_AfterAll(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRenderPhase phase;
        if (this.world.getDimension().isNether()) {
            phase = SkyBoxRenderPhase.THE_NETHER;
        } else {
            phase = SkyBoxRenderPhase.OVERWORLD;
        }
        SkyBoxRendering.renderSkyBox(
                this.world, this.textureManager, matrixStack, phase
        );
    }

    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEndSky(Lnet/minecraft/client/util/math/MatrixStack;)V",
            shift = At.Shift.AFTER
    ))
    private void injectRenderSky_EndSky(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.world, this.textureManager, matrixStack, SkyBoxRenderPhase.THE_END
        );
    }


    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableTexture()V",
            ordinal = 0, shift = At.Shift.AFTER
    ))
    private void injectRenderSky_BeforeOverworldSky(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.world, this.textureManager, matrixStack, SkyBoxRenderPhase.BEFORE_OVERWORLD_SKY
        );
    }


    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableFog()V",
            ordinal = 0, shift = At.Shift.AFTER
    ))
    private void injectRenderSky_BeforeOverworldDawnFog(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.world, this.textureManager, matrixStack, SkyBoxRenderPhase.BEFORE_DAWN_FOG
        );
    }


    @Inject(method = "renderSky", at = @At(
            value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;enableTexture()V",
            ordinal = 0, shift = At.Shift.BEFORE
    ))
    private void injectRenderSky_BeforeOverworldSunAndMoon(MatrixStack matrixStack, float f, CallbackInfo ci) {
        SkyBoxRendering.renderSkyBox(
                this.world, this.textureManager, matrixStack, SkyBoxRenderPhase.BEFORE_SUN_AND_MOON
        );
    }

}
