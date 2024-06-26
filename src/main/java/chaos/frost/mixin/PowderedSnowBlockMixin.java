package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static chaos.frost.NewFrostwalker.hasFrostWalker;

@Mixin(PowderSnowBlock.class)
public abstract class PowderedSnowBlockMixin {

    @ModifyReturnValue(
            method = "canWalkOnPowderSnow",
            at = @At("RETURN")
    )
    private static boolean standOnPowderedSnowWithFrostWalker(boolean original, Entity entity) {
        if (!NewFrostwalker.CONFIG.standingOnPowderedSnow) return original;
        if (!((Object) entity instanceof LivingEntity livingEntity)) return original;
        if (!hasFrostWalker(livingEntity, livingEntity.getWorld())) return original;

        return true;
    }
}
