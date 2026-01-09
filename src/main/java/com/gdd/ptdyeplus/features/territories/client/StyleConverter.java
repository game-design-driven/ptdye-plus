package com.gdd.ptdyeplus.features.territories.client;

import com.gdd.ptdyeplus.features.territories.common.TerritoryStyle;
import journeymap.client.api.model.ShapeProperties;

public class StyleConverter {
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
}
