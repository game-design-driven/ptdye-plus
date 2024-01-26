package com.kikis.ptdyeplus.appeng;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKeyType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SimpleCellItem extends Item implements ICommonCellItem {
    protected final double powerCost;
    protected final int capacity;
    private final AEKeyType keyType;

    @Override
    public AEKeyType _getKeyType() { return keyType; }

    @Override
    public int _getBytes(ItemStack itemStack) { return this.capacity; }

    @Override
    public double _getIdleDrain() { return powerCost; }

    @Override
    public FuzzyMode getFuzzyMode(ItemStack itemStack) {
        // clone of: https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/137cb538538b7ef9c66261931f662182ddf937b4/src/main/java/appeng/items/storage/BasicStorageCell.java#L133
        final String fz = itemStack.getOrCreateTag().getString("FuzzyMode");
        if (fz.isEmpty()) {
            return FuzzyMode.IGNORE_ALL;
        }
        try {
            return FuzzyMode.valueOf(fz);
        } catch (Throwable t) {
            return FuzzyMode.IGNORE_ALL;
        }
    }

    @Override
    public void setFuzzyMode(ItemStack itemStack, FuzzyMode fuzzyMode) {
        // clone of: https://github.com/AppliedEnergistics/Applied-Energistics-2/blob/137cb538538b7ef9c66261931f662182ddf937b4/src/main/java/appeng/items/storage/BasicStorageCell.java#L145
        itemStack.getOrCreateTag().putString("FuzzyMode", fuzzyMode.name());
    }

    public SimpleCellItem(Properties properties, double drain, int bytes, AEKeyType keyType) {
        super(properties);
        this.powerCost = drain;
        this.capacity = bytes;
        this.keyType = keyType;
    }
}