package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import com.atom.adventofcode.common.engine.DefaultAppLogic;
import com.atom.adventofcode.common.engine.Engine;
import com.atom.adventofcode.common.engine.Window;
import com.atom.adventofcode.common.engine.scene.Scene;
import com.atom.adventofcode.common.game.PlaneGeneratorSimple;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Messy today
 *
 * FIXME, don't need Elf class
 *
 */
public class D23 {

    enum Direction {N, S, W, E}
    record Pos(int x, int y){}

    static class Elf {
        Pos p;
        int count = 0;
        public Elf(Pos p) {
            this.p = p;
        }
    }

    private static List<Elf> parseLine(List<Elf> posList, String line, Integer lineNum) {
        for(int x=0; x<line.length(); x++) {
            if(line.charAt(x) == '#') {
                posList.add(new Elf(new Pos(x, lineNum)));
            }
        }
        return posList;
    }

    private static Map<Direction, Integer> assess(Set<Pos> elfPositions, Pos p) {
        Set<Pos> north = Set.of(new Pos(p.x-1, p.y-1), new Pos(p.x, p.y-1), new Pos(p.x+1, p.y-1));
        Set<Pos> south = Set.of(new Pos(p.x-1, p.y+1), new Pos(p.x, p.y+1), new Pos(p.x+1, p.y+1));
        Set<Pos> west = Set.of(new Pos(p.x-1, p.y-1), new Pos(p.x-1, p.y), new Pos(p.x-1, p.y+1));
        Set<Pos> east = Set.of(new Pos(p.x+1, p.y-1), new Pos(p.x+1, p.y), new Pos(p.x+1, p.y+1));

        return Map.of(
                Direction.N, (int)north.stream().filter(elfPositions::contains).count(),
                Direction.S, (int)south.stream().filter(elfPositions::contains).count(),
                Direction.W, (int)west.stream().filter(elfPositions::contains).count(),
                Direction.E, (int)east.stream().filter(elfPositions::contains).count()
        );
    }

    private static Pos nextPos(Pos p, Direction direction) {
        return switch (direction) {
            case N -> new Pos(p.x, p.y-1);
            case S -> new Pos(p.x, p.y+1);
            case W -> new Pos(p.x-1, p.y);
            case E -> new Pos(p.x+1, p.y);
        };
    }

    private static int step(final List<Elf> elfList) {

        Set<Pos> elfSet = elfList.stream().map(e -> e.p).collect(Collectors.toSet());

        // calculate new positions
        Map<Elf, Pos> elfNewPositions = new HashMap<>();
        for(Elf elf : elfList) {
            Map<Direction, Integer> count = assess(elfSet, elf.p);
            if(Arrays.stream(Direction.values()).map(count::get).anyMatch(i -> i != 0)) {
                for(int c=0; c<4; c++){
                    Direction d = Direction.values()[(c + elf.count)%4];
                    if(count.get(d) == 0) {
                        elfNewPositions.put(elf, nextPos(elf.p, d));
                        break;
                    }
                }
            }
        }

        // Check for clashes
        Map<Pos, List<Elf>> mapPos = new HashMap<>();
        for(Map.Entry<Elf, Pos> e : elfNewPositions.entrySet()) {
            List<Elf> elves = mapPos.getOrDefault(e.getValue(), new ArrayList<>());
            elves.add(e.getKey());
            mapPos.put(e.getValue(), elves);
        }
        
        // Remove all clashes
        mapPos.entrySet().stream().filter(e -> e.getValue().size() > 1)
                .forEach(e -> e.getValue().forEach(elfNewPositions.keySet()::remove));

        // Update position of remaining elves
        elfNewPositions.forEach((key, value) -> key.p = value);

        // Update all elves, even if not moved, mis-understood the question
        elfList.forEach(e -> e.count++);

        return elfNewPositions.size();
    }

    private int countEmpty(final List<Elf> elfList) {
        int minx = elfList.stream().mapToInt(e -> e.p.x).min().orElseThrow();
        int miny = elfList.stream().mapToInt(e -> e.p.y).min().orElseThrow();
        int maxx = elfList.stream().mapToInt(e -> e.p.x).max().orElseThrow();
        int maxy = elfList.stream().mapToInt(e -> e.p.y).max().orElseThrow();
        Set<Pos> elfSet = elfList.stream().map(e -> e.p).collect(Collectors.toSet());
        return (1+maxx - minx) * (1+maxy - miny) - elfSet.size();
    }

    static class CraterEngine extends DefaultAppLogic {

        private final PlaneGeneratorSimple planeGeneratorSimple;
        final List<Elf> elfList;
        long count = 0;
        Set<Pos> elfSet;
        long lastUpdated = -1;

        public CraterEngine(int maxx, int maxy, List<Elf> elfList) {
            this.elfList = elfList;
            this.planeGeneratorSimple = new PlaneGeneratorSimple(
                    20,20,20+maxx, 20+maxy,
                    (x, y) -> {
                        Pos p = new Pos(x, y);
                        if(elfSet.contains(p))
                            return new Vector3f(0.3f, 0.8f, 0.3f);
                        return new Vector3f(0.1f, 0.1f, 0.1f);
                    });
        }

        @Override
        public void update(Window window, Scene scene, long diffTimeMillis) {
            lastUpdated = step(elfList);
            count++;

            if(scene != null) {
                elfSet = elfList.stream().map(e -> e.p).collect(Collectors.toSet());
                scene.addMesh("plane", planeGeneratorSimple.createMesh());
            }
        }
    }

    @Test
    public void testSpace3() {
        List<Elf> posList =
                FileReader.readFileForObject("src/test/resources/2022/D23_t1.txt", new ArrayList<>(), D23::parseLine);
        for(int i=0; i<10; i++) {
            step(posList);
        }
        assertEquals(110, countEmpty(posList));

        posList =
                FileReader.readFileForObject("src/test/resources/2022/D23.txt", new ArrayList<>(), D23::parseLine);
        for(int i=0; i<10; i++) {
            step(posList);
        }
        assertEquals(4254, countEmpty(posList));
    }

    private int runUntilComplete(List<Elf> elfList) {
        int count = 1;
        while(step(elfList) != 0) {
            count++;
        }
        return count;
    }

    @Test
    public void testUntilComplete() {
        List<Elf> posList =
                FileReader.readFileForObject("src/test/resources/2022/D23_t1.txt", new ArrayList<>(), D23::parseLine);
        assertEquals(20, runUntilComplete(posList));

        posList =
                FileReader.readFileForObject("src/test/resources/2022/D23.txt", new ArrayList<>(), D23::parseLine);
        assertEquals(992, runUntilComplete(posList));
    }

    @Test
    public void testUntilCompleteWithGUI() {
        List<Elf> posList =
                FileReader.readFileForObject("src/test/resources/2022/D23.txt", new ArrayList<>(), D23::parseLine);

        CraterEngine engine = new CraterEngine(200, 200, posList);
        Engine gameEng = new Engine("AdventOfCode - D23",
                new Window.WindowOptions().setUps(30).setGui(true), engine);
        gameEng.start(() -> engine.lastUpdated == 0);
        assertEquals(992, engine.count);
    }
}
