package com.gdd.ptdyeplus.features.territories.server;

import com.gdd.ptdyeplus.PTDyePlus;
import com.gdd.ptdyeplus.features.territories.common.Edge;
import com.gdd.ptdyeplus.features.territories.common.IslandGeometry;
import com.gdd.ptdyeplus.features.territories.common.TerritoryGeometrySyncPacket;
import com.gdd.ptdyeplus.init.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

public class TerritoryManager {
    private final Set<ChunkPos> chunks = new HashSet<>();
    private List<IslandGeometry> islandGeometries = Collections.emptyList();
    private final UUID territoryId = UUID.randomUUID();

    public void addChunk(ChunkPos pos) {
        if (chunks.add(pos))
            rebuildTerritoryGeometry();
    }

    public void addChunks(List<ChunkPos> chunks) {
        boolean update = false;
        for (ChunkPos pos : chunks)
            update = chunks.add(pos) || update;
        if (update)
            rebuildTerritoryGeometry();
    }

    public void removeChunk(ChunkPos pos) {
        if (chunks.remove(pos))
            rebuildTerritoryGeometry();
    }

    public void removeChunks(List<ChunkPos> chunks) {
        boolean update = false;
        for (ChunkPos pos : chunks)
            update = chunks.remove(pos) || update;
        if (update)
            rebuildTerritoryGeometry();
    }

    public void modifyChunks(List<ChunkPos> toAdd, List<ChunkPos> toRemove) {
        boolean update = false;
        for (ChunkPos pos : toAdd)
            update = chunks.add(pos) || update;
        for (ChunkPos pos : toRemove)
            update = chunks.remove(pos) || update;
        if (update)
            rebuildTerritoryGeometry();
    }

    public void rebuildTerritoryGeometry() {
        List<Set<ChunkPos>> islands = findIslands();
        islandGeometries = rebuildPerimeters(islands);

        Packet.CHANNEL.send(PacketDistributor.ALL.noArg(),
            new TerritoryGeometrySyncPacket(territoryId, islandGeometries));
    }

    protected List<Set<ChunkPos>> findIslands() {
        List<Set<ChunkPos>> islands = new ArrayList<>();
        Set<ChunkPos> unvisited = new HashSet<>(chunks);

        while (!unvisited.isEmpty()) {
            Set<ChunkPos> island = new HashSet<>();
            Queue<ChunkPos> queue = new LinkedList<>();
            ChunkPos start = unvisited.iterator().next();
            queue.add(start);
            unvisited.remove(start);

            while (!queue.isEmpty()) {
                ChunkPos current = queue.poll();
                island.add(current);
                // Chunk 4 neighbors
                ChunkPos[] neighbors = {
                    new ChunkPos(current.x + 1, current.z),
                    new ChunkPos(current.x - 1, current.z),
                    new ChunkPos(current.x, current.z + 1),
                    new ChunkPos(current.x, current.z - 1)
                };
                for (ChunkPos n : neighbors) {
                    if (unvisited.contains(n)) {
                        unvisited.remove(n);
                        queue.add(n);
                    }
                }
            }
            islands.add(island);
        }
        return islands;
    }

    protected List<IslandGeometry> rebuildPerimeters(List<Set<ChunkPos>> islands) {
        List<IslandGeometry> allGeometries = new ArrayList<>();

        for (Set<ChunkPos> island : islands) {
            Set<Edge> allEdges = new HashSet<>();
            for (ChunkPos chunk : island) {
                int minX = chunk.getMinBlockX();
                int maxX = minX + 16; // getMaxBlockN, returns inclusive(+15), we want exclusive, so we add +16
                int minZ = chunk.getMinBlockZ();
                int maxZ = minZ + 16;

                BlockPos nw = new BlockPos(minX, 0, minZ);
                BlockPos ne = new BlockPos(maxX, 0, minZ);
                BlockPos se = new BlockPos(maxX, 0, maxZ);
                BlockPos sw = new BlockPos(minX, 0, maxZ);

                // Add edges clockwise order
                allEdges.add(new Edge(nw, ne)); // North Edge
                allEdges.add(new Edge(ne, se)); // East Edge
                allEdges.add(new Edge(se, sw)); // South Edge
                allEdges.add(new Edge(sw, nw)); // West Edge
            }

            List<Edge> boundaryEdges = new ArrayList<>();
            for (Edge edge : allEdges) {
                Edge reverse = new Edge(edge.p2(), edge.p1());
                if (!allEdges.contains(reverse))
                    boundaryEdges.add(edge);
            }

            List<List<BlockPos>> loops = extractDirectedLoops(boundaryEdges);

            loops.replaceAll(this::simplify);

            allGeometries.add(classifyHullAndHoles(loops));
        }
        return allGeometries;
    }

