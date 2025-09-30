package me.basiqueevangelist.spiritwalker.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    @Shadow @Final protected Minecraft minecraft;

    @SuppressWarnings("ConstantConditions")
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;isAlwaysFlying()Z"))
    private boolean lockFlyingForCamera(boolean orig) {
        if ((Object) this instanceof FakeCameraEntity)
            return true;

        return orig;
    }

    @ModifyExpressionValue(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isControlledCamera()Z"))
    private boolean sendMovementPackets(boolean orig) {
        if (minecraft.getCameraEntity() instanceof FakeCameraEntity)
            return true;

        return orig;
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
        method = "aiStep",
        at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z")
        }
    )
    private boolean forceNotInWater(boolean orig) {
        return !((Object) this instanceof FakeCameraEntity) && orig;
    }
}
