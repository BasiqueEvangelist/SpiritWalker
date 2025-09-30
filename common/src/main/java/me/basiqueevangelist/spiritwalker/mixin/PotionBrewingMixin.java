package me.basiqueevangelist.spiritwalker.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
    @Inject(method = "hasContainerMix", at = @At("HEAD"), cancellable = true)
    private void mald(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
        if (!SpiritWalker.CONFIG.disableSplashAndLingering()) return;

        var potion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElse(null);

        if (potion == null) return;

        if (SpiritWalker.POTIONS.get().contains((Holder.Reference<Potion>) potion))
            cir.setReturnValue(false);
    }

    @ModifyExpressionValue(method = "mix", at = @At(value = "FIELD", target = "Lnet/minecraft/world/item/alchemy/PotionBrewing;containerMixes:Ljava/util/List;"))
    private List<?> mald(List<?> original, ItemStack ingredient, ItemStack input) {
        var potion = input.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).potion().orElse(null);

        if (potion == null) return Collections.emptyList();

        if (SpiritWalker.CONFIG.disableSplashAndLingering()
         && SpiritWalker.POTIONS.get().contains((Holder.Reference<Potion>) potion))
            return Collections.emptyList();

        return original;
    }
}
