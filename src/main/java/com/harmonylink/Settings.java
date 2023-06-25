package com.harmonylink;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.SimpleOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class Settings {
    public File file;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public HLSimpleOption<Integer> renderDistance;
    public HLSimpleOption<Integer> simulationDistance;
    public HLSimpleOption<GraphicsMode> GraphicsMode;
    public HLSimpleOption<Integer> BiomeBlendRadius;

    public Settings(String FileName) {

        this.file = new File(MinecraftClient.getInstance().runDirectory + FileName);

        this.renderDistance = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getViewDistance().getValue());
        this.simulationDistance = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getSimulationDistance().getValue());
        this.GraphicsMode = new HLSimpleOption<GraphicsMode>(MinecraftClient.getInstance().options.getGraphicsMode().getValue());
        this.BiomeBlendRadius = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getBiomeBlendRadius().getValue());

        createFileIfNotExists(this.file);
        loadOptions(this.file);
    }

    public boolean loadOptions(File configFile) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(configFile)) {
            Settings loadedSettings = gson.fromJson(reader, Settings.class);

            if (loadedSettings == null) {
                LOGGER.error("Error loading options: Unable to deserialize settings from JSON.");
                return false;
            }

            if (loadedSettings.renderDistance != null)
            {
                this.renderDistance = loadedSettings.renderDistance;
            } else {
                this.renderDistance.setValue(MinecraftClient.getInstance().options.getViewDistance().getValue());
            }

            if (loadedSettings.simulationDistance != null)
            {
                this.simulationDistance = loadedSettings.simulationDistance;
            } else {
                this.simulationDistance.setValue(MinecraftClient.getInstance().options.getSimulationDistance().getValue());
            }

            if (loadedSettings.BiomeBlendRadius != null)
            {
                this.BiomeBlendRadius = loadedSettings.BiomeBlendRadius;
            } else {
                this.BiomeBlendRadius.setValue(MinecraftClient.getInstance().options.getBiomeBlendRadius().getValue());
            }

            if (loadedSettings.GraphicsMode != null)
            {
                this.GraphicsMode = loadedSettings.GraphicsMode;
            } else {
                this.GraphicsMode.setValue(MinecraftClient.getInstance().options.getGraphicsMode().getValue());
            }

            LOGGER.info("Options loaded successfully.");
            return true;

        } catch (IOException e) {
            LOGGER.error("Error loading options: " + e.getMessage());
            return false;
        }
    }


    public boolean saveSettingToFile(String filePath, String key, String value) {
        return false;
    }

    private void createFileIfNotExists(File filePath) {
        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
