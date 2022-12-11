package me.basiqueevangelist.spiritwalker.mixin.client;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import me.basiqueevangelist.spiritwalker.network.StopSpiritWalkC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Entity cameraEntity;

    @Shadow @Nullable public HitResult crosshairTarget;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"), cancellable = true)
    private void mald(CallbackInfoReturnable<Boolean> cir) {
        if (cameraEntity instanceof FakeCameraEntity) {
            if (crosshairTarget instanceof EntityHitResult ehr
             && ehr.getEntity() == player
             && SpiritWalker.CONFIG.allowReturningImmediately()) {
                SpiritWalkerNetworking.CHANNEL.clientHandle().send(new StopSpiritWalkC2SPacket());
            }

            cir.setReturnValue(true);
        }
    }

    @Inject(method = {"doItemUse", "doItemPick"}, at = @At("HEAD"), cancellable = true)
    private void mald(CallbackInfo ci) {
        if (cameraEntity instanceof FakeCameraEntity) {
            ci.cancel();
        }
    }

    @Inject(method = "handleBlockBreaking", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;attackCooldown:I", opcode = Opcodes.GETFIELD), cancellable = true)
    private void mald(boolean breaking, CallbackInfo ci) {
        if (cameraEntity instanceof FakeCameraEntity) {
            ci.cancel();
        }
    }
}
