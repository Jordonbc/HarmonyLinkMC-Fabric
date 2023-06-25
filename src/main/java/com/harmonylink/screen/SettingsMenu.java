package com.harmonylink.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.harmonylink.HarmonyLinkClient;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

/**
 * This is the Settings Menu screen class.
 * It extends the Screen class of Minecraft.
 */
public class SettingsMenu extends Screen {
    private final Screen parent;  // The parent screen
    private final Text title;  // Title text of the settings menu
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);  // Logger instance

    /**
     * SettingsMenu constructor.
     * @param parent The parent screen.
     */
    public SettingsMenu(Screen parent) {
        super(Text.of("Settings Menu"));
        this.parent = parent;
        this.title = Text.of("HarmonyLink Settings");
    }

    /**
     * Overriding the init method from the Screen class to initialize the widgets.
     */
    @Override
    protected void init() {
        super.init();
        initWidgets();  // Initialize the settings menu widgets
    }

    /**
     * Method to initialize the widgets of the settings menu.
     */
    private void initWidgets() {
        int buttonWidth = 150;

        // Initialize back button
        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"), button -> MinecraftClient.getInstance().setScreen(parent))
                .size(30, 20)
                .position(0, 0)
                .build();
        this.addDrawableChild(backButton);

        // Initialize grid widget
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4);
        GridWidget.Adder adder = gridWidget.createAdder(2);

        // Add various buttons to the grid

        adder.add(ButtonWidget.builder(Text.of("Docked Settings: " + HarmonyLinkClient.HLSETTINGS.EnableDocked.getValue()), button -> {
            Boolean isDocked = HarmonyLinkClient.HLSETTINGS.EnableDocked.getValue();
            if (isDocked) {
                HarmonyLinkClient.HLSETTINGS.EnableDocked.setValue(false);
            }else {
                HarmonyLinkClient.HLSETTINGS.EnableDocked.setValue(true);
            }
            //button.setMessage(Text.of("Docked Settings: " + HarmonyLinkClient.HLSETTINGS.EnableDocked.getValue()));
            HarmonyLinkClient.HLSETTINGS.saveSettingsToFile();
            clearAndInit();
        }).build());

        adder.add(ButtonWidget.builder(Text.of("Battery"), button -> {
            MinecraftClient.getInstance().setScreen(new OptionsMenu(this, "Battery", HarmonyLinkClient.batterySettings));
        }).build());

        adder.add(ButtonWidget.builder(Text.of("Charging"), button -> {
            MinecraftClient.getInstance().setScreen(new OptionsMenu(this, "Charging", HarmonyLinkClient.chargingSettings));
        }).build());

        if (HarmonyLinkClient.HLSETTINGS.EnableDocked.getValue()) {
            adder.add(ButtonWidget.builder(Text.of("Docked"), button -> {
                MinecraftClient.getInstance().setScreen(new OptionsMenu(this, "Docked", HarmonyLinkClient.dockedSettings));
            }).build());
        }

        // Position and add the grid to the screen
        gridWidget.refreshPositions();
        int gridWidth = gridWidget.getWidth();
        int gridX = (width - gridWidth) / 2; // Calculate the x-position to center the grid
        SimplePositioningWidget.setPos(gridWidget, gridX, 80, gridWidth, height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    /**
     * Resize method is called when the window size changes.
     * It calls the super method and re-initializes the widgets.
     * @param client The Minecraft client instance.
     * @param width The new width of the window.
     * @param height The new height of the window.
     */
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        initWidgets();
    }

    /**
     * Overriding the render method from the Screen class to draw this screen.
     * @param matrices The MatrixStack instance.
     * @param mouseX The x-coordinate of the mouse cursor.
     * @param mouseY The y-coordinate of the mouse cursor.
     * @param delta The amount of time since the last frame.
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices); // Renders the default screen background
        super.render(matrices, mouseX, mouseY, delta);

        int titleX = width / 2;
        int titleY = 20;

        drawCenteredTextWithShadow(matrices, textRenderer, title, titleX, titleY, 0xFFFFFF);
    }
}
