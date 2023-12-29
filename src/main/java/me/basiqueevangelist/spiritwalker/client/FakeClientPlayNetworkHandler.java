package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.Packet;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public FakeClientPlayNetworkHandler(MinecraftClient client) {
        super(client, new FakeClientConnection(NetworkSide.CLIENTBOUND), new ClientConnectionState(client.getGameProfile(), null, null, null, null, null, null));
    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }
}
