package com.kikis.ptdyeplus.block;

import com.kikis.ptdyeplus.block.entity.BarrelEntity;
import com.kikis.ptdyeplus.block.property.fullness;
import com.kikis.ptdyeplus.Shape;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Barrel extends BaseEntityBlock {
    public static final IntegerProperty FULLNESS;
    public static final IntegerProperty POWER;

    private static final VoxelShape hitbox = new Shape()
            .box(1,0,1, 15,16,15)
            .box(3,0,0, 13,16,16)
            .box(0,0,3, 16,16,13)
            .shape;

    public Barrel(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(fullness.PROPERTY, fullness.MIN)
                .setValue(BlockStateProperties.POWER, 0)
        );
    }

    private void updatePower(BlockState blockState, Level level, BlockPos blockPos) {
        if (!level.isClientSide) {
            int power = level.getBestNeighborSignal(blockPos);
            level.setBlock(blockPos, blockState.setValue(POWER, Math.max(0, Math.min(power, 15))), 2);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
       if (level.isClientSide)
           return InteractionResult.SUCCESS;
       BlockEntity entity = level.getBlockEntity(blockPos);
       if (entity instanceof BarrelEntity) {
           player.openMenu((BarrelEntity) entity);
           player.awardStat(Stats.OPEN_BARREL);
       }
       return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean p_60519_) {
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof Container container) {
                Containers.dropContents(level, blockPos, container);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }

            super.onRemove(blockState, level, blockPos, blockState, p_60519_);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BarrelEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Barrel.hitbox;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof BarrelEntity barrelEntity)
                barrelEntity.setCustomName(itemStack.getHoverName());
        }
        this.updatePower(blockState, level, blockPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FULLNESS).add(POWER);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.neighborChanged(blockState, level, blockPos, neighborBlock, fromPos, moving);
        this.updatePower(blockState, level, blockPos);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_49058_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        // edge case that 'should' never happen
        if (!(level.getBlockEntity(blockPos) instanceof BarrelEntity entity))
            return 0;

        int power = blockState.getValue(POWER);
        if (power == 0)
            return Math.min(entity.getItemCount(null), 15);
        return Math.min(entity.getItemCount(entity.getItem(power - 1)), 15);
    }

    static {
        FULLNESS =  fullness.PROPERTY;
        POWER = BlockStateProperties.POWER;
    }
}
