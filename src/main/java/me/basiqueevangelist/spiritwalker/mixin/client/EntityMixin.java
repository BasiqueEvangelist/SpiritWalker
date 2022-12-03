package me.basiqueevangelist.spiritwalker.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.basiqueevangelist.spiritwalker.client.FakeCameraEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @SuppressWarnings("ConstantConditions")
    @ModifyExpressionValue(method = "pushAwayFrom", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;noClip:Z"))
    private boolean mald(boolean orig, Entity other) {
        if (other instanceof FakeCameraEntity || (Object) this instanceof FakeCameraEntity) {
            return true;
        }

        return orig;
    }
}
