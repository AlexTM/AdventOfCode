package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D12 {
    private static final String TEST_INPUT = """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE""";

    record Pos(int x, int y){};

    private Map<Pos, Character> parseInput(String input) {
        Map<Pos, Character> map = new HashMap<>();
        String[] lines = input.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length(); j++) {
                map.put(new Pos(j, i), line.charAt(j));
            }
        }
        return map;
    }


    private Set<Pos> getNextPos(final Map<Pos, Character> input, final Pos pos) {
        return surroundingSquares.stream().map(f -> f.apply(pos))
                .filter(p -> input.containsKey(p) && input.get(p) == input.get(pos))
                .collect(Collectors.toSet());
    }

    private List<Pos> findRegion(final Map<Pos, Character> input, Pos pos) {
        List<Pos> visited = new ArrayList<>();
        Queue<Pos> queue = new LinkedList<>();
        queue.add(pos);
        while(!queue.isEmpty()) {
            Pos current = queue.poll();
            if(visited.contains(current))
                continue;
            visited.add(current);
            queue.addAll(getNextPos(input, current));
        }
        return visited;
    }

    private List<List<Pos>> findAllRegions(final Map<Pos, Character> input) {
        List<List<Pos>> regions = new ArrayList<>();
        Set<Pos> visited = new HashSet<>();
        for (Pos pos : input.keySet()) {
            if(visited.contains(pos))
                continue;
            List<Pos> region = findRegion(input, pos);
            regions.add(region);
            visited.addAll(region);
        }
        return regions;
    }

    private long calculateRegionCost(final List<Pos> region, final Map<Pos, Character> input) {
        long area = region.size();
        long perimeter = region.stream().map(p -> surroundingSquares.stream().map(f -> f.apply(p))
                .filter(p1 -> input.get(p1) != input.get(p)).count()).reduce(0L, Long::sum);
        return area*perimeter;
    }

    private long calculateTotalCost(final List<List<Pos>> regions, final Map<Pos, Character> input) {
        return regions.stream().map(r -> calculateRegionCost(r, input)).reduce(0L, Long::sum);
    }

    @Test
    public void testPartOne() {
        Map<Pos, Character> input = parseInput(TEST_INPUT);
        assertEquals(1930, calculateTotalCost(findAllRegions(input), input));

        input = parseInput(FileReader.readFileString("src/test/resources/2024/D12.txt"));
        assertEquals(1488414L, calculateTotalCost(findAllRegions(input), input));
    }

    private static final List<Function<Pos, Pos>> surroundingSquares = List.of(
            p -> new Pos(p.x, p.y - 1),
            p -> new Pos(p.x + 1, p.y),
            p -> new Pos(p.x, p.y + 1),
            p -> new Pos(p.x - 1, p.y)
    );

    enum Direction { UP, RIGHT, DOWN, LEFT }
    record Edge(Pos p1, Direction direction){}

    private Direction nextDirection(List<Pos> region, Edge currentEdge) {
        switch(currentEdge.direction) {
            case RIGHT -> {
                if(region.contains(new Pos(currentEdge.p1.x+1, currentEdge.p1.y-1)) &&
                        region.contains(new Pos(currentEdge.p1.x+1, currentEdge.p1.y)))
                    return Direction.UP;
                if(region.contains(new Pos(currentEdge.p1.x+1, currentEdge.p1.y)))
                    return Direction.RIGHT;
                return Direction.DOWN;
            }
            case DOWN -> {
                if(region.contains(new Pos(currentEdge.p1.x+1, currentEdge.p1.y+1)) &&
                        region.contains(new Pos(currentEdge.p1.x, currentEdge.p1.y+1)))
                    return Direction.RIGHT;
                if(region.contains(new Pos(currentEdge.p1.x, currentEdge.p1.y+1)))
                    return Direction.DOWN;
                return Direction.LEFT;
            }
            case LEFT -> {
                if(region.contains(new Pos(currentEdge.p1.x-1, currentEdge.p1.y+1)) &&
                        region.contains(new Pos(currentEdge.p1.x-1, currentEdge.p1.y)))
                    return Direction.DOWN;
                if(region.contains(new Pos(currentEdge.p1.x-1, currentEdge.p1.y)))
                    return Direction.LEFT;
                return Direction.UP;
            }
            case UP -> {
                if(region.contains(new Pos(currentEdge.p1.x-1, currentEdge.p1.y-1)) &&
                        region.contains(new Pos(currentEdge.p1.x, currentEdge.p1.y-1)))
                    return Direction.LEFT;
                if(region.contains(new Pos(currentEdge.p1.x, currentEdge.p1.y-1)))
                    return Direction.UP;
                return Direction.RIGHT;
            }
        }
        return null;
    }

    private long calculateBulkRegionCost(List<Pos> region) {
        // If you go clockwise around the shape, always try to turn most left.
        // This will ensure you always go around the outside of the shape
        // Get top left corner, and go right
        Edge current = new Edge(region.get(0), Direction.RIGHT);
        List<Edge> visited = new ArrayList<>();

        int straight = 0;
        // run around the perimeter of the region
        Queue<Edge> queue = new LinkedList<>();
        queue.add(current);
        Direction lastDirection = null;
        while(!queue.isEmpty()) {
            current = queue.poll();

            if(visited.contains(current)) {
                break;
            }

            visited.add(current);

            Direction nextDirection = nextDirection(region, current);

            if(lastDirection != current.direction) {
                straight++;
            }
            lastDirection = current.direction;
        }
        return (long)straight*region.size();
    }

    private long calculateBulkTotalCost(List<List<Pos>> allRegions) {
        long total = 0;
        for (List<Pos> region : allRegions) {
            total += calculateBulkRegionCost(region);
        }
        return total;
    }

    // Going badly wrong
    // Might make more sense to just count corners
    @Test
    public void testPartTwo() {
        String test = """
                AAAA
                BBCD
                BBCC
                EEEC""";
        assertEquals(80, calculateBulkTotalCost(findAllRegions(parseInput(test))));


        Map<Pos, Character> input = parseInput(TEST_INPUT);
        assertEquals(1206, calculateBulkTotalCost(findAllRegions(input)));
//
//        input = parseInput(FileReader.readFileString("src/test/resources/2024/D12.txt"));
//        assertEquals(1488414L, findRegions(input));
    }

}
