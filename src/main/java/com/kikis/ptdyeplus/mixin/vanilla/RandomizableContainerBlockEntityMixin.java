package com.kikis.ptdyeplus.mixin.vanilla;

import com.kikis.ptdyeplus.Distribution;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(RandomizableContainerBlockEntity.class)
public class RandomizableContainerBlockEntityMixin {
    @ModifyConstant(method = "stillValid", constant = {@Constant(doubleValue = 64.0)})
    public double validDistance(double value) {
        return Distribution.REACH_DISTANCE_SQR;
    }
}
