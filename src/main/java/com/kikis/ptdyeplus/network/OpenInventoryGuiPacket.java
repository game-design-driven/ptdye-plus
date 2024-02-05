package com.kikis.ptdyeplus.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenInventoryGuiPacket {

    private int open = 0;

    public OpenInventoryGuiPacket(int open) {
        this.open = open;
    }

    public static void encode(OpenInventoryGuiPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.open);
    }

    public static OpenInventoryGuiPacket decode(FriendlyByteBuf buffer) {
        return new OpenInventoryGuiPacket(buffer.readInt());
    }

    public static class Handler {
        public static void handle(OpenInventoryGuiPacket msg, Supplier<NetworkEvent.Context> ctx) {


            LocalPlayer player = Minecraft.getInstance().player;
            player.sendSystemMessage(Component.literal("goobs"));


//            assert player != null;
//            Minecraft.getInstance().setScreen(new InventoryScreen(player));
//            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
//
//            });
//            ctx.get().setPacketHandled(true);
        }
    }
}
