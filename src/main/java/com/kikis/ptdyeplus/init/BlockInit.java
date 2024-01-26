package com.kikis.ptdyeplus.init;

import com.kikis.ptdyeplus.PtdyePlus;
import com.kikis.ptdyeplus.block.Barrel;
import com.kikis.ptdyeplus.block.Crate;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PtdyePlus.ID);

    // for create to use the custom-containers they must end in either '_barrel' or '_chest'

    public static final RegistryObject<Barrel> BARREL = register(
            "barrel_barrel",
            () -> new Barrel(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).dynamicShape().strength(2, 8)),
            new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)
    );

    public static final RegistryObject<Crate> CRATE = register(
            "crate_barrel",
            () -> new Crate(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).dynamicShape().strength(2, 8)),
            new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)
    );

    public static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ItemInit.ITEMS.register(name, ()-> new BlockItem(block.get(), properties));
        return block;
    }
}

