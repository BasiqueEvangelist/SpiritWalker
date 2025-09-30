package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.flag.FeatureFlagSet;

public class FakeClientPlayNetworkHandler extends ClientPacketListener {
    public FakeClientPlayNetworkHandler(Minecraft client) {
        super(client, new FakeClientConnection(PacketFlow.CLIENTBOUND), new CommonListenerCookie(client.getGameProfile(), null, null, FeatureFlagSet.of(), null, null, null, null, null, false, null, null));
    }

    @Override
    public void send(Packet<?> packet) {

    }
}
