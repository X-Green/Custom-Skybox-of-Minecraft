package dev.eeasee.custom_skybox.mixin;

import dev.eeasee.custom_skybox.gui.SkyboxOptionsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SettingsScreen.class)
public abstract class MixinSettingsScreen extends Screen {
    private MixinSettingsScreen() {
        super(null);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void drawMenuButton(CallbackInfo ci) {
        this.addButton(new TexturedButtonWidget(
                this.width / 2 - 180,
                this.height / 6 + 72 - 6,
                20,
                20,
                0,
                0,
                20,
                SkyboxOptionsScreen.BUTTON_ICON_TEXTURE,
                32,
                64,
                (buttonWidget) -> MinecraftClient.getInstance().openScreen(new SkyboxOptionsScreen(this))
        ));
    }
}
