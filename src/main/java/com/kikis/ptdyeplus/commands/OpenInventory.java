package com.kikis.ptdyeplus.commands;

import com.kikis.ptdyeplus.network.OpenInventoryGuiPacket;
import com.kikis.ptdyeplus.network.PacketHandler;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.NotNull;

public class OpenInventory {

    public static final String COMMAND = "openInventory";

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal(COMMAND).executes(OpenInventory::execute);
    }

    private static int execute(@NotNull CommandContext<CommandSourceStack> command)  {
        if(command.getSource().getEntity() instanceof ServerPlayer player){
            PacketHandler.sendTo(new OpenInventoryGuiPacket(1), player);
        }
        return Command.SINGLE_SUCCESS;
    }

}
