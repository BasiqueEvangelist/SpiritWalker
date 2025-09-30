package me.basiqueevangelist.spiritwalker.mixin.client;

import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final Minecraft minecraft;

    // Taken from https://github.com/maruohon/tweakeroo/blob/pre-rewrite/fabric/1.19.x/src/main/java/fi/dy/masa/tweakeroo/mixin/MixinGameRenderer.java#L145-L152.
    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void noHandRendering(Camera camera, float tickDelta, Matrix4f matrix4f, CallbackInfo ci) {
        if (minecraft.getCameraEntity() instanceof FakeCameraEntity) {
            ci.cancel();
        }
    }
}
