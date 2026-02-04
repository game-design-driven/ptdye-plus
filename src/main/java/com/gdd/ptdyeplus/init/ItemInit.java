package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PTDyePlus.ID);

    public static final RegistryObject<Item> THRUSTER = ITEMS.register("thruster",
        () -> new BlockItem(BlockInit.THRUSTER.get(), new Item.Properties()));

    public static final RegistryObject<Item> INVISIBLE_SEAT = ITEMS.register("invisible_seat",
        () -> new BlockItem(BlockInit.INVISIBLE_SEAT.get(), new Item.Properties()));
}
