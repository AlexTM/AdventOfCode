package com.atom.adventofcode.test2015;

import com.atom.adventofcode.common.FileReader;
import com.atom.adventofcode.common.engine.DefaultAppLogic;
import com.atom.adventofcode.common.engine.Engine;
import com.atom.adventofcode.common.engine.Window;
import com.atom.adventofcode.common.engine.scene.Scene;
import com.atom.adventofcode.common.game.PlaneGeneratorSimple;
import com.atom.adventofcode.controllers.DemoController;
import org.joml.Vector3f;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class D24 {

    enum Direction{ N, E, S, W, NONE }
    public record Pos(int x, int y){}
    record Blizzard(Pos p, Direction d){
        public Blizzard(int x, int y, Direction d) {
            this(new Pos(x, y), d);
        }
    }

    static class State {
        int maxx, maxy;
        final List<Blizzard> blizzards = new ArrayList<>();
        Set<Pos> possibleLocations = new HashSet<>();
        Pos start, end;
        int count = 0;
        boolean finished;

        public State reset(Pos start, Pos end) {
            this.start = start;
            this.end = end;
            this.count = 0;
            this.possibleLocations = new HashSet<>();
            this.possibleLocations.add(start);
            this.finished = false;
            return this;
        }
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

    private static boolean step(State state) {
        state.count++;
        update(state);

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
        State state = FileReader.readFileForObject(fileName, new State(), D24::parseLine);
        state.end = new Pos(state.maxx-2, state.maxy-1);
        state.start = new Pos(1, 0);
        return state;
    }

    static class ValleyEngine extends DefaultAppLogic {

        private final PlaneGeneratorSimple planeGeneratorSimple;
        private final State state;

        public ValleyEngine(State state) {
            this.state = state;
            this.planeGeneratorSimple = new PlaneGeneratorSimple(
                    0,0,state.maxx, state.maxy,
                    (x, y) -> {
                        Pos p = new Pos(x, y);
                        if(state.possibleLocations.contains(p))
                            return new Vector3f(0.8f, 0.8f, 0.8f);
                        if(state.blizzards.stream().map(b -> b.p).anyMatch(b -> b.equals(p)))
                            return new Vector3f(0.8f, 0.3f, 0.3f);
                        return new Vector3f(0.1f, 0.1f, 0.1f);
                    });
        }

        @Override
        public void update(Window window, Scene scene, long diffTimeMillis) {
            state.finished = D24.step(state);
            if(scene != null) {
                scene.addMesh("plane", planeGeneratorSimple.createMesh());
            }
        }
    }


    public void testSolveThreeWayTripWithGraphics() {
        State state = D24.createState("src/main/resources/2022/D24.txt");

        Window.WindowOptions options = new Window.WindowOptions().setUps(100).setGui(Window.GUI_OPTIONS.GUI);
        Pos start = new Pos(1, 0);
        Pos end = new Pos(state.maxx-2, state.maxy-1);

        ValleyEngine valleyEngine = new ValleyEngine(state);
        Engine gameEng = new Engine("AdventOfCode - D24 - 1",
                options, valleyEngine);
        state.reset(start, end);
        gameEng.start(() -> state.finished);
        int trip1 = valleyEngine.state.count;

        valleyEngine = new ValleyEngine(state);
        gameEng = new Engine("AdventOfCode - D24 - 2",
                options, valleyEngine);
        state.reset(end, start);
        gameEng.start(() -> state.finished);
        int trip2 = valleyEngine.state.count;

        valleyEngine = new ValleyEngine(state);
        gameEng = new Engine("AdventOfCode - D24 - 3",
                options, valleyEngine);
        state.reset(start, end);
        gameEng.start(() -> state.finished);
        int trip3 = valleyEngine.state.count;

//        assertEquals(789, trip1+trip2+trip3);
    }

}
