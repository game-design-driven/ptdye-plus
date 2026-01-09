package com.gdd.ptdyeplus.journeymap;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.features.territories.client.TerritoryConverter;
import com.gdd.ptdyeplus.features.territories.common.IslandGeometry;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.api.model.MapPolygonWithHoles;
import journeymap.client.api.model.ShapeProperties;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TerritoryOverlay {
    private static IClientAPI jmAPI = null;

    public static void updateAPI(IClientAPI jmAPI) {
        TerritoryOverlay.jmAPI = jmAPI;
    }

    private final UUID territoryId;
    private List<IslandGeometry> geometries = Collections.emptyList();
    private List<PolygonOverlay> overlays = Collections.emptyList();
    private ShapeProperties shapeProperties = TerritoryOverlay.createDefaultProperties();

    private static ShapeProperties createDefaultProperties() {
        return new ShapeProperties()
            .setFillColor(0xFF0000)
            .setStrokeColor(0xFF0000)
            .setStrokeOpacity(0.8f)
            .setFillOpacity(0.6f)
            .setStrokeWidth(1.5f);
    }

    public TerritoryOverlay(UUID territoryId) {
        this.territoryId = territoryId;
    }

    public UUID getTerritoryId() {
        return territoryId;
    }

    public void updateGeometry(List<IslandGeometry> geometries) throws Exception {
        this.geometries = geometries;
        refreshOverlay();
    }

    public void updateStyle(ShapeProperties shapeProperties) throws Exception {
        this.shapeProperties = shapeProperties;
        if (jmAPI != null && !overlays.isEmpty()) {
            for (PolygonOverlay overlay : overlays) {
                overlay.setShapeProperties(this.shapeProperties);
                jmAPI.show(overlay); // update overlay
            }
        }
    }

    public void refreshOverlay() throws Exception {
        if (jmAPI == null)
            return;
        clear();
        if (geometries.isEmpty())
            return; // If geometries is empty; there are no overlays to add.
        overlays = new ArrayList<>();
        for (int i = 0; i < geometries.size(); i++) {
            IslandGeometry island = geometries.get(i);
            MapPolygonWithHoles shape = TerritoryConverter.toMapPolygon(island);

            PolygonOverlay overlay = new PolygonOverlay(
                PTDyePlus.ID,
                territoryId + "_" + i,
                Level.OVERWORLD,
                shapeProperties,
                shape
            );
            overlays.add(overlay);
        }
        add();
    }

    private void clear() {
        if (jmAPI == null) {
            PTDyePlus.LOGGER.info("Tried to clear TerritoryOverlay's before jmAPI was initialized.");
            return;
        }
        if (overlays.isEmpty())
            return;
        for (PolygonOverlay overlay : overlays) {
            jmAPI.remove(overlay);
        }
        overlays = Collections.emptyList();
    }

    private void add() throws Exception {
        if (jmAPI == null) {
            PTDyePlus.LOGGER.info("Tried to add TerritoryOverlay's before jmAPI was initialized.");
            return;
        }
        if (overlays.isEmpty())
            return;
        for (PolygonOverlay overlay : overlays) {
            jmAPI.show(overlay);
        }
    }
}
