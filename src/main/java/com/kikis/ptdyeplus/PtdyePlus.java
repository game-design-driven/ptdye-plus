package com.kikis.ptdyeplus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kikis.ptdyeplus.kubejs.PtdyeJS;
import com.kikis.ptdyeplus.kubejs.Utils;
import com.kikis.ptdyeplus.util.KeyBinding;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.ui.PonderUI;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import net.minecraft.client.renderer.ItemBlockRenderTypes;

@Mod("ptdyeplus")
public class PtdyePlus
{
    public static final String ID = "ptdyeplus";
    private static final Minecraft minecraft = Minecraft.getInstance();

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

    @SuppressWarnings("deprecation")
    @Mod.EventBusSubscriber(modid = PtdyePlus.ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) throws NoSuchElementException {
            if(KeyBinding.TRY_PONDER_KEY.consumeClick()) {

                // minecraft.player.sendSystemMessage(Component.translatable());

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
            event.register(KeyBinding.TRY_PONDER_KEY);
        }
    }
}