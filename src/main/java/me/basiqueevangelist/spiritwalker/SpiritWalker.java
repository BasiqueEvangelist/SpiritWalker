package me.basiqueevangelist.spiritwalker;

import me.basiqueevangelist.spiritwalker.config.SpiritWalkerConfig;
import me.basiqueevangelist.spiritwalker.item.EmptyVaseItem;
import me.basiqueevangelist.spiritwalker.network.SpiritWalkerNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;

public class SpiritWalker implements ModInitializer {
    public static final SpiritWalkerConfig CONFIG = SpiritWalkerConfig.createAndLoad();

    public static final CustomStatusEffect EFFECT
        = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xA020F0);
    public static final Potion POTION = new Potion(
        new StatusEffectInstance(EFFECT, 20 * CONFIG.normalPotionLength()));
    public static final Potion STRONG_POTION = new Potion("spirit_walk",
        new StatusEffectInstance(EFFECT, 20 * CONFIG.strongPotionLength(), 1));
    public static final Potion LONG_POTION = new Potion("spirit_walk",
        new StatusEffectInstance(EFFECT, 20 * CONFIG.longPotionLength()));
    public final static List<Potion> POTIONS = List.of(
        POTION,
        LONG_POTION,
        STRONG_POTION
    );

    public static final EmptyVaseItem EMPTY_VASE = new EmptyVaseItem();
    public static final Item FILLED_VASE = new Item(new FabricItemSettings().maxCount(1));

    public static TagKey<Item> BREAKS_ON_FALL = TagKey.of(RegistryKeys.ITEM, id("breaks_on_fall"));
    public static TagKey<Item> SAVES_FROM_FALL_DEATH = TagKey.of(RegistryKeys.ITEM, id("saves_from_fall_death"));

    @Override
    public void onInitialize() {
        Registry.register(Registries.STATUS_EFFECT, id("spirit_walk"), EFFECT);
        Registry.register(Registries.POTION, id("spirit_walk"), POTION);
        Registry.register(Registries.POTION, id("strong_spirit_walk"), STRONG_POTION);
        Registry.register(Registries.POTION, id("long_spirit_walk"), LONG_POTION);
        Registry.register(Registries.ITEM, id("empty_vase"), EMPTY_VASE);
        Registry.register(Registries.ITEM, id("filled_vase"), FILLED_VASE);

        SpiritWalkerNetworking.init();
        SpiritWalkerParticles.init();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(EMPTY_VASE);
            entries.add(FILLED_VASE);
        });

        if (CONFIG.enableDefaultRecipe()) {
            BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.WARPED_FUNGUS, POTION);
            BrewingRecipeRegistry.registerPotionRecipe(POTION, Items.REDSTONE, LONG_POTION);
            BrewingRecipeRegistry.registerPotionRecipe(POTION, Items.GLOWSTONE_DUST, STRONG_POTION);
        }
    }

    public static Identifier id(String path) {
        return new Identifier("spirit-walker", path);
    }
}
