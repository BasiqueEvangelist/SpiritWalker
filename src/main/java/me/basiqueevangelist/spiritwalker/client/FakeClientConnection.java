package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;

public class FakeClientConnection extends Connection {
    public FakeClientConnection(PacketFlow side) {
        super(side);
    }
}
