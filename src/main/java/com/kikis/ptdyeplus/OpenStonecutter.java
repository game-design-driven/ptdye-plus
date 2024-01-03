package com.kikis.ptdyeplus;

import com.kikis.ptdyeplus.util.EntityContainerLevelAccess;
import com.kikis.ptdyeplus.util.MinecraftMenuBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.StonecutterMenu;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class OpenStonecutter {

    @Contract(" -> new")
    private static @NotNull SimpleMenuProvider getMenuProvider() {
        MinecraftMenuBuilder builder = (a, b, c)->new StonecutterMenu(a,b,c) {
            @Override
            public boolean stillValid(@NotNull Player player) {
                return true;
            }
        };

        return new SimpleMenuProvider(
                (id, inv, player) -> builder.create(id, inv, new EntityContainerLevelAccess(player)),
                Component.translatable("Personal Assembler")
        );
    }

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("openStonecutter").executes(OpenStonecutter::execute));
    }
    
    private static int execute(@NotNull CommandContext<CommandSourceStack> command){
        if(command.getSource().getEntity() instanceof Player player){
            player.openMenu(getMenuProvider());
        }
        return Command.SINGLE_SUCCESS;
    }
}