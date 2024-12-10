package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D10 {

    private static final String TEST_DATA = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
        """;

    record Pos(int x, int y){};

    private Map<Pos, Integer> parseInput(String input) {
        Map<Pos, Integer> pos = new HashMap<>();
        String[] split = input.split("\n");
        for(int y=0; y<split.length; y++) {
            String line = split[y];
            for(int x=0; x<line.length(); x++) {
                if(line.charAt(x) != '.')
                    pos.put(new Pos(x, y), Character.getNumericValue(line.charAt(x)));
            }
        }
        return pos;
    }

    private Set<Pos> findZeros(Map<Pos, Integer> map) {
        return map.entrySet().stream().filter(e -> e.getValue() == 0).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    private static final List<Function<Pos, Pos>> fns = List.of(
            p -> new Pos(p.x+1, p.y),
            p -> new Pos(p.x-1, p.y),
            p -> new Pos(p.x, p.y+1),
            p -> new Pos(p.x, p.y-1)
    );

    private static Set<Pos> getNextPositions(Map<Pos, Integer> map, Pos current) {
        Set<Pos> candidates = new HashSet<>();
        for(Function<Pos, Pos> fn : fns) {
            Pos nextPos = fn.apply(current);
            if(map.containsKey(nextPos) && map.get(nextPos) - map.get(current) == 1) {
                candidates.add(nextPos);
            }
        }
        return candidates;
    }

    private static Long followPath(Map<Pos, Integer> map, Pos start, boolean returnPathNum) {
        Queue<Pos> locations = new LinkedList<>();
        locations.add(start);
        Set<Pos> visited = new HashSet<>();

        long count = 0;
        while(!locations.isEmpty()) {
            Pos current = locations.poll();

            if(map.get(current) == 9)
                count++;

            visited.add(current);
            locations.addAll(getNextPositions(map, current));
        }

        return returnPathNum ? count : visited.stream().filter(v -> map.get(v) == 9).count();
    }

    private long findAllPaths(Map<Pos, Integer> map, boolean returnPathNum) {
        long count = 0;
        for(Pos start : findZeros(map)) {
            count += followPath(map, start, returnPathNum);
        }
        return count;
    }

    @Test
    public void testPartOne() {
        Map<Pos, Integer> pos = parseInput(TEST_DATA);
        assertEquals(36, findAllPaths(pos, false));

        pos = parseInput(FileReader.readFileString("src/test/resources/2024/D10.txt"));
        assertEquals(472, findAllPaths(pos, false));
    }

    @Test
    public void testPartTwo() {
        Map<Pos, Integer> pos = parseInput(TEST_DATA);
        assertEquals(81, findAllPaths(pos, true));

        pos = parseInput(FileReader.readFileString("src/test/resources/2024/D10.txt"));
        assertEquals(969, findAllPaths(pos, true));
    }
}
