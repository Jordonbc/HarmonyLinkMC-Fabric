package com.harmonylink.screen;

import com.harmonylink.RangedValue;
import com.harmonylink.Settings;
import com.harmonylink.screen.widgets.HLSliderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;


public class OptionsMenu extends Screen {
    private final Screen parent;
    private final Text title;

    private Settings settings;

    private String fileName;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public OptionsMenu(Screen parent, String title, String fileName) {
        super(Text.of("Options Menu"));
        this.parent = parent;
        this.fileName = fileName;
        this.title = Text.of(title + " Options");
    }

    @Override
    protected void init() {
        super.init();
        this.settings = new Settings(fileName);
        initWidgets();
    }

    private void initWidgets() {
        int buttonWidth = 150;

        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"), button -> MinecraftClient.getInstance().setScreen(parent))
                .size(30, 20)
                .position(0, 0)
                .build();
        this.addDrawableChild(backButton);

        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4);
        GridWidget.Adder adder = gridWidget.createAdder(2);

        adder.add(ButtonWidget.builder(Text.of("Graphics: " + settings.GraphicsMode.getValue().toString()), button -> {
            GraphicsMode currentMode = settings.GraphicsMode.getValue();
            switch (currentMode) {
                case FABULOUS -> settings.GraphicsMode.setValue(GraphicsMode.FAST);
                case FANCY -> settings.GraphicsMode.setValue(GraphicsMode.FABULOUS);
                case FAST -> settings.GraphicsMode.setValue(GraphicsMode.FANCY);
            }

            GraphicsMode NewGraphicsMode = settings.GraphicsMode.getValue();

            String newMode = settings.GraphicsMode.getValue().toString();
            newMode = newMode.substring(0, 1).toUpperCase() + newMode.substring(1).toLowerCase();

            button.setMessage(Text.of("Graphics: " + newMode));
            })
        .build());


        HLSliderWidget renderDistanceSlider = new HLSliderWidget(0, 0, 150, 20, Text.of("Render Distance: " + settings.renderDistance.getValue()),
                new RangedValue(2, 32, settings.renderDistance.getValue().doubleValue()),
                (slider, value) -> {
                    int intValue = (int) Math.round(value);
                    LOGGER.info("Value: {}", intValue);
                    slider.setMessage(Text.of("Render Distance: " + intValue));
                    settings.renderDistance.setValue(intValue);
                },
                (slider, value) -> {
                    int intValue = (int) Math.round(value);
                    LOGGER.info("Value: {}", intValue);
                    slider.setMessage(Text.of("Render Distance: " + intValue));
                }
        );


        adder.add(renderDistanceSlider);

        RangedValue simulationDistanceRanged = new RangedValue(5, 32, settings.simulationDistance.getValue().doubleValue());

        HLSliderWidget simulationDistanceSlider = new HLSliderWidget(0, 0, 150, 20, Text.of("Simulation Distance: " + settings.simulationDistance.getValue()),
                simulationDistanceRanged,
                (slider, value) -> {
                    int intValue = (int) value;
                    LOGGER.info("Value: {}", intValue);
                    slider.setMessage(Text.of("Simulation Distance: " + intValue));
                },
                (slider, value) -> {
                    int intValue = (int) value;
                    LOGGER.info("Value: {}", intValue);
                    settings.simulationDistance.setValue(intValue);
                    simulationDistanceRanged.setValue(intValue);  // Update the RangedValue instance
                }
        );
        adder.add(simulationDistanceSlider);

        RangedValue biomeBlendRadiusRanged = new RangedValue(0, 7, settings.BiomeBlendRadius.getValue().doubleValue());

        HLSliderWidget biomeBlendRadiusSlider = new HLSliderWidget(0, 0, 150, 20, Text.of("Biome Blend Radius: " + settings.BiomeBlendRadius.getValue()),
                biomeBlendRadiusRanged,
                (slider, value) -> {
                    int intValue = (int) value;
                    LOGGER.info("Value: {}", intValue);
                    if (intValue == 0) {
                        slider.setMessage(Text.of("Biome Blend Radius: OFF"));
                    }
                    else {
                        slider.setMessage(Text.of("Biome Blend Radius: " + intValue));
                    }
                },
                (slider, value) -> {
                    int intValue = (int) value;
                    LOGGER.info("Value: {}", intValue);
                    settings.BiomeBlendRadius.setValue(intValue);
                    biomeBlendRadiusRanged.setValue(intValue);  // Update the RangedValue instance
                }
        );
        adder.add(biomeBlendRadiusSlider);


        // Position and add the grid to the screen
        gridWidget.refreshPositions();
        int gridWidth = gridWidget.getWidth();
        int gridX = (width - gridWidth) / 2; // Calculate the x-position to center the grid
        SimplePositioningWidget.setPos(gridWidget, gridX, 80, gridWidth, height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        initWidgets();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices); // Renders the default screen background
        super.render(matrices, mouseX, mouseY, delta);

        int titleX = width / 2;
        int titleY = 20;

        drawCenteredTextWithShadow(matrices, textRenderer, title, titleX, titleY, 0xFFFFFF);
    }

    public static double getRangedValue(double input, double minValue, double maxValue) {
        return MathHelper.clamp((input - minValue) / (maxValue - minValue), 0.0, 1.0);
    }
}
