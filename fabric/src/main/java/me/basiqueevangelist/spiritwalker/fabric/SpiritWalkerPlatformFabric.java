package me.basiqueevangelist.spiritwalker.fabric;


import me.basiqueevangelist.spiritwalker.SpiritWalkerPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SpiritWalkerPlatformFabric implements SpiritWalkerPlatform {
    @SuppressWarnings("unchecked")
    @Override
    public <T> Supplier<Holder.Reference<T>> register(ResourceKey<? extends Registry<?>> registryKey, ResourceLocation id, Supplier<T> supplier) {
        T instance = supplier.get();

        var holder = Registry.registerForHolder((Registry<? super T>) BuiltInRegistries.REGISTRY.get(registryKey.location()), id, instance);

        return () -> (Holder.Reference<T>) holder;
    }

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public boolean isOnClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}