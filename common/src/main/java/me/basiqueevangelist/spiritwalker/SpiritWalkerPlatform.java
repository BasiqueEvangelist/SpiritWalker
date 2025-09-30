package me.basiqueevangelist.spiritwalker;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface SpiritWalkerPlatform {
    SpiritWalkerPlatform I = ServiceLoader.load(SpiritWalkerPlatform.class).findFirst().get();

    <T> Supplier<Holder.Reference<T>> register(ResourceKey<? extends Registry<?>> registryKey, ResourceLocation id, Supplier<T> supplier);

    boolean isModLoaded(String id);

    boolean isOnClient();
}