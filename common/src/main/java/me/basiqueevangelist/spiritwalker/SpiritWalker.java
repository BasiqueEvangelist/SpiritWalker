package me.basiqueevangelist.spiritwalker;

import com.google.common.base.Suppliers;
import me.basiqueevangelist.spiritwalker.client.SpiritWalkerClient;
import me.basiqueevangelist.spiritwalker.config.SpiritWalkerConfig;
import me.basiqueevangelist.spiritwalker.criteria.DiedDuringSpiritWalkCriterion;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;
import java.util.function.Supplier;

public class SpiritWalker {
    public static final String MOD_ID = "spirit-walker";
    public static final SpiritWalkerConfig CONFIG = SpiritWalkerConfig.createAndLoad();

    public static final Supplier<Holder.Reference<MobEffect>> EFFECT
        = SpiritWalkerPlatform.I.register(
            Registries.MOB_EFFECT,
            id("spirit_walk"),
            () -> new CustomStatusEffect(MobEffectCategory.BENEFICIAL, 0xA020F0)
        );
    public static final Supplier<Holder.Reference<Potion>> POTION
        = SpiritWalkerPlatform.I.register(
            Registries.POTION,
            id("spirit_walk"),
            () -> new Potion(new MobEffectInstance(EFFECT.get(), 20 * CONFIG.normalPotionLength()))
        );
    public static final Supplier<Holder.Reference<Potion>> STRONG_POTION = SpiritWalkerPlatform.I.register(
        Registries.POTION,
        id("strong_spirit_walk"),
        () -> new Potion("spirit_walk", new MobEffectInstance(EFFECT.get(), 20 * CONFIG.strongPotionLength(), 1))
    );
    public static final Supplier<Holder.Reference<Potion>> LONG_POTION = SpiritWalkerPlatform.I.register(
        Registries.POTION,
        id("long_spirit_walk"),
        () -> new Potion("spirit_walk", new MobEffectInstance(EFFECT.get(), 20 * CONFIG.longPotionLength()))
    );
    public final static Supplier<List<Holder.Reference<Potion>>> POTIONS = Suppliers.memoize(() -> List.of(
        POTION.get(),
        LONG_POTION.get(),
        STRONG_POTION.get()
    ));

    public static final DiedDuringSpiritWalkCriterion DIED_DURING_SPIRIT_WALK = new DiedDuringSpiritWalkCriterion();

    public static void init() {
        SpiritWalkerPlatform.I.register(
            Registries.TRIGGER_TYPE,
            id("died_during_spirit_walk"),
            () -> DIED_DURING_SPIRIT_WALK
        );

        SpiritWalkerNetworking.init();
    }

    public static boolean isInSpiritWalk(Player player) {
        if (SpiritWalkerPlatform.I.isOnClient())
            return SpiritWalkerClient.isInSpiritWalk(player);

        // TODO: make this still be true while the player is returning to their body.
        return player.hasEffect(EFFECT.get());
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
