package com.gdd.ptdyeplus.content.contraptions;

import com.simibubi.create.content.contraptions.StructureTransform;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.phys.Vec3;

public class IndependentStructureTransform extends StructureTransform {
    protected Vec3 pivot;

    public IndependentStructureTransform(BlockPos offset, Vec3 pivot, float xRot, float yRot, float zRot) {
        super(offset, xRot, yRot, zRot);
        this.pivot = pivot;
    }

    @Override
    public Vec3 applyWithoutOffset(Vec3 localVec) {
        Vec3 vec = localVec;
        if (mirror != Mirror.NONE)
            vec = VecHelper.mirror(vec.subtract(pivot), mirror).add(pivot);
        if (rotationAxis != null)
            vec = VecHelper.rotate(vec.subtract(pivot), angle, rotationAxis).add(pivot);
        return vec;
    }

    @Override
    public Vec3 unapplyWithoutOffset(Vec3 globalVec) {
        Vec3 vec = globalVec;
        if (rotationAxis != null)
            vec = VecHelper.rotate(vec.subtract(pivot), -angle, rotationAxis).add(pivot);
        if (mirror != Mirror.NONE)
            vec = VecHelper.mirror(vec.subtract(pivot), mirror).add(pivot);
        return vec;
    }
}
