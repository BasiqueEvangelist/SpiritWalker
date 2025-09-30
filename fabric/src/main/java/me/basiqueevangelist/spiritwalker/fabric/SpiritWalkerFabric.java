package me.basiqueevangelist.spiritwalker.fabric;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class SpiritWalkerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SpiritWalker.init();

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (!(entity instanceof ServerPlayer player)) return;
            if (!(player.hasEffect(SpiritWalker.EFFECT.get()))) return;

            SpiritWalker.DIED_DURING_SPIRIT_WALK.trigger(player);
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (SpiritWalker.isInSpiritWalk(player))
                return InteractionResult.FAIL;

            return InteractionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (SpiritWalker.isInSpiritWalk(player))
                return InteractionResult.FAIL;

            return InteractionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getItemInHand(hand);

            if (SpiritWalker.isInSpiritWalk(player))
                return InteractionResultHolder.fail(stack);

            return InteractionResultHolder.pass(stack);
        });

        if (SpiritWalker.CONFIG.enableDefaultRecipe()) {
            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                builder.addMix(Potions.AWKWARD, Items.WARPED_FUNGUS, SpiritWalker.POTION.get());
                builder.addMix(SpiritWalker.POTION.get(), Items.REDSTONE, SpiritWalker.LONG_POTION.get());
                builder.addMix(SpiritWalker.POTION.get(), Items.GLOWSTONE_DUST, SpiritWalker.STRONG_POTION.get());
            });
        }
    }
}
