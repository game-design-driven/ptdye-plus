package com.kikis.ptdyeplus.appeng;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKeyType;
import appeng.api.storage.cells.IBasicCellItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SimpleStorageCell extends Item implements IBasicCellItem {
    protected final double powerCost;
    protected final int capacity;
    protected final int typeCapacity;
    protected final int costPerType;
    private final AEKeyType keyType;

    @Override
    public AEKeyType getKeyType() { return keyType; }

    @Override
    public int getBytes(ItemStack itemStack) { return this.capacity; }

    @Override
    public int getBytesPerType(ItemStack itemStack) { return costPerType; }

    @Override
    public int getTotalTypes(ItemStack itemStack) { return typeCapacity; }

    @Override
    public double getIdleDrain() { return powerCost; }

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

    public SimpleStorageCell(Properties properties, double drain, int bytes, int types, int bytesPerType, AEKeyType keyType) {
        super(properties);
        if (bytes < 8 || bytes % 8 != 0) throw new IllegalArgumentException("Bytes must be a positive non-zero multiple of 8");
        this.powerCost = drain;
        this.capacity = bytes;
        this.typeCapacity = types;
        this.costPerType = bytesPerType;
        this.keyType = keyType;
    }
}