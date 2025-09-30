package me.basiqueevangelist.spiritwalker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import me.basiqueevangelist.spiritwalker.mixin.client.LevelRendererAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class SpiritWalkerClient {
    public static boolean STOPPING_SPIRIT_WALK = false;

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

    public static void endClientTick(Minecraft client) {
        if (client.isPaused()) return;
        if (!(client.cameraEntity instanceof FakeCameraEntity camera)) return;

        double prevX = camera.getX();
        double prevY = camera.getY();
        double prevZ = camera.getZ();

        if (SpiritWalkerClient.STOPPING_SPIRIT_WALK) {
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

        if (SpiritWalkerClient.STOPPING_SPIRIT_WALK && camera.distanceToSqr(client.player) < 0.1) {
            client.setCameraEntity(client.player);
            client.player.input = new KeyboardInput(client.options);
            SpiritWalkerClient.STOPPING_SPIRIT_WALK = false;
        }
    }

    public static void renderAfterEntities(Camera camera, PoseStack matrixStack, LevelRenderer levelRenderer, MultiBufferSource bufSource) {
        if (camera.getEntity() instanceof FakeCameraEntity camEntity
            && camera.isDetached()) {
            matrixStack.pushPose();

            camEntity.xOld = camEntity.xo;
            camEntity.yOld = camEntity.yo;
            camEntity.zOld = camEntity.zo;
            ((LevelRendererAccessor) levelRenderer).callRenderEntity(
                camEntity,
                camera.getPosition().x,
                camera.getPosition().y,
                camera.getPosition().z,
                Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false),
                matrixStack,
                bufSource
            );
            matrixStack.popPose();
        }
    }
}
