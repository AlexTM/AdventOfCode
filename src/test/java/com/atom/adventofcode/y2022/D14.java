package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D14 {

    record Pos(int x, int y){};

    private static Set<Pos> buildWall(Pos from, Pos to) {
        Set<Pos> pList = new HashSet<>();
        Pos p = from;
        pList.add(from);
        while(!p.equals(to)) {
            p = p.x < to.x ? new Pos(p.x+1, p.y) : p;
            p = p.x > to.x ? new Pos(p.x-1, p.y) : p;
            p = p.y < to.y ? new Pos(p.x, p.y+1) : p;
            p = p.y > to.y ? new Pos(p.x, p.y-1) : p;
            pList.add(p);
        }
        return pList;
    }

    public static Set<Pos> parseLine(String line, Set<Pos> walls) {
        String[] splits1 = line.split("->");
        Pos from = null;
        for(String s : splits1) {
            String[] splits2 = s.split(",");
            Pos to = new Pos(Integer.parseInt(splits2[0].trim()), Integer.parseInt(splits2[1].trim()));
            if(from != null) {
                walls.addAll(buildWall(from, to));
            }
            from = to;
        }
        return walls;
    }

    private Pos nextPos(final Set<Pos> walls, Pos sand) {
        return Stream.of(new Pos(sand.x, sand.y+1),
                        new Pos(sand.x-1, sand.y+1),
                        new Pos(sand.x+1, sand.y+1))
                .filter(m -> !walls.contains(m)).findFirst().orElse(sand);
    }

    private boolean sandDrop(Set<Pos> walls, Integer floorLevel) {
        Pos pos = new Pos(500, 0);

        while(true) {
            Pos nextPos = nextPos(walls, pos);
            nextPos = floorLevel != null && floorLevel == nextPos.y ? pos : nextPos;
            if(nextPos.equals(pos)) {
                walls.add(nextPos);
                // check for blockage
                return nextPos.equals(new Pos(500, 0));
            }
            // check for the abyss
            if (nextPos.y > 1000)
                return true;
            pos = nextPos;
        }
    }

    private int dropSandUntilAbyss(Set<Pos> walls) {
        int count = 0;
        while (!sandDrop(walls, null)) {
            count++;
        }
        return count;
    }

    private int dropSandUntilFull(Set<Pos> walls) {
        int floorLevel = walls.stream().map(p -> p.y).max(Comparator.naturalOrder()).orElseThrow()+2;
        int count = 0;
        while (!sandDrop(walls, floorLevel)) {
            count++;
        }
        return count+1;
    }

    @Test
    public void testSandAbyss() {
        assertEquals(24, dropSandUntilAbyss(
                FileReader.readFileForObject("src/test/resources/2022/D14_t.txt", new HashSet<>(), D14::parseLine)));
        assertEquals(610, dropSandUntilAbyss(
                FileReader.readFileForObject("src/test/resources/2022/D14.txt", new HashSet<>(), D14::parseLine)));
    }

    @Test
    public void testSandUntilFull() {
        assertEquals(93, dropSandUntilFull(
                FileReader.readFileForObject("src/test/resources/2022/D14_t.txt", new HashSet<>(), D14::parseLine)));
        assertEquals(27194, dropSandUntilFull(
                FileReader.readFileForObject("src/test/resources/2022/D14.txt", new HashSet<>(), D14::parseLine)));
    }
}
