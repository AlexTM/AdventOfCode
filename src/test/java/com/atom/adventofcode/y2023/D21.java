package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D21 {

    record Pos(int x, int y) {};

    private static Pos startPos = null;
    private static int maxx = 0, maxy = 0;

    private static Set<Pos> readInput(String content) {
        return FileReader.parseStringForObject(content, new HashSet<>(), (set, line, i) -> {
            for(int j = 0; j < line.length(); j++) {
                if(line.charAt(j) == '.')
                    set.add(new Pos(j, i));
                if(line.charAt(j) == 'S') {
                    set.add(new Pos(j, i));
                    startPos = new Pos(j, i);
                }
                maxx = Math.max(maxx, j+1);
                maxy = Math.max(maxy, i+1);
            }
            return set;
        });
    }

    private static Set<Pos> getNeighbours(Set<Pos> plots, Pos pos) {
        Set<Pos> neighbors = new HashSet<>();
        neighbors.add(new Pos(pos.x - 1, pos.y));
        neighbors.add(new Pos(pos.x + 1, pos.y));
        neighbors.add(new Pos(pos.x, pos.y - 1));
        neighbors.add(new Pos(pos.x, pos.y + 1));
        return neighbors.stream()
                .filter(p -> p.x >= 0 && p.y >= 0 && p.x < maxx && p.y < maxy)
                .filter(plots::contains).collect(Collectors.toSet());
    }

    private static Integer solveGetLastValue(Set<Pos> plots, int loops) {
        return solve(plots, loops).stream().reduce((first, second) -> second).orElse(null);
    }
    private static List<Integer> solve(Set<Pos> plots, int loops) {
        List<Integer> counts = new ArrayList<>();
        int steps = 0;
        Set<Pos> queue = new HashSet<>();
        queue.add(startPos);
        while(!queue.isEmpty()) {
            Set<Pos> nextRound = new HashSet<>();
            queue.stream().map(p -> getNeighbours(plots, p)).forEach(nextRound::addAll);
            queue = nextRound;
            counts.add(nextRound.size());
            if(++steps == loops)
                break;
        }
        return counts;
    }

    @Test
    public void partOne() {
        Set<Pos> plots = readInput(FileReader.readFileString("src/test/resources/2023/D21.txt"));
        assertEquals(3809, solveGetLastValue(plots, 64));
    }

    @Test
    public void partTwo() {

        Set<Pos> plots = readInput(FileReader.readFileString("src/test/resources/2023/D21.txt"));
        // Work out the repeating pattern.
        List<Integer> results = solve(plots, 200);
        results.forEach(System.out::println);

        // after 140 warm up
        // it gets into a repeating pattern of
        //141: 7675
        //142: 7712
        // this is the max each tile can sustain
        //
        // How long does it fill a tile?

    }
}

