package com.gdd.ptdyeplus.features.territories.server;

import com.gdd.ptdyeplus.features.territories.common.Edge;
import com.gdd.ptdyeplus.features.territories.common.IslandGeometry;
import com.gdd.ptdyeplus.features.territories.common.TerritoryGeometrySyncPacket;
import com.gdd.ptdyeplus.init.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

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
            Map<Edge, Integer> edgeCounts = new HashMap<>();
            for (ChunkPos chunk : island) {
                int minX = chunk.getMinBlockX();
                int maxX = minX + 16; // getMaxBlockN, returns inclusive(+15), we want exclusive, so we add +16
                int minZ = chunk.getMinBlockZ();
                int maxZ = minZ + 16;

                BlockPos nw = new BlockPos(minX, 0, minZ);
                BlockPos ne = new BlockPos(maxX, 0, minZ);
                BlockPos se = new BlockPos(maxX, 0, maxZ);
                BlockPos sw = new BlockPos(minX, 0, maxZ);

                addEdge(edgeCounts, new Edge(nw, ne));
                addEdge(edgeCounts, new Edge(ne, se));
                addEdge(edgeCounts, new Edge(se, sw));
                addEdge(edgeCounts, new Edge(sw, nw));
            }
            List<Edge> boundaryEdges = edgeCounts.entrySet().stream()
                .filter(e -> e.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

            List<List<BlockPos>> loops = extractLoops(boundaryEdges);

            loops.replaceAll(this::simplify);

            allGeometries.add(classifyHullAndHoles(loops));
        }
        return allGeometries;
    }

    protected void addEdge(Map<Edge, Integer> counts, Edge edge) {
        counts.put(edge, counts.getOrDefault(edge, 0) + 1);
    }

    protected List<List<BlockPos>> extractLoops(List<Edge> edges) {
        List<List<BlockPos>> loops = new ArrayList<>();

        Map<BlockPos, List<Edge>> pointMap = new HashMap<>();
        for (Edge e : edges) {
            pointMap.computeIfAbsent(e.p1(), k -> new ArrayList<>()).add(e);
            pointMap.computeIfAbsent(e.p2(), k -> new ArrayList<>()).add(e);
        }

        Set<Edge> unvisited = new HashSet<>(edges);
        while (!unvisited.isEmpty()) {
            List<BlockPos> currentLoop = new ArrayList<>();

            // Pick any remaining edges to start a new loop
            Edge startEdge = unvisited.iterator().next();
            unvisited.remove(startEdge);

            // Determine direction
            BlockPos current = startEdge.p2();
            currentLoop.add(startEdge.p1());
            currentLoop.add(current);

            while (true) {
                List<Edge> neighbors = pointMap.get(current);
                Edge nextEdge = null;

                // Find the neighbor that is still unvisited
                if (neighbors != null) {
                    for (Edge e : neighbors) {
                        if (unvisited.contains(e)) {
                            nextEdge = e;
                            break;
                        }
                    }
                }

                if (nextEdge == null)
                    break; // Loop is closed

                unvisited.remove(nextEdge);

                // Move to next point
                current = (nextEdge.p1().equals(current)) ? nextEdge.p2() : nextEdge.p1();
                currentLoop.add(current);
            }
            if (currentLoop.get(0).equals(currentLoop.get(currentLoop.size() - 1))) {
                currentLoop.remove(currentLoop.size() - 1);
            }
            loops.add(currentLoop);
        }
        return loops;
    }

    private IslandGeometry classifyHullAndHoles(List<List<BlockPos>> loops) {
        if (loops.isEmpty())
            return new IslandGeometry(Collections.emptyList(), null);

        // The loop with the latest area is the outer hull
        List<BlockPos> hull = loops.get(0);
        double maxArea = getPolygonArea(hull);
        for (int i = 1; i < loops.size(); i++) {
            double area = getPolygonArea(loops.get(i));
            if (area > maxArea) {
                maxArea = area;
                hull = loops.get(i);
            }
        }

        // Everything else is a hole
        List<List<BlockPos>> holes = new ArrayList<>(loops);
        holes.remove(hull);

        return new IslandGeometry(hull, holes.isEmpty() ? null : holes);
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
