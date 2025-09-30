package me.basiqueevangelist.spiritwalker.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    @ModifyExpressionValue(method = "updateSource", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getPosition()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 replaceWithPlayer(Vec3 orig) {
        Minecraft client = Minecraft.getInstance();
        if (client.getCameraEntity() instanceof FakeCameraEntity && SpiritWalker.CONFIG.listenFromBody())
            return client.player.getEyePosition();

        return orig;
    }
}
