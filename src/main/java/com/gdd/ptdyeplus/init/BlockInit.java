package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.content.block.InvisibleSeatBlock;
import com.gdd.ptdyeplus.content.block.ThrusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PTDyePlus.ID);

    public static final RegistryObject<Block> THRUSTER = BLOCKS.register("thruster",
        () -> new ThrusterBlock(BlockBehaviour.Properties.of()
            .strength(5f)
            .noOcclusion()
            .dynamicShape()
        ));

    public static final RegistryObject<Block> INVISIBLE_SEAT = BLOCKS.register("invisible_seat",
        () -> new InvisibleSeatBlock(BlockBehaviour.Properties.of()
            .strength(5f)
            .noOcclusion()
            .isValidSpawn((a, b, c, d) -> false)
        ));
}
