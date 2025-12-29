package com.gdd.ptdyeplus;

import com.gdd.ptdyeplus.init.BlockEntityInit;
import com.gdd.ptdyeplus.init.BlockInit;
import com.gdd.ptdyeplus.init.ItemInit;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PTDyePlus.ID)
public class PTDyePlus {

    public static final String ID = "ptdyeplus";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PTDyePlus(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus eventBus = context.getModEventBus();

        ItemInit.ITEMS.register(eventBus);
        BlockInit.BLOCKS.register(eventBus);
        BlockEntityInit.ENTITY_TYPES.register(eventBus);
    }
}
