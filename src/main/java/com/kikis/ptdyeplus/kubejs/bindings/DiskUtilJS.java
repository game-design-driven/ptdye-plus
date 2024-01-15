package com.kikis.ptdyeplus.kubejs.bindings;


import appeng.api.client.StorageCellModels;
import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.AEKeyTypes;
import com.kikis.ptdyeplus.appeng.DiskUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

public final class DiskUtilJS {

    public void registerDisk(ResourceLocation id, Item.Properties properties, @Nullable ResourceLocation cellTexture, @Nullable ResourceLocation keyType, int typeCount, int byteCount, int bytesPerType, double powerDrain) {
        DiskUtil.registerDisk(properties, id, cellTexture == null ? StorageCellModels.getDefaultModel() : cellTexture, keyType == null ? AEKeyType.items() : AEKeyTypes.get(keyType), typeCount, byteCount, powerDrain, bytesPerType);
    }
}
