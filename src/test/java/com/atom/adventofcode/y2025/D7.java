package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D7 {
    private static final String TEST_INPUT = """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """;

    record Data(List<Pos> splits, Pos start, int maxY){};
    record Pos(int x, int y) {}

    private Data parseInput(String input) {
        List<Pos> results = new ArrayList<>();
        Pos start = null;
        String[] rows = input.split("\n");
        for(int i=0; i<rows.length; i++) {
            for(int j=0; j<rows[0].length(); j++) {
                if(rows[i].charAt(j) == '^') {
                    results.add(new Pos(j, i));
                }
                if(rows[i].charAt(j) == 'S') {
                    start = new Pos(j, i);
                }
            }
        }
        return new Data(results, start, rows.length);
    }

    private int countSplits(List<Pos> splits, Pos start, int maxY) {
        System.out.println(splits);

        Queue<Pos> q = new ArrayDeque<>();
        Set<Pos> visited = new HashSet<>();
        q.add(start);
        while(!q.isEmpty()) {
            Pos p = q.poll();

            // check to see if we should process
            if(p.y >= maxY)
                continue;

            if(splits.contains(p)) {
                // on a split go sideways
                Pos newP1 = new Pos(p.x-1, p.y);
                if(!q.contains(newP1)) {
                    q.add(newP1);
                }
                Pos newP2 = new Pos(p.x+1, p.y);
                if(!q.contains(newP2)) {
                    q.add(newP2);
                }
                visited.add(p);
            } else {
                q.add(new Pos(p.x, p.y+1));
            }
        }

        return visited.size();
    }

    @Test
    public void testPart1() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(21, countSplits(data.splits, data.start, data.maxY));

        data = parseInput(FileReader.readFileString("src/test/resources/2025/D7.txt"));
        assertEquals(1533, countSplits(data.splits, data.start, data.maxY));
    }

    private long countTimelines(List<Pos> splits, Pos p, int maxY, HashMap<Pos, Long> cache) {
        // depth first with caching

        if(p.y >= maxY) return 1;
        if(cache.containsKey(p)) return cache.get(p);

        long c = 0;
        if(splits.contains(p)) {
            c += countTimelines(splits, new Pos(p.x-1, p.y), maxY, cache);
            c += countTimelines(splits, new Pos(p.x+1, p.y), maxY, cache);
        } else {
            c = countTimelines(splits, new Pos(p.x, p.y+1), maxY, cache);
        }

        cache.put(p, c);
        return c;
    }

    @Test
    public void testPart2() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(40L, countTimelines(data.splits, data.start, data.maxY, new HashMap<>()));
        data = parseInput(FileReader.readFileString("src/test/resources/2025/D7.txt"));
        assertEquals(10733529153890L, countTimelines(data.splits, data.start, data.maxY, new HashMap<>()));
    }

}