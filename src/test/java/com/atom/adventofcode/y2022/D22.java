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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D22 {

    enum Orientation {E, S, W, N}
    enum Turn {L, R}
    record Pos(int x, int y){}
    record Direction(Integer magnitude, Turn turn){}
    record Vector(Pos position, Orientation orientation){}

    // fixme remove
    static class LoadingState {
        boolean loadingMap = true;
        Set<Pos> map = new HashSet<>();
        Set<Pos> walls = new HashSet<>();
        List<Direction> directions = new ArrayList<>();
    }

    static class State {
        Set<Pos> map;
        Set<Pos> walls;
        List<Direction> directions;
        int directionPosition = 0;
        Vector vector;
        int movements = 0;
    }

    private static LoadingState parseLine(LoadingState loadingState, String line, Integer lineNumber) {
        if(line.isEmpty()) {
            loadingState.loadingMap = false;
            return loadingState;
        }
        if(loadingState.loadingMap) {
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == '.') {
                    loadingState.map.add(new Pos(x, lineNumber));
                } else if (c == '#') {
                    loadingState.walls.add(new Pos(x, lineNumber));
                    loadingState.map.add(new Pos(x, lineNumber));
                }
            }
        } else {
            Pattern p = Pattern.compile("([RL]|\\d+)");
            Matcher m = p.matcher(line);
            while(m.find()) {
                String grp = m.group(1);
                if(grp.equalsIgnoreCase("R") || grp.equalsIgnoreCase("L"))
                    loadingState.directions.add(new Direction(null, Turn.valueOf(grp)));
                else
                    loadingState.directions.add(new Direction(Integer.parseInt(grp), null));
            }
        }
        return loadingState;
    }

    private static Orientation applyTurn(Orientation orientation, Turn turn) {
        return Orientation.values()[
                Math.abs((orientation.ordinal() + (turn.equals(Turn.R) ? 1 : -1))) % Orientation.values().length];
    }

    private static Pos applyStep(Pos pos, Orientation orientation) {
        return switch(orientation) {
            case N -> new Pos(pos.x, pos.y-1);
            case S -> new Pos(pos.x, pos.y+1);
            case W -> new Pos(pos.x-1, pos.y);
            case E -> new Pos(pos.x+1, pos.y);
        };
    }

    private static Pos offMap(Set<Pos> map, Pos p, Orientation orientation) {
        return switch(orientation) {
            case N -> new Pos(p.x, map.stream().filter(m -> m.x == p.x).mapToInt(m -> m.y).max().orElseThrow());
            case S -> new Pos(p.x, map.stream().filter(m -> m.x == p.x).mapToInt(m -> m.y).min().orElseThrow());
            case W -> new Pos(map.stream().filter(m -> m.y == p.y).mapToInt(m -> m.x).max().orElseThrow(), p.y);
            case E -> new Pos(map.stream().filter(m -> m.y == p.y).mapToInt(m -> m.x).min().orElseThrow(), p.y);
        };
    }

    private static long result(Vector vector) {
        return ((1+ vector.position.x)*4L) + ((1+ vector.position.y)*1000L) + vector.orientation.ordinal();
    }

    private static void step(State state) {

        Direction d = state.directions.get(state.directionPosition);

        Pos currentPosition = state.vector.position;
        Orientation currentOrientation = state.vector.orientation;

        if(d.turn != null) {
            currentOrientation = applyTurn(currentOrientation, d.turn);
            state.directionPosition++;
            state.movements = 0;
        } else if(d.magnitude != null) {
            Pos newPos = applyStep(currentPosition, currentOrientation);

            if (state.walls.contains(newPos)) {
                state.directionPosition++;
            } else if (state.map.contains(newPos)) {
                currentPosition = newPos;
            } else {
                // gone off end of map, appear on other side
                Pos p = offMap(state.map, currentPosition, currentOrientation);
                if(state.walls.contains(p)) {
                    state.directionPosition++;
                } else {
                    currentPosition = p;
                }
            }
            if(++state.movements == d.magnitude) {
                state.directionPosition++;
            }
        }
        state.vector = new Vector(currentPosition, currentOrientation);
    }

    public State createState(LoadingState loadingState) {
        State state = new State();
        state.map = loadingState.map;
        state.walls = loadingState.walls;
        state.directions = loadingState.directions;

        // find the start position
        state.vector = new Vector(offMap(state.map, new Pos(0,0), Orientation.E), Orientation.E);
        return state;
    }

    static class MonkeyMapEngine extends DefaultAppLogic {

        private final PlaneGeneratorSimple planeGeneratorSimple;
        private final State state;
        private Set<Pos> tracePos = new HashSet<>();
        int minx, miny, maxx, maxy;

        public MonkeyMapEngine(State state) {
            this.state = state;

            minx = state.map.stream().mapToInt(m -> m.x).min().orElseThrow();
            miny = state.map.stream().mapToInt(m -> m.y).min().orElseThrow();
            maxx = state.map.stream().mapToInt(m -> m.x).max().orElseThrow();
            maxy = state.map.stream().mapToInt(m -> m.y).max().orElseThrow();

            this.planeGeneratorSimple = new PlaneGeneratorSimple(
                    minx, miny, maxx+10, maxy+10,
                    (x, y) -> {
                        // fixme convert to coords
                        Pos p = new Pos(x, y);

                        if(tracePos.contains(p)) {
                            return new Vector3f(1.0f, 0.0f, 0.0f);
                        }

                        if(state.walls.contains(p)) {
                            return new Vector3f(0.8f, 0.8f, 0.8f);
                        }

                        if(state.map.contains(p)) {
                            return new Vector3f(0.2f, 0.2f, 0.2f);
                        }

                        return new Vector3f(0.0f, 0.0f, 0.0f);
                    });
        }

        @Override
        public void update(Window window, Scene scene, long diffTimeMillis) {
            step(state);
            tracePos.add(state.vector.position);

            if(scene != null) {
                scene.addMesh("plane", planeGeneratorSimple.createMesh());
            }
        }
    }

    @Test
    public void testDirections() {
        State state = createState(
                FileReader.readFileForObject(
                        "src/test/resources/2022/D22_t.txt", new LoadingState(), D22::parseLine));

        Engine gameEng = new Engine(
                "AdventOfCode - D22",
                new Window.WindowOptions().setGui(true).setUps(1).setWidth(500).setHeight(500),
                new MonkeyMapEngine(state));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(6032, result(state.vector));
    }


    @Test
    public void testDirectionsWithGUI() {
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22.txt", new LoadingState(), D22::parseLine);

        State state = createState(loadingState);

        MonkeyMapEngine engine = new MonkeyMapEngine(state);
        Engine gameEng = new Engine("AdventOfCode - D22",
                new Window.WindowOptions().setFps(10).setUps(200).setGui(true), engine);

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(6032, result(state.vector));
    }


/*
    @Test
    public void testProblem() {
        Map<Pos, Orientation> trace = new HashMap<>();
        // 44556
        // 8479
        // 89220
        // 89220
        // 120247
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22.txt",
                        new LoadingState(), D22::parseLine);
        long res = setUpAndRun(loadingState.map, loadingState.walls, loadingState.directions, trace);
//        print(loadingState.map, loadingState.walls, trace);
        assertNotEquals(89220, res);
        System.out.println("Res "+res);
    }
*/
}
