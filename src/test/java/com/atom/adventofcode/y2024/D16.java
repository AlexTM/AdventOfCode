package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D16 {
    private static final String TEST_INPUT = """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############""";
//    private static final String TEST_INPUT2 = """
//            #################
//            #...#...#...#..E#
//            #.#.#.#.#.#.#.#.#
//            #.#.#.#...#...#.#
//            #.#.#.#.###.#.#.#
//            #...#.#.#.....#.#
//            #.#.#.#.#.#####.#
//            #.#...#.#.#.....#
//            #.#.#####.#.###.#
//            #.#.#.......#...#
//            #.#.###.#####.###
//            #.#.#...#.....#.#
//            #.#.#.#####.###.#
//            #.#.#.........#.#
//            #.#.#.#########.#
//            #S#.............#
//            #################""";

    record Pos(int x, int y) {}
    record Puzzle(Set<Pos> path, Pos start, Pos end) {}

    private Puzzle parseInput(String input) {
        String[] parts = input.split("\n");

        Set<Pos> path = new HashSet<>();
        Pos start = null;
        Pos end = null;

        for(int y=0; y<parts.length; y++) {
            for(int x=0; x<parts[y].length(); x++) {
                char c = parts[y].charAt(x);
                if(c == '.') {
                    path.add(new Pos(x, y));
                } else if(c == 'S') {
                    path.add(new Pos(x, y));
                    start = new Pos(x, y);
                } else if(c == 'E') {
                    path.add(new Pos(x, y));
                    end = new Pos(x, y);
                }
            }
        }
        return new Puzzle(path, start, end);
    }

    enum Direction { NORTH, WEST, SOUTH, EAST }

    // breadth first search with Dijkstra's algorithm
    private Map<Pos, Long> solve(Puzzle puzzle) {
        Map<Pos, Long> distance = new HashMap<>();
        distance.put(puzzle.start, 0L);

        Queue<Pos> queue = new LinkedList<>();
        queue.add(puzzle.start);
        Map<Pos, Direction> visited = new HashMap<>();
        visited.put(puzzle.start, Direction.EAST);

        while(!queue.isEmpty()) {
            Pos current = queue.poll();

            Map<Pos, Direction> candidates = getCandidates(puzzle.path, current);
            for(Map.Entry<Pos, Direction> e : candidates.entrySet()) {
                long oldDistance = distance.getOrDefault(e.getKey(), Long.MAX_VALUE);
                long newDistance = distance.get(current) + 1 + (visited.get(current) == e.getValue() ? 0 : 1000);
                if(newDistance < oldDistance) {
                    distance.put(e.getKey(), newDistance);
                    visited.put(e.getKey(), e.getValue());
                    queue.add(e.getKey());
                }
            }
        }
        print(puzzle, distance, visited);
        return distance;
    }

    private void print(Puzzle puzzle, Map<Pos, Long> distance, Map<Pos, Direction> visited) {
        for(int y=0; y<15; y++) {
            for(int x=0; x<15; x++) {
                Pos p = new Pos(x, y);
                if(puzzle.path.contains(p)) {
                    if(distance.getOrDefault(p, Long.MAX_VALUE) == Long.MAX_VALUE) {
                        System.out.print("X\t\t");
                    } else {
                        System.out.print(distance.get(p)+"\t");
//                        switch (visited.get(p)) {
//                            case NORTH -> System.out.print("^");
//                            case WEST -> System.out.print("<");
//                            case SOUTH -> System.out.print("v");
//                            case EAST -> System.out.print(">");
//                        }
//                        System.out.print("\t\t");
                    }
                } else {
//                    System.out.print("#\t\t");
                    System.out.print("#\t\t");
                }
//                System.out.print("\t\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void print2(Puzzle puzzle, Set<Pos> distance) {
        for(int y=0; y<15; y++) {
            for(int x=0; x<15; x++) {
                Pos p = new Pos(x, y);
                if(puzzle.path.contains(p)) {
                    if(distance.contains(p)) {
                        System.out.print("X");
                    } else {
                        System.out.print(".");
                    }
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    private Map<Pos, Direction> getCandidates(Set<Pos> path, Pos current) {
        return Stream.of(
                new AbstractMap.SimpleEntry<>(new Pos(current.x-1, current.y), Direction.WEST),
                new AbstractMap.SimpleEntry<>(new Pos(current.x+1, current.y), Direction.EAST),
                new AbstractMap.SimpleEntry<>(new Pos(current.x, current.y-1), Direction.NORTH),
                new AbstractMap.SimpleEntry<>(new Pos(current.x, current.y+1), Direction.SOUTH))
                .filter(e -> path.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    @Test
    public void testPart1() {
        Puzzle puzzle = parseInput(TEST_INPUT);
        assertEquals(7036, solve(puzzle).get(puzzle.end));

        puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D16.txt"));
        assertEquals(102504, solve(puzzle).get(puzzle.end));
    }


    private int countWinningPaths(Puzzle puzzle, Map<Pos, Long> solve) {
        long maxValue = solve.get(puzzle.end);
        Set<Pos> c = depthFirstSearch(solve, puzzle.start, puzzle.end, maxValue, 0, new HashSet<>());

        print2(puzzle, c);

        long last = 0;
        long tot = 0;
        for(Pos p : c) {
            long vv = solve.get(p);
            long diff = vv - last;
            tot += diff;
            last = vv;
        }
        System.out.println("Diffs : "+tot);

        System.out.println(c.stream().mapToLong(cc -> solve.get(cc)).sum());

        return c.size();
    }

    private Set<Pos> depthFirstSearch(final Map<Pos, Long> solve,
                                      final Pos current, final Pos end,
                                      final long maxValue, final long currentValue,
                                      final Set<Pos> visited) {
        if(currentValue > maxValue) {
            return Set.of();
        }
        if(current.equals(end)) {
            return visited;
        }

        Set<Pos> newVisited = new HashSet<>(visited);
        newVisited.add(current);

        List<Pos> tmp = List.of(
                new Pos(current.x-1, current.y),
                new Pos(current.x+1, current.y),
                new Pos(current.x, current.y-1),
                new Pos(current.x, current.y+1));

        for(Pos p: tmp) {
            if(solve.containsKey(p) && !visited.contains(p)) {
                long diff = solve.get(p) - solve.get(current);
                Set<Pos> v = depthFirstSearch(
                        solve, p, end, maxValue,
                        currentValue + Math.abs(diff),
                        newVisited);
                if(v != null && !v.isEmpty()) {
                    return v;
                }
            }
        }
        return Set.of();


//        Set<Pos> v = Stream.of(
//                new Pos(current.x-1, current.y),
//                new Pos(current.x+1, current.y),
//                new Pos(current.x, current.y-1),
//                new Pos(current.x, current.y+1))
//                .filter(solve::containsKey)
//                .filter(p -> !visited.contains(p))
//                .map(p -> depthFirstSearch(solve, p, end, maxValue, currentValue + solve.get(p) - solve.get(current), newVisited))
//                .flatMap(Set::stream)
//                .collect(Collectors.toSet());

//        System.out.println(v);
//
//        return null;
    }


    @Test
    public void testPart2() {
        Puzzle puzzle = parseInput(TEST_INPUT);
        assertEquals(45, countWinningPaths(puzzle, solve(puzzle)));

        puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D16.txt"));
        assertEquals(0,  countWinningPaths(puzzle, solve(puzzle)));
    }


}
