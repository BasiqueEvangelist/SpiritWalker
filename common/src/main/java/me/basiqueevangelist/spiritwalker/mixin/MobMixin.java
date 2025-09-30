package me.basiqueevangelist.spiritwalker.mixin;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "isNoAi", at = @At("HEAD"), cancellable = true)
    private void disableAiDuringSpiritWalk(CallbackInfoReturnable<Boolean> cir) {
        if (hasEffect(SpiritWalker.EFFECT.get()))
            cir.setReturnValue(true);
    }
}
