package me.basiqueevangelist.spiritwalker.neoforge;

import me.basiqueevangelist.spiritwalker.SpiritWalker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.brewing.BrewingRecipeRegistry;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(SpiritWalkerNeoforge.MOD_ID)
@EventBusSubscriber(modid = SpiritWalkerNeoforge.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class SpiritWalkerNeoforge {
    // I F***ING HATE NEOFORGE
    public static final String MOD_ID = "spirit_walker";

    public SpiritWalkerNeoforge() {
        SpiritWalker.init();
    }

    @SubscribeEvent
    public static void afterDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.hasEffect(SpiritWalker.EFFECT.get()))) return;

        SpiritWalker.DIED_DURING_SPIRIT_WALK.trigger(player);
    }

    @SubscribeEvent
    public static void useEntity(PlayerInteractEvent.EntityInteract event) {
        if (SpiritWalker.isInSpiritWalk(event.getEntity()))
            event.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void useBlock(PlayerInteractEvent.RightClickBlock event) {
        if (SpiritWalker.isInSpiritWalk(event.getEntity()))
            event.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void useItem(PlayerInteractEvent.RightClickItem event) {
        if (SpiritWalker.isInSpiritWalk(event.getEntity()))
            event.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void addRecipes(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, Items.WARPED_FUNGUS, SpiritWalker.POTION.get());
        builder.addMix(SpiritWalker.POTION.get(), Items.REDSTONE, SpiritWalker.LONG_POTION.get());
        builder.addMix(SpiritWalker.POTION.get(), Items.GLOWSTONE_DUST, SpiritWalker.STRONG_POTION.get());
    }

}
