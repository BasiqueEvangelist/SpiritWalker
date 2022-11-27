package me.basiqueevangelist.potionofdissociation.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.potionofdissociation.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundSystem.class)
public class SoundSystemMixin {
    @ModifyExpressionValue(method = "updateListenerPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getPos()Lnet/minecraft/util/math/Vec3d;"))
    private Vec3d replaceWithPlayer(Vec3d orig) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getCameraEntity() instanceof FakeCameraEntity)
            return client.player.getEyePos();

        return orig;
    }
}
