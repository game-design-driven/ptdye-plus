package com.kikis.ptdyeplus.init;

import com.kikis.ptdyeplus.PtdyePlus;
import com.kikis.ptdyeplus.block.entity.BarrelEntity;
import com.kikis.ptdyeplus.block.entity.CrateEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PtdyePlus.ID);

    public static final RegistryObject<BlockEntityType<BarrelEntity>> BARREL = register("barrel", () -> BlockEntityType.Builder.of(BarrelEntity::new, BlockInit.BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<CrateEntity>> CRATE = register("crate", () -> BlockEntityType.Builder.of(CrateEntity::new, BlockInit.CRATE.get()).build(null));

    public static <T extends BlockEntityType>RegistryObject<T> register(String name, Supplier<T> supplier) {
        PtdyePlus.LOGGER.info("Registering block as block-entity: {}", name);

        RegistryObject<T> entityType = ENTITY_TYPES.register(name, supplier);
        return entityType;
    }
}
