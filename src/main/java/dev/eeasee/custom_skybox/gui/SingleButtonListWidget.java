package dev.eeasee.custom_skybox.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.options.Option;

public class SingleButtonListWidget extends ButtonListWidget {
    SingleButtonListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    @Override
    public void addAll(Option[] options) {
        for(int i = 0; i < options.length; i += 2) {
            this.addSingleOptionEntry(options[i]);
        }
    }
}
