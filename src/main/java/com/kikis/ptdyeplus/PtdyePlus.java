package com.kikis.ptdyeplus;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;

@Mod("ptdyeplus")
public class PtdyePlus
{
    public static final String ID = "ptdyeplus";

    public PtdyePlus()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEventListener {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event){
            OpenStonecutter.register(event.getDispatcher());
        }
    }
}