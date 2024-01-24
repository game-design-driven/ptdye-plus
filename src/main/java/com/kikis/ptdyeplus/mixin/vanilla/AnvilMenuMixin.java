package com.kikis.ptdyeplus.mixin.vanilla;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    @Shadow @Final
    private DataSlot cost;

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", shift = At.Shift.BEFORE, ordinal = 1))
    public void NoCostRename(CallbackInfo ci, @Local(ordinal = 2) int k) {
        if (k == 1) cost.set(cost.get() - 1);
    }

    @ModifyConstant(method = "mayPickup", constant = @Constant(intValue = 0))
    public int AllowNoCost(int original) {
        return -1;
    }
}
