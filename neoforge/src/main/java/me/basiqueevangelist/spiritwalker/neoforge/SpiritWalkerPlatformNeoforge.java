package me.basiqueevangelist.spiritwalker.neoforge;

import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import me.basiqueevangelist.spiritwalker.SpiritWalker;
import me.basiqueevangelist.spiritwalker.SpiritWalkerPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = SpiritWalkerNeoforge.MOD_ID)
public class SpiritWalkerPlatformNeoforge implements SpiritWalkerPlatform {
    private static final Multimap<ResourceKey<? extends Registry<?>>, Pair<ResourceLocation, Supplier<Object>>> TO_REGISTER = HashMultimap.create();

    @SuppressWarnings("unchecked")
    @Override
    public <T> Supplier<Holder.Reference<T>> register(ResourceKey<? extends Registry<?>> registryKey, ResourceLocation id, Supplier<T> supplier) {
        Supplier<Object> memoized = new Supplier<>() {
            private T value = null;

            @Override
            public T get() {
                if (value == null) {
                    value = supplier.get();
                }

                return value;
            }
        };

        TO_REGISTER.put(registryKey, new Pair<>(id, memoized));

        return Suppliers.memoize(() -> (Holder.Reference<T>) BuiltInRegistries.REGISTRY.get(registryKey.location()).getHolder(id).orElseThrow());
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void onRegister(RegisterEvent event) {
        var toRegister = TO_REGISTER.get(event.getRegistryKey());
        if (toRegister.isEmpty()) return;

        for (var element : toRegister) {
            event.register((ResourceKey<? extends Registry<Object>>) event.getRegistryKey(), element.getFirst(), element.getSecond());
        }
    }

    @Override
    public boolean isModLoaded(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public boolean isOnClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }
}