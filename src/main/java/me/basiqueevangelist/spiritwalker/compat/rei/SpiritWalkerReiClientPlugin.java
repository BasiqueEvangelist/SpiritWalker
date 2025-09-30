package me.basiqueevangelist.spiritwalker.compat.rei;

import dev.architectury.event.EventResult;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

public class SpiritWalkerReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerVisibilityPredicate((category, display) -> {
            if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return EventResult.pass();
            if (!category.getCategoryIdentifier().equals(BuiltinPlugin.BREWING)) return EventResult.pass();

            if (!(registry.getDisplayOrigin(display) instanceof BrewingRecipe recipe))
                return EventResult.pass();

            if (SpiritWalker.POTIONS.contains((Holder.Reference<Potion>) recipe.output.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElse(Potions.WATER))
             && recipe.output.getItem() != Items.POTION)
                return EventResult.interruptFalse();

            return EventResult.pass();
        });
    }
}
