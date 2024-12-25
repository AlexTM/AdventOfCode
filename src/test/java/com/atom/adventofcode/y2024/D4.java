package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D4 {
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
    private static final char[] XMAS = "XMAS".toCharArray();

    private static final List<Function<Position, Position>> operations = List.of(
            p -> new Position(p.x + 1, p.y),
            p -> new Position(p.x - 1, p.y),
            p -> new Position(p.x, p.y + 1),
            p -> new Position(p.x, p.y - 1),
            p -> new Position(p.x + 1, p.y + 1),
            p -> new Position(p.x - 1, p.y - 1),
            p -> new Position(p.x + 1, p.y - 1),
            p -> new Position(p.x - 1, p.y + 1)
    );

    private int XmasFromHere(Puzzle puzzle, Position position) {
        int count = 0;
        for(Function<Position, Position> operation : operations) {
            Position next = position;
            int i = 0;
            while(i < XMAS.length) {
                if(!puzzle.map.containsKey(next) || puzzle.map.get(next) != XMAS[i]) {
                    break;
                }
                next = operation.apply(next);
                i++;
            }
            if(i == XMAS.length) {
                count++;
            }
        }

        return count;
    }

    private int findXMAS(Puzzle puzzle) {
        int count = 0;
        for(int y = 0; y < puzzle.y; y++) {
            for(int x = 0; x < puzzle.x; x++) {
                count += XmasFromHere(puzzle, new Position(x, y));
            }
        }
        return count;
    }

    @Test
    public void testPartOne() {
        assertEquals(18, findXMAS(parseInput(TEST_INPUT)));
        assertEquals(2554, findXMAS(parseInput(FileReader.readFileString("src/test/resources/2024/D4.txt"))));
    }

    private boolean spellsMAS(Position p1, Position p2, Map<Position, Character> map) {
        if(!map.containsKey(p1) || !map.containsKey(p2)) {
            return false;
        }
        return map.get(p1) == 'M' && map.get(p2) == 'S' || map.get(p1) == 'S' && map.get(p2) == 'M';
    }

    private int findMASasX(Puzzle puzzle) {
        Set<Position> apositions = puzzle.map.entrySet().stream()
                .filter(e -> e.getValue() == 'A')
                .map(Map.Entry::getKey).collect(Collectors.toSet());

        // for evert a check if is an M and S either diagonally
        int count = 0;
        for(Position a : apositions) {
            Position p1 = new Position(a.x - 1, a.y - 1);
            Position p2 = new Position(a.x + 1, a.y + 1);
            Position p3 = new Position(a.x - 1, a.y + 1);
            Position p4 = new Position(a.x + 1, a.y - 1);
            if(spellsMAS(p1, p2, puzzle.map) && spellsMAS(p3, p4, puzzle.map)) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void testPartTwo() {
        assertEquals(9, findMASasX(parseInput(TEST_INPUT)));
        assertEquals(1916, findMASasX(parseInput(FileReader.readFileString("src/test/resources/2024/D4.txt"))));
    }

}
