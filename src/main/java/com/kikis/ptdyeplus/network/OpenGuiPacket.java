package com.kikis.ptdyeplus.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class OpenGuiPacket  {

    private int open = 0;

    public OpenGuiPacket(int open) {
        this.open = open;
    }

    public static void encode(OpenGuiPacket packet, FriendlyByteBuf buffer) {
        buffer.writeFloat(packet.open);
    }

    public static OpenGuiPacket decode(FriendlyByteBuf buffer) {
        return new OpenGuiPacket(buffer.readInt());
    }
    public static class Handler {
        public static void handle(OpenGuiPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {

                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {

                    ServerPlayer player = ctx.get().getSender();

                    if (player == null)
                        return;

                    player.sendSystemMessage(Component.literal(String.valueOf(msg.open)));

                    InventoryScreen is = new InventoryScreen(player);
                    Minecraft.getInstance().setScreen(is);

                });


            });

            ctx.get().setPacketHandled(true);
        }
    }



//    public OpenGuiPacket() {}

//    public void fromBytes(FriendlyByteBuf buf) {}
//
//    public void toBytes(FriendlyByteBuf buf) {}

//    public OpenGuiPacket(FriendlyByteBuf buf) {
//    }
//
//    public void toBytes(FriendlyByteBuf buf) {
//    }
//
//    public static void handle(OpenGuiPacket packet, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
//                ServerPlayer sender = ctx.get().getSender();
//                assert sender != null;
//
//                InventoryScreen is = new InventoryScreen(sender);
//                Minecraft.getInstance().setScreen(is);
//            });
//        });
//        ctx.get().setPacketHandled(true);
//    }

//    public static void handle(OpenGuiPacket packet, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handlePacket(msg, ctx));
//        });
//        ctx.get().setPacketHandled(true);
//    }

//    public static void encode(Message msg, FriendlyByteBuf buffer) {
//        buffer.writeCharSequence(msg.getCommand(), StandardCharsets.UTF_8);
//    }
//
//    public static OpenGuiPacket decode(FriendlyByteBuf buffer) {
//        return new OpenGuiPacket((String) buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8));
//    }
}
