package chaos.frost.mixin;

import chaos.frost.access.ReplaceDiskEnchantmentEffectAccess;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.effect.entity.ReplaceDiskEnchantmentEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ReplaceDiskEnchantmentEffect.class)
public abstract class ReplaceDiskEnchantmentEffectMixin implements ReplaceDiskEnchantmentEffectAccess {

    @Unique
    private boolean betterfrostwalker$isFrostWalker = false;

    @Override
    public void betterfrostwalker$setIsFrostWalker() {
        betterfrostwalker$isFrostWalker = true;
    }

    @WrapOperation(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            )
    )
    private boolean betterfrostwalker$dropReplacedItemsWhenIsFrostWalker(ServerWorld world, BlockPos currentBlockPos, BlockState newBlockState, Operation<Boolean> original) {
        // Don't do anything if this isn't for frost walker
        if (!betterfrostwalker$isFrostWalker) return original.call(world, currentBlockPos, newBlockState);


        final BlockState originalBlockState = world.getBlockState(currentBlockPos);

        // Original operation replaces the block if possible.
        if (!original.call(world, currentBlockPos, newBlockState)) return false;

        Block.dropStacks(originalBlockState, world, currentBlockPos);
        return true;
    }
}
