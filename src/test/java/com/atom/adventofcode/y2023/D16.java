package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D16 {

    private static final String testInput = """
            .|...\\....
            |.-.\\.....
            .....|-...
            ........|.
            ..........
            .........\\
            ..../.\\\\..
            .-.-/..|..
            .|....-|.\\
            ..//.|....
            """;

    char[][] parseInput(String input) {
        String[] split = input.split("\n");
        char[][] map = new char[split.length][];
        for (int i = 0; i < split.length; i++) {
            map[i] = split[i].toCharArray();
        }
        return map;
    }

    enum Direction {NORTH, EAST, SOUTH, WEST}

    record Pos(int x, int y) {}
    record LightRay(int x, int y, Direction dir) {}

    private static Set<LightRay> rayTrace(char[][] map, LightRay startRay) {
        Set<LightRay> history = new HashSet<>();
        Queue<LightRay> queue = new LinkedList<>();
        queue.add(startRay);

        while (!queue.isEmpty()) {
            LightRay ray = queue.poll();

            if (history.contains(ray))
                continue;
            if (ray.x < 0 || ray.x >= map[0].length || ray.y < 0 || ray.y >= map.length)
                continue;

            history.add(ray);
            queue.addAll(rayTraceUpdate(map, ray));
        }
        return history;
    }

    private static List<LightRay> rayTraceUpdate(char[][] map, LightRay ray) {
        int posx = ray.x, posy = ray.y;
        Direction dir = ray.dir;

        List<LightRay> tmp = new ArrayList<>();
        switch (map[posy][posx]) {
            case '\\' -> {
                switch (dir) {
                    case NORTH -> dir = Direction.WEST;
                    case EAST -> dir = Direction.SOUTH;
                    case SOUTH -> dir = Direction.EAST;
                    case WEST -> dir = Direction.NORTH;
                }
                tmp.add(new LightRay(posx, posy, dir));
            }
            case '/' -> {
                switch (dir) {
                    case NORTH -> dir = Direction.EAST;
                    case EAST -> dir = Direction.NORTH;
                    case SOUTH -> dir = Direction.WEST;
                    case WEST -> dir = Direction.SOUTH;
                }
                tmp.add(new LightRay(posx, posy, dir));
            }
            case '|' -> {
                tmp.add(new LightRay(posx, posy, Direction.NORTH));
                tmp.add(new LightRay(posx, posy, Direction.SOUTH));
            }
            case '-' -> {
                tmp.add(new LightRay(posx, posy, Direction.WEST));
                tmp.add(new LightRay(posx, posy, Direction.EAST));
            }
            case '.' -> {
                tmp.add(new LightRay(posx, posy, dir));
            }
        }
        List<LightRay> res = new ArrayList<>();
        for (LightRay r : tmp) {
            switch (r.dir) {
                case NORTH -> res.add(new LightRay(posx, posy - 1, Direction.NORTH));
                case EAST -> res.add(new LightRay(posx + 1, posy, Direction.EAST));
                case SOUTH -> res.add(new LightRay(posx, posy + 1, Direction.SOUTH));
                case WEST -> res.add(new LightRay(posx - 1, posy, Direction.WEST));
            }
        }
        return res;
    }

    private static long evaluate(Set<LightRay> history) {
        return history.stream().map(s -> new Pos(s.x, s.y)).distinct().count();
    }

    @Test
    public void partOne() {
        char[][] map = parseInput(testInput);
        assertEquals(46, evaluate(rayTrace(map, new LightRay(0, 0, Direction.EAST))));

        char[][] map2 = parseInput(FileReader.readFileString("src/test/resources/2023/D16.txt"));
        assertEquals(7788, evaluate(rayTrace(map2, new LightRay(0, 0, Direction.EAST))));
    }

    private static long checkAllEdgeTiles(char[][] map) {

        long max = 0;
        for(int c = 0; c<map[0].length; c++) {
            max = Math.max(max, evaluate(rayTrace(map, new LightRay(c, 0, Direction.SOUTH))));
            max = Math.max(max, evaluate(rayTrace(map, new LightRay(c, map.length-1, Direction.NORTH))));
        }
        for(int r=0; r<map.length; r++) {
            max = Math.max(max, evaluate(rayTrace(map, new LightRay(0, r, Direction.EAST))));
            max = Math.max(max, evaluate(rayTrace(map, new LightRay(map[0].length-1, r, Direction.WEST))));
        }

        return max;
    }

    @Test
    public void partTwo() {
        assertEquals(51, checkAllEdgeTiles(parseInput(testInput)));

        String contents = FileReader.readFileString("src/test/resources/2023/D16.txt");
        assertEquals(7987, checkAllEdgeTiles(parseInput(contents)));
    }
}