package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import chaos.frost.block.FrostedMagmaBlock;
import chaos.frost.block.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.ItemEntity;
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
	private static void init(LivingEntity user, World world, BlockPos blockPos, int level, CallbackInfo info) {
        final BlockState frostedIceState = Blocks.FROSTED_ICE.getDefaultState();
        final BlockState frostedMagmaState = ModBlocks.FROSTED_MAGMA.getDefaultState();

        int radius = Math.min(100, 2 + level);
        BlockPos startingPos = blockPos.add(0, -1, 0);

        if (user.isSpectator()) return;

        // TODO: Do we really want to check blocks all the way 'radius' blocks above the player?
        //  Especially when we check if the position is below the player during the loop and ignore it if so
        for (BlockPos currentPos : BlockPos.iterate(startingPos.add(-radius, -radius, -radius), startingPos.add(radius, radius, radius))) {
            BlockPos abovePos = currentPos.up();

            if (!(shouldPlaceFrostedBlock(startingPos, currentPos, radius) && world.isAir(abovePos) && currentPos.getY() < user.getBlockPos().getY() + 1))
                continue;


            Block currentBlock = world.getBlockState(currentPos).getBlock();

            if ((world.getBlockState(currentPos) == FrostedMagmaBlock.getMeltedState() || currentBlock == ModBlocks.FROSTED_MAGMA) && level >= NewFrostwalker.CONFIG.MaxLevel()) {
                if (!frostedMagmaState.canPlaceAt(world, currentPos) || !world.canPlace(frostedMagmaState, currentPos, ShapeContext.absent()))
                    continue;
                world.setBlockState(currentPos, frostedMagmaState);
                world.scheduleBlockTick(currentPos, ModBlocks.FROSTED_MAGMA, MathHelper.nextInt(user.getRandom(), 60, 120));
            }

            // TODO: Maybe drop the kelp/seagrass as an item. -DONE
            //  Maybe use a tag instead of hardcoding kelp and seagrass? HOW??
            if (world.getBlockState(currentPos) == FrostedIceBlock.getMeltedState() || currentBlock == Blocks.FROSTED_ICE || (world.getBlockState(currentPos).getFluidState().isIn(FluidTags.WATER)
                    && (currentBlock == Blocks.KELP || currentBlock == Blocks.SEAGRASS))) {
                if (!frostedIceState.canPlaceAt(world, currentPos) || !world.canPlace(frostedIceState, currentPos, ShapeContext.absent()))
                    continue;
                ItemEntity items = new ItemEntity(user.getWorld(), currentPos.getX(), currentPos.getY(), currentPos.getZ(), currentBlock.asItem().getDefaultStack());
                world.setBlockState(currentPos, frostedIceState);
                world.spawnEntity(items);
                world.scheduleBlockTick(currentPos, Blocks.FROSTED_ICE, MathHelper.nextInt(user.getRandom(), 60, 120));
            }
        }

        info.cancel();
    }

    @Unique
    private static boolean shouldPlaceFrostedBlock(BlockPos centerPos, BlockPos currentPos, int radius) {
        return centerPos.isWithinDistance(currentPos, radius);
    }
}