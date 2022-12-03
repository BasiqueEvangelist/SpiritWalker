package me.basiqueevangelist.spiritwalker.client;

import me.basiqueevangelist.spiritwalker.mixin.client.WorldRendererAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.TypedActionResult;

@Environment(EnvType.CLIENT)
public class SpiritWalkerClient implements ClientModInitializer {
    private static FakeCameraEntity CAMERA;
    private static boolean STOPPING_SPIRIT_WALK = false;

    public static void enterSpiritWalk(LivingEntity entity, int level) {
        if (!(entity instanceof ClientPlayerEntity player)) return;

        STOPPING_SPIRIT_WALK = false;

        if (CAMERA == null) {
            var client = MinecraftClient.getInstance();
            CAMERA = new FakeCameraEntity(client, client.world);
            CAMERA.updatePositionAndAngles(player.getX(), player.getY(), player.getZ(), player.getHeadYaw(), player.getPitch());
            CAMERA.input = new KeyboardInput(client.options);
            client.player.input = new Input();
            client.setCameraEntity(CAMERA);
        }

        CAMERA.canNoClip = level > 0;
    }

    public static void leaveSpiritWalk(LivingEntity entity) {
        if (!(entity instanceof ClientPlayerEntity player)) return;
        if (CAMERA == null) return;

        STOPPING_SPIRIT_WALK = true;
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused()) return;
            if (CAMERA == null) return;

            double prevX = CAMERA.getX();
            double prevY = CAMERA.getY();
            double prevZ = CAMERA.getZ();

            if (STOPPING_SPIRIT_WALK) {
                CAMERA.updatePosition(
                    (CAMERA.getX() * 4 + client.player.getX()) / 5,
                    (CAMERA.getY() * 4 + client.player.getY()) / 5,
                    (CAMERA.getZ() * 4 + client.player.getZ()) / 5
                );
            } else {
                CAMERA.tick();
            }

            CAMERA.prevX = prevX;
            CAMERA.prevY = prevY;
            CAMERA.prevZ = prevZ;

            if (STOPPING_SPIRIT_WALK && CAMERA.squaredDistanceTo(client.player) < 0.1) {
                client.setCameraEntity(client.player);
                client.player.input = new KeyboardInput(client.options);
                CAMERA = null;
                STOPPING_SPIRIT_WALK = false;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            CAMERA = null;
            STOPPING_SPIRIT_WALK = false;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            var stack = player.getStackInHand(hand);

            if (!world.isClient) return TypedActionResult.pass(stack);
            if (CAMERA == null) return TypedActionResult.pass(stack);

            return TypedActionResult.fail(stack);
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
