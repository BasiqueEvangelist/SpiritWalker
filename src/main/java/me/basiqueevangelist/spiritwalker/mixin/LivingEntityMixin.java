package me.basiqueevangelist.spiritwalker.mixin;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
    private void based(StatusEffectInstance effect, Entity source, CallbackInfo ci) {
        if (effect.getEffectType() == SpiritWalker.EFFECT && world.isClient) {
            SpiritWalkerClient.enterSpiritWalk((LivingEntity)(Object) this, effect.getAmplifier());
        }
    }

    @Inject(method = "removeStatusEffectInternal", at = @At("HEAD"))
    private void based(StatusEffect type, CallbackInfoReturnable<StatusEffectInstance> cir) {
        if (type == SpiritWalker.EFFECT && world.isClient) {
            SpiritWalkerClient.leaveSpiritWalk((LivingEntity)(Object) this);
        }
    }

    @Inject(method = "onStatusEffectUpgraded", at = @At("HEAD"))
    private void based(StatusEffectInstance effect, boolean reapplyEffect, Entity source, CallbackInfo ci) {
        if (effect.getEffectType() == SpiritWalker.EFFECT && world.isClient) {
            SpiritWalkerClient.enterSpiritWalk((LivingEntity)(Object) this, effect.getAmplifier());
        }
    }
}
