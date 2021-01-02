package dev.eeasee.custom_skybox.gui;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.configs.ConfigHolder;
import dev.eeasee.custom_skybox.configs.ConfigIO;
import dev.eeasee.custom_skybox.render.OverworldOcclusionLevel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;
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

    private SingleButtonListWidget[] listWidgets;

    private SingleButtonListWidget currentListWidget;

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
        listWidgets = new SingleButtonListWidget[]{
                new SingleButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25),
                new SingleButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25),
                new SingleButtonListWidget(this.minecraft, this.width, this.height, 64, this.height - 32, 25)
        };
        this.listWidgets[0].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.overworld.enable_overworld_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableOverworldCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableOverworldCustomSkyBox = aBoolean
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.overworld.occlusion_level",
                        0.0, 3.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.overworldOcclusionLevel.ordinal(),
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.overworldOcclusionLevel = OverworldOcclusionLevel.values()[aDouble.intValue()],
                        (gameOptions, doubleOption) -> CustomSkyBoxMod.configs.overworldOcclusionLevel.descText.asString()
                )
        });
        this.listWidgets[1].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.nether.enable_nether_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox = aBoolean)
        });
        this.listWidgets[2].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.end.enable_end_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableEndCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableEndCustomSkyBox = aBoolean
                )
        });
    }

    private void addScreenAndButton() {
        this.addButton(new ButtonWidget(32, 32, 80, 20,
                new TranslatableText("createWorld.customize.preset.overworld").asString(),
                (buttonWidget) -> this.changeIndex(0)));
        this.addButton(new ButtonWidget(32 + 100, 32, 80, 20,
                new TranslatableText("advancements.nether.root.title").asString(),
                (buttonWidget) -> this.changeIndex(1)));
        this.addButton(new ButtonWidget(32 + 200, 32, 80, 20,
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

        System.out.println(ConfigIO.GSON.toJson(CustomSkyBoxMod.configs));
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
