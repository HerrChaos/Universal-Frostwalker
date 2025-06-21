package chaos.better_frost_walker.mixin;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import chaos.better_frost_walker.block.ModBlocks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static chaos.better_frost_walker.BetterFrostWalkerMain.CONFIG;
import static chaos.better_frost_walker.BetterFrostWalkerMain.hasFrostWalker;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @WrapOperation(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isInvulnerableTo(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;)Z"
            )
    )
    public boolean betterfrostwalker$disableFallDamageOnFrostedBlocks(PlayerEntity instance, ServerWorld world, DamageSource source, Operation<Boolean> original) {
        final PlayerEntity player = (PlayerEntity) (Object) this;
        if (!BetterFrostWalkerMain.CONFIG.noIceFallDamage) return false;
        if (!source.isOf(DamageTypes.FALL)) return false;
        if (!hasFrostWalker(player, player.getWorld())) return false;

        final BlockState blockState = player.getWorld().getBlockState(player.getBlockPos().down());
        return blockState.isOf(Blocks.FROSTED_ICE) || (!CONFIG.serverSideOnly && blockState.isOf(ModBlocks.FROSTED_MAGMA));
    }
}
