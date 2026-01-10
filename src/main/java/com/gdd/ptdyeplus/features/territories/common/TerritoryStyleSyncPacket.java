package com.gdd.ptdyeplus.features.territories.common;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public record TerritoryStyleSyncPacket(UUID territoryId, TerritoryStyle style) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(territoryId);

        buf.writeInt(style.strokeColor());
        buf.writeInt(style.fillColor());
        buf.writeFloat(style.strokeOpacity());
        buf.writeFloat(style.fillOpacity());
        buf.writeFloat(style.strokeWidth());
        buf.writeResourceLocation(style.imageLocation());
        buf.writeDouble(style.texturePositionX());
        buf.writeDouble(style.texturePositionY());
        buf.writeDouble(style.textureScaleX());
        buf.writeDouble(style.textureScaleY());
    }

    public static TerritoryStyleSyncPacket decode(FriendlyByteBuf buf) {
        UUID id = buf.readUUID();

        TerritoryStyle style = new TerritoryStyle.Builder()
            .strokeColor(buf.readInt())
            .fillColor(buf.readInt())
            .strokeOpacity(buf.readFloat())
            .fillOpacity(buf.readFloat())
            .strokeWidth(buf.readFloat())
            .imageLocation(buf.readResourceLocation())
            .texturePosition(buf.readDouble(), buf.readDouble())
            .textureScale(buf.readDouble(), buf.readDouble())
            .build();

        return new TerritoryStyleSyncPacket(id, style);
    }
}
