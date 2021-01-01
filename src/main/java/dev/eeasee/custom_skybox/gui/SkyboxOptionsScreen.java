package dev.eeasee.custom_skybox.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class SkyboxOptionsScreen extends Screen {
    public static final Identifier BUTTON_ICON_TEXTURE = new Identifier(
            "eeasee_custom_skybox", "texture/gui/settings_button.png"
    );
    private static final TranslatableText OPTION_SCREEN_TITLE = new TranslatableText(
            "dev.eeasee.custom_skybox.gui.option_screen_title"
    );

    private final Screen parent;

    public SkyboxOptionsScreen(Screen parent) {
        super(OPTION_SCREEN_TITLE);
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        this.renderBackground();
    }

    @Override
    public void onClose() {
        this.minecraft.openScreen(this.parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 15, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}