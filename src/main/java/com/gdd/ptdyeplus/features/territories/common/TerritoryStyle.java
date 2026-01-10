package com.gdd.ptdyeplus.features.territories.common;

import net.minecraft.resources.ResourceLocation;

public record TerritoryStyle(
    int strokeColor,
    int fillColor,
    float strokeOpacity,
    float fillOpacity,
    float strokeWidth,
    ResourceLocation imageLocation,
    double texturePositionX,
    double texturePositionY,
    double textureScaleX,
    double textureScaleY
) {
    public static class Builder {
        private int strokeColor = 0;
        private int fillColor = 0;
        private float strokeOpacity = 1.0f;
        private float fillOpacity = 0.5f;
        private float strokeWidth = 2.0f;
        private ResourceLocation imageLocation = null;
        private double texturePositionX = 0.0d;
        private double texturePositionY = 0.0d;
        private double textureScaleX = 1.0d;
        private double textureScaleY = 1.0d;

        public TerritoryStyle build() {
            return new TerritoryStyle(strokeColor, fillColor, strokeOpacity, fillOpacity, strokeWidth,
                imageLocation, texturePositionX, texturePositionY, textureScaleX, textureScaleY);
        }

        public Builder color(int color) {
            this.strokeColor = color;
            this.fillColor = color;
            return this;
        }

        public Builder opacity(float opacity) {
            this.fillOpacity = opacity;
            return this;
        }

        public Builder strokeColor(int strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public Builder fillColor(int fillColor) {
            this.fillColor = fillColor;
            return this;
        }

        public Builder strokeOpacity(float strokeOpacity) {
            this.strokeOpacity = strokeOpacity;
            return this;
        }

        public Builder fillOpacity(float fillOpacity) {
            this.fillOpacity = fillOpacity;
            return this;
        }

        public Builder strokeWidth(float strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public Builder imageLocation(ResourceLocation imageLocation) {
            this.imageLocation = imageLocation;
            return this;
        }

        public Builder imageLocation(String imageLocation) {
            this.imageLocation = ResourceLocation.parse(imageLocation);
            return this;
        }

        public Builder texturePosition(double x, double y) {
            this.texturePositionX = x;
            this.texturePositionY = y;
            return this;
        }

        public Builder textureScale(double x, double y) {
            this.textureScaleX = x;
            this.textureScaleY = y;
            return this;
        }
    }
}
