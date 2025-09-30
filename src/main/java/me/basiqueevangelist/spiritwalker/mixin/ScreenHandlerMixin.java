package me.basiqueevangelist.spiritwalker.mixin;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public class ScreenHandlerMixin {
    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void blockOnSpiritWalk(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci) {
        if (SpiritWalker.isInSpiritWalk(player))
            ci.cancel();
    }
}
