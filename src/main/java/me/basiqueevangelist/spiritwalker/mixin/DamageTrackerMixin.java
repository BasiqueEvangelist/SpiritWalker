package me.basiqueevangelist.spiritwalker.mixin;

import me.basiqueevangelist.spiritwalker.SpiritWalkerParticles;
import me.basiqueevangelist.spiritwalker.item.LeakyBucketItem;
import me.basiqueevangelist.spiritwalker.network.BreakItemS2CPacket;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
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
        if (damageSource.isFromFalling()
            && entity.getHealth() <= 0
            && entity instanceof ServerPlayerEntity player) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);

                if (stack.getItem() instanceof LeakyBucketItem) {
                    entity.setHealth(originalHealth);
                    var targets = new ArrayList<>(PlayerLookup.tracking(player));
                    targets.add(player);
                    SpiritWalkerNetworking.CHANNEL.serverHandle(targets)
                        .send(new BreakItemS2CPacket(player.getId(), stack));
                    SpiritWalkerParticles.BIG_SPLASH.spawn(player.world, player.getPos());
                    stack.decrement(1);

                    if (player.world.canPlayerModifyAt(player, player.getBlockPos())) {
                        BlockPos pos = entity.getBlockPos();
                        BlockState state = entity.world.getBlockState(entity.getBlockPos());
                        if (state.isAir()) {
                            entity.world.setBlockState(entity.getBlockPos(), Blocks.WATER.getDefaultState());
                        } else if (state instanceof FluidFillable fillable
                            && fillable.canFillWithFluid(entity.world, pos, state, Fluids.WATER)) {
                            fillable.tryFillWithFluid(entity.world, pos, state, Fluids.WATER.getStill(false));
                        }
                    }

                    ci.cancel();
                    return;
                }
            }
        }
    }
}
