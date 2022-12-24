package me.basiqueevangelist.spiritwalker.network;

import net.minecraft.item.ItemStack;

public record BreakItemS2CPacket(int entityId, ItemStack brokenStack) {
}
