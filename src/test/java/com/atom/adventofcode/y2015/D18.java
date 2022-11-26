package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D18 {

    private static final String initialStateTest =
            ".#.#.#\n" +
            "...##.\n" +
            "#....#\n" +
            "..#...\n" +
            "#.#..#\n" +
            "####..";

    private static final String initialState =
            "###.##..##.#..#.##...#..#.####..#.##.##.##..###...#....#...###..#..###..###.#.#.#..#.##..#...##.#..#\n" +
            ".#...##.#####..##.......#..####.###.##.#..###.###.....#.#.####.##.###..##...###....#.##.....#.#.#.##\n" +
            ".....#.#.....#..###..####..#.....##.#..###.####.#.######..##......#####.#.##.#########.###..#.##.#.#\n" +
            "...###......#.#..###..#.#.....#.##..#.##..###...#.##.#..#..#.##.#..##......##.##.##.######...#....##\n" +
            ".###.....#...#.#...####.#.###..#..####.#..#.##..####...##.#...#..###...###...####..##....####.##..#.\n" +
            "..#....#...#.......#..###.###....#.##..#.....###.#.##.#....#.#....##.##..#.##.#..###.###.##.##..##.#\n" +
            "##..#####.#.#....#.#...#.#.####..#....#..#....#.#..#.#####...#..##.#.....#.##..##.####......#.#.##..\n" +
            ".#..##..#.#.###..##..##...#....##...#..#.#..##.##..###.####.....#.####.#.....##.#.##...#..####..#...\n" +
            "#.#####.......#####...#...####.#.#.#....#.###.#.##.#####..#.###.#..##.##.#.##....#.##..#....####.#.#\n" +
            "#.##...#####....##.#.#.....##......##.##...#.##.##...##...###.###.##.#.####.####.##..#.##.#.#.####..\n" +
            "#.##.##....###.###.#..#..##.##.#..#.#..##..#.#...#.##........###..#...##.#.#.##.......##.....#...###\n" +
            "###..#.#..##.##.#.#.#...#..#...##.##.#.########.......#.#...#....########..#.#.###..#.#..#.##..#####\n" +
            "####.#.#...#.##.##..#.#...#....#..###..#.#.#.####.#.##.##.#..##..##..#..#####.####.##..########..##.\n" +
            ".#.#...#..##.#..#..###.#..####.......##.#.#.#.##.#####..#..##...#.##...#..#....#..#..###..####.#....\n" +
            "..#.#...#....##...#####..#..#...###.###.....#.###.#....#.#..##...#.##.##.####.#.#.#..#.##.#....#.#..\n" +
            "#....###.####.##..#.#.###..###.##.##..#.#...###..#.##.#####.##.#######..#.#...##.#..........####.###\n" +
            "#.#####.#......#.#......#.....##...##.#.#########.#......##..##..##.#..##.##..#....##...###...#.#...\n" +
            "#..#..##..###.#.#.#.#.....###.#.####.##.##....#.#..##....#.#..#.####..###.##...#######.#####.##.#.#.\n" +
            "..###.#........##.#...###..#.##..#.#....##.#......#..#.##..#.#..#.#..#.####.#####..###.##..#.##.#...\n" +
            "##.###....#..##...#..#.#......##..#...#..#.####..#.##...##.####.#...#..###...#.#.#....###.##..#.#...\n" +
            "..##.##.#.##..##.#..#.###...##..##..#....##..##...####.#..####.###...#.....#..#.##..##..###..#.#...#\n" +
            "#.#....#.....#...##.#...####..#..##..##.####..##..##...####...#....##.#.#######..##.#......######.#.\n" +
            "#.#...###.######.######..##..##....#.#......#......#.#.##.#.##.#.#.#...#...#....#.#.#.#..#.##..#...#\n" +
            "####.###.#.#.##..#.##.#...#.##...#.##.##...#.....#.#..#.####.##..######.#..#.#..##....#.#.#..#.#.#.#\n" +
            "..##......#.#...#.##.##..##..##..#..##..#########.#..###..###.##...#..##.#..#.#.#.######..#....#.#..\n" +
            "..##.##.#...###.#...##..######.##.#..####..#..#.#.##.####.##.##.#...##....#...###.##.####..#....#.#.\n" +
            "####...###..#.#.##.#.#....###..##.#.#..########..#...#.#...#.##....##.##...#.....#.#.....#.....#....\n" +
            ".#.###############....#.##..###..#.####.#.##.##..#..#.#...###...##..##.##.#.....##...###.###.....#..\n" +
            ".###..#..##.##..####.#.###.##.##..#..##....#.#......#......##.#...#.#...#..##.#.#...#...#.##..#.##..\n" +
            "###.#.#.########.#.#..####.#..##.#.##.##.###.##..######...#..##.##.#..#.#...#.##..#####.....#.#.#..#\n" +
            ".##.##..#.#...#####.#.#.###...##...####...#......#...#..####..#.##..........#..#.#..###....######.##\n" +
            "..#####...#.#.#.#..#.##..#...#.#..#.##...##..##.##.#.##.#..#.#...#.......##.#...###.....#...#.#.#.##\n" +
            "##.##.#..######.##...#.....#.###.#..##.#.#.#..####.#....##.#....####...##....#.#.##.#..###.##.##..##\n" +
            ".###.##.#..#.###.####..#.##..####.#.#.##..###.#######.###.###...####........##....###.#...#.#.####.#\n" +
            "........#..#.#..##..########..........#.##.#..##.#...#.....####....##..#..#.#####.###...#...#.##.###\n" +
            ".....#..##.####...##.#####..######.##.#.###.####.##.##.#..##.##.######.##......#..#.####..##....#.##\n" +
            "##...####....#.##.##.###....#.#...#.####..##.#.##.#.#...####.#.#.#.#...##.###...##...###...######.##\n" +
            ".#....#.#.####...#.##.....##...###.#.#.##...##.#####....#.######.#.#....##..##...##....##.#.##.#.#.#\n" +
            ".###..###.#.......#.#######..#.#.#.######....#.#####.#.....#.#########...#....##...##.####.#..#.....\n" +
            "##.#..##..##.....#..##...#..##.##.#..#.#####.##.##.#.##.##...##.######.####..#.##..#####.##...##..#.\n" +
            "#.###...##.#.#.#.##....#.#.##.##..#....#...#.#.........#..#..####..####.####..#.##.##.#....####..##.\n" +
            ".#..######..#####.####.##.#.....#.#.#####..##..###.#.#.#..#.#...#.#######..##....##.##...#######..#.\n" +
            "#...#....#.#.##..#####..#########..#.....#...##.#.#.###...#####..##...##...####.......#######.#..###\n" +
            ".#......#...##.###..#....#...#.#.....#.#...##.#.#..#..###.##.###.#.##..##...#.##......#.###..#.#..##\n" +
            ".#....####...###..#.....##..#...#.#.###.#.#.##...#.##.##.#.#.#..####..###.#.#.#.##.#.#...#..#...####\n" +
            "......##.##.#...#####.##..#.###..#.#####..##.#..##.###......#...#...#..#......###.######...#.#.##..#\n" +
            "###..#...#.##..###.#....##...#..#####.#.#..#.###...#####.#....##..####.#.##...#.#...##..#.#.#.#..#.#\n" +
            "...##.#.##.##..#.#.#.###.#.#...#.....###.###.##...#.###.##...##..#..###.#..##.##..###.#....###..##..\n" +
            ".##.#..###..###.##.##...#..#####...#.....#####.##..####...#.##.#.#..##.#.#.#....###.....#....##.....\n" +
            "######.#..#.#..#....#.###...####.####.#.........#..##.#..##..##.....#..#.##.##...#...#####.#.##..#.#\n" +
            ".##.###...####....#.####...#####..#..#...#..#.....###.#..#.###..#.###.#.......##.####..#.##.#...##..\n" +
            "........#.#.##.#.....#####.###......##..#.##.#..#...####.#...#..###.#.#...##..#.#...#.####...#.#.###\n" +
            ".#..#.##..##...######.###.##.#.#...#.#.#.#.##..##..##.#.##..#....#.##...#.##.##...##....##.###.##.#.\n" +
            "##...#...#...###.#.#...#...#..###......##.#.#....##..##.#..##.#.######...#..##.#.##.#.#....#.##.##..\n" +
            "...#..###.#....#...#.##..##.#.##.#..###.##..#.##..####.#########....#.....##.#.##.##..##.##.######.#\n" +
            "#.##.#..##.......###...#.###....###.#..####..##.#####.##.###....##....#.###...####..#.#.#.##.....###\n" +
            ".......#...#...##.#...##.#.#..#.##..##.#....###...##.#####...#.........#.......###.##.#.#.###....##.\n" +
            "###.#.##.##.....#.#..#.#..####.####..#..###..........####.#.##...#######.###..#####..#.....#..###..#\n" +
            "#...##.##..####.##.###.#.#######..###.#..#######..#.##.####...#..#.##.####..####.#.#.......####.#...\n" +
            "...#.##..#..#..##........#.#..#..#.#....#.###.#.###..#.......###..#.....#....#..##.#...#.###...##.#.\n" +
            "###.##..#.##.#.#####..#.##.####....#####..###.#.#..#...#...###.#.##..#.#.#.....#.####.#.#.#.#.#.#...\n" +
            "..##..##..#..##.##.#...#..#....####....#...#..####..#.....######.###.####.#....##....##.#.#.###....#\n" +
            ".#.#.#.##..####..#.....#.####.#....#.....#....#.##..#.#..#.#...#.#.#.#..#..#..##.#....####.......#..\n" +
            "..##.##..###......#...#..##...#.###.####.#...#.####..#.#.#.....#.#...####...#.########.##.#.#.#..###\n" +
            "#....#.##.....##.###.##.###..#.####.....####.##...#..##.###...###..###.#....####.#..#..#..#.#..##.#.\n" +
            ".#.#.##....#.##......#.#..###.#....###....#......#.#.##.##.#########..##..#...#.####..#...####..#..#\n" +
            ".#.#.......##.#.##.#...#...#.##.#..#.#.#.##....#..###.###.##.#.#...##.#..#..##....#..###.#...#.#.##.\n" +
            "#.##.#....####...#..##..#.#.#.#.##.#...#####.#...#..#..#.####.####.#.#....#......##..##..###...#..##\n" +
            "..##.###..##.####..#..#..##...###.#.#.#######.####...####......##.##..#...#.##...##....#..#..#.....#\n" +
            "....#..#..#.#.####.#...##..#....####.#..####...#.#...###...#..#..##...#....##...#.....#.#..#.#.#...#\n" +
            "...#.#.#.##..##.###..#.######....####.###...##...###.#...##.####..#.#..#.#..#.##.....#.#.#..##......\n" +
            ".#.##.##.....##.#..###.###.##....#...###.#......#...##.###.#.##.##...###...###...#.######..#......#.\n" +
            "###..#...#......#..##...#....##.#..###.##.####..##..##....####.#...#.#....##..#.#######..#.#.#####..\n" +
            "##...#####..####..##....#.#.###.##.#..#.#..#.....###...###.#####.....#..##.#......#...#.###.##.##...\n" +
            "...#.#.#..#.###..#.#.#....##.#.#..####.##.#.####.#.#.#...#....##....#.##.####..###.#.#...##.#..#..##\n" +
            "#.#.#..#.##..##.##.#...##.#....#...###..##..#.#######.#.###..##......##.#..###.########.#.##..#.#.##\n" +
            "######.###....##..#..#...####....#.#.#..#...#..######.#.#.##..##....##....##.##.##...#..#.####.#.#..\n" +
            "#####.###..#..###......##...##.####.#.#.#.###.......##..##.####..##.####.#..#..####..#.####.#####...\n" +
            "##.#.#.###..##.#.##.#.#.#.##.#...##........###.#.##..####....###.#.####.####.#.......##.##.##...##..\n" +
            "#.#..###...#..##.....##.#..#.#..##..######.#####...###.#.......###...#..##..#..#..##.#.#....#..#..#.\n" +
            "#.#..####.###..#...#...#...#.###..#.#.#.#.#.#.#..#....#.##.##.##..###..####.#..##..##.###.###....##.\n" +
            "#..#.##.#####........#..#.##.#..##.#...#....#..#.##..###..##..##.##..#..##.#.#...#.#.##.#.##....#.#.\n" +
            ".......##..#.....#..#.#.....#.##...####.###..####..#.#.#.#..#.....#....##...#..#.##..###.#.#....#...\n" +
            "#...###########.##.....##...###.#.##.##..####.##...#.####.#####.#####.####...###.##...##..#.#.###..#\n" +
            "....#.#.###.####.###...#...#.#..###.#.#.##...#..#.#.#..#.####..#..###.######.#.####.###...###.#.##.#\n" +
            ".....#..#..########...#.#.#.#.#.#.#.#..###.##..####...##.#.#.#...##..#####.##.#...#.####.#######.##.\n" +
            ".......#...#.#..#..#...#..#..##.....#.##....##.##...##..##.##...##...#.#..#.##.#.###.#.####.#.#..##.\n" +
            ".####...#...#.#.#....##..........##.##.###.##.#.#..#.#.#......########.#...#.####.##.###..##...####.\n" +
            "#.#.#...##.###..##..#..#.....####.#.....##.##.#..#.#.###.#..#######...##..#.#..#.#..############.###\n" +
            ".##..####.#..#.....###..#..#.#.....#.#.#...##.##.#....#..#..###.#...#....#.#...####..#.....###.####.\n" +
            "..#...#.###.###....##.#..#.##..####.##.#.##.##.##...###.####..#.#.#.##.#.#.#..###..##.##.##.##.#..##\n" +
            "#...............##.....######.#.#####.##.#....#.#..#.##...#.##....#........##.##...#.##.##.#..#.##.#\n" +
            "#..##..#.#.#.##.#..#.#.##.##...#...#..#.#.##..#.#...###...##...###..#####.#.#..#..#.#..#.#.##...##.#\n" +
            ".#######.#.....##...#.#.####.######.#..#......#....##.#.#..#..###.#...###...#....#.#..#.##.#...#.#..\n" +
            "#.###......##.#.##..#.###.###..####..##....#..###......##..##..#####.####....#...###.....###.#..#...\n" +
            "###...#....###.#..#.###.##...###.##.......##.##.#.#.#....####....###..##.###...#..##....#.#.##..##..\n" +
            ".##.......##.######.#.#..#..##....#####.###.#.##.....####....#......####....#.##.#.##..#.##...##.#.#\n" +
            ".#.###...#.#.#.##.###..###...##..#.##.##..##..#.....###.#..#.##.##.####........##.#####.#.#....#...#\n" +
            "##...##..#.##.#######.###.#.##.#####....##.....##.#.....#.#.##.#....#.##.#....##.#..#.###..#..#.#...\n" +
            ".#..#.#.#.#...#.##...###.##.#.#...###.##...#.#..###....###.#.###...##..###..#..##.##....###...###.##\n";

    record Pos(int c, int r){};

    private static List<Pos> parseState(String inp) {
        List<Pos> pos = new ArrayList<>();
        String[] rows = inp.split("\\n");
        for(int r=0; r<rows.length; r++) {
            for(int c=0; c<rows[0].length(); c++) {
                if(rows[r].charAt(c) == '#')
                   pos.add(new Pos(c, r));
            }
        }
        return pos;
    }

    @Test
    public void testLights() {
        assertEquals(4, lightSteps(6,6, new HashSet<>(parseState(initialStateTest)), 4, new HashSet<>()));
        assertEquals(821, lightSteps(100,100, new HashSet<>(parseState(initialState)), 100, new HashSet<>()));
        assertEquals(17, lightSteps(6,6, new HashSet<>(parseState(initialStateTest)), 5, Set.of(
                new Pos(0, 0),
                new Pos(5, 0),
                new Pos(0, 5),
                new Pos(5, 5))));
        assertEquals(886, lightSteps(100,100, new HashSet<>(parseState(initialState)), 100, Set.of(
                new Pos(0, 0),
                new Pos(99, 0),
                new Pos(0, 99),
                new Pos(99, 99))));
    }

    private int lightSteps(final int columns, final int rows, Set<Pos> allPos, final int steps, final Set<Pos> brokenLights) {

        allPos.addAll(brokenLights);

        for(int step = 0; step < steps; step++) {
            Set<Pos> nextStep = new HashSet<>();

            for(int row=0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Pos p = new Pos(column, row);
                    Set<Pos> candidates = generateCandidates(p, columns, rows);
                    candidates.retainAll(allPos);

                    if(allPos.contains(p)) {
                        if(candidates.size() == 2 || candidates.size() == 3)
                            nextStep.add(p);
                    } else {
                        if(candidates.size() == 3)
                            nextStep.add(p);
                    }
                }
            }

            allPos = nextStep;
            allPos.addAll(brokenLights);
        }

        return allPos.size();
    }

    private Set<Pos> generateCandidates(Pos pos, int columns, int rows) {
        Set<Pos> cand = new HashSet<>();
        for(int c = pos.c-1; c< pos.c+2; c++) {
            for(int r = pos.r-1; r < pos.r+2; r++) {
                if(c >= 0 && c < columns && r >= 0 && r < rows)
                    cand.add(new Pos(c, r));
            }
        }
        cand.remove(pos);
        return cand;
    }
}
