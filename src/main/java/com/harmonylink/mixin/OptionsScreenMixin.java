package com.harmonylink.mixin;

import com.harmonylink.HarmonyLinkClient;
import com.harmonylink.screen.SettingsMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
    @Shadow @Final private Screen parent;

    @Shadow @Final private GameOptions settings;
    protected int tickCount = 0;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info) {
        if (this.parent != null && !(this.parent instanceof SettingsMenu))
        {
            if (HarmonyLinkClient.getIsConnected())
            {
                this.addDrawableChild(ButtonWidget.builder(Text.of("HL"), button ->  {
                    MinecraftClient.getInstance().setScreen(new SettingsMenu(this));
                }).size(25, 25).build());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (++tickCount > 20) {
            tickCount = 0;
            refreshScreen();
        }
    }

    private void refreshScreen() {
        MinecraftClient client = MinecraftClient.getInstance();

        // Check the current screen type and set the appropriate new screen
        Screen currentScreen = client.currentScreen;

        if (currentScreen instanceof OptionsScreen) {
            // Example: Refresh the OptionsScreen
            client.setScreen(new OptionsScreen(parent, settings));
        }
    }
}
