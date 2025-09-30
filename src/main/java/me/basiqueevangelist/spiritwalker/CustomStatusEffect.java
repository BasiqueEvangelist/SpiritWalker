package me.basiqueevangelist.spiritwalker;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CustomStatusEffect extends MobEffect {
    public CustomStatusEffect(MobEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
}
