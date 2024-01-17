package com.kikis.ptdyeplus.mixin.vanilla;

import com.kikis.ptdyeplus.PtdyePlus;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EnderChestBlockEntity.class)
public class EnderChestBlockEntityMixin {
    @ModifyConstant(method = "stillValid", constant = {@Constant(doubleValue = 64.0)})
    public double validDistance(double value) {
        return PtdyePlus.REACH_DISTANCE_SQR;
    }
}
