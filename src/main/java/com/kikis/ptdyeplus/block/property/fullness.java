package com.kikis.ptdyeplus.block.property;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class fullness {
    public static final int MIN = 0;
    public static final int MAX = 5;
    public static final IntegerProperty PROPERTY = IntegerProperty.create("fullness", MIN, MAX);
}
