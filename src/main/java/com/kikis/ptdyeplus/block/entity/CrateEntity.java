package com.kikis.ptdyeplus.block.entity;

import com.kikis.ptdyeplus.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CrateEntity extends BaseContainerEntity {
    public CrateEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState, 9, 2, BlockEntityInit.CRATE.get(), "ptdyeplus.container.crate");
    }
}
