package me.basiqueevangelist.spiritwalker.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyExpressionValue(method = "updateMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;player:Lnet/minecraft/client/network/ClientPlayerEntity;"))
    private ClientPlayerEntity moveCamera(ClientPlayerEntity orig) {
        if (client.getCameraEntity() instanceof FakeCameraEntity camera)
            return camera;

        return orig;
    }

//    @WrapWithCondition(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"))
//    private boolean mald(PlayerInventory instance, double scrollAmount) {
//        return !(client.getCameraEntity() instanceof FakeCameraEntity);
//    }
}
