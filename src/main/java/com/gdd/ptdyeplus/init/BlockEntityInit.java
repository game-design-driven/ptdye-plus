package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PTDyePlus.ID);
}
