package me.basiqueevangelist.spiritwalker.compat.rei;

import dev.architectury.event.EventResult;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;

public class SpiritWalkerReiClientPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<FillLeakyBucketDisplay> FILL_LEAKY_BUCKET = CategoryIdentifier.of(SpiritWalker.MOD_ID, "fill_leaky_bucket");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FillLeakyBucketCategory());
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.add(new FillLeakyBucketDisplay(
            EntryIngredients.of(SpiritWalker.EMPTY_LEAKY_BUCKET),
            EntryIngredients.of(Fluids.WATER),
            EntryIngredients.of(SpiritWalker.FILLED_LEAKY_BUCKET)
        ));

        registry.registerVisibilityPredicate((category, display) -> {
            if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return EventResult.pass();
            if (!category.getCategoryIdentifier().equals(BuiltinPlugin.BREWING)) return EventResult.pass();

            if (!(registry.getDisplayOrigin(display) instanceof BrewingRecipe recipe))
                return EventResult.pass();

            if (SpiritWalker.POTIONS.contains(PotionUtil.getPotion(recipe.output))
             && recipe.output.getItem() != Items.POTION)
                return EventResult.interruptFalse();

            return EventResult.pass();
        });
    }
}
