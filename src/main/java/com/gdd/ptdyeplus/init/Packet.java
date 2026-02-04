package com.gdd.ptdyeplus.init;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.content.contraptions.IndependentContraptionEntity;
import com.gdd.ptdyeplus.content.contraptions.WinchContraptionEntity;
import com.gdd.ptdyeplus.features.territories.client.TerritoryNetworkHandler;
import com.gdd.ptdyeplus.features.territories.common.TerritoryGeometrySyncPacket;
import com.gdd.ptdyeplus.features.territories.common.TerritoryStyleSyncPacket;
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

    // NOTE: KEEP PACKET ORDER! add new messages from the bottom to keep compatibility
    public static void register() {
        // Independent_Contraption's packet
        CHANNEL.messageBuilder(IndependentContraptionEntity.IndependentContraptionControlPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(IndependentContraptionEntity.IndependentContraptionControlPacket::new)
            .encoder(IndependentContraptionEntity.IndependentContraptionControlPacket::write)
            .consumerMainThread(IndependentContraptionEntity.IndependentContraptionControlPacket::handle)
            .add();

        // Territory's geometry packet
        CHANNEL.messageBuilder(TerritoryGeometrySyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(TerritoryGeometrySyncPacket::decode)
            .encoder(TerritoryGeometrySyncPacket::encode)
            .consumerMainThread(TerritoryNetworkHandler::handleGeometry)
            .add();

        // Territory's style packet
        CHANNEL.messageBuilder(TerritoryStyleSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(TerritoryStyleSyncPacket::decode)
            .encoder(TerritoryStyleSyncPacket::encode)
            .consumerMainThread(TerritoryNetworkHandler::handleStyle)
            .add();

        // Winch_Contraption's packet
        CHANNEL.messageBuilder(WinchContraptionEntity.WinchContraptionControlPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
            .decoder(WinchContraptionEntity.WinchContraptionControlPacket::new)
            .encoder(WinchContraptionEntity.WinchContraptionControlPacket::write)
            .consumerMainThread(WinchContraptionEntity.WinchContraptionControlPacket::handle)
            .add();
    }
}
