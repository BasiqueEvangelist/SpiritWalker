package me.basiqueevangelist.potionofdissociation;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionOfDissociation implements ModInitializer {
    public static final DissociationStatusEffect EFFECT = new DissociationStatusEffect();
    public static final Potion POTION = new Potion(new StatusEffectInstance(EFFECT, 20 * 60 * 2));

    @Override
    public void onInitialize() {
        Registry.register(Registry.STATUS_EFFECT, id("dissociation"), EFFECT);
        Registry.register(Registry.POTION, id("dissociation"), POTION);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.RED_MUSHROOM, POTION);
        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.BROWN_MUSHROOM, POTION);
    }

    private Identifier id(String path) {
        return new Identifier("potion-of-dissociation", path);
    }
}
