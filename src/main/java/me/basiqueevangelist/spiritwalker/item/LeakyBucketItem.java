package me.basiqueevangelist.spiritwalker.item;

import me.basiqueevangelist.spiritwalker.SpiritWalkerParticles;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LeakyBucketItem extends Item {
    public LeakyBucketItem() {
        super(new FabricItemSettings()
            .group(ItemGroup.MISC)
            .maxCount(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity == null) return;
        if (world.isClient) return;

        if (world.random.nextInt(20) == 0) {
            SpiritWalkerParticles.DRIP_WATER.spawn(world, entity.getPos());
        }
    }
}
