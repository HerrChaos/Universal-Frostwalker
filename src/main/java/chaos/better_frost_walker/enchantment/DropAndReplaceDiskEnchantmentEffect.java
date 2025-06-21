package chaos.better_frost_walker.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.Optional;

// TODO: this is unused? yeah looks like ReplaceDiskEnchantmentEffectMixin does stuff?
public record DropAndReplaceDiskEnchantmentEffect(
        EnchantmentLevelBasedValue radius,
        EnchantmentLevelBasedValue height,
        Vec3i offset,
        Optional<BlockPredicate> predicate,
        BlockStateProvider blockState,
        Optional<RegistryEntry<GameEvent>> triggerGameEvent
) implements EnchantmentEntityEffect {
    public static final MapCodec<DropAndReplaceDiskEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            EnchantmentLevelBasedValue.CODEC.fieldOf("radius").forGetter(DropAndReplaceDiskEnchantmentEffect::radius),
                            EnchantmentLevelBasedValue.CODEC.fieldOf("height").forGetter(DropAndReplaceDiskEnchantmentEffect::height),
                            Vec3i.CODEC.optionalFieldOf("offset", Vec3i.ZERO).forGetter(DropAndReplaceDiskEnchantmentEffect::offset),
                            BlockPredicate.BASE_CODEC.optionalFieldOf("predicate").forGetter(DropAndReplaceDiskEnchantmentEffect::predicate),
                            BlockStateProvider.TYPE_CODEC.fieldOf("block_state").forGetter(DropAndReplaceDiskEnchantmentEffect::blockState),
                            GameEvent.CODEC.optionalFieldOf("trigger_game_event").forGetter(DropAndReplaceDiskEnchantmentEffect::triggerGameEvent)
                    )
                    .apply(instance, DropAndReplaceDiskEnchantmentEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        final Entity vehicle = user.getControllingVehicle();
        if (vehicle != null) pos = vehicle.getPos();

        final BlockPos startingPos = BlockPos.ofFloored(pos).add(this.offset);
        final Random random = user.getRandom();
        int radius = (int) this.radius.getValue(level);
        int height = (int) this.height.getValue(level);

        for(BlockPos currentBlockPos : BlockPos.iterate(startingPos.add(-radius, 0, -radius), startingPos.add(radius, height, radius))) {
            if (currentBlockPos.getSquaredDistanceFromCenter(pos.getX(), currentBlockPos.getY() + 0.5, pos.getZ()) >= MathHelper.square(radius)) continue;
            if (!this.predicate.map(predicate -> predicate.test(world, currentBlockPos)).orElse(true)) continue;

            final BlockState initialBlockState = world.getBlockState(currentBlockPos);
            if (!world.setBlockState(currentBlockPos, this.blockState.get(random, currentBlockPos))) continue;

            Block.dropStacks(initialBlockState, world, currentBlockPos);
            this.triggerGameEvent.ifPresent(gameEvent -> world.emitGameEvent(user, gameEvent, currentBlockPos));
        }
    }

    @Override
    public MapCodec<DropAndReplaceDiskEnchantmentEffect> getCodec() {
        return CODEC;
    }
}
