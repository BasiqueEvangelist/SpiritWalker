package me.basiqueevangelist.spiritwalker.mixin.client;

import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    private int spiritwalker$hotbarTime = 0;

    // Taken from https://github.com/maruohon/tweakeroo/blob/pre-rewrite/fabric/1.19.x/src/main/java/fi/dy/masa/tweakeroo/mixin/MixinInGameHud.java#L29-L37.
    @Inject(method = "getCameraPlayer", at = @At("HEAD"), cancellable = true)
    private void mald(CallbackInfoReturnable<PlayerEntity> cir) {
        if (client.getCameraEntity() instanceof FakeCameraEntity)
            cir.setReturnValue(client.player);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/util/math/MatrixStack;)V"))
    private void wrapRenderHotbar(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.getCameraEntity() instanceof FakeCameraEntity) {
            spiritwalker$hotbarTime++;
        } else {
            spiritwalker$hotbarTime--;
        }

        spiritwalker$hotbarTime = MathHelper.clamp(spiritwalker$hotbarTime, 0, 24);
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), index = 2)
    private int modifyY1(int y) {
        return y + spiritwalker$hotbarTime;
    }

    @ModifyArg(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/util/math/MatrixStack;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"), index = 2)
    private int modifyY2(int y) {
        return y + spiritwalker$hotbarTime;
    }
}
