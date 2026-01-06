package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.content.contraptions.IndependentContraptionEntity;
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
                .setTrackingRange(20) // Distance the player sees it? 20 is the default create defined
                .setUpdateInterval(40)
                .setShouldReceiveVelocityUpdates(true)
                .fireImmune()
                .sized(1f, 1f)
                .build("independent_contraption"));
}
