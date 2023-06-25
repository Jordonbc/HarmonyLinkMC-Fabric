package com.harmonylink.screen;

import com.harmonylink.Settings;
import com.harmonylink.screen.widgets.HLSliderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;


public class OptionsMenu extends Screen {
    private final Screen parent;
    private final Text title;

    private Settings Settings;

    private String FileName;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public OptionsMenu(Screen parent, String FileName) {
        super(Text.of("Options Menu"));
        this.parent = parent;
        this.FileName = FileName;
        this.title = Text.of("Options");
    }

    @Override
    protected void init() {
        super.init();

        this.Settings = new Settings(this.FileName);
        updateButtonPosition();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);

        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);


        updateButtonPosition();
    }

    public void a(Integer b) {

    }

    private void updateButtonPosition() {
        int buttonWidth = 150;

        ButtonWidget backButton = ButtonWidget.builder(Text.of("Back"), button -> MinecraftClient.getInstance().setScreen(parent))
                .size(30, 20)
                .position(0, 0)
                .build();
        this.addDrawableChild(backButton);

        ButtonWidget batteryOptionsButton = ButtonWidget.builder(Text.of("Graphics:" + Settings.GraphicsMode.getValue().toString()), button -> MinecraftClient.getInstance().setScreen(parent))
                .position((width / 2) - buttonWidth - 10, 50)
                .build();
        this.addDrawableChild(batteryOptionsButton);

        HLSliderWidget renderDistanceSlider = new HLSliderWidget(
                (width / 2) - buttonWidth + (buttonWidth + 10),
                50,
                150,
                20,
                Text.of("Render Distance: " + Settings.renderDistance.getValue()),
                Settings.renderDistance.getValue().doubleValue(),
                2.0,
                32.0,
                (slider, value) -> {
                    int intValue = (int) value;
                    LOGGER.info("Value: {}", intValue);
                    Settings.renderDistance.setValue(intValue);
                }
        );

        this.addDrawableChild(renderDistanceSlider);

        ButtonWidget dockedOptionsButton = ButtonWidget.builder(Text.of("Docked"), button -> MinecraftClient.getInstance().setScreen(parent))
                .position(width / 2 - (buttonWidth / 2), 80)
                .build();
        this.addDrawableChild(dockedOptionsButton);

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
