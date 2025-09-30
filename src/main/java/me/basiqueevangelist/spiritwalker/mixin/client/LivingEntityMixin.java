package me.basiqueevangelist.spiritwalker.mixin.client;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    private void based(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        if (effect.getEffect() == SpiritWalker.EFFECT && level().isClientSide) {
            SpiritWalkerClient.enterSpiritWalk((LivingEntity)(Object) this, effect.getAmplifier());
        }
    }

    @Inject(method = "removeEffectNoUpdate", at = @At("HEAD"))
    private void based(Holder<MobEffect> effect, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (effect == SpiritWalker.EFFECT && level().isClientSide) {
            SpiritWalkerClient.leaveSpiritWalk((LivingEntity)(Object) this);
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("HEAD"))
    private void based(MobEffectInstance effect, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        if (effect.getEffect() == SpiritWalker.EFFECT && level().isClientSide) {
            SpiritWalkerClient.enterSpiritWalk((LivingEntity)(Object) this, effect.getAmplifier());
        }
    }
}
