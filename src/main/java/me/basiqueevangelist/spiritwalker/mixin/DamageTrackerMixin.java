package me.basiqueevangelist.spiritwalker.mixin;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.duck.LivingEntityAccess;
import me.basiqueevangelist.spiritwalker.network.BreakItemS2CPacket;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(DamageTracker.class)
public class DamageTrackerMixin {
    @Shadow @Final private LivingEntity entity;

    @Inject(method = "onDamage", at = @At("HEAD"), cancellable = true)
    private void tryUseLeakyBucket(DamageSource damageSource, float originalHealth, float damage, CallbackInfo ci) {
        if (damageSource.isIn(DamageTypeTags.IS_FALL)
            && originalHealth <= damage
            && entity instanceof ServerPlayerEntity player) {
            boolean saved = false;

            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);

                if (stack.isIn(SpiritWalker.SAVES_FROM_FALL_DEATH)) saved = true;

                if (stack.isIn(SpiritWalker.BREAKS_ON_FALL)) {
                    var targets = new ArrayList<>(PlayerLookup.tracking(player));
                    targets.add(player);
                    SpiritWalkerNetworking.CHANNEL.serverHandle(targets)
                        .send(new BreakItemS2CPacket(player.getId(), stack));
                    player.world.playSound(
                        null,
                        player.getBlockPos(),
                        SoundEvents.BLOCK_GLASS_BREAK,
                        SoundCategory.PLAYERS,
                        1.0f,
                        player.getRandom().nextFloat() * 0.1F + 0.9F
                    );
                    stack.setCount(0);
                }
            }

            if (saved) {
                ((LivingEntityAccess) player).spiritWalker$setSaveFromFall();
                ci.cancel();
            }
        }
    }
}
