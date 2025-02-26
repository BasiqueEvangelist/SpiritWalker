package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.Packet;
import net.minecraft.resource.featuretoggle.FeatureSet;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    public FakeClientPlayNetworkHandler(MinecraftClient client) {
        super(client, new FakeClientConnection(NetworkSide.CLIENTBOUND), new ClientConnectionState(client.getGameProfile(), null, null, FeatureSet.empty(), null, null, null, null, null, false, null, null));
    }

    @Override
    public void sendPacket(Packet<?> packet) {

    }
}
