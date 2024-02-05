package com.kikis.ptdyeplus.commands;

import com.kikis.ptdyeplus.PtdyePlus;
import com.kikis.ptdyeplus.network.OpenGuiPacket;
import com.kikis.ptdyeplus.network.PacketHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.NotNull;

public class OpenInventory {

    public static final String COMMAND = "openInventory";

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal(COMMAND).executes(OpenInventory::execute);
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof ServerPlayer serverPlayer){

            PacketHandler.sendTo(new OpenGuiPacket(1), serverPlayer);


//            InventoryScreen is = new InventoryScreen(serverPlayer);
//            Minecraft.getInstance().setScreen(is);

//            PtdyePlus.network.sendTo(new OpenGuiPacket(), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

//            PtdyePlus.network.send(new OpenGuiPacket(), PacketDistributor.PLAYER.with(() -> serverPlayer));
//            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new Message(COMMAND));
        }
        return Command.SINGLE_SUCCESS;
    }

}
