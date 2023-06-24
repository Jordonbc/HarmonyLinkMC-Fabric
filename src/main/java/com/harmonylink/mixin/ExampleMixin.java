package com.harmonylink.mixin;

import com.harmonylink.SettingsMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class ExampleMixin extends Screen {
    protected ExampleMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info) {
        this.addDrawableChild(ButtonWidget.builder(Text.of("HL"), button ->  {
            MinecraftClient.getInstance().setScreen(new SettingsMenu(this));
        }).size(25, 25).build());
    }
}
