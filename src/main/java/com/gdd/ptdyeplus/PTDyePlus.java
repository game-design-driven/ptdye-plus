package com.gdd.ptdyeplus;

import com.gdd.ptdyeplus.client.renderer.AnchorRenderer;
import com.gdd.ptdyeplus.features.Ponder;
import com.gdd.ptdyeplus.init.*;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.contraptions.render.ContraptionEntityRenderer;
import com.simibubi.create.content.contraptions.render.ContraptionVisual;
import dev.engine_room.flywheel.lib.visualization.SimpleEntityVisualizer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        EntityInit.ENTITY_TYPES.register(eventBus);
        BlockEntityInit.ENTITY_TYPES.register(eventBus);
        Packet.register();
    }

    @Mod.EventBusSubscriber(modid = PTDyePlus.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                SimpleEntityVisualizer.builder(EntityInit.INDEPENDENT_CONTRAPTION.get())
                    .factory(ContraptionVisual::new)
                    .skipVanillaRender(entity -> false)
                    .apply();
            });
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Ponder.KEY_PONDER);
        }

        @SubscribeEvent
        public static void registerRenders(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(EntityInit.INDEPENDENT_CONTRAPTION.get(), ContraptionEntityRenderer::new);
            event.registerEntityRenderer(EntityInit.ANCHOR.get(), AnchorRenderer::new);
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
