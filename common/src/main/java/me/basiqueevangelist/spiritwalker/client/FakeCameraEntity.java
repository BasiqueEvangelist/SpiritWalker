package me.basiqueevangelist.spiritwalker.client;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;

public class FakeCameraEntity extends LocalPlayer {
    public boolean canNoClip = false;

    public FakeCameraEntity(Minecraft client, ClientLevel world) {
        super(client, world, new FakeClientPlayNetworkHandler(client), new StatsCounter(), new ClientRecipeBook(), false, false);

        getAbilities().flying = true;
        getAbilities().mayfly = true;
        setInvisible(true);
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        return player != minecraft.player;
    }

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible();
    }

    @Nullable
    @Override
    public PlayerTeam getTeam() {
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
