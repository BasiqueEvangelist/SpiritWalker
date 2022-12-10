package me.basiqueevangelist.spiritwalker.network;

import io.wispforest.owo.network.OwoNetChannel;
import me.basiqueevangelist.spiritwalker.SpiritWalker;

public final class SpiritWalkerNetworking {
    public static final OwoNetChannel CHANNEL = OwoNetChannel.createOptional(SpiritWalker.id("channel"));

    private SpiritWalkerNetworking() {

    }

    public static void init() {
        CHANNEL.registerServerbound(StopSpiritWalkC2SPacket.class, (message, access) -> {
            if (!SpiritWalker.CONFIG.allowReturningImmediately()) return;

            access.player().removeStatusEffect(SpiritWalker.EFFECT);
        });
    }
}
