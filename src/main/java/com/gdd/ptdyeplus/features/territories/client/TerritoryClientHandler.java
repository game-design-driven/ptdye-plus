package com.gdd.ptdyeplus.features.territories.client;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.features.territories.common.TerritoryGeometrySyncPacket;
import com.gdd.ptdyeplus.features.territories.common.TerritoryStyleSyncPacket;
import com.gdd.ptdyeplus.journeymap.TerritoryOverlay;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.model.ShapeProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TerritoryClientHandler {
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

    public static void handleGeometry(TerritoryGeometrySyncPacket packet) {
        TerritoryOverlay territoryOverlay = get(packet.territoryId());
        try {
            territoryOverlay.updateGeometry(packet.geometries());
        } catch (Exception e) {
            PTDyePlus.LOGGER.warn("Failed to update geometry for territory ({})", packet.territoryId(), e);
        }
    }

    public static void handleStyle(TerritoryStyleSyncPacket packet) {
        TerritoryOverlay territoryOverlay = get(packet.territoryId());
        try {
            ShapeProperties shapeProperties = TerritoryConverter.toShapeProperties(packet.style());
            territoryOverlay.updateStyle(shapeProperties);
        } catch (Exception e) {
            PTDyePlus.LOGGER.warn("Failed to update style for territory ({})", packet.territoryId(), e);
        }
    }
}
