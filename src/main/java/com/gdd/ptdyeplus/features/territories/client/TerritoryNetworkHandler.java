package com.gdd.ptdyeplus.features.territories.client;

import com.gdd.ptdyeplus.features.territories.common.TerritoryGeometrySyncPacket;
import com.gdd.ptdyeplus.features.territories.common.TerritoryStyleSyncPacket;
import com.gdd.ptdyeplus.util.ExternalMods;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TerritoryNetworkHandler {
    public static void handleGeometry(TerritoryGeometrySyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (ExternalMods.isJourneyMapPresent()) {
            context.enqueueWork(() -> TerritoryClientHandler.handleGeometry(packet));
        }
        context.setPacketHandled(true);
    }

    public static void handleStyle(TerritoryStyleSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (ExternalMods.isJourneyMapPresent()) {
            context.enqueueWork(() -> TerritoryClientHandler.handleStyle(packet));
        }
        context.setPacketHandled(true);
    }
}
