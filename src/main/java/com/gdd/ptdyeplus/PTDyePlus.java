package com.gdd.ptdyeplus;

import com.gdd.ptdyeplus.features.Ponder;
import com.gdd.ptdyeplus.init.BlockEntityInit;
import com.gdd.ptdyeplus.init.BlockInit;
import com.gdd.ptdyeplus.init.ItemInit;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PTDyePlus.ID)
public class PTDyePlus {

    public static final String ID = "ptdyeplus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PTDyePlus(FMLJavaModLoadingContext context) {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus eventBus = context.getModEventBus();

        ItemInit.ITEMS.register(eventBus);
        BlockInit.BLOCKS.register(eventBus);
        BlockEntityInit.ENTITY_TYPES.register(eventBus);
    }

    @Mod.EventBusSubscriber(modid = PTDyePlus.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Ponder.KEY_PONDER);
        }
    }


    @Mod.EventBusSubscriber(modid = PTDyePlus.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientForgeBusEvents {

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            Ponder.onRenderTick(Minecraft.getInstance(), event);
        }
    }
}
