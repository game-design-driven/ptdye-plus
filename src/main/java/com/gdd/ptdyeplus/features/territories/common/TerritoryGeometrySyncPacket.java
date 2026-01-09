package com.gdd.ptdyeplus.features.territories.common;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record TerritoryGeometrySyncPacket(UUID territoryId, List<IslandGeometry> geometries) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(territoryId);

        buf.writeCollection(geometries, (buffer, island) -> {
            buffer.writeCollection(island.hull(), FriendlyByteBuf::writeBlockPos);
            buffer.writeCollection(island.holes(), (innerBuf, hole) -> {
                innerBuf.writeCollection(hole, FriendlyByteBuf::writeBlockPos);
            });
        });
    }

    public static TerritoryGeometrySyncPacket decode(FriendlyByteBuf buf) {
        UUID id = buf.readUUID();
        List<IslandGeometry> geometries = buf.readCollection(ArrayList::new, buffer -> {
            List<BlockPos> hull = buffer.readCollection(ArrayList::new, FriendlyByteBuf::readBlockPos);

            List<List<BlockPos>> holes = buffer.readCollection(ArrayList::new, innerBuf ->
                innerBuf.readCollection(ArrayList::new, FriendlyByteBuf::readBlockPos)
            );

            return new IslandGeometry(hull, holes);
        });
        return new TerritoryGeometrySyncPacket(id, geometries);
    }
}
