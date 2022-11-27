package me.basiqueevangelist.potionofdisassociation.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.potionofdisassociation.client.FakeCameraEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @SuppressWarnings("ConstantConditions")
    @ModifyExpressionValue(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;isFlyingLocked()Z"))
    private boolean lockFlyingForCamera(boolean orig) {
        if ((Object) this instanceof FakeCameraEntity)
            return true;

        return orig;
    }
}
