package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class FrostRefreshMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        LivingEntity thiss = (LivingEntity) (Object) this;

        int i = EnchantmentHelper.getEquipmentLevel(Enchantments.FROST_WALKER, thiss);
        if (i > 0 && NewFrostwalker.CONFIG.generateIceWhileStill()) {
            FrostWalkerEnchantment.freezeWater(thiss, thiss.getWorld(), thiss.getBlockPos(), i);
        }
    }
}