    protected List<List<BlockPos>> extractDirectedLoops(List<Edge> edges) {
        List<List<BlockPos>> loops = new ArrayList<>();

        Map<BlockPos, List<Edge>> outgoing = new HashMap<>();
        for (Edge e : edges) {
            outgoing.computeIfAbsent(e.p1(), k -> new ArrayList<>()).add(e);
        }

        while (!outgoing.isEmpty()) {
            List<BlockPos> currentLoop = new ArrayList<>();

            // Start new loop
            BlockPos startPos = outgoing.keySet().iterator().next();
            BlockPos currentPos = startPos;

            while (true) {
                currentLoop.add(currentPos);

                List<Edge> available = outgoing.get(currentPos);
                if (available == null || available.isEmpty())
                    break;
                Edge edge = available.remove(0);
                if (available.isEmpty())
                    outgoing.remove(currentPos);
                currentPos = edge.p2();
                if (currentPos.equals(startPos))
                    break;
            }
            if (currentLoop.size() >= 3) {
                loops.add(currentLoop);
            } else {
                PTDyePlus.LOGGER.warn("Created a loop that ");
            }
        }
        return loops;
    }

    private IslandGeometry classifyHullAndHoles(List<List<BlockPos>> loops) {
        loops.removeIf(l -> l.size() < 3);

        if (loops.isEmpty())
            return new IslandGeometry(Collections.emptyList(), null);

        // Format all loops (CCW Winding + SouthEast starting point)
        List<List<BlockPos>> readyLoops = new ArrayList<>();
        for (List<BlockPos> loop : loops) {
            readyLoops.add(formatLoop(loop));
        }

        // The loop with the latest area is the outer hull
        List<BlockPos> hull = readyLoops.get(0);
        double maxArea = getPolygonArea(hull);
        for (int i = 1; i < readyLoops.size(); i++) {
            double area = getPolygonArea(readyLoops.get(i));
            if (area > maxArea) {
                maxArea = area;
                hull = readyLoops.get(i);
            }
        }

        // Everything else is a hole
        List<List<BlockPos>> holes = new ArrayList<>(readyLoops);
        holes.remove(hull);

        return new IslandGeometry(hull, holes.isEmpty() ? null : holes);
    }

    // We want to format all loops to be CCW, and the first index must be LOWER-LEFT(SouthEast) point in the sequence
    private List<BlockPos> formatLoop(List<BlockPos> points) {
        if (points.size() < 3)
            return points; // This is an invalid loop

        List<BlockPos> result = new ArrayList<>(points);

        // CCW
        double windingSum = 0;
        for (int i = 0; i < points.size(); i++) {
            BlockPos p1 = points.get(i);
            BlockPos p2 = points.get((i + 1) % points.size());
            windingSum += (double) (p2.getX() - p1.getX()) * (p2.getZ() + p1.getZ());
        }
        if (windingSum > 0)
            Collections.reverse(result);

        // Find SouthEast point (Lower-Left in JM terms)
        int seIndex = 0;
        BlockPos sePos = points.get(seIndex);
        for (int i = 0; i < points.size(); i++) {
            BlockPos p = result.get(i);
            if (p.getZ() > sePos.getZ() || (p.getZ() == sePos.getZ() && p.getX() > sePos.getX())) {
                sePos = p;
                seIndex = i;
            }
        }
        if (seIndex != 0) { // Rotate so SouthEast is at index 0
            Collections.rotate(result, -seIndex);
        }

        return result;
    }

    // Shoelace area
    private double getPolygonArea(List<BlockPos> path) {
        double area = 0.0;
        int j = path.size() - 1;
        for (int i = 0; i < path.size(); i++) {
            BlockPos p1 = path.get(j);
            BlockPos p2 = path.get(i);
            area += (p1.getX() + p2.getX()) * (p1.getZ() - p2.getZ());
            j = i;
        }
        return Math.abs(area / 2.0);
    }

    protected List<BlockPos> simplify(List<BlockPos> path) {
        if (path.size() < 3)
            return path;

        List<BlockPos> simplified = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            BlockPos previous = path.get((i + path.size() - 1) % path.size());
            BlockPos current = path.get(i);
            BlockPos next = path.get((i + 1) % path.size());

            boolean collinear = (previous.getX() == current.getX() && current.getX() == next.getX()) ||
                (previous.getZ() == current.getZ() && current.getZ() == next.getZ());

            if (!collinear) {
                simplified.add(current);
            }
        }
        return simplified;
    }
}
