package com.atom.adventofcode.y2022;
import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D24 {

    enum Direction{ N, E, S, W, NONE }
    record Pos(int x, int y){}
    record Blizzard(Pos p, Direction d){
        public Blizzard(int x, int y, Direction d) {
            this(new Pos(x, y), d);
        }
    }

    static class Valley {
        final List<Blizzard> blizzards = new ArrayList<>();
        int maxx, maxy;
    }

    private static void print(Valley v, Pos pos) {
        Map<Pos, List<Blizzard>> blizzardPos = new HashMap<>();
        for(Blizzard b : v.blizzards) {
            List<Blizzard> bb = blizzardPos.getOrDefault(b.p, new ArrayList<>());
            bb.add(b);
            blizzardPos.put(b.p, bb);
        }

        for (int y = 0; y < v.maxy; y++) {
            for (int x = 0; x < v.maxx; x++) {
                Pos p = new Pos(x, y);

                if(pos != null && pos.equals(p)) {
                  System.out.print("X");
                } else if(blizzardPos.containsKey(p)) {
                    int value = blizzardPos.get(p).size();
                    if(value == 1) {
                        switch (blizzardPos.get(p).get(0).d) {
                            case N -> System.out.print("^");
                            case S -> System.out.print("v");
                            case W -> System.out.print("<");
                            case E -> System.out.print(">");
                            default -> System.out.print(" ");
                        }
                    } else {
                        System.out.print(value);
                    }
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private static Valley parseLine(Valley valley, String line, Integer y) {
        for(int x=0; x<line.length(); x++) {
            switch(line.charAt(x)){
                case '>' -> valley.blizzards.add(new Blizzard(x, y, Direction.E));
                case '<' -> valley.blizzards.add(new Blizzard(x, y, Direction.W));
                case 'v' -> valley.blizzards.add(new Blizzard(x, y, Direction.S));
                case '^' -> valley.blizzards.add(new Blizzard(x, y, Direction.N));
            }
            valley.maxy = y+1;
            valley.maxx = line.length();
        }

        return valley;
    }

    private static boolean inLimits(int xLimit, int yLimit, Pos pn) {
        // Special positions, (outside the rectangle bounds)
        if(pn.equals(new Pos(1,0)) || pn.equals(new Pos(xLimit-2, yLimit-1)))
            return true;

        if(pn.x <= 0)
            return false;
        if(pn.y <= 0)
            return false;
        if(pn.x >= xLimit-1)
            return false;
        if(pn.y >= yLimit-1)
            return false;
        return true;
    }

    private static Pos nextPosWrap(int xLimit, int yLimit, Pos p, Direction direction) {
        Pos pn = nextPos(p, direction);
        if(pn.x == 0)
            return new Pos(xLimit-2, pn.y);
        if(pn.y == 0)
            return new Pos(pn.x, yLimit-2);
        if(pn.x == xLimit-1)
            return new Pos(1, pn.y);
        if(pn.y == yLimit-1)
            return new Pos(pn.x, 1);
        return pn;
    }

    private static Pos nextPos(Pos p, Direction direction) {
        return switch (direction) {
            case N -> new Pos(p.x, p.y-1);
            case S -> new Pos(p.x, p.y+1);
            case W -> new Pos(p.x-1, p.y);
            case E -> new Pos(p.x+1, p.y);
            default -> p;
        };
    }

    private static void update(Valley valley) {
        IntStream.range(0, valley.blizzards.size())
            .forEach(i -> {
                    Blizzard blizzard = valley.blizzards.get(i);
                    valley.blizzards.set(i,new Blizzard(
                            nextPosWrap(valley.maxx, valley.maxy, blizzard.p, blizzard.d),
                            blizzard.d));
                     });
    }

    private int solve(final Valley v, final Pos start, final Pos end) {

        int count = 0;

        Set<Pos> possibleLocations = new HashSet<>();
        possibleLocations.add(start);

        while(possibleLocations.size() != 0 && count < 1000) {
            count++;
            update(v);

            final Set<Pos> blizzardPos = v.blizzards.stream()
                    .map(b -> b.p)
                    .collect(Collectors.toSet());

            possibleLocations = possibleLocations.stream()
                    .flatMap(p -> getPossibleMoves(v, blizzardPos, p).stream())
                    .collect(Collectors.toSet());

            if(possibleLocations.contains(end))
                break;
        }
        return count;
    }

    private List<Pos> getPossibleMoves(final Valley v, final Set<Pos> blizzards, final Pos current) {
        List<Pos> ps = Arrays.stream(Direction.values())
                .map(d -> nextPos(current, d))
                .filter(p -> inLimits(v.maxx, v.maxy, p))
                .filter(p -> !blizzards.contains(p))
                .collect(Collectors.toList());
        return ps;
    }

    @Test
    public void testSolveOneWayTrip() {
        Valley valley =
                FileReader.readFileForObject("src/test/resources/2022/D24_t.txt",
                        new Valley(), D24::parseLine);
        Pos end = new Pos(valley.maxx-2, valley.maxy-1);
        Pos start = new Pos(1, 0);

        assertEquals(18, solve(valley, start, end));

        valley =
                FileReader.readFileForObject("src/test/resources/2022/D24.txt",
                        new Valley(), D24::parseLine);
        end = new Pos(valley.maxx-2, valley.maxy-1);
        start = new Pos(1, 0);

        assertEquals(264, solve(valley, start, end));
    }

    @Test
    public void testSolveThreeWayTrip() {
        Valley valley =
                FileReader.readFileForObject("src/test/resources/2022/D24_t.txt",
                        new Valley(), D24::parseLine);
        Pos end = new Pos(valley.maxx-2, valley.maxy-1);
        Pos start = new Pos(1, 0);
        Pos start2 = new Pos(valley.maxx-2, valley.maxy-1);
        Pos end2 = new Pos(1, 0);

        int trip1 = solve(valley, start, end);
        assertEquals(18, trip1);
        int trip2 = solve(valley, start2, end2);
        print(valley, start2);
        assertEquals(23, trip2);
        int trip3 = solve(valley, start, end);
        assertEquals(13, trip3);
    }

    @Test
    public void testSolveThreeWayTrip2() {
        Valley valley =
                FileReader.readFileForObject("src/test/resources/2022/D24.txt",
                        new Valley(), D24::parseLine);
        Pos end = new Pos(valley.maxx-2, valley.maxy-1);
        Pos start = new Pos(1, 0);
        Pos start2 = new Pos(valley.maxx-2, valley.maxy-1);
        Pos end2 = new Pos(1, 0);

        int trip1 = solve(valley, start, end);
        int trip2 = solve(valley, start2, end2);
        int trip3 = solve(valley, start, end);
        assertEquals(789, trip1+trip2+trip3);
    }
}
