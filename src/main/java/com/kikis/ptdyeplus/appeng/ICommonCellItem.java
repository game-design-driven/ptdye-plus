package com.kikis.ptdyeplus.appeng;

import appeng.api.stacks.AEKeyType;
import appeng.api.storage.cells.ICellWorkbenchItem;
import net.minecraft.world.item.ItemStack;

// Shared non-default definitions between IDiskCellItem and IBasicCellItem
public interface ICommonCellItem extends ICellWorkbenchItem {
    AEKeyType _getKeyType();

    int _getBytes(ItemStack is);

    double _getIdleDrain();
}