package me.basiqueevangelist.spiritwalker.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
    @Invoker
    void callRenderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta,
                          PoseStack matrices, MultiBufferSource vertexConsumers);
}
