package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D4 {
    private static final String TEST_INPUT1 = "MMMSXXMASM\nMSAMXMSMSA";
    private static final String TEST_INPUT2 = """
            ..X...
            .SAMX.
            .A..A.
            XMAS.S
            .X....""";

    private static final String TEST_INPUT = """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX""";


    private Puzzle parseInput(String input) {
        Map<Position, Character> map = new HashMap<>();
        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                map.put(new Position(x, y), line.charAt(x));
            }
        }
        return new Puzzle(map, lines[0].length(), lines.length);
    }

    record Position(int x, int y) { }
    record Puzzle(Map<Position, Character> map, int x, int y) { }

    private int findXMAS(Puzzle puzzle) {

        int count = 0;
        for(int y = 0; y < puzzle.y; y++) {
            for(int x = 0; x < puzzle.x; x++) {
                if(XmasFromHere(puzzle, x, y, 0) > 0)
                    count++;
            }
        }

        return count;
    }

    private static final char[] XMAS = "XMAS".toCharArray();
    private static final Set<Position> visited = new HashSet<>();

    private int XmasFromHere(Puzzle puzzle, int x, int y, int location) {
        if (location == XMAS.length) {
            return 1;
        }

        Position position = new Position(x, y);
        if (!puzzle.map.containsKey(position) || puzzle.map.get(position) != XMAS[location]) {
            return 0;
        }

        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                count += XmasFromHere(puzzle, x + i, y + j, location + 1);

            }
        }

        if(count!=0) {
            visited.add(position);
        }

        return count;
    }

    @Test
    public void testPart1() {
        Puzzle puzzle = parseInput(TEST_INPUT);
        int count = findXMAS(puzzle);
        System.out.println(count);
        printVisited(puzzle);

        assertEquals(18, findXMAS(puzzle));

        puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D4.txt"));
        assertNotEquals(3692, findXMAS(puzzle));
        assertEquals(0, findXMAS(puzzle));
    }

    private void printVisited(Puzzle puzzle) {
        for(int y = 0; y < puzzle.y; y++) {
            for(int x = 0; x < puzzle.x; x++) {
                if(visited.contains(new Position(x, y))) {
                    System.out.print(puzzle.map.get(new Position(x, y)));
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}
