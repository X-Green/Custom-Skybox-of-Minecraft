package dev.eeasee.custom_skybox.gui;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.Option;
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

    private ButtonListWidget[] listWidgets;

    private ButtonListWidget currentListWidget;

    public SkyboxOptionsScreen(Screen parent) {
        super(OPTION_SCREEN_TITLE);
        this.parent = parent;
    }

    @Override
    public void init() {
        super.init();
        this.addScreenAndButton();
        this.addOptions();
        this.currentListWidget = listWidgets[0];
        this.children.add(currentListWidget);
    }

    private void addOptions() {
        listWidgets = new ButtonListWidget[]{
                new ButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25),
                new ButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25),
                new ButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25)
        };
        this.listWidgets[0].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.main.enable_nether_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox = aBoolean
                )
        });
        this.listWidgets[1].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.main.enable_overworld_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox = aBoolean
                )
        });
        this.listWidgets[2].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.main.enable_end_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox = aBoolean
                )
        });
    }

    private void addScreenAndButton() {
        this.addButton(new ButtonWidget(32, 20, 80, 20,
                new TranslatableText("createWorld.customize.preset.overworld").asString(),
                (buttonWidget) -> this.changeIndex(0)));
        this.addButton(new ButtonWidget(32 + 100, 20, 80, 20,
                new TranslatableText("advancements.nether.root.title").asString(),
                (buttonWidget) -> this.changeIndex(1)));
        this.addButton(new ButtonWidget(32 + 200, 20, 80, 20,
                new TranslatableText("advancements.end.root.title").asString(),
                (buttonWidget) -> this.changeIndex(2)));
    }

    private ButtonListWidget getCurrentButtonList() {
        return this.currentListWidget;
    }

    private void changeIndex(int index) {
        this.children.remove(currentListWidget);
        this.currentListWidget = listWidgets[index];
        this.children.add(currentListWidget);
    }

    @Override
    public void onClose() {
        this.minecraft.openScreen(this.parent);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.currentListWidget.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 15, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}
