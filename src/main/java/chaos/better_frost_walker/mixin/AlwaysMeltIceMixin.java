package chaos.better_frost_walker.mixin;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import net.minecraft.block.FrostedIceBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FrostedIceBlock.class)
public abstract class AlwaysMeltIceMixin {
    @ModifyConstant(method = "scheduledTick", constant = @Constant(intValue = 11))
    public int mixinLimitInt(int constant) {
        if (BetterFrostWalkerMain.CONFIG.meltIceInTheDark) {
            return -1;
        }
        return constant;
    }
}
