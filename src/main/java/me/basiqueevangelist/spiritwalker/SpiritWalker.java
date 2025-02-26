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
import net.fabricmc.fabric.mixin.content.registry.BrewingRecipeRegistryBuilderMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.effect.StatusEffect;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.List;

public class SpiritWalker implements ModInitializer {
    public static final String MOD_ID = "spirit-walker";
    public static final SpiritWalkerConfig CONFIG = SpiritWalkerConfig.createAndLoad();

    public static final RegistryEntry.Reference<StatusEffect> EFFECT
        = Registry.registerReference(
            Registries.STATUS_EFFECT,
            id("spirit_walk"),
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xA020F0)
        );
    public static final RegistryEntry.Reference<Potion> POTION
        = Registry.registerReference(
            Registries.POTION,
            id("spirit_walk"),
            new Potion(new StatusEffectInstance(EFFECT, 20 * CONFIG.normalPotionLength()))
        );
    public static final RegistryEntry.Reference<Potion> STRONG_POTION = Registry.registerReference(
        Registries.POTION,
        id("strong_spirit_walk"),
        new Potion("spirit_walk", new StatusEffectInstance(EFFECT, 20 * CONFIG.strongPotionLength(), 1))
    );
    public static final RegistryEntry.Reference<Potion> LONG_POTION = Registry.registerReference(
        Registries.POTION,
        id("long_spirit_walk"),
        new Potion("spirit_walk", new StatusEffectInstance(EFFECT, 20 * CONFIG.longPotionLength()))
    );
    public final static List<RegistryEntry.Reference<Potion>> POTIONS = List.of(
        POTION,
        LONG_POTION,
        STRONG_POTION
    );

    public static final DiedDuringSpiritWalkCriterion DIED_DURING_SPIRIT_WALK = new DiedDuringSpiritWalkCriterion();

    @Override
    public void onInitialize() {
        Criteria.register(id("died_during_spirit_walk").toString(), DIED_DURING_SPIRIT_WALK);

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
            FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
                builder.registerPotionRecipe(Potions.AWKWARD, Items.WARPED_FUNGUS, POTION);
                builder.registerPotionRecipe(POTION, Items.REDSTONE, LONG_POTION);
                builder.registerPotionRecipe(POTION, Items.GLOWSTONE_DUST, STRONG_POTION);
            });
        }
    }

    public static boolean isInSpiritWalk(PlayerEntity player) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            return SpiritWalkerClient.isInSpiritWalk(player);

        // TODO: make this still be true while the player is returning to their body.
        return player.hasStatusEffect(EFFECT);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
