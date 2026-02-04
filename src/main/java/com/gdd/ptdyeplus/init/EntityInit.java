package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.content.contraptions.IndependentContraptionEntity;
import com.gdd.ptdyeplus.content.contraptions.WinchContraptionEntity;
import com.gdd.ptdyeplus.content.entity.AnchorEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PTDyePlus.ID);

    public static final RegistryObject<EntityType<IndependentContraptionEntity>> INDEPENDENT_CONTRAPTION =
        ENTITY_TYPES.register("independent_contraption",
            () -> EntityType.Builder.of(IndependentContraptionEntity::new, MobCategory.MISC)
                .setTrackingRange(20)
                .setUpdateInterval(40)
                .setShouldReceiveVelocityUpdates(true)
                .fireImmune()
                .sized(1f, 1f)
                .build("independent_contraption"));

    public static final RegistryObject<EntityType<AnchorEntity>> ANCHOR =
        ENTITY_TYPES.register("anchor",
            () -> EntityType.Builder.of(AnchorEntity::new, MobCategory.MISC)
                .setTrackingRange(64)
                .setUpdateInterval(1)
                .setShouldReceiveVelocityUpdates(true)
                .noSummon()
                .fireImmune()
                .sized(0.1f, 0.1f)
                .build("anchor"));

    public static final RegistryObject<EntityType<WinchContraptionEntity>> WINCH_CONTRAPTION =
        ENTITY_TYPES.register("winch_contraption",
            () -> EntityType.Builder.of(WinchContraptionEntity::new, MobCategory.MISC)
                .setTrackingRange(20)
                .setUpdateInterval(40)
                .setShouldReceiveVelocityUpdates(true)
                .fireImmune()
                .sized(1f, 1f)
                .build("winch_contraption"));
}
