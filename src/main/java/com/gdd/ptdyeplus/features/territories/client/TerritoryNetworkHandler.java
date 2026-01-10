package com.gdd.ptdyeplus.features.territories.client;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.features.territories.common.TerritoryGeometrySyncPacket;
import com.gdd.ptdyeplus.features.territories.common.TerritoryStyleSyncPacket;
import com.gdd.ptdyeplus.journeymap.TerritoryOverlay;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.model.ShapeProperties;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TerritoryNetworkHandler {

    private static final Map<UUID, TerritoryOverlay> CACHE = new HashMap<>();

    public static TerritoryOverlay get(UUID territoryID) {
        return CACHE.computeIfAbsent(territoryID, TerritoryOverlay::new);
    }

    public static void onAPIReady(IClientAPI jmAPI) {
        TerritoryOverlay.updateAPI(jmAPI);
        for (TerritoryOverlay overlay : CACHE.values()) {
            try {
                overlay.refreshOverlay();
            } catch (Exception e) {
                PTDyePlus.LOGGER.warn("Failed to initial-draw territory ({})", overlay.getTerritoryId(), e);
            }
        }
    }

    public static void handleGeometry(TerritoryGeometrySyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            TerritoryOverlay territoryOverlay = get(packet.territoryId());
            try {
                territoryOverlay.updateGeometry(packet.geometries());
            } catch (Exception e) {
                PTDyePlus.LOGGER.warn("Failed to update geometry for territory ({})", packet.territoryId(), e);
            }
        });
        context.setPacketHandled(true);
    }

    public static void handleStyle(TerritoryStyleSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            TerritoryOverlay territoryOverlay = get(packet.territoryId());
            try {
                ShapeProperties shapeProperties = TerritoryConverter.toShapeProperties(packet.style());
                territoryOverlay.updateStyle(shapeProperties);
            } catch (Exception e) {
                PTDyePlus.LOGGER.warn("Failed to update style for territory ({})", packet.territoryId(), e);
            }
        });
        context.setPacketHandled(true);
    }
}
