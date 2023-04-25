package me.basiqueevangelist.spiritwalker.client;

import me.basiqueevangelist.spiritwalker.mixin.LivingEntityAccessor;
import me.basiqueevangelist.spiritwalker.mixin.client.WorldRendererAccessor;
import me.basiqueevangelist.spiritwalker.network.BreakItemS2CPacket;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class SpiritWalkerClient implements ClientModInitializer {
    private static boolean STOPPING_SPIRIT_WALK = false;

    public static void enterSpiritWalk(LivingEntity entity, int level) {
        if (!(entity instanceof ClientPlayerEntity player)) return;

        STOPPING_SPIRIT_WALK = false;
        var client = MinecraftClient.getInstance();

        if (!(client.cameraEntity instanceof FakeCameraEntity camera)) {
            var camera = new FakeCameraEntity(client, client.world);
            camera.updatePositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getHeadYaw(), player.getPitch());
            camera.input = new KeyboardInput(client.options);
            client.player.input = new Input();
            client.setCameraEntity(camera);
            camera.canNoClip = level > 0;
        } else {
            camera.canNoClip = level > 0;
        }
    }

    public static void leaveSpiritWalk(LivingEntity entity) {
        if (!(entity instanceof ClientPlayerEntity)) return;
        if (!(MinecraftClient.getInstance().cameraEntity instanceof FakeCameraEntity)) return;

        STOPPING_SPIRIT_WALK = true;
    }

    @Override
    public void onInitializeClient() {
        SpiritWalkerNetworking.CHANNEL.registerClientbound(BreakItemS2CPacket.class, (message, access) -> {
            var entity = access.player().world.getEntityById(message.entityId());

            if (!(entity instanceof LivingEntity living)) return;

            ((LivingEntityAccessor) living).callPlayEquipmentBreakEffects(message.brokenStack());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused()) return;
            if (!(client.cameraEntity instanceof FakeCameraEntity camera)) return;

            double prevX = camera.getX();
            double prevY = camera.getY();
            double prevZ = camera.getZ();

            if (STOPPING_SPIRIT_WALK) {
                camera.updatePosition(
                    (camera.getX() * 4 + client.player.getX()) / 5,
                    (camera.getY() * 4 + client.player.getY()) / 5,
                    (camera.getZ() * 4 + client.player.getZ()) / 5
                );
            } else {
                camera.tick();
            }

            camera.prevX = prevX;
            camera.prevY = prevY;
            camera.prevZ = prevZ;

            if (STOPPING_SPIRIT_WALK && camera.squaredDistanceTo(client.player) < 0.1) {
                client.setCameraEntity(client.player);
                client.player.input = new KeyboardInput(client.options);
                STOPPING_SPIRIT_WALK = false;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            STOPPING_SPIRIT_WALK = false;
        });

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (context.camera().getFocusedEntity() instanceof FakeCameraEntity camEntity
             && context.camera().isThirdPerson()) {
                context.matrixStack().push();
                camEntity.lastRenderX = camEntity.prevX;
                camEntity.lastRenderY = camEntity.prevY;
                camEntity.lastRenderZ = camEntity.prevZ;
                ((WorldRendererAccessor) context.worldRenderer()).callRenderEntity(
                    camEntity,
                    context.camera().getPos().x,
                    context.camera().getPos().y,
                    context.camera().getPos().z,
                    context.tickDelta(),
                    context.matrixStack(),
                    context.consumers()
                );
                context.matrixStack().pop();
            }
        });
    }
}
