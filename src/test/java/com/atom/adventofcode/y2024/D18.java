package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D18 {
    private static final String TEST_INPUT = """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0""";

    record Pos(int x, int y) {}

    private List<Pos> parseInput(String input) {
        String[] parts = input.split("\n");
        List<Pos> points = new ArrayList<>();
        for (String part : parts) {
            String[] xy = part.split(",");
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            points.add(new Pos(x, y));
        }
        return points;
    }

    private Set<Pos> fallNBits(List<Pos> mem, int n, int x, int y) {
        Set<Pos> fallen = new HashSet<>();
        for(int i=0; i<n; i++) {
            fallen.add(mem.get(i));
        }
        Set<Pos> path = new HashSet<>();
        for(int nx = 0; nx < x; nx++) {
            for(int ny = 0; ny < y; ny++) {
                Pos p = new Pos(nx, ny);
                if(!fallen.contains(p)) {
                    path.add(p);
                }
            }
        }
        return path;
    }

    private Set<Pos> getCandidates(Set<Pos> path, Pos current) {
        return Stream.of(
                new Pos(current.x-1, current.y),
                new Pos(current.x+1, current.y),
                new Pos(current.x, current.y-1),
                new Pos(current.x, current.y+1))
                .filter(path::contains)
                .collect(Collectors.toSet());
    }

    private Map<Pos, Long> solve(Set<Pos> path) {
        Pos start = new Pos(0,0);

        Map<Pos, Long> distanceMap = new HashMap<>();
        distanceMap.put(start, 0L);

        Queue<Pos> queue = new LinkedList<>();
        queue.add(start);

        while(!queue.isEmpty()) {
            Pos current = queue.poll();

            Set<Pos> candidates = getCandidates(path, current);
            for(Pos e : candidates) {
                long oldDistance = distanceMap.getOrDefault(e, Long.MAX_VALUE);
                long newDistance = distanceMap.get(current) + 1;
                if(newDistance < oldDistance) {
                    distanceMap.put(e, newDistance);
                    queue.add(e);
                }
            }
        }
        return distanceMap;
    }

    @Test
    public void testPartOne() {
        List<Pos> points = parseInput(TEST_INPUT);
        Set<Pos> path = fallNBits(points, 12, 7, 7);
        Map<Pos, Long> distance = solve(path);
        assertEquals(22, distance.get(new Pos(6, 6)));

        points = parseInput(FileReader.readFileString("src/test/resources/2024/D18.txt"));
        path = fallNBits(points, 1024, 71, 71);
        distance = solve(path);
        assertEquals(360, distance.get(new Pos(70, 70)));
    }

    private Pos findFirstBlocking(List<Pos> points, int start, int maxX, int maxY) {
        Set<Pos> path = fallNBits(points, start, maxX, maxY);
        Pos end = new Pos(maxX-1, maxY-1);
        for(int i=start; i<points.size(); i++) {
            Pos p = points.get(i);
            path.remove(p);
            Map<Pos, Long> distance = solve(path);
            if(distance.get(end) == null) {
                return p;
            }
        }
        return null;
    }

    @Test
    public void testPartTwo() {
        List<Pos> points = parseInput(FileReader.readFileString("src/test/resources/2024/D18.txt"));
        Pos res = findFirstBlocking(points, 1024, 71, 71);
        assertEquals("58, 62", res.x + ", " + res.y);
    }
}
