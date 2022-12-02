package me.basiqueevangelist.spiritwalker;

import me.basiqueevangelist.spiritwalker.config.SpiritWalkerConfig;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SpiritWalker implements ModInitializer {
    public static final SpiritWalkerConfig CONFIG = SpiritWalkerConfig.createAndLoad();

    public static final CustomStatusEffect EFFECT
        = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xA020F0);
    public static final Potion POTION = new Potion(
        new StatusEffectInstance(EFFECT, 20 * 60));
    public static final Potion STRONG_POTION = new Potion("spirit_walk",
        new StatusEffectInstance(EFFECT, 20 * 30, 1));
    public static final Potion LONG_POTION = new Potion("spirit_walk",
        new StatusEffectInstance(EFFECT, 20 * 60 * 5 / 2));
    @Override
    public void onInitialize() {
        Registry.register(Registry.STATUS_EFFECT, id("spirit_walk"), EFFECT);
        Registry.register(Registry.POTION, id("spirit_walk"), POTION);
        Registry.register(Registry.POTION, id("strong_spirit_walk"), STRONG_POTION);
        Registry.register(Registry.POTION, id("long_spirit_walk"), LONG_POTION);

        SpiritWalkerNetworking.init();

        if (CONFIG.enableDefaultRecipe()) {
            BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.RED_MUSHROOM, POTION);
            BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.BROWN_MUSHROOM, POTION);
            BrewingRecipeRegistry.registerPotionRecipe(POTION, Items.REDSTONE, LONG_POTION);
            BrewingRecipeRegistry.registerPotionRecipe(POTION, Items.GLOWSTONE_DUST, STRONG_POTION);
        }
    }

    public static Identifier id(String path) {
        return new Identifier("spirit-walker", path);
    }
}
