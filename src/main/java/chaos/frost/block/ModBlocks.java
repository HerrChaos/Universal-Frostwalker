package chaos.frost.block;

import chaos.frost.NewFrostwalker;
import chaos.frost.block.custom.frostedMagma;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.enums.Instrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class ModBlocks {
    public static final Block FROSTED_MAGMA  = new frostedMagma(FabricBlockSettings.create()
            .mapColor(MapColor.DARK_RED)
            .instrument(Instrument.BASEDRUM)
            .luminance((state) -> 3)
            .strength(0.5F)
            .allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune())
            .postProcess(ModBlocks::always)
            .emissiveLighting(ModBlocks::always));

    public static void registerModBlocks() {
        Registry.register(Registries.BLOCK, new Identifier(NewFrostwalker.MOD_ID, "frosted_magma"), FROSTED_MAGMA);
        NewFrostwalker.LOGGER.info("Registering ModBlocks for " + NewFrostwalker.MOD_ID);
    }


    public static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}
