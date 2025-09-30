package me.basiqueevangelist.spiritwalker.fabric.client;

import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import me.basiqueevangelist.spiritwalker.mixin.client.LevelRendererAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.player.KeyboardInput;

public class SpiritWalkerClientFabric implements ClientModInitializer {
    @SuppressWarnings({"resource", "DataFlowIssue"})
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(SpiritWalkerClient::endClientTick);

        ClientPlayConnectionEvents.DISCONNECT.register(
            (handler, client) -> SpiritWalkerClient.STOPPING_SPIRIT_WALK = false);

        WorldRenderEvents.AFTER_ENTITIES.register(context ->
            SpiritWalkerClient.renderAfterEntities(context.camera(), context.matrixStack(), context.worldRenderer(), context.consumers()));
    }
}
