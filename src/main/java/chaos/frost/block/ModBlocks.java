package chaos.frost.block;

import chaos.frost.NewFrostwalker;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Function;

import static chaos.frost.NewFrostwalker.id;
import static chaos.frost.block.FrostedMagmaBlock.AGE;

public class ModBlocks {
    public static final Block FROSTED_MAGMA = register(
            FrostedMagmaBlock::new,
            AbstractBlock.Settings.create()
                    .mapColor(MapColor.DARK_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .luminance((state) -> state.get(AGE) + 3)
                    .strength(0.5F)
                    .allowsSpawning((state, world, pos, entityType) -> entityType.isFireImmune())
                    .postProcess(ModBlocks::always)
                    .emissiveLighting(ModBlocks::always)
                    .ticksRandomly(),
            "frosted_magma"
    );

    private static <T extends Block> T register(Function<AbstractBlock.Settings, T> blockConstructor, AbstractBlock.Settings settings, String name) {
        final RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id(name));

        settings.registryKey(key);

        return Registry.register(Registries.BLOCK, key, blockConstructor.apply(settings));
    }

    public static void registerModBlocks() {
        //Registry.register(Registries.BLOCK, id("frosted_magma"), FROSTED_MAGMA);
        NewFrostwalker.LOGGER.info("Registering ModBlocks for " + NewFrostwalker.MOD_ID);
    }


    public static boolean always(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}
