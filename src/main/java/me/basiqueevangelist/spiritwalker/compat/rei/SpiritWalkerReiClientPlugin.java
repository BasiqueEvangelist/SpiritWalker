package me.basiqueevangelist.spiritwalker.compat.rei;

import dev.architectury.event.EventResult;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;

public class SpiritWalkerReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {

        registry.registerVisibilityPredicate((category, display) -> {
            if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return EventResult.pass();
            if (!category.getCategoryIdentifier().equals(BuiltinPlugin.BREWING)) return EventResult.pass();

            BrewingRecipe recipe = (BrewingRecipe) registry.getDisplayOrigin(display);
            if (SpiritWalker.POTIONS.contains(PotionUtil.getPotion(recipe.output))
             && recipe.output.getItem() != Items.POTION)
                return EventResult.interruptFalse();

            return EventResult.pass();
        });
    }
}
