package chaos.frost.mixin;

import chaos.frost.NewFrostwalker;
import net.minecraft.block.FrostedIceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FrostedIceBlock.class)
public class AlwaysMeltIceMixin {
    @ModifyConstant(method = "scheduledTick", constant = @Constant(intValue = 11))
    public int mixinLimitInt(int constant) {
        if (NewFrostwalker.CONFIG.meltIceInTheDark) {
            return -1;
        }
        return constant;
    }
}
