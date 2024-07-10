package chaos.frost.block;

import chaos.frost.NewFrostwalker;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import static chaos.frost.NewFrostwalker.id;
import static chaos.frost.block.FrostedMagmaBlock.AGE;

public class ModBlocks {
    public static final Block FROSTED_MAGMA  = new FrostedMagmaBlock(AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_RED)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .luminance((state) -> state.get(AGE) + 3)
            .strength(0.5F)
            .allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune())
            .postProcess(ModBlocks::always)
            .emissiveLighting(ModBlocks::always)
            .ticksRandomly()
    );

    public static void registerModBlocks() {
        Registry.register(Registries.BLOCK, id("frosted_magma"), FROSTED_MAGMA);
        NewFrostwalker.LOGGER.info("Registering ModBlocks for " + NewFrostwalker.MOD_ID);
    }


    public static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}
