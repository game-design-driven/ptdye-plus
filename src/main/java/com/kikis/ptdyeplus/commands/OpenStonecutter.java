package com.kikis.ptdyeplus.commands;

import com.kikis.ptdyeplus.commands.stonecutter.EntityContainerLevelAccess;
import com.kikis.ptdyeplus.commands.stonecutter.MinecraftMenuBuilder;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class OpenStonecutter {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("openStonecutter").executes(OpenStonecutter::execute));
    }
    
    private static int execute(@NotNull CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof ServerPlayer serverPlayer){
            NetworkHooks.openScreen(serverPlayer, createMenu());

        }
        return Command.SINGLE_SUCCESS;
    }

    public static SimpleMenuProvider createMenu() {
        MinecraftMenuBuilder builder = (a, b, c)->new StonecutterMenu(a,b,c) {
            @Override
            public boolean stillValid(@NotNull Player player) {
                return player.hasContainerOpen();
            }
        };

        return new SimpleMenuProvider(
                (id, inv, player) -> builder.create(id, player.getInventory(), new EntityContainerLevelAccess(player)),
                Component.translatable("ptdyeplus.stonecutter.title")
        );
    }
}