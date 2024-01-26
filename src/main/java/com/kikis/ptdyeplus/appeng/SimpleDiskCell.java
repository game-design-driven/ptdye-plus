package com.kikis.ptdyeplus.appeng;

import appeng.api.stacks.AEKeyType;
import appeng.items.contents.CellConfig;
import appeng.util.ConfigInventory;
import io.github.projectet.ae2things.storage.IDISKCellItem;
import net.minecraft.world.item.ItemStack;


public class SimpleDiskCell extends SimpleCellItem implements IDISKCellItem {
    @Override
    public AEKeyType getKeyType() { return _getKeyType(); }

    @Override
    public int getBytes(ItemStack is) { return _getBytes(is); }

    @Override
    public double getIdleDrain() { return _getIdleDrain(); }

    @Override
    public ConfigInventory getConfigInventory(ItemStack is) {
        // clone of: https://github.com/Technici4n/AE2Things-Forge/blob/57d1ee0338e970cdd72387037aa44d1f9b0c7c6c/src/main/java/io/github/projectet/ae2things/item/DISKDrive.java#L72C12-L72C12
        return CellConfig.create(getKeyType().filter(), is);
    }

    public SimpleDiskCell(Properties properties, double drain, int bytes, AEKeyType keyType) {
        super(properties, drain, bytes, keyType);
    }
}
