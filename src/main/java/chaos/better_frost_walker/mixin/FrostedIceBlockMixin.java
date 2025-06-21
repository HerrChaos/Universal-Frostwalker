package chaos.better_frost_walker.mixin;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrostedIceBlock.class)
public abstract class FrostedIceBlockMixin {

    @WrapOperation(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;getLightLevel(Lnet/minecraft/util/math/BlockPos;)I"
            )
    )
    public int betterfrostwalker$meltIceInTheDarkIfEnabled(ServerWorld instance, BlockPos blockPos, Operation<Integer> original) {
        if (BetterFrostWalkerMain.CONFIG.meltIceInTheDark) return 15; // max light level, should always be higher than the other part of the condition

        return original.call(instance, blockPos);
    }
}
