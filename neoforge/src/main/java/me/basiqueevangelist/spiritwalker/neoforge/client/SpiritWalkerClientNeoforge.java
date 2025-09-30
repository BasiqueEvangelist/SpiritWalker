package me.basiqueevangelist.spiritwalker.neoforge.client;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import me.basiqueevangelist.spiritwalker.neoforge.SpiritWalkerNeoforge;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = SpiritWalkerNeoforge.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class SpiritWalkerClientNeoforge {
    // ClientTickEvents.END_CLIENT_TICK.register(client -> {
    //            if (client.isPaused()) return;
    //            if (!(client.cameraEntity instanceof FakeCameraEntity camera)) return;
    //
    //            double prevX = camera.getX();
    //            double prevY = camera.getY();
    //            double prevZ = camera.getZ();
    //
    //            if (SpiritWalkerClient.STOPPING_SPIRIT_WALK) {
    //                camera.absMoveTo(
    //                    (camera.getX() * 4 + client.player.getX()) / 5,
    //                    (camera.getY() * 4 + client.player.getY()) / 5,
    //                    (camera.getZ() * 4 + client.player.getZ()) / 5
    //                );
    //            } else {
    //                camera.tick();
    //            }
    //
    //            camera.xo = prevX;
    //            camera.yo = prevY;
    //            camera.zo = prevZ;
    //
    //            if (SpiritWalkerClient.STOPPING_SPIRIT_WALK && camera.distanceToSqr(client.player) < 0.1) {
    //                client.setCameraEntity(client.player);
    //                client.player.input = new KeyboardInput(client.options);
    //                SpiritWalkerClient.STOPPING_SPIRIT_WALK = false;
    //            }
    //        });
    //
    //        ClientPlayConnectionEvents.DISCONNECT.register(
    //            (handler, client) -> SpiritWalkerClient.STOPPING_SPIRIT_WALK = false);
    //
    //        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
    //            if (context.camera().getEntity() instanceof FakeCameraEntity camEntity
    //                && context.camera().isDetached()) {
    //                context.matrixStack().pushPose();
    //                camEntity.xOld = camEntity.xo;
    //                camEntity.yOld = camEntity.yo;
    //                camEntity.zOld = camEntity.zo;
    //                ((LevelRendererAccessor) context.worldRenderer()).callRenderEntity(
    //                    camEntity,
    //                    context.camera().getPosition().x,
    //                    context.camera().getPosition().y,
    //                    context.camera().getPosition().z,
    //                    context.tickCounter().getGameTimeDeltaPartialTick(false),
    //                    context.matrixStack(),
    //                    context.consumers()
    //                );
    //                context.matrixStack().popPose();
    //            }
    //        });

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

        SpiritWalkerClient.renderAfterEntities(event.getCamera(), event.getPoseStack(), event.getLevelRenderer(), Minecraft.getInstance().renderBuffers().bufferSource());
    }
}
