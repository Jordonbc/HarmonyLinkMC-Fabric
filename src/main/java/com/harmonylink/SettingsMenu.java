package com.harmonylink;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class SettingsMenu extends Screen {
    private final Screen parent;
    private final Text title;
    private ButtonWidget backButton;
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
        int buttonWidth = 30;
        int buttonHeight = 20;
        int centerX = 0;
        int centerY = 0;

        backButton = ButtonWidget.builder(Text.of("Back"), button -> MinecraftClient.getInstance().setScreen(parent))
                .size(buttonWidth, buttonHeight)
                .position(centerX, centerY)
                .build();

        this.addDrawableChild(backButton);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices); // Renders the default screen background
        super.render(matrices, mouseX, mouseY, delta);

        //backButton.render(matrices, mouseX, mouseY, delta);

        int titleX = width / 2;
        int titleY = 20;


        LOGGER.info("titleX = {}", titleX);
        LOGGER.info("titleY = {}", titleY);

        drawCenteredTextWithShadow(matrices, textRenderer, title, titleX, titleY, 0xFFFFFF);
    }
}
