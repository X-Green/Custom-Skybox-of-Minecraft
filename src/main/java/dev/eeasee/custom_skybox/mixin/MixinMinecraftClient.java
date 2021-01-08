package dev.eeasee.custom_skybox.mixin;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigIO;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Inject(method = "stop", at = @At("HEAD"))
    private void beforeClientStop(CallbackInfo ci) {
        ConfigIO.writeConfigToFile(CustomSkyBoxMod.configs);
    }
}
