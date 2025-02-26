package me.basiqueevangelist.spiritwalker.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "hasItemRecipe", at = @At("HEAD"), cancellable = true)
    private void mald(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return;

        var potion = input.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().orElse(null);

        if (potion == null) return;

        if (SpiritWalker.POTIONS.contains((RegistryEntry.Reference<Potion>) potion))
            cir.setReturnValue(false);
    }

    @ModifyExpressionValue(method = "craft", at = @At(value = "FIELD", target = "Lnet/minecraft/recipe/BrewingRecipeRegistry;itemRecipes:Ljava/util/List;"))
    private List<?> mald(List<?> original, ItemStack ingredient, ItemStack input) {
        var potion = input.getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT).potion().orElse(null);

        if (potion == null) return Collections.emptyList();

        if (SpiritWalker.CONFIG.disableSplashAndLingering()
         && SpiritWalker.POTIONS.contains((RegistryEntry.Reference<Potion>) potion))
            return Collections.emptyList();

        return original;
    }
}
