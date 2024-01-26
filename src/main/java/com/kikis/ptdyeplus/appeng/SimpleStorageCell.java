package com.kikis.ptdyeplus.appeng;

import appeng.api.stacks.AEKeyType;
import appeng.api.storage.cells.IBasicCellItem;
import net.minecraft.world.item.ItemStack;

public class SimpleStorageCell extends SimpleCellItem implements IBasicCellItem {
    protected final int types;
    protected final int bytesPerType;

    @Override
    public AEKeyType getKeyType() { return _getKeyType(); }

    @Override
    public int getBytes(ItemStack is) { return _getBytes(is); }

    @Override
    public int getBytesPerType(ItemStack is) { return bytesPerType; }

    @Override
    public int getTotalTypes(ItemStack cellItem) { return types; }

    @Override
    public double getIdleDrain() { return _getIdleDrain(); }

    public SimpleStorageCell(Properties properties, double drain, int bytes, int types, int bytesPerType, AEKeyType keyType) {
        super(properties, drain, bytes, keyType);
        this.types = types;
        this.bytesPerType = bytesPerType;
    }
}