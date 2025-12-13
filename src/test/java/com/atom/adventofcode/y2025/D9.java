package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D9 {
    private static final String TEST_INPUT = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
            """;

    record Pos(long x, long y){}

    private List<Pos> parseInput(String input) {
        List<Pos> results = new ArrayList<>();
        String[] split = input.split("\n");
        for(String s : split) {
            String[] split2 = s.split(",");
            long x = Long.parseLong(split2[0]);
            long y = Long.parseLong(split2[1]);
            results.add(new Pos(x, y));
        }
        return results;
    }

    private long area(Pos p1, Pos p2) {
        return (Math.abs(p1.x - p2.x)+1) * (Math.abs(p1.y - p2.y)+1);
    }

    private long findMaxArea(List<Pos> positions) {
        long max = 0;
        for(int i=0; i<positions.size(); i++) {
            for(int j=0; j<positions.size(); j++) {
                max = Math.max(max, area(positions.get(i), positions.get(j)));
            }
        }
        return max;
    }

    @Test
    public void testPart1() {
        assertEquals(50, findMaxArea(parseInput(TEST_INPUT)));
        long res = findMaxArea(parseInput(FileReader.readFileString("src/test/resources/2025/D9.txt")));
        assertEquals(4764078684L, res);
    }

    //..............
    //.......#...#..
    //..............
    //..#....#......
    //..............
    //..#......#....
    //..............
    //.........#.#..
    //..............

    private boolean checkCornersInside(Pos pos1, Pos pos2, List<Pos> positions) {
        Pos tl = new Pos(Math.min(pos1.x, pos2.x), Math.min(pos1.y, pos2.y));
        Pos br = new Pos(Math.max(pos1.x, pos2.x), Math.max(pos1.y, pos2.y));
        Set<Pos> tmp = new HashSet<>(positions);
        tmp.remove(pos1);
        tmp.remove(pos2);
        for(Pos p : tmp) {
            if(p.x >= tl.x && p.x <= br.x && p.y >= tl.y && p.y <= br.y) {
                return true;
            }
        }
        System.out.println(tl + " " + br);
        return false;
    }

    private long findMaxArea2(List<Pos> positions) {
        long max = 0;
        for(int i=0; i<positions.size(); i++) {
            for(int j=0; j<positions.size(); j++) {
                if(!checkCornersInside(positions.get(i), positions.get(j), positions)) {
                    max = Math.max(max, area(positions.get(i), positions.get(j)));
                }
            }
        }
        return max;
    }



    @Test
    public void testPart2() {
        assertEquals(24, findMaxArea2(parseInput(TEST_INPUT)));

    }
}
