package me.basiqueevangelist.spiritwalker.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import me.basiqueevangelist.spiritwalker.network.StopSpiritWalkC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Nullable public Entity cameraEntity;

    @Shadow @Nullable public HitResult hitResult;

    @Shadow @Nullable public LocalPlayer player;

    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/HitResult;getType()Lnet/minecraft/world/phys/HitResult$Type;"), cancellable = true)
    private void mald(CallbackInfoReturnable<Boolean> cir) {
        if (cameraEntity instanceof FakeCameraEntity) {
            if (hitResult instanceof EntityHitResult ehr
             && ehr.getEntity() == player
             && SpiritWalker.CONFIG.allowReturningImmediately()) {
                SpiritWalkerNetworking.CHANNEL.clientHandle().send(new StopSpiritWalkC2SPacket());
            }

            cir.setReturnValue(true);
        }
    }

    @Inject(method = {"startUseItem", "pickBlock"}, at = @At("HEAD"), cancellable = true)
    private void mald(CallbackInfo ci) {
        if (cameraEntity instanceof FakeCameraEntity) {
            ci.cancel();
        }
    }

    @Inject(method = "continueAttack", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;missTime:I", opcode = Opcodes.GETFIELD), cancellable = true)
    private void mald(boolean breaking, CallbackInfo ci) {
        if (cameraEntity instanceof FakeCameraEntity) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(
        method = "handleKeybinds",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"),
        slice = @Slice(
            from = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;keySwapOffhand:Lnet/minecraft/client/KeyMapping;"),
            to = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;keyChat:Lnet/minecraft/client/KeyMapping;")
        )
    )
    private boolean forceSpectator(boolean old) {
        return old || cameraEntity instanceof FakeCameraEntity;
    }
}
