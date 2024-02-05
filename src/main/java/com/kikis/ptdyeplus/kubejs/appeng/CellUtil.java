package com.kikis.ptdyeplus.kubejs.appeng;

import appeng.api.client.StorageCellModels;
import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public final class CellUtil {
    public static void registerCell(Item.Properties itemProperties, ResourceLocation itemLocation, ResourceLocation textureLocation, AEKeyType keyType, int bytes, int bytesPerType, int types, double drain) {
        Preconditions.checkArgument(types > 0 && types < 64, "Type count must be between 1 and 63");
        Preconditions.checkArgument(bytes > 7 && bytes % 8 == 0, "Bytes must be a positive non-zero multiple of 8");
        var cell = new SimpleStorageCell(itemProperties, drain, bytes, types, bytesPerType, keyType);
        ForgeRegistries.ITEMS.register(itemLocation, cell);
        StorageCellModels.registerModel(cell, textureLocation);
    }

    public static void registerDisk(Item.Properties itemProperties, ResourceLocation itemLocation, ResourceLocation textureLocation, AEKeyType keyType, int bytes, double drain) {
        Preconditions.checkArgument(bytes > 7 && bytes % 8 == 0, "Bytes must be a positive non-zero multiple of 8");
        var cell = new SimpleDiskCell(itemProperties, drain, bytes, keyType);
        ForgeRegistries.ITEMS.register(itemLocation, cell);
        StorageCellModels.registerModel(cell, textureLocation);
    }
}
