package com.kikis.ptdyeplus;

import com.kikis.ptdyeplus.commands.Commands;
import com.kikis.ptdyeplus.init.BlockEntityInit;
import com.kikis.ptdyeplus.init.BlockInit;
import com.kikis.ptdyeplus.init.ItemInit;
import com.kikis.ptdyeplus.jade.PonderTooltipComponentProvider;
import com.kikis.ptdyeplus.network.PacketHandler;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.*;

// todo: make gradle task that sets up environment or add shortcut to readme to refresh gradle

@Mod(PtdyePlus.ID)
public class PtdyePlus {
    public static final String ID = "ptdyeplus";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PtdyePlus() {

        MinecraftForge.EVENT_BUS.register(this);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::CommonSetup);


        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.ENTITY_TYPES.register(bus);


    }

    private void CommonSetup(FMLCommonSetupEvent event) {
        PacketHandler.register();
    }

    @Mod.EventBusSubscriber(modid = ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ModEventListener {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            Commands.register(event.getDispatcher());
        }
    }

    @SuppressWarnings("deprecation")
    @Mod.EventBusSubscriber(modid = PtdyePlus.ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        private static final Minecraft minecraft = Minecraft.getInstance();

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) throws NoSuchElementException {
            if (PonderTooltipComponentProvider.KEY_TRY_PONDER.get().consumeClick()) {

                Entity entity = minecraft.getCameraEntity();
                assert entity != null;
                HitResult block = entity.pick(20.0D, 0.0F, false);

                if (block.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockpos = ((BlockHitResult) block).getBlockPos();
                    assert minecraft.level != null;
                    BlockState blockstate = minecraft.level.getBlockState(blockpos);

                    String item_id = String.valueOf(Registry.BLOCK.getKey(blockstate.getBlock()));

                    ResourceLocation id = new ResourceLocation(item_id);
                    if (!PonderRegistry.ALL.containsKey(id)) {
                        assert minecraft.player != null;
                        minecraft.player.displayClientMessage(Component.translatable("ptdyeplus.no_ponder"), true);
                    } else {
                        ScreenOpener.transitionTo(PonderUI.of(id));
                    }
                }

            }
        }
    }

    @Mod.EventBusSubscriber(modid = PtdyePlus.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(PonderTooltipComponentProvider.KEY_TRY_PONDER.get());
        }
    }
}