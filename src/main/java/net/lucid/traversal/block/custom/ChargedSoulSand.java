package net.lucid.traversal.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ChargedSoulSand extends Block implements SculkSpreadable{
    public static final MapCodec<ChargedSoulSand> CODEC = createCodec(ChargedSoulSand::new);
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
    private static final int SCHEDULED_TICK_DELAY = 20;

    @Override
    public MapCodec<ChargedSoulSand> getCodec() {
        return CODEC;
    }

    public ChargedSoulSand(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BubbleColumnBlock.update(world, pos.up(), state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if (direction == Direction.UP && neighborState.isOf(Blocks.WATER)) {
            world.scheduleBlockTick(pos, this, 20);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        world.scheduleBlockTick(pos, this, 20);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        if (!(entity instanceof LivingEntity) || entity.getBlockStateAtPos().isOf(this)) {
            if (world.isClient) {
                Random random = world.getRandom();
                boolean bl = entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ();
                if (bl && random.nextBoolean()) {
                    world.addParticle(
                            ParticleTypes.SOUL,
                            entity.getX(),
                            pos.getY() + 1,
                            entity.getZ(),
                            MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F,
                            0.0005F,
                            MathHelper.nextBetween(random, -1.0F, 1.0F) * 0.083333336F
                    );
                }
            }

            if (entity.wasOnFire) {
                if (entity.isSprinting()) {

                    entity.damage(world.getDamageSources().onFire(), 0.5F);
                    entity.setOnFireFromLava();
                    entity.setOnFire(true);
                    entity.setSwimming(true);

                    Vec3d vec3d = entity.getRotationVec(1.0F);
                    Vec3d lookSwimSpeed = vec3d.multiply(1.5);
                    entity.setVelocity(lookSwimSpeed);
                    entity.velocityModified = true;

                    if (MinecraftClient.getInstance().options.jumpKey.isPressed()){
                        Vec3d lookJumpBoost = vec3d.multiply(3.0);
                        entity.setVelocity(lookJumpBoost);
                        entity.velocityModified = true;
                    }

                    if (entity.horizontalCollision){
                        entity.damage(world.getDamageSources().flyIntoWall(),3);
                    }

                }

            } else {
                entity.slowMovement(state, new Vec3d(0.5F, 0.2, 0.5F));
            }
        }
    }

    @Override
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 0.2F;
    }

    // Sculk Aspect

    @Override
    public int spread(
            SculkSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, SculkSpreadManager spreadManager, boolean shouldConvertToBlock
    ) {
        int i = cursor.getCharge();
        if (i != 0 && random.nextInt(spreadManager.getSpreadChance()) == 0) {
            BlockPos blockPos = cursor.getPos();
            boolean bl = blockPos.isWithinDistance(catalystPos, spreadManager.getMaxDistance());
            if (!bl && shouldNotDecay(world, blockPos)) {
                int j = spreadManager.getExtraBlockChance();
                if (random.nextInt(j) < i) {
                    BlockPos blockPos2 = blockPos.up();
                    BlockState blockState = this.getExtraBlockState(world, blockPos2, random, spreadManager.isWorldGen());
                    world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
                    world.playSound(null, blockPos, blockState.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return Math.max(0, i - j);
            } else {
                return random.nextInt(spreadManager.getDecayChance()) != 0 ? i : i - (bl ? 1 : getDecay(spreadManager, blockPos, catalystPos, i));
            }
        } else {
            return i;
        }
    }

    private static boolean shouldNotDecay(WorldAccess world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.up());
        if (blockState.isAir() || blockState.isOf(Blocks.WATER) && blockState.getFluidState().isOf(Fluids.WATER)) {
            int i = 0;

            for (BlockPos blockPos : BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 2, 4))) {
                BlockState blockState2 = world.getBlockState(blockPos);
                if (blockState2.isOf(Blocks.SCULK_SENSOR) || blockState2.isOf(Blocks.SCULK_SHRIEKER)) {
                    i++;
                }

                if (i > 2) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private BlockState getExtraBlockState(WorldAccess world, BlockPos pos, Random random, boolean allowShrieker) {
        BlockState blockState;
        if (random.nextInt(11) == 0) {
            blockState = Blocks.SCULK_SHRIEKER.getDefaultState().with(SculkShriekerBlock.CAN_SUMMON, allowShrieker);
        } else {
            blockState = Blocks.SCULK_SENSOR.getDefaultState();
        }

        return blockState.contains(Properties.WATERLOGGED) && !world.getFluidState(pos).isEmpty() ? blockState.with(Properties.WATERLOGGED, true) : blockState;
    }

    private static int getDecay(SculkSpreadManager spreadManager, BlockPos cursorPos, BlockPos catalystPos, int charge) {
        int i = spreadManager.getMaxDistance();
        float f = MathHelper.square((float)Math.sqrt(cursorPos.getSquaredDistance(catalystPos)) - i);
        int j = MathHelper.square(24 - i);
        float g = Math.min(1.0F, f / j);
        return Math.max(1, (int)(charge * g * 0.5F));
    }






}
