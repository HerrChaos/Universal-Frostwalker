package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chaos.frost.NewFrostwalker.hasFrostWalker;

@Mixin(PlayerEntity.class)
public abstract class NoIceFallDamage {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        final PlayerEntity player = (PlayerEntity) (Object) this;
        if (hasFrostWalker(player, player.getWorld()) &&
                player.getWorld().getBlockState(player.getBlockPos().down()).getBlock() == Blocks.FROSTED_ICE &&
                source.isOf(DamageTypes.FALL) && NewFrostwalker.CONFIG.noIceFallDamage) {
            cir.setReturnValue(false);
        }
    }
}
