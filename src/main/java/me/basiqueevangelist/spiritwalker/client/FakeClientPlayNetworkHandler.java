package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.Packet;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public FakeClientPlayNetworkHandler(MinecraftClient client) {
        super(client, null, new FakeClientConnection(NetworkSide.CLIENTBOUND), null, client.getSession().getProfile(), null);
    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }
}
