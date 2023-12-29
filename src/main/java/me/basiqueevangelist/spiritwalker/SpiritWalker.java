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
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.List;

public class SpiritWalker implements ModInitializer {
    public static final String MOD_ID = "spirit-walker";
    public static final SpiritWalkerConfig CONFIG = SpiritWalkerConfig.createAndLoad();

    public static final CustomStatusEffect EFFECT
        = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xA020F0);
    public static final Potion POTION = new Potion(
        new StatusEffectInstance(EFFECT, 20 * CONFIG.normalPotionLength()));
    public static final Potion STRONG_POTION = new Potion("spirit_walk",
        new StatusEffectInstance(EFFECT, 20 * CONFIG.strongPotionLength(), 1));
    public static final Potion LONG_POTION = new Potion("spirit_walk",
        new StatusEffectInstance(EFFECT, 20 * CONFIG.longPotionLength()));
    public final static List<Potion> POTIONS = List.of(
        POTION,
        LONG_POTION,
        STRONG_POTION
    );

    public static final DiedDuringSpiritWalkCriterion DIED_DURING_SPIRIT_WALK = new DiedDuringSpiritWalkCriterion();

    @Override
    public void onInitialize() {
        Registry.register(Registries.STATUS_EFFECT, id("spirit_walk"), EFFECT);
        Registry.register(Registries.POTION, id("spirit_walk"), POTION);
        Registry.register(Registries.POTION, id("strong_spirit_walk"), STRONG_POTION);
        Registry.register(Registries.POTION, id("long_spirit_walk"), LONG_POTION);

        Criteria.register(DIED_DURING_SPIRIT_WALK);

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return;
            if (!(player.hasStatusEffect(EFFECT))) return;

            DIED_DURING_SPIRIT_WALK.trigger(player);
        });

        SpiritWalkerNetworking.init();

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (isInSpiritWalk(player))
                return ActionResult.FAIL;

            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (isInSpiritWalk(player))
                return ActionResult.FAIL;

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (isInSpiritWalk(player))
                return TypedActionResult.fail(stack);

            return TypedActionResult.pass(stack);
        });

        if (CONFIG.enableDefaultRecipe()) {
            BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.WARPED_FUNGUS, POTION);
            BrewingRecipeRegistry.registerPotionRecipe(POTION, Items.REDSTONE, LONG_POTION);
            BrewingRecipeRegistry.registerPotionRecipe(POTION, Items.GLOWSTONE_DUST, STRONG_POTION);
        }
    }

    public static boolean isInSpiritWalk(PlayerEntity player) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            return SpiritWalkerClient.isInSpiritWalk(player);

        // TODO: make this still be true while the player is returning to their body.
        return player.hasStatusEffect(EFFECT);
    }

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
