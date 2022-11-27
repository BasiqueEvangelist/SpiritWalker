package me.basiqueevangelist.potionofdissociation;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class DissociationStatusEffect extends StatusEffect {
    public DissociationStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xFFFF66);
    }
}
