package com.gdd.ptdyeplus.features.territories.common;

import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.MapPolygonWithHoles;
import net.minecraft.core.BlockPos;

import java.util.List;

public record IslandGeometry(List<BlockPos> hull, List<List<BlockPos>> holes) {

    public MapPolygonWithHoles toMapPolygon() {
        return new MapPolygonWithHoles(
            convertHull(this.hull),
            convertHoles(this.holes)
        );
    }

    private MapPolygon convertHull(List<BlockPos> points) {
        return new MapPolygon(points);
    }

    private List<MapPolygon> convertHoles(List<List<BlockPos>> holeList) {
        if (holeList == null)
            return null;
        return holeList.stream().map(MapPolygon::new).toList();
    }
}
