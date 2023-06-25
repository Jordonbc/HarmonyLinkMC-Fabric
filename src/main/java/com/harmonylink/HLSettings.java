package com.harmonylink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class HLSettings {
    public transient File file;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @SerializedName("EnableDocked")
    public HLSimpleOption<Boolean> EnableDocked;

    public HLSettings(String FileName) {

        file = new File(FabricLoader.getInstance().getConfigDir() + "/HarmonyLink/" + FileName);

        this.EnableDocked = new HLSimpleOption<Boolean>(false);

        createFileIfNotExists(file);
        loadOptions(file);
    }

    public void loadOptions(File configFile) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(configFile)) {
            HLSettings loadedSettings = gson.fromJson(reader, HLSettings.class);

            if (loadedSettings == null) {
                LOGGER.error("Error loading options: Unable to deserialize settings from JSON.");
                return;
            }

            // Copy values from loadedSettings to the current instance
            this.EnableDocked = loadedSettings.EnableDocked;

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
