package chaos.better_frost_walker.mixin;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import chaos.better_frost_walker.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chaos.better_frost_walker.BetterFrostWalkerMain.CONFIG;
import static chaos.better_frost_walker.BetterFrostWalkerMain.hasFrostWalker;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void betterfrostwalker$disableFallDamageOnFrostedBlocks(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        final PlayerEntity player = (PlayerEntity) (Object) this;
        if (!BetterFrostWalkerMain.CONFIG.noIceFallDamage) return;
        if (!source.isOf(DamageTypes.FALL)) return;
        if (!hasFrostWalker(player, player.getWorld())) return;

        final BlockState blockState = player.getWorld().getBlockState(player.getBlockPos().down());
        if (
                blockState.isOf(Blocks.FROSTED_ICE)
                || (!CONFIG.serverSideOnly && blockState.isOf(ModBlocks.FROSTED_MAGMA))
        ) cir.setReturnValue(false);
    }
}
