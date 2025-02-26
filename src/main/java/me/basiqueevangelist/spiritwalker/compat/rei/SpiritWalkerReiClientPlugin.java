package me.basiqueevangelist.spiritwalker.compat.rei;

import dev.architectury.event.EventResult;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.brewing.BrewingRecipe;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.entry.RegistryEntry;

public class SpiritWalkerReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerVisibilityPredicate((category, display) -> {
            if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return EventResult.pass();
            if (!category.getCategoryIdentifier().equals(BuiltinPlugin.BREWING)) return EventResult.pass();

            if (!(registry.getDisplayOrigin(display) instanceof BrewingRecipe recipe))
                return EventResult.pass();

            if (SpiritWalker.POTIONS.contains((RegistryEntry.Reference<Potion>) recipe.output.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().orElse(Potions.WATER))
             && recipe.output.getItem() != Items.POTION)
                return EventResult.interruptFalse();

            return EventResult.pass();
        });
    }
}
