package me.basiqueevangelist.spiritwalker.duck;

import net.minecraft.item.ItemStack;

public interface LivingEntityAccess {
    void callSpawnItemParticles(ItemStack stack, int count);

    void spiritWalker$setSaveFromFall();
}
