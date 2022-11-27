package me.basiqueevangelist.potionofdisassociation.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.telemetry.TelemetrySender;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;
import net.minecraft.util.Util;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public FakeClientPlayNetworkHandler(MinecraftClient client) {
        super(client, null, new FakeClientConnection(NetworkSide.CLIENTBOUND), new GameProfile(Util.NIL_UUID, "Camera"), null);
    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }
}
