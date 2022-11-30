package me.basiqueevangelist.potionofdissociation.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.potionofdissociation.client.FakeCameraEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @SuppressWarnings("ConstantConditions")
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z", ordinal = 0))
    private boolean disableNoClipIfLevel1(boolean orig) {
        if ((Object) this instanceof FakeCameraEntity cam)
            return cam.canNoClip;

        return orig;
    }
}
