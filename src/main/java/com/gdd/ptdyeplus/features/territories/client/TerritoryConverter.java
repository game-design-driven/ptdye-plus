package com.gdd.ptdyeplus.features.territories.client;

import com.gdd.ptdyeplus.features.territories.common.IslandGeometry;
import com.gdd.ptdyeplus.features.territories.common.TerritoryStyle;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.MapPolygonWithHoles;
import journeymap.client.api.model.ShapeProperties;
import net.minecraft.core.BlockPos;

import java.util.List;

public class TerritoryConverter {
    public static ShapeProperties toShapeProperties(TerritoryStyle style) {
        return new ShapeProperties()
            .setStrokeColor(style.strokeColor())
            .setFillColor(style.fillColor())
            .setStrokeOpacity(style.strokeOpacity())
            .setFillOpacity(style.fillOpacity())
            .setStrokeWidth(style.strokeWidth())
            .setImageLocation(style.imageLocation())
            .setTexturePositionX(style.texturePositionX())
            .setTexturePositionY(style.texturePositionY())
            .setTextureScaleX(style.textureScaleX())
            .setTextureScaleY(style.textureScaleY());
    }

    public static MapPolygonWithHoles toMapPolygon(IslandGeometry island) {
        return new MapPolygonWithHoles(
            convertHull(island.hull()),
            convertHoles(island.holes())
        );
    }

    private static MapPolygon convertHull(List<BlockPos> points) {
        return new MapPolygon(points);
    }

    private static List<MapPolygon> convertHoles(List<List<BlockPos>> holeList) {
        if (holeList == null)
            return null;
        return holeList.stream().map(MapPolygon::new).toList();
    }
}
