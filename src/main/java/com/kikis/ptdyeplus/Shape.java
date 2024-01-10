package com.kikis.ptdyeplus;

import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Shape {
    public VoxelShape shape;
    public Shape() {
        this.shape = Shapes.empty();
    }

    public Shape box(int x0, int y0, int z0, int x1, int y1, int z1) {
        float x0f = (float) x0 / 16.0F, y0f = (float) y0 / 16.F, z0f = (float) z0 / 16.F;
        float x1f = (float) x1 / 16.0F, y1f = (float) y1 / 16.F, z1f = (float) z1 / 16.F;
        this.shape = Shapes.join(shape, Shapes.box(x0f, y0f, z0f, x1f, y1f, z1f), BooleanOp.OR);
        return this;
    }
}
