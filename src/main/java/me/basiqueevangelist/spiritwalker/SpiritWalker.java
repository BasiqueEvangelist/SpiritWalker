package me.basiqueevangelist.spiritwalker;

import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import me.basiqueevangelist.spiritwalker.config.SpiritWalkerConfig;
import me.basiqueevangelist.spiritwalker.criteria.DiedDuringSpiritWalkCriterion;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

public class SpiritWalker implements ModInitializer {
    public static final String MOD_ID = "spirit-walker";
    public static final SpiritWalkerConfig CONFIG = SpiritWalkerConfig.createAndLoad();

    public static final Holder.Reference<MobEffect> EFFECT
        = Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            id("spirit_walk"),
            new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0xA020F0)
        );
    public static final Holder.Reference<Potion> POTION
        = Registry.registerForHolder(
            BuiltInRegistries.POTION,
            id("spirit_walk"),
            new Potion(new MobEffectInstance(EFFECT, 20 * CONFIG.normalPotionLength()))
        );
    public static final Holder.Reference<Potion> STRONG_POTION = Registry.registerForHolder(
        BuiltInRegistries.POTION,
        id("strong_spirit_walk"),
        new Potion("spirit_walk", new MobEffectInstance(EFFECT, 20 * CONFIG.strongPotionLength(), 1))
    );
    public static final Holder.Reference<Potion> LONG_POTION = Registry.registerForHolder(
        BuiltInRegistries.POTION,
        id("long_spirit_walk"),
        new Potion("spirit_walk", new MobEffectInstance(EFFECT, 20 * CONFIG.longPotionLength()))
    );
    public final static List<Holder.Reference<Potion>> POTIONS = List.of(
        POTION,
        LONG_POTION,
        STRONG_POTION
    );

    public static final DiedDuringSpiritWalkCriterion DIED_DURING_SPIRIT_WALK = new DiedDuringSpiritWalkCriterion();

    @Override
    public void onInitialize() {
        CriteriaTriggers.register(id("died_during_spirit_walk").toString(), DIED_DURING_SPIRIT_WALK);

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (!(entity instanceof ServerPlayer player)) return;
            if (!(player.hasEffect(EFFECT))) return;

            DIED_DURING_SPIRIT_WALK.trigger(player);
        });

        SpiritWalkerNetworking.init();

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (isInSpiritWalk(player))
                return InteractionResult.FAIL;

            return InteractionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (isInSpiritWalk(player))
                return InteractionResult.FAIL;

            return InteractionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getItemInHand(hand);

            if (isInSpiritWalk(player))
                return InteractionResultHolder.fail(stack);

            return InteractionResultHolder.pass(stack);
        });

        if (CONFIG.enableDefaultRecipe()) {
            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                builder.addMix(Potions.AWKWARD, Items.WARPED_FUNGUS, POTION);
                builder.addMix(POTION, Items.REDSTONE, LONG_POTION);
                builder.addMix(POTION, Items.GLOWSTONE_DUST, STRONG_POTION);
            });
        }
    }

    public static boolean isInSpiritWalk(Player player) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            return SpiritWalkerClient.isInSpiritWalk(player);

        // TODO: make this still be true while the player is returning to their body.
        return player.hasEffect(EFFECT);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
