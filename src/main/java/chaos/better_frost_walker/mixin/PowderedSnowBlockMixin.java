package chaos.better_frost_walker.mixin;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static chaos.better_frost_walker.BetterFrostWalkerMain.hasFrostWalker;

@Mixin(PowderSnowBlock.class)
public abstract class PowderedSnowBlockMixin {

    @ModifyReturnValue(
            method = "canWalkOnPowderSnow",
            at = @At("RETURN")
    )
    private static boolean standOnPowderedSnowWithFrostWalker(boolean original, Entity entity) {
        if (!BetterFrostWalkerMain.CONFIG.standingOnPowderedSnow) return original;
        if (!((Object) entity instanceof LivingEntity livingEntity)) return original;
        if (!hasFrostWalker(livingEntity, livingEntity.getWorld())) return original;

        return true;
    }
}
