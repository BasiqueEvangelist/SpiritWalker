package me.basiqueevangelist.potionofdisassociation.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.potionofdisassociation.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;getFocusedEntity()Lnet/minecraft/entity/Entity;", ordinal = 3))
    private Entity makePlayerRender(Entity old) {
        if (client.getCameraEntity() instanceof FakeCameraEntity)
            return client.player;

        return old;
    }
}