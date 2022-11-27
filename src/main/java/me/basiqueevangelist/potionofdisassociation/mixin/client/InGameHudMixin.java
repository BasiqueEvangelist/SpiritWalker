package me.basiqueevangelist.potionofdisassociation.mixin.client;

import me.basiqueevangelist.potionofdisassociation.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "getCameraPlayer", at = @At("HEAD"), cancellable = true)
    private void mald(CallbackInfoReturnable<PlayerEntity> cir) {
        if (client.getCameraEntity() instanceof FakeCameraEntity)
            cir.setReturnValue(client.player);
    }
}
