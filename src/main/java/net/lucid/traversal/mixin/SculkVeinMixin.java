package net.lucid.traversal.mixin;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)

public class SculkVeinMixin {
    @Inject(method = "onBlockAdded", at = @At("TAIL"))
    private void chargeSoulSandFrom(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify, CallbackInfo ci) throws InterruptedException {
         if (state.isOf(Blocks.SCULK_VEIN)) {
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.offset(direction);
                BlockState neighborState = world.getBlockState(neighborPos);

                if (neighborState.isOf(Blocks.SOUL_SAND)) {

                    world.setBlockState(neighborPos, net.lucid.traversal.block.ModBlocks.CHARGED_SOUL_SAND.getDefaultState());
                    world.addParticle(ParticleTypes.SOUL, pos.getX(), pos.getY()+0.6f, pos.getZ(), 0.0, 0.2, 0.0);

                }
            }
        }
    }



}