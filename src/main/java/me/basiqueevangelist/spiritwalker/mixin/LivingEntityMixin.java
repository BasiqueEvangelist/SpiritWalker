package me.basiqueevangelist.spiritwalker.mixin;

import me.basiqueevangelist.spiritwalker.duck.LivingEntityAccess;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntityAccess {
    private boolean spiritwalker$saveFromFall;

    @Invoker
    @Override
    public abstract void callSpawnItemParticles(ItemStack stack, int count);

    @Override
    public void spiritWalker$setSaveFromFall() {
        spiritwalker$saveFromFall = true;
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void save(float health, CallbackInfo ci) {
        if (spiritwalker$saveFromFall) {
            spiritwalker$saveFromFall = false;
            ci.cancel();
        }
    }
}
