package com.gdd.ptdyeplus.features.territories.common;

import net.minecraft.core.BlockPos;

public record Edge(BlockPos p1, BlockPos p2) {
    public Edge {
        if (p1.getX() > p2.getX() || (p1.getX() == p2.getX() && p1.getZ() > p2.getZ())) {
            BlockPos temp = p1;
            p1 = p2;
            p2 = temp;
        }
    }
}
