package me.basiqueevangelist.potionofdisassociation;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionOfDisassociation implements ModInitializer {
    public static final DisassociationStatusEffect EFFECT = new DisassociationStatusEffect();
    public static final Potion POTION = new Potion(new StatusEffectInstance(EFFECT, 20 * 60 * 2));

    @Override
    public void onInitialize() {
        Registry.register(Registry.STATUS_EFFECT, id("disassociation"), EFFECT);
        Registry.register(Registry.POTION, id("disassociation"), POTION);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.RED_MUSHROOM, POTION);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.BROWN_MUSHROOM, POTION);
    }

    private Identifier id(String path) {
        return new Identifier("potion-of-disassociation", path);
    }
}
