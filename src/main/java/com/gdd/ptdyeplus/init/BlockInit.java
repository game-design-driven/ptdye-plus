package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PTDyePlus.ID);
}
