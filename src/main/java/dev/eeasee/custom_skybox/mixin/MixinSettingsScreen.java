package dev.eeasee.custom_skybox.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SettingsScreen.class)
public abstract class MixinSettingsScreen extends Screen {
    private MixinSettingsScreen() {
        super(null);
    }

}
