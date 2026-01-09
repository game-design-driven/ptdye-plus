package com.gdd.ptdyeplus.features.territories.common;

import net.minecraft.core.BlockPos;

import java.util.List;

public record IslandGeometry(List<BlockPos> hull, List<List<BlockPos>> holes) {
}
