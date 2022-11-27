package me.basiqueevangelist.potionofdisassociation.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;

@Environment(EnvType.CLIENT)
public class PotionOfDisassociationClient implements ClientModInitializer {
    private static FakeCameraEntity CAMERA;

    public static void enterDisassociation(LivingEntity entity) {
        if (!(entity instanceof ClientPlayerEntity player)) return;
        if (CAMERA != null) return;

        var client = MinecraftClient.getInstance();
        CAMERA = new FakeCameraEntity(client, client.world);
        CAMERA.updatePositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getHeadYaw(), player.getPitch());
        CAMERA.input = new KeyboardInput(client.options);
        client.player.input = new Input();
        client.setCameraEntity(CAMERA);
    }

    public static void leaveDisassociation(LivingEntity entity) {
        if (!(entity instanceof ClientPlayerEntity player)) return;
        if (CAMERA == null) return;


        MinecraftClient client = MinecraftClient.getInstance();
        client.setCameraEntity(player);
        player.input = new KeyboardInput(client.options);
        CAMERA = null;
    }

    public static boolean isDisassociating() {
        return CAMERA == null;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused()) return;

            if (CAMERA != null) {
                double prevX = CAMERA.getX();
                double prevY = CAMERA.getY();
                double prevZ = CAMERA.getZ();
                CAMERA.tick();
                CAMERA.prevX = prevX;
                CAMERA.prevY = prevY;
                CAMERA.prevZ = prevZ;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            CAMERA = null;
        });
    }
}
