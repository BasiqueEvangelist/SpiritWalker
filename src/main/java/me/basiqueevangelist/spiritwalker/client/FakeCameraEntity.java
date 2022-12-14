package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.stat.StatHandler;
import org.jetbrains.annotations.Nullable;

public class FakeCameraEntity extends ClientPlayerEntity {
    public boolean canNoClip = false;

    public FakeCameraEntity(MinecraftClient client, ClientWorld world) {
        super(client, world, new FakeClientPlayNetworkHandler(client), new StatHandler(), new ClientRecipeBook(), false, false);

        getAbilities().flying = true;
        getAbilities().allowFlying = true;
        setInvisible(true);
    }

    @Override
    public boolean isInvisibleTo(PlayerEntity player) {
        return player != client.player;
    }

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible();
    }

    @Nullable
    @Override
    public AbstractTeam getScoreboardTeam() {
        return null;
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
