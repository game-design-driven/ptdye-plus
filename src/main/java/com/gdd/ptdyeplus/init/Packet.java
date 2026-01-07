package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.content.contraptions.IndependentContraptionEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Packet {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        ResourceLocation.fromNamespaceAndPath(PTDyePlus.ID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    private static int id() {return packetId++;}

    public static void register() {
        // Independent_Contraption's packet
        CHANNEL.messageBuilder(IndependentContraptionEntity.IndependentContraptionControlPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(IndependentContraptionEntity.IndependentContraptionControlPacket::new)
            .encoder(IndependentContraptionEntity.IndependentContraptionControlPacket::write)
            .consumerMainThread(IndependentContraptionEntity.IndependentContraptionControlPacket::handle)
            .add();
    }
}
