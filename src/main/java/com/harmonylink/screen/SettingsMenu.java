package com.harmonylink.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class SettingsMenu extends Screen {
    private final Screen parent;
    private final Text title;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SettingsMenu(Screen parent) {
        super(Text.of("Settings Menu"));
        this.parent = parent;

        this.title = Text.of("HarmonyLink Settings");
    }

    @Override
    protected void init() {
        super.init();
        updateButtonPosition();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        updateButtonPosition();
    }

    private void updateButtonPosition() {
        int buttonWidth = 150;

        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"), button -> MinecraftClient.getInstance().setScreen(parent))
                .size(30, 20)
                .position(0, 0)
                .build();
        this.addDrawableChild(backButton);

        ButtonWidget batteryOptionsButton = ButtonWidget.builder(Text.of("Battery"), button -> {
            MinecraftClient.getInstance().setScreen(new OptionsMenu(this, "Battery.json"));
            })
                .position((width / 2) - buttonWidth - 10, 50)
                .build();
        this.addDrawableChild(batteryOptionsButton);

        ButtonWidget chargingOptionsButton = ButtonWidget.builder(Text.of("Charging"), button -> MinecraftClient.getInstance().setScreen(parent))
                .position((width / 2) - buttonWidth + (buttonWidth + 10), 50)
                .build();
        this.addDrawableChild(chargingOptionsButton);

        ButtonWidget dockedOptionsButton = ButtonWidget.builder(Text.of("Docked"), button -> MinecraftClient.getInstance().setScreen(parent))
                .position(width / 2 - (buttonWidth / 2), 80)
                .build();
        this.addDrawableChild(dockedOptionsButton);

        LOGGER.info("BUTTON: {}", dockedOptionsButton.getWidth());
        LOGGER.info("OPTIONS: {}", MinecraftClient.getInstance().runDirectory);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices); // Renders the default screen background
        super.render(matrices, mouseX, mouseY, delta);

        //backButton.render(matrices, mouseX, mouseY, delta);

        int titleX = width / 2;
        int titleY = 20;


        //LOGGER.info("titleX = {}", titleX);
        //LOGGER.info("titleY = {}", titleY);

        drawCenteredTextWithShadow(matrices, textRenderer, title, titleX, titleY, 0xFFFFFF);
    }
}
