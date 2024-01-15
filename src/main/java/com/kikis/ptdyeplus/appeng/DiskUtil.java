package com.kikis.ptdyeplus.appeng;

import appeng.api.client.StorageCellModels;
import appeng.api.stacks.AEKeyType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public final class DiskUtil {
    public static void registerDisk(Item.Properties itemProperties, ResourceLocation itemLocation, ResourceLocation textureLocation, AEKeyType keyType, int types, int bytes, double drain, int bytesPerType) {
        if (types < 1 || types > 63) throw new IllegalArgumentException("Invalid type count");
        if (bytes < 8 || bytes % 8 != 0) throw new IllegalArgumentException("Bytes must be a positive non-zero multiple of 8");
        var cell = new SimpleStorageCell(itemProperties, drain, bytes, types, bytesPerType, keyType);
        ForgeRegistries.ITEMS.register(itemLocation, cell);
        StorageCellModels.registerModel(cell, textureLocation);
    }
}
