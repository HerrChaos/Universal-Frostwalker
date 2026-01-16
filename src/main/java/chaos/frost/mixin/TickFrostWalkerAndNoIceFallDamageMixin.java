package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import chaos.frost.block.ModBlocks;
import chaos.frost.tag.ModBlockTags;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chaos.frost.NewFrostwalker.hasFrostWalker;

@Mixin(PlayerEntity.class)
public abstract class TickFrostWalkerAndNoIceFallDamageMixin {

    @WrapMethod(method = "tick")
    public void tick(Operation<Void> original) {
        original.call();

        final PlayerEntity player = (PlayerEntity) (Object) this;

        if (!NewFrostwalker.CONFIG.generateIceWhileStill && isStandingStill(player)) {
            return;
        }

        if (!hasFrostWalker(player, player.getEntityWorld())) {
            return;
        }

        final RegistryKey<Registry<Enchantment>> enchantmentRegistry = RegistryKeys.ENCHANTMENT;
        int level = EnchantmentHelper.getEquipmentLevel(player.getEntityWorld().getRegistryManager().getOrThrow(enchantmentRegistry).getEntry(Enchantments.FROST_WALKER.getValue()).orElseThrow(), player);

        BlockPos blockPos = player.getRootVehicle().getBlockPos();

        int radius = 2 + level;

        replaceAllBlocksAround(blockPos, radius, player);
        replaceAllBlocksAround(blockPos.add(BlockPos.ofFloored(player.getVelocity())), radius, player);
        tryReplaceAt(player, player.getEntityWorld().raycast(new RaycastContext(player.getRootVehicle().getEntityPos(), player.getRootVehicle().getEntityPos().add(player.getRootVehicle().getVelocity()), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, player)).getBlockPos());
    }

    @Unique
    private boolean isStandingStill(PlayerEntity player) {
        return player.getX() == player.lastX && player.getY() == player.lastY && player.getZ() == player.lastZ;
    }

    @Unique
    private static void replaceAllBlocksAround(BlockPos blockPos, int radius, PlayerEntity player) {
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-radius, -1, -radius), blockPos.add(radius, -1, radius))) {
            if (blockPos2.getSquaredDistanceFromCenter(player.getX(), (double)blockPos2.getY() + (double)0.5F, player.getZ()) < (double) MathHelper.square(radius)) {
                tryReplaceAt(player, blockPos2);
            }
        }
    }

    @Unique
    private static void tryReplaceAt(PlayerEntity player, BlockPos blockPos2) {
        if (!player.getEntityWorld().getBlockState(blockPos2.add(0,1,0)).isAir()) {
            return;
        }

        if ((player.getBlockPos().getY() == blockPos2.getY() || player.getBlockPos().up().getY() == blockPos2.getY()) && player.getVehicle() == null) {
            return;
        }

        BlockState blockState = player.getEntityWorld().getBlockState(blockPos2);
        BlockState replacementState = getReplacementState(blockState);

        if (((blockState.isOf(Blocks.WATER) || (!NewFrostwalker.CONFIG.serverSideOnly && blockState.isOf(Blocks.LAVA))) && blockState.get(FluidBlock.LEVEL) == 0) || (blockState.isOf(Blocks.FROSTED_ICE) || (!NewFrostwalker.CONFIG.serverSideOnly && blockState.isOf(ModBlocks.FROSTED_MAGMA)))) {
            if (blockState.isIn(ModBlockTags.REPLACED_BY_FROST_WALKER) && blockState.getFluidState().isIn(FluidTags.WATER)) {
                Block.dropStacks(blockState, player.getEntityWorld(), blockPos2);
            }

            player.getEntityWorld().setBlockState(blockPos2, replacementState);
        }
    }

    @Unique
    private static BlockState getReplacementState(BlockState blockState) {
        if (blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.FROSTED_ICE) || (blockState.isIn(ModBlockTags.REPLACED_BY_FROST_WALKER) && blockState.getFluidState().isIn(FluidTags.WATER))) {
            return Blocks.FROSTED_ICE.getDefaultState();
        }

        if (!NewFrostwalker.CONFIG.serverSideOnly && (blockState.isOf(Blocks.LAVA) || blockState.isOf(ModBlocks.FROSTED_MAGMA))) {
            return ModBlocks.FROSTED_MAGMA.getDefaultState();
        }

        return blockState;
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        final PlayerEntity player = (PlayerEntity) (Object) this;
        if (!NewFrostwalker.CONFIG.noIceFallDamage) return;
        if (!source.isOf(DamageTypes.FALL)) return;
        if (!hasFrostWalker(player, player.getEntityWorld())) return;

        final BlockState blockState = player.getEntityWorld().getBlockState(player.getBlockPos().down());
        if (
                blockState.isOf(Blocks.FROSTED_ICE)
                || (!NewFrostwalker.CONFIG.serverSideOnly && blockState.isOf(ModBlocks.FROSTED_MAGMA))
        ) cir.setReturnValue(false);
    }
}
