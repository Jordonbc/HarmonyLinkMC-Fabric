package com.harmonylink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GraphicsMode;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class GraphicsSettings {
    public transient File file;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @SerializedName("renderDistance")
    public HLSimpleOption<Integer> renderDistance;
    @SerializedName("simulationDistance")
    public HLSimpleOption<Integer> simulationDistance;
    @SerializedName("GraphicsMode")
    public HLSimpleOption<GraphicsMode> GraphicsMode;
    @SerializedName("BiomeBlendRadius")
    public HLSimpleOption<Integer> BiomeBlendRadius;
    @SerializedName("guiScale")
    public HLSimpleOption<Integer> guiScale;

    public GraphicsSettings(String FileName) {

        file = new File(FabricLoader.getInstance().getConfigDir() + "/HarmonyLink/" + FileName);

        this.renderDistance = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getViewDistance().getValue());
        this.simulationDistance = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getSimulationDistance().getValue());
        this.GraphicsMode = new HLSimpleOption<GraphicsMode>(MinecraftClient.getInstance().options.getGraphicsMode().getValue());
        this.BiomeBlendRadius = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getBiomeBlendRadius().getValue());
        this.guiScale = new HLSimpleOption<Integer>(MinecraftClient.getInstance().options.getGuiScale().getValue());

        createFileIfNotExists(file);
        loadOptions(file);
    }

    public void loadOptions(File configFile) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(configFile)) {
            GraphicsSettings loadedSettings = gson.fromJson(reader, GraphicsSettings.class);

            if (loadedSettings == null) {
                LOGGER.error("Error loading options: Unable to deserialize settings from JSON.");
                return;
            }

            // Copy values from loadedSettings to the current instance
            this.renderDistance = loadedSettings.renderDistance;
            this.simulationDistance = loadedSettings.simulationDistance;
            this.GraphicsMode = loadedSettings.GraphicsMode;
            this.BiomeBlendRadius = loadedSettings.BiomeBlendRadius;
            this.guiScale = loadedSettings.guiScale;

            LOGGER.info("Options loaded successfully.");

        } catch (IOException e) {
            LOGGER.error("Error loading options: " + e.getMessage());
        }
    }



    public boolean saveSettingsToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
            LOGGER.info("Options saved successfully.");
            return true;
        } catch (IOException e) {
            LOGGER.error("Error saving options: " + e.getMessage());
            return false;
        }
    }

    private void createFileIfNotExists(File filePath) {
        File parentDirectory = filePath.getParentFile();
        if (!parentDirectory.exists()) {
            if (!parentDirectory.mkdirs()) {
                throw new RuntimeException("Error creating directories: " + parentDirectory.getAbsolutePath());
            }
        }

        if (!filePath.exists()) {
            try {
                filePath.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + filePath.getAbsolutePath(), e);
            }
        }
    }

}
