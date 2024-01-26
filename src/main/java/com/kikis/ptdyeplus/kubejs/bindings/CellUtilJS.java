package com.kikis.ptdyeplus.kubejs.bindings;


import appeng.api.client.StorageCellModels;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.AEKeyTypes;
import com.kikis.ptdyeplus.appeng.CellUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class CellUtilJS {

    public void registerItemCell(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, int typeCount, int byteCount, int bytesPerType, double powerDrain) {
        CellUtil.registerCell(properties, id, textureOrDefault(cellTexture), AEKeyType.items(), byteCount, bytesPerType, typeCount, powerDrain);
    }

    public void registerFluidCell(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, int typeCount, int byteCount, int bytesPerType, double powerDrain) {
        CellUtil.registerCell(properties, id, textureOrDefault(cellTexture), AEKeyType.fluids(), byteCount, bytesPerType, typeCount, powerDrain);
    }

    public void registerCell(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, ResourceLocation keyType, int typeCount, int byteCount, int bytesPerType, double powerDrain) {
        CellUtil.registerCell(properties, id, textureOrDefault(cellTexture), AEKeyTypes.get(keyType), byteCount, bytesPerType, typeCount, powerDrain);
    }

    public void registerItemDisk(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, int byteCount, int powerDrain) {
        CellUtil.registerDisk(properties, id, textureOrDefault(cellTexture), AEKeyType.items(), byteCount, powerDrain);
    }

    public void registerFluidDisk(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, int byteCount, int powerDrain) {
        CellUtil.registerDisk(properties, id, textureOrDefault(cellTexture), AEKeyType.fluids(), byteCount, powerDrain);
    }

    public void registerDisk(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, ResourceLocation keyType, int byteCount, int powerDrain) {
        CellUtil.registerDisk(properties, id, textureOrDefault(cellTexture), AEKeyTypes.get(keyType), byteCount, powerDrain);
    }

    private ResourceLocation textureOrDefault(@Nullable ResourceLocation cellTexture) { return cellTexture != null ? cellTexture : StorageCellModels.getDefaultModel(); }
}
