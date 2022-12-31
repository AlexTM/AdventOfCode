package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import com.atom.adventofcode.common.engine.Engine;
import com.atom.adventofcode.common.engine.IAppLogic;
import com.atom.adventofcode.common.engine.Window;
import com.atom.adventofcode.common.engine.graph.Render;
import com.atom.adventofcode.common.engine.scene.Scene;
import com.atom.adventofcode.common.game.PlaneGeneratorSimple;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D24B {

    enum Direction{ N, E, S, W, NONE }
    public record Pos(int x, int y){}
    record Blizzard(Pos p, Direction d){
        public Blizzard(int x, int y, Direction d) {
            this(new Pos(x, y), d);
        }
    }

    static class State {
        final List<Blizzard> blizzards = new ArrayList<>();
        int maxx, maxy;
        Set<Pos> possibleLocations = new HashSet<>();
        Pos start, end;
        int count = 0;
    }

    private static State parseLine(State valley, String line, Integer y) {
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

    public static int loopUntilComplete(State state) {
        while(state.possibleLocations.size() != 0 && state.count < 1000) {
            if(step(state))
                break;
        }
        return state.count;
    }

    private static boolean step(State state) {
        state.count++;
        D24B.update(state);

        final Set<Pos> blizzardPos = state.blizzards.stream()
                .map(b -> b.p)
                .collect(Collectors.toSet());

        state.possibleLocations = state.possibleLocations.stream()
                .flatMap(p -> getPossibleMoves(state, blizzardPos, p).stream())
                .collect(Collectors.toSet());

        return state.possibleLocations.contains(state.end);
    }

    private static void update(State valley) {
        IntStream.range(0, valley.blizzards.size())
            .forEach(i -> {
                    Blizzard blizzard = valley.blizzards.get(i);
                    valley.blizzards.set(i,new Blizzard(
                            nextPosWrap(valley.maxx, valley.maxy, blizzard.p, blizzard.d),
                            blizzard.d));
                     });
    }

    private static List<Pos> getPossibleMoves(final State v, final Set<Pos> blizzards, final Pos current) {
        return Arrays.stream(Direction.values())
                .map(d -> nextPos(current, d))
                .filter(p -> inLimits(v.maxx, v.maxy, p))
                .filter(p -> !blizzards.contains(p))
                .collect(Collectors.toList());
    }

    private static State createState(String fileName) {
        State state = FileReader.readFileForObject(fileName, new State(), D24B::parseLine);
        state.end = new Pos(state.maxx-2, state.maxy-1);
        state.start = new Pos(1, 0);
        state.possibleLocations.add(state.start);
        return state;
    }

    static class ValleyEngine implements IAppLogic {

        private final PlaneGeneratorSimple planeGeneratorSimple;
        private final State state;

        public ValleyEngine(String fileName) {
            this.state = D24B.createState(fileName);
            this.planeGeneratorSimple = new PlaneGeneratorSimple(
                    state.maxx, state.maxy,
                    pos -> {
                        // fixme convert to coords
                        Pos p = new Pos((int)pos.xpos(), (int)pos.zpos());
                        if(state.possibleLocations.contains(p))
                            return new Vector3f(0.8f, 0.8f, 0.8f);
                        if(state.blizzards.stream().map(b -> b.p).anyMatch(b -> b.equals(p)))
                            return new Vector3f(0.8f, 0.3f, 0.3f);
                        return new Vector3f(0.1f, 0.1f, 0.1f);
                    });
        }

        @Override
        public void cleanup() {}

        @Override
        public void init(Window window, Scene scene, Render render) {}

        @Override
        public void input(Window window, Scene scene, long diffTimeMillis) {}

        @Override
        public boolean update(Window window, Scene scene, long diffTimeMillis) {
            boolean res = D24B.step(state);
            scene.addMesh("plane", planeGeneratorSimple.createMesh());
            return res;
        }
    }

    @Test
    public void testSolveOneWayTrip() {
        assertEquals(18, loopUntilComplete(D24B.createState("src/test/resources/2022/D24_t.txt")));
        assertEquals(264, loopUntilComplete(D24B.createState("src/test/resources/2022/D24.txt")));
    }

    @Test
    public void testSolveOneWayTripWithGraphics() {
        ValleyEngine valleyEngine = new ValleyEngine("src/test/resources/2022/D24.txt");

        Engine gameEng = new Engine("AdventOfCode - D24",
                new Window.WindowOptions(), valleyEngine);
        gameEng.start();
        assertEquals(264, valleyEngine.state.count);
    }
}
