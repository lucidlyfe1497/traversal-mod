package net.lucid.traversal.fluid.custom;

import net.lucid.traversal.fluid.ModFluids;
import net.lucid.traversal.item.ModItems;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

public class NetherOilFluid extends FlowableFluid {

    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        BlockPos blockPos = pos.up();
        if (world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) {
            if (random.nextInt(100) == 0) {
                double d = (double)pos.getX() + random.nextDouble();
                double e = (double)pos.getY() + (double)1.0F;
                double f = (double)pos.getZ() + random.nextDouble();
                world.addParticle(ParticleTypes.LAVA, d, e, f, (double)0.0F, (double)0.0F, (double)0.0F);
                world.playSound(d, e, f, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                world.playSound((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

    }

    public void onRandomTick(World world, BlockPos pos, FluidState state, Random random) {
        if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
            int i = random.nextInt(3);
            if (i > 0) {
                BlockPos blockPos = pos;

                for(int j = 0; j < i; ++j) {
                    blockPos = blockPos.add(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                    if (!world.canSetBlock(blockPos)) {
                        return;
                    }

                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.isAir()) {
                        if (this.canLightFire(world, blockPos)) {
                            world.setBlockState(blockPos, AbstractFireBlock.getState(world, blockPos));
                            return;
                        }
                    } else if (blockState.blocksMovement()) {
                        return;
                    }
                }
            } else {
                for(int k = 0; k < 3; ++k) {
                    BlockPos blockPos2 = pos.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                    if (!world.canSetBlock(blockPos2)) {
                        return;
                    }

                    if (world.isAir(blockPos2.up()) && this.hasBurnableBlock(world, blockPos2)) {
                        world.setBlockState(blockPos2.up(), AbstractFireBlock.getState(world, blockPos2));
                    }
                }
            }

        }
    }

    private boolean hasBurnableBlock(WorldView world, BlockPos pos) {
        return pos.getY() >= world.getBottomY() && pos.getY() < world.getTopY() && !world.isChunkLoaded(pos) ? false : world.getBlockState(pos).isBurnable();
    }

    private boolean canLightFire(WorldView world, BlockPos pos) {
        for(Direction direction : Direction.values()) {
            if (this.hasBurnableBlock(world, pos.offset(direction))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_NETHER_OIL;
    }

    @Override
    public Fluid getStill() {
        return ModFluids.NETHER_OIL;
    }

    @Override
    protected boolean isInfinite(World world) {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {

    }

    @Override
    protected int getMaxFlowDistance(WorldView world) {
        return 0;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 0;
    }

    @Override
    public Item getBucketItem() {
        return ModItems.BUCKET_OF_NETHER_OIL;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 0;
    }

    @Override
    protected float getBlastResistance() {
        return 0;
    }

    @Override
    protected BlockState toBlockState(FluidState state) {
        return null;
    }

    @Override
    public boolean isStill(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }
}
