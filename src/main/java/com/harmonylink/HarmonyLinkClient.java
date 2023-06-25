package com.harmonylink;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harmonylink.API.BatteryInfo;
import com.harmonylink.API.ChargingStatus;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.option.GraphicsMode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class HarmonyLinkClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private BatteryInfo batteryInfo;
    private int tickCount = 0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused() && client.world != null) {  // We don't want to execute the code if the game is paused
                if (++tickCount >= 20) {  // Increase the tick count and check if we've reached 20 yet (1-second timer)
                    tickCount = 0;  // Reset the tick count for next time

                    HttpClient httpclient = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://127.0.0.1:9000/v1/battery_info"))
                            .build();

                    httpclient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                            .thenApply(HttpResponse::body)
                            .thenAccept(this::handleResponse);
                }
            }
        });
    }

    private void handleResponse(String jsonResponse)
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        LOGGER.info("JSON Response: {}", this.batteryInfo);

        if (this.batteryInfo != gson.fromJson(jsonResponse, BatteryInfo.class))
        {
            BatteryInfo newBatteryInfo = gson.fromJson(jsonResponse, BatteryInfo.class);
            if (newBatteryInfo.hasBattery)
            {
                if (newBatteryInfo.chargingStatus == ChargingStatus.BATTERY)
                {
                    setRenderDistance(4);
                    MinecraftClient.getInstance().options.getSimulationDistance().setValue(4);
                    MinecraftClient.getInstance().options.getGraphicsMode().setValue(GraphicsMode.FAST);
                    MinecraftClient.getInstance().options.getBiomeBlendRadius().setValue(0);
                }

                else if (newBatteryInfo.chargingStatus == ChargingStatus.CHARGING)
                { setRenderDistance(12);
                    MinecraftClient.getInstance().options.getSimulationDistance().setValue(12);
                    MinecraftClient.getInstance().options.getGraphicsMode().setValue(GraphicsMode.FANCY);
                    MinecraftClient.getInstance().options.getBiomeBlendRadius().setValue(2);
                }
            }

            this.batteryInfo = newBatteryInfo;
        }
    }

    private void setRenderDistance(int distance)
    {
        // Check if the game is running on the client side
        if(MinecraftClient.getInstance().player != null)
        {
            LOGGER.info("Setting render distance to {}", distance);
            MinecraftClient.getInstance().options.getViewDistance().setValue(distance);
        }
        else
        {
            LOGGER.warn("Attempted to set render distance from server side, this is not supported.");
        }
    }
}
