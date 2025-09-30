package me.basiqueevangelist.spiritwalker.client;

import me.basiqueevangelist.spiritwalker.mixin.client.LevelRendererAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

@Environment(EnvType.CLIENT)
@ApiStatus.Internal
public class SpiritWalkerClient implements ClientModInitializer {
    private static boolean STOPPING_SPIRIT_WALK = false;

    @SuppressWarnings("DataFlowIssue")
    @ApiStatus.Internal
    public static void enterSpiritWalk(LivingEntity entity, int level) {
        if (!(entity instanceof LocalPlayer player)) return;

        STOPPING_SPIRIT_WALK = false;
        var client = Minecraft.getInstance();

        if (!(client.cameraEntity instanceof FakeCameraEntity camera)) {
            var camera = new FakeCameraEntity(client, client.level);
            camera.absMoveTo(player.getX(), player.getY(), player.getZ(), player.getYHeadRot(), player.getXRot());
            camera.input = new KeyboardInput(client.options);
            client.player.input = new Input();
            client.setCameraEntity(camera);
            camera.canNoClip = level > 0;
        } else {
            camera.canNoClip = level > 0;
        }
    }

    public static void leaveSpiritWalk(LivingEntity entity) {
        if (!(entity instanceof LocalPlayer)) return;
        if (!(Minecraft.getInstance().cameraEntity instanceof FakeCameraEntity)) return;

        STOPPING_SPIRIT_WALK = true;
    }

    public static boolean isInSpiritWalk(Player player) {
        return player instanceof LocalPlayer
            && Minecraft.getInstance().cameraEntity instanceof FakeCameraEntity;
    }

    @SuppressWarnings({"resource", "DataFlowIssue"})
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.isPaused()) return;
            if (!(client.cameraEntity instanceof FakeCameraEntity camera)) return;

            double prevX = camera.getX();
            double prevY = camera.getY();
            double prevZ = camera.getZ();

            if (STOPPING_SPIRIT_WALK) {
                camera.absMoveTo(
                    (camera.getX() * 4 + client.player.getX()) / 5,
                    (camera.getY() * 4 + client.player.getY()) / 5,
                    (camera.getZ() * 4 + client.player.getZ()) / 5
                );
            } else {
                camera.tick();
            }

            camera.xo = prevX;
            camera.yo = prevY;
            camera.zo = prevZ;

            if (STOPPING_SPIRIT_WALK && camera.distanceToSqr(client.player) < 0.1) {
                client.setCameraEntity(client.player);
                client.player.input = new KeyboardInput(client.options);
                STOPPING_SPIRIT_WALK = false;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register(
            (handler, client) -> STOPPING_SPIRIT_WALK = false);

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (context.camera().getEntity() instanceof FakeCameraEntity camEntity
             && context.camera().isDetached()) {
                context.matrixStack().pushPose();
                camEntity.xOld = camEntity.xo;
                camEntity.yOld = camEntity.yo;
                camEntity.zOld = camEntity.zo;
                ((LevelRendererAccessor) context.worldRenderer()).callRenderEntity(
                    camEntity,
                    context.camera().getPosition().x,
                    context.camera().getPosition().y,
                    context.camera().getPosition().z,
                    context.tickCounter().getGameTimeDeltaPartialTick(false),
                    context.matrixStack(),
                    context.consumers()
                );
                context.matrixStack().popPose();
            }
        });
    }
}
