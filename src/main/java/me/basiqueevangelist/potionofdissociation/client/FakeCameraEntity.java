package me.basiqueevangelist.potionofdissociation.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;

public class FakeCameraEntity extends ClientPlayerEntity {
    public FakeCameraEntity(MinecraftClient client, ClientWorld world) {
        super(client, world, new FakeClientPlayNetworkHandler(client), new StatHandler(), new ClientRecipeBook(), false, false);
        getAbilities().flying = true;
        getAbilities().allowFlying = true;
    }

    @Override
    public boolean isSpectator() {
        return true;
    }

    @Override
    public boolean isCreative() {
        return false;
    }
}
