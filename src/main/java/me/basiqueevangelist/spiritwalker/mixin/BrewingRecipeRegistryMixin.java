package me.basiqueevangelist.spiritwalker.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {
    @Inject(method = "hasItemRecipe", at = @At("HEAD"), cancellable = true)
    private static void mald(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return;

        if (SpiritWalker.POTIONS.contains(PotionUtil.getPotion(input)))
            cir.setReturnValue(false);
    }

    @ModifyExpressionValue(method = "craft", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    private static int mald(int old, ItemStack ingredient, ItemStack input) {
        if (SpiritWalker.CONFIG.disableSplashAndLingering()
         && SpiritWalker.POTIONS.contains(PotionUtil.getPotion(input)))
            return 0;

        return old;
    }
}
