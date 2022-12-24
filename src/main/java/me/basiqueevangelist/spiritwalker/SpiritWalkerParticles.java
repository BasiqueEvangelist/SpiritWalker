package me.basiqueevangelist.spiritwalker;

import io.wispforest.owo.particles.ClientParticles;
import io.wispforest.owo.particles.systems.ParticleSystem;
import io.wispforest.owo.particles.systems.ParticleSystemController;
import net.minecraft.fluid.Fluids;

public class SpiritWalkerParticles {
    public static final ParticleSystemController CONTROLLER
        = new ParticleSystemController(SpiritWalker.id("particles"));

    public static final ParticleSystem<Void> DRIP_WATER = CONTROLLER.register(Void.class, (world, pos, data) -> {
        ClientParticles.setParticleCount(1);
        ClientParticles.spawn(Fluids.WATER.getDefaultState().getParticle(), world, pos, 0.5);
    });

    public static void init() {

    }
}
