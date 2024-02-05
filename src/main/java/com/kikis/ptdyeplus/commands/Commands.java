package com.kikis.ptdyeplus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class Commands {

    //todo: refactor openInventory and openStonecutter commands into a general open gui function

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        LiteralArgumentBuilder<CommandSourceStack> root = net.minecraft.commands.Commands.literal("ptdye")
                .then(OpenStonecutter.register())
                .then(OpenInventory.register());

        dispatcher.register(root);

    }

}
