package chaos.better_frost_walker.block;

import chaos.better_frost_walker.BetterFrostWalkerMain;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;

import static chaos.better_frost_walker.BetterFrostWalkerMain.hasFrostWalker;

public class FrostedMagmaBlock extends Block {
    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = Properties.AGE_3;

    public FrostedMagmaBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }

    public static BlockState getMeltedState() {
        return Blocks.LAVA.getDefaultState();
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        world.setBlockState(pos, getMeltedState());
        return super.onBreak(world, pos, state, player);
    }
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if ((world instanceof ServerWorld serverWorld) && !entity.bypassesSteppingEffects() && entity instanceof LivingEntity livingEntity && !hasFrostWalker(livingEntity, world)) {
            entity.damage(serverWorld, world.getDamageSources().hotFloor(), 1.0f);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.scheduledTick(state, world, pos, random);
        super.randomTick(state, world, pos,random);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.scheduleBlockTick(pos, this, MathHelper.nextInt(random, 20, 40));

        if (!shouldRunScheduledTick(state, world, pos, random)) {
            world.scheduleBlockTick(pos, this, MathHelper.nextInt(random, 20, 40));
            return;
        }


        final BlockPos.Mutable mutable = new BlockPos.Mutable();
        final Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            mutable.set(pos, direction);

            final BlockState currentState = world.getBlockState(mutable);
            if (!currentState.isOf(this) || this.tryIncreaseAge(currentState, world, mutable)) continue;

            world.scheduleBlockTick(mutable, this, MathHelper.nextInt(random, 20, 40));
        }
    }

    private boolean shouldRunScheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        if (random.nextInt(3) == 0) return true;

        if (!canMelt(world, pos, 4)) return false;

        if (BetterFrostWalkerMain.CONFIG.meltIceInTheDark) return tryIncreaseAge(state, world, pos);
        if (world.getLightLevel(pos) > 11 - state.get(AGE) - state.getOpacity()) return tryIncreaseAge(state, world, pos);
        return false;
    }

    private boolean tryIncreaseAge(BlockState state, World world, BlockPos pos) {
        int age = state.get(AGE);
        if (age < MAX_AGE) {
            world.setBlockState(pos, state.with(AGE, age + 1), 2);
            return false;
        }

        this.melt(world, pos);
        return true;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, @Nullable WireOrientation wireOrientation, boolean notify) {
        if (sourceBlock.getDefaultState().isOf(this) && this.canMelt(world, pos, 2)) {
            this.melt(world, pos);
        }

        super.neighborUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    private boolean canMelt(BlockView world, BlockPos pos, int maxNeighbors) {
        int i = 0;
        final BlockPos.Mutable mutable = new BlockPos.Mutable();
        final Direction[] directions = Direction.values();

        for (Direction direction : directions) {
            mutable.set(pos, direction);

            if (!world.getBlockState(mutable).isOf(this)) continue;

            if (++i >= maxNeighbors) return false;
        }
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    private void melt(World world, BlockPos pos) {
        world.setBlockState(pos, getMeltedState());
        world.updateNeighbor(pos, getMeltedState().getBlock(), null);
    }
}
