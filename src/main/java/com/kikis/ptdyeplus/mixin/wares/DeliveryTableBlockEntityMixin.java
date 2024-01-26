package com.kikis.ptdyeplus.mixin.wares;

import com.kikis.ptdyeplus.PtdyePlus;
import io.github.mortuusars.wares.block.entity.DeliveryTableBlockEntity;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DeliveryTableBlockEntity.class)
public class DeliveryTableBlockEntityMixin {
    @ModifyConstant(method = "stillValid", constant = {@Constant(doubleValue = 64.0)})
    public double validDistance(double value) {
        return PtdyePlus.REACH_DISTANCE_SQR;
    }
}
