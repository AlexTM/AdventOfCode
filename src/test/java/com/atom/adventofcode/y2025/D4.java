package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D4 {
    private static final String TEST_INPUT = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """;

    record Pos(int x, int y) {}

    private Set<Pos> parseInput(String input) {
        Set<Pos> results = new HashSet<>();
        String[] rows = input.split("\n");
        for(int i=0; i<rows.length; i++) {
            for(int j=0; j<rows[0].length(); j++) {
                if(rows[i].charAt(j) == '@')
                    results.add(new Pos(i, j));
            }
        }
        return results;
    }

    private Set<Pos> findAccessible(Set<Pos> positions) {
        Set<Pos> results = new HashSet<>();
        for(Pos p : positions) {
            int prox = 0;
            for(int i=-1; i<=1; i++) {
                for(int j=-1; j<=1; j++) {
                    if(i==0 && j==0) continue;
                    if(positions.contains(new Pos(p.x+i, p.y+j)))
                        prox++;
                }
            }
            if(prox < 4) {
                results.add(p);
            }
        }
        return results;
    }

    @Test
    public void testPart1() {
        assertEquals(13, findAccessible(parseInput(TEST_INPUT)).size());
        assertEquals(1480, findAccessible(parseInput(FileReader.readFileString("src/test/resources/2025/D4.txt"))).size());
    }

    private int repeatUntilZero(Set<Pos> positions) {
        int count = 0;
        while(true) {
            var last = findAccessible(positions);
            if(last.isEmpty())
                break;
            positions.removeAll(last);
            count += last.size();
        }
        return count;
    }

    @Test
    public void testPart2() {
        assertEquals(43, repeatUntilZero(parseInput(TEST_INPUT)));
        assertEquals(8899, repeatUntilZero(parseInput(FileReader.readFileString("src/test/resources/2025/D4.txt"))));
    }

}
