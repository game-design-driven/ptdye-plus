package com.kikis.ptdyeplus.mixin.vanilla;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Accessor mixins must be interfaces.
// Prefixed with _s so if there is a function with identical signature, there isn't
@Mixin(AnvilMenu.class)
public interface AnvilMenuAccessor {
    @Accessor("itemName")
    public String _getItemName();

    @Accessor("itemName")
    public void _setItemName(String val);

    @Accessor("cost")
    public DataSlot _getCost();
}
