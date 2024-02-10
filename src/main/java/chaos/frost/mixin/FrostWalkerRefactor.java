package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import chaos.frost.block.ModBlocks;
import chaos.frost.block.custom.frostedMagma;
import net.minecraft.block.*;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrostWalkerEnchantment.class)
public class FrostWalkerRefactor {

	@Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
	private void onGetMaxLevel(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(NewFrostwalker.CONFIG.MaxLevel());
	}

	@Inject(method = "freezeWater", at = @At("HEAD"), cancellable = true)
	private static void init(LivingEntity entity, World world, BlockPos blockPos, int level, CallbackInfo info) {
        BlockState FrostedIceState = Blocks.FROSTED_ICE.getDefaultState();
        BlockState FrostedMagmaState = ModBlocks.FROSTED_MAGMA.getDefaultState();
        int radius = Math.min(100, 2 + level);
        BlockPos startingPos = blockPos.add(0, -1, 0);
        if (!entity.isSpectator()) {
            for (BlockPos currentPos : BlockPos.iterate(startingPos.add(-radius, -radius, -radius), startingPos.add(radius, radius, radius))) {
                BlockPos abovePos = currentPos.up();
                if (shouldPlaceWaterBlock(startingPos, currentPos, radius) && world.isAir(abovePos) && currentPos.getY() < entity.getBlockPos().getY() + 1) {
                    Block currantBlock = world.getBlockState(currentPos).getBlock();
                    if ((world.getBlockState(currentPos) == frostedMagma.getMeltedState() || currantBlock == ModBlocks.FROSTED_MAGMA) && level >= NewFrostwalker.CONFIG.MaxLevel()) {
                        if (!FrostedMagmaState.canPlaceAt(world, currentPos) || !world.canPlace(FrostedMagmaState, currentPos, ShapeContext.absent())) continue;
                        world.setBlockState(currentPos, FrostedMagmaState);
                        world.scheduleBlockTick(currentPos, ModBlocks.FROSTED_MAGMA, MathHelper.nextInt(entity.getRandom(), 60, 120));
                    }

                    if (world.getBlockState(currentPos) == FrostedIceBlock.getMeltedState() || currantBlock == Blocks.FROSTED_ICE || (world.getBlockState(currentPos).getFluidState().isIn(FluidTags.WATER)
                            && (currantBlock == Blocks.KELP || currantBlock == Blocks.SEAGRASS))) {
                        if (!FrostedIceState.canPlaceAt(world, currentPos) || !world.canPlace(FrostedIceState, currentPos, ShapeContext.absent())) continue;
                        world.setBlockState(currentPos, FrostedIceState);
                        world.scheduleBlockTick(currentPos, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getRandom(), 60, 120));
                    }
                }
            }
        }
        info.cancel();
    }

    @Unique
    private static boolean shouldPlaceWaterBlock(BlockPos centerPos, BlockPos currentPos, int radius) {
        int dx = currentPos.getX() - centerPos.getX();
        int dy = currentPos.getY() - centerPos.getY();
        int dz = currentPos.getZ() - centerPos.getZ();
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }
}