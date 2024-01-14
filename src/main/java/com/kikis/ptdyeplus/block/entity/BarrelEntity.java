package com.kikis.ptdyeplus.block.entity;

import com.kikis.ptdyeplus.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BarrelEntity extends BaseContainerEntity {

    public BarrelEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState, 9, 2, BlockEntityInit.BARREL.get(), "ptdyeplus.container.barrel");
    }
}
