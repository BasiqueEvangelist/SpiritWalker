package me.basiqueevangelist.spiritwalker.neoforge.client;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import me.basiqueevangelist.spiritwalker.neoforge.SpiritWalkerNeoforge;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = SpiritWalkerNeoforge.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class SpiritWalkerClientNeoforge {

    @SubscribeEvent
    public static void endClientTick(ClientTickEvent.Post event) {
        SpiritWalkerClient.endClientTick(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void disconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        SpiritWalkerClient.STOPPING_SPIRIT_WALK = false;
    }

    @SubscribeEvent
    public static void afterEntities(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        SpiritWalkerClient.renderAfterEntities(
                event.getCamera(),
                event.getPoseStack(),
                event.getLevelRenderer(),
                Minecraft.getInstance().renderBuffers().bufferSource()
        );
    }
}