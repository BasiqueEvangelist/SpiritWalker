package me.basiqueevangelist.spiritwalker.item;

import eu.pb4.common.protection.api.CommonProtection;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EmptyVaseItem extends Item {
    public EmptyVaseItem() {
        super(new FabricItemSettings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        BlockHitResult res = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        if (res.getType() == HitResult.Type.MISS
         || res.getType() != HitResult.Type.BLOCK)
            return TypedActionResult.pass(stack);

        BlockPos blockPos = res.getBlockPos();

        if (!world.canPlayerModifyAt(user, blockPos)
         || !CommonProtection.canBreakBlock(world, blockPos, user.getGameProfile(), user))
            return TypedActionResult.pass(stack);

        if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);

            return TypedActionResult.success(new ItemStack(SpiritWalker.FILLED_VASE), world.isClient());
        }

        return TypedActionResult.pass(stack);
    }
}
