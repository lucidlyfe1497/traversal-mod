package net.lucid.traversal.block.custom;

import net.lucid.traversal.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class NetherOil extends Block {

    public NetherOil(Settings settings) {
        super(settings);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {

            if (stack.isOf(Items.BUCKET)) {
                stack.decrement(1);
                world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (stack.isEmpty()) {
                    player.setStackInHand(hand, new ItemStack(ModItems.BUCKET_OF_NETHER_OIL));
                } else if (!player.getInventory().insertStack(new ItemStack(ModItems.BUCKET_OF_NETHER_OIL))) {
                    player.dropItem(new ItemStack(ModItems.BUCKET_OF_NETHER_OIL), false);
                }
                world.setBlockState(pos, Blocks.SOUL_SAND.getDefaultState());
                world.emitGameEvent(player, GameEvent.FLUID_PICKUP, pos);
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            Direction direction = Direction.random(random);
            if (direction != Direction.UP) {
                BlockPos blockPos = pos.offset(direction);
                BlockState blockState = world.getBlockState(blockPos);
                if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
                    double d = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetX() * 0.6;
                    double e = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetY() * 0.6;
                    double f = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetZ() * 0.6;
                    world.addParticle(ParticleTypes.ASH, pos.getX() + d, pos.getY() + e, pos.getZ() + f, 0.0, 0.0, 0.0);
                }
            }
        }
    }



}
