package dev.eeasee.custom_skybox.gui;

import dev.eeasee.custom_skybox.CustomSkyBoxMod;
import dev.eeasee.custom_skybox.render.OverworldOcclusionLevel;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class SkyboxOptionsScreen extends Screen {
    public static final Identifier BUTTON_ICON_TEXTURE = new Identifier(
            "eeasee_custom_skybox", "textures/gui/manager.png"
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
                new SingleButtonListWidget(this.client, this.width, this.height, 64, this.height - 32, 25),
                new SingleButtonListWidget(this.client, this.width, this.height, 64, this.height - 32, 25),
                new SingleButtonListWidget(this.client, this.width, this.height, 64, this.height - 32, 25)
        };
        this.listWidgets[0].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.overworld.enable_overworld_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableOverworldCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableOverworldCustomSkyBox = aBoolean
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.overworld.skybox_noontime_overworld",
                        0.0, 24000.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.skyBoxNoonTimeOverworld,
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.skyBoxNoonTimeOverworld = aDouble.intValue(),
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.overworld.skybox_noontime_overworld")
                                + ": " + CustomSkyBoxMod.configs.skyBoxNoonTimeOverworld)
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.overworld.rotation_cycles_in_single_overworld_day",
                        -100.0, 100.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.rotationCyclesInSingleOverworldDay,
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.rotationCyclesInSingleOverworldDay = aDouble.intValue(),
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.overworld.rotation_cycles_in_single_overworld_day")
                                + ": " + CustomSkyBoxMod.configs.rotationCyclesInSingleOverworldDay)
                ),
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.overworld.enable_darkened_overworld_sky_under_certain_level",
                        gameOptions -> CustomSkyBoxMod.configs.enableDarkenedOverworldSkyUnderCertainLevel,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableDarkenedOverworldSkyUnderCertainLevel = aBoolean
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.overworld.occlusion_level",
                        0.0, 3.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.overworldOcclusionLevel.ordinal(),
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.overworldOcclusionLevel = OverworldOcclusionLevel.values()[aDouble.intValue()],
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.overworld.occlusion_level")
                                + ": " + I18n.translate(CustomSkyBoxMod.configs.overworldOcclusionLevel.desc))
                ),
        });
        this.listWidgets[1].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.nether.enable_nether_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableNetherCustomSkyBox = aBoolean
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.nether.skybox_noontime_nether",
                        0.0, 24000.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.skyBoxNoonTimeNether,
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.skyBoxNoonTimeNether = aDouble.intValue(),
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.nether.skybox_noontime_nether")
                                + ": " + CustomSkyBoxMod.configs.skyBoxNoonTimeNether)
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.nether.rotation_cycles_in_single_nether_day",
                        -100.0, 100.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.rotationCyclesInSingleNetherDay,
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.rotationCyclesInSingleNetherDay = aDouble.intValue(),
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.nether.rotation_cycles_in_single_nether_day")
                                + ": " + CustomSkyBoxMod.configs.rotationCyclesInSingleNetherDay)
                ),

        });
        this.listWidgets[2].addAll(new Option[]{
                new BooleanOption(
                        "dev.eeasee.custom_skybox.option.end.enable_end_custom_skybox",
                        gameOptions -> CustomSkyBoxMod.configs.enableTheEndCustomSkyBox,
                        (gameOptions, aBoolean) -> CustomSkyBoxMod.configs.enableTheEndCustomSkyBox = aBoolean
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.end.skybox_noontime_end",
                        0.0, 24000.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.skyBoxNoonTimeTheEnd,
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.skyBoxNoonTimeTheEnd = aDouble.intValue(),
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.end.skybox_noontime_end")
                                + ": " + CustomSkyBoxMod.configs.skyBoxNoonTimeTheEnd)
                ),
                new DoubleOption(
                        "dev.eeasee.custom_skybox.option.end.rotation_cycles_in_single_end_day",
                        -100.0, 100.0, 1.0F,
                        gameOptions -> (double) CustomSkyBoxMod.configs.rotationCyclesInSingleTheEndDay,
                        (gameOptions, aDouble) -> CustomSkyBoxMod.configs.rotationCyclesInSingleTheEndDay = aDouble.intValue(),
                        (gameOptions, doubleOption) -> Text.of(I18n.translate("dev.eeasee.custom_skybox.option.end.rotation_cycles_in_single_end_day")
                                + ": " + CustomSkyBoxMod.configs.rotationCyclesInSingleTheEndDay)
                ),

        });
    }

    private void addScreenAndButton() {
        this.addButton(new ButtonWidget(32, 32, 80, 20,
                new TranslatableText("createWorld.customize.preset.overworld"),
                (buttonWidget) -> this.changeIndex(0)));
        this.addButton(new ButtonWidget(32 + 100, 32, 80, 20,
                new TranslatableText("advancements.nether.root.title"),
                (buttonWidget) -> this.changeIndex(1)));
        this.addButton(new ButtonWidget(32 + 200, 32, 80, 20,
                new TranslatableText("advancements.end.root.title"),
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
        this.client.openScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.currentListWidget.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
