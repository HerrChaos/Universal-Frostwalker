package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import chaos.frost.block.FrostedMagmaBlock;
import chaos.frost.block.ModBlocks;
import chaos.frost.tag.ModBlockTags;
import net.minecraft.block.*;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrostWalkerEnchantment.class)
public abstract class FrostWalkerRefactor {

	@Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
	private void onGetMaxLevel(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(NewFrostwalker.CONFIG.MaxLevel());
	}

	@Inject(method = "freezeWater", at = @At("HEAD"), cancellable = true)
	private static void init(LivingEntity user, World world, BlockPos startingPos, int level, CallbackInfo info) {
        final BlockState frostedIceState = Blocks.FROSTED_ICE.getDefaultState();
        final BlockState frostedMagmaState = ModBlocks.FROSTED_MAGMA.getDefaultState();

        int radius = Math.min(255, 2 + level);

        if (user.isSpectator()) return;

        for (BlockPos currentPos : BlockPos.iterate(startingPos.add(-radius, -2, -radius), startingPos.add(radius, 0, radius))) {
            BlockPos abovePos = currentPos.up();

            if (!(shouldPlaceFrostedBlock(startingPos, currentPos, radius) && world.isAir(abovePos)))
                continue;


            final BlockState currentBlockState = world.getBlockState(currentPos);
            final Block currentBlock = currentBlockState.getBlock();

            if ((world.getBlockState(currentPos) == FrostedMagmaBlock.getMeltedState() || currentBlock == ModBlocks.FROSTED_MAGMA) && level >= NewFrostwalker.CONFIG.MaxLevel()) {
                if (!frostedMagmaState.canPlaceAt(world, currentPos) || !world.canPlace(frostedMagmaState, currentPos, ShapeContext.absent()))
                    continue;
                world.setBlockState(currentPos, frostedMagmaState);
                world.scheduleBlockTick(currentPos, ModBlocks.FROSTED_MAGMA, MathHelper.nextInt(user.getRandom(), 60, 120));
            }

            boolean shouldDropItem = false;
            if (world.getBlockState(currentPos) == FrostedIceBlock.getMeltedState() || currentBlock == Blocks.FROSTED_ICE ||
                    (shouldDropItem = (currentBlockState.getFluidState().isIn(FluidTags.WATER) && currentBlockState.isIn(ModBlockTags.REPLACED_BY_FROST_WALKER)))) {
                if (!frostedIceState.canPlaceAt(world, currentPos) || !world.canPlace(frostedIceState, currentPos, ShapeContext.absent()))
                    continue;
                world.setBlockState(currentPos, frostedIceState);
                world.scheduleBlockTick(currentPos, Blocks.FROSTED_ICE, MathHelper.nextInt(user.getRandom(), 60, 120));

                if (!shouldDropItem) continue;

                Block.dropStacks(currentBlockState, world, currentPos, world.getBlockEntity(currentPos));
            }
        }

        info.cancel();
    }

    @Unique
    private static boolean shouldPlaceFrostedBlock(BlockPos centerPos, BlockPos currentPos, int radius) {
        return centerPos.isWithinDistance(currentPos, radius);
    }
}