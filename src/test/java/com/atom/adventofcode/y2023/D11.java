package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D11 {

    private static final String input = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """;

    record Position(long x, long y) {}

    private FileReader.TriFunction<HashSet<Position>, String, Integer, HashSet<Position>> tri = (map, line, yy) -> {
        for(int xx=0; xx<line.length(); xx++) {
            if(line.charAt(xx) == '#') {
                map.add(new Position(yy, xx));
            }
        }
        return map;
    };

    private HashSet<Position> expandGalaxy(HashSet<Position> positions, int size) {

        Set<Long> xs = positions.stream().mapToLong(p -> p.x).boxed().collect(Collectors.toSet());
        Set<Long> ys = positions.stream().mapToLong(p -> p.y).boxed().collect(Collectors.toSet());

        // maxx. maxy
        long maxx = xs.stream().mapToLong(i -> i).max().orElseThrow();
        long maxy = ys.stream().mapToLong(i -> i).max().orElseThrow();

        List<Long> rangex = LongStream.range(0, maxx).boxed().collect(Collectors.toList());
        List<Long> rangey = LongStream.range(0, maxy).boxed().collect(Collectors.toList());

        rangex.removeAll(xs);
        rangey.removeAll(ys);

        HashMap<Integer, Position> numberedGalaxies = new HashMap<>();
        int count = 0;
        for(Position p : positions) {
            numberedGalaxies.put(count++, p);
        }

        // for all rows and columns in rangex and rangey, increase the galaxy positions
        rangex.sort(Collections.reverseOrder());
        for(long x : rangex) {
            for(Map.Entry<Integer, Position> e : numberedGalaxies.entrySet()) {
                if(e.getValue().x > x) {
                    numberedGalaxies.put(e.getKey(), new Position(e.getValue().x+size, e.getValue().y));
                }
            }
        }
        rangey.sort(Collections.reverseOrder());
        for(long y : rangey) {
            for(Map.Entry<Integer, Position> e : numberedGalaxies.entrySet()) {
                if(e.getValue().y > y) {
                    numberedGalaxies.put(e.getKey(), new Position(e.getValue().x, e.getValue().y+size));
                }
            }
        }

        return new HashSet<>(numberedGalaxies.values());
    }

    private long shortestPath(Position a, Position b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private long shortestPath(HashSet<Position> map) {
        // get a value from Map
        List<Position> list = new ArrayList<>(map);
        long result = 0;

        for(int i=0; i<map.size(); i++) {
            Position p = list.get(i);
            // get all other values from map
            for(int j=i+1; j<map.size(); j++) {
                Position p2 = list.get(j);
                result += shortestPath(p, p2);
            }
        }
        return result;
    }

    @Test
    public void partOne() {
        HashSet<Position> map = FileReader.parseStringForObject(input, new HashSet<>(), tri);
        map = expandGalaxy(map, 1);
        assertEquals(374, shortestPath(map));

        HashSet<Position> map2 = FileReader.readFileForObject("src/main/resources/2023/D11.txt", new HashSet<>(), tri);
        map2 = expandGalaxy(map2, 1);
        assertEquals(9556712, shortestPath(map2));
    }

    @Test
    public void partTwo() {
        // Not sure why -1 on the expansion, but is seems current work
        HashSet<Position> map = FileReader.parseStringForObject(input, new HashSet<>(), tri);
        assertEquals(1030, shortestPath(expandGalaxy(map, 9)));
        assertEquals(8410, shortestPath(expandGalaxy(map, 99)));

        HashSet<Position> map2 = FileReader.readFileForObject("src/main/resources/2023/D11.txt", new HashSet<>(), tri);
        long r = shortestPath(expandGalaxy(map2, 999999));
        assertEquals(678626199476L, r);
    }

}
