package me.basiqueevangelist.potionofdisassociation.mixin;

import me.basiqueevangelist.potionofdisassociation.PotionOfDisassociation;
import me.basiqueevangelist.potionofdisassociation.client.PotionOfDisassociationClient;
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
        if (effect.getEffectType() == PotionOfDisassociation.EFFECT && world.isClient) {
            PotionOfDisassociationClient.enterDisassociation((LivingEntity)(Object) this);
        }
    }

    @Inject(method = "removeStatusEffectInternal", at = @At("HEAD"))
    private void based(StatusEffect type, CallbackInfoReturnable<StatusEffectInstance> cir) {
        if (type == PotionOfDisassociation.EFFECT && world.isClient) {
            PotionOfDisassociationClient.leaveDisassociation((LivingEntity)(Object) this);
        }
    }
}
