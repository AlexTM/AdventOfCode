package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D24 {

    enum Direction{ N, E, S, W }
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
            return new Pos(pn.x+xLimit-2, pn.y);
        if(pn.y == 0)
            return new Pos(pn.x, pn.y+yLimit-2);
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
        };
    }

    private static void update(Valley valley) {
        for(int i=0; i<valley.blizzards.size(); i++) {
            Blizzard blizzard = valley.blizzards.get(i);
            blizzard = new Blizzard(nextPosWrap(valley.maxx, valley.maxy, blizzard.p, blizzard.d), blizzard.d);
            valley.blizzards.set(i, blizzard);
        }
    }

    private int solve(Valley v) {
        Pos end = new Pos(v.maxx-2, v.maxy-2);

        int count = 0;

        Set<Pos> possibleLocations = new HashSet<>();
        possibleLocations.add(new Pos(1, 0));

        while(possibleLocations.size() != 0 && count < 1000) {
            count++;

            update(v);
            Set<Pos> blizzardPos = v.blizzards.stream().map(b -> b.p).collect(Collectors.toSet());

            Set<Pos> newPossibleLocations = new HashSet<>();
            for(Pos p : possibleLocations) {
                newPossibleLocations.add(p);
                newPossibleLocations.addAll(getPossibleMoves(v, blizzardPos, p));
            }
            possibleLocations = newPossibleLocations;
//            System.out.println("Queue size:"+possibleLocations.size());

            if(possibleLocations.contains(end)) {
                print(v, end);
                break;
            }

        }
        return count+2;
    }

    private List<Pos> getPossibleMoves(Valley v, Set<Pos> blizzards, Pos current) {
        List<Pos> posList = new ArrayList<>();

        for(Direction d : Direction.values()) {
            Pos n = nextPos(current, d);
            if(inLimits(v.maxx, v.maxy, n)) {
                if(!blizzards.contains(n)) {
                    posList.add(n);
                }
            }
        }
        return posList;
    }

    @Test
    public void testSolve() {
        Valley valley =
                FileReader.readFileForObject("src/test/resources/2022/D24_t.txt",
                        new Valley(), D24::parseLine);

        assertEquals(18, solve(valley));
    }

    // 211
    @Test
    public void testSolve1() {
        Valley valley =
                FileReader.readFileForObject("src/test/resources/2022/D24.txt",
                        new Valley(), D24::parseLine);

        assertEquals(0, solve(valley));
    }


//    @Test
//    public void testBlizzard() {
//        Valley valley =
//                FileReader.readFileForObject(
//                        "src/test/resources/2022/D24_t.txt",
//                        new Valley(), D24::parseLine);
//        valley.print();
//        update(valley);
//        valley.print();
//        update(valley);
//        valley.print();
//        update(valley);
//        valley.print();
//    }
}
