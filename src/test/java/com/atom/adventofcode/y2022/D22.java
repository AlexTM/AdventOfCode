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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class D22 {

    enum Orientation {E, S, W, N}
    enum Turn {L, R}
    record Pos(int x, int y){}
    record Direction(Integer magnitude, Turn turn){}
    record Vector(Pos position, Orientation orientation){}

    static class State {
        boolean loadingMap = true;
        Set<Pos> map = new HashSet<>();
        Set<Pos> walls = new HashSet<>();
        List<Direction> directions = new ArrayList<>();
        int directionPosition = 0;
        Vector vector;
        int movements = 0;
    }

    private static State parseLine(State state, String line, Integer lineNumber) {
        if(line.isEmpty()) {
            state.loadingMap = false;
            return state;
        }
        if(state.loadingMap) {
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == '.') {
                    state.map.add(new Pos(x, lineNumber));
                } else if (c == '#') {
                    state.walls.add(new Pos(x, lineNumber));
                    state.map.add(new Pos(x, lineNumber));
                }
            }
        } else {
            Pattern p = Pattern.compile("([RL]|\\d+)");
            Matcher m = p.matcher(line);
            while(m.find()) {
                String grp = m.group(1);
                if(grp.equalsIgnoreCase("R") || grp.equalsIgnoreCase("L"))
                    state.directions.add(new Direction(null, Turn.valueOf(grp)));
                else
                    state.directions.add(new Direction(Integer.parseInt(grp), null));
            }
            // find the start position
            state.vector = offBasicMap(state.map, new Pos(0,0), Orientation.E);
        }
        return state;
    }

    private static Orientation applyTurn(Orientation orientation, Turn turn) {
        int p = Turn.R.equals(turn) ? orientation.ordinal() + 1 : orientation.ordinal() - 1;
        if(p < 0)
            p += 4;
        if(p > 3)
            p -= 4;
        return Orientation.values()[p];
//        return Orientation.values()[
//                Math.abs((orientation.ordinal() + (turn.equals(Turn.R) ? 1 : -1))) % Orientation.values().length];
    }

    interface TriFunction {
        Vector apply(Set<Pos> map, Pos p, Orientation o);
    }

    private static Pos applyStep(Pos pos, Orientation orientation) {
        return switch(orientation) {
            case N -> new Pos(pos.x, pos.y-1);
            case S -> new Pos(pos.x, pos.y+1);
            case W -> new Pos(pos.x-1, pos.y);
            case E -> new Pos(pos.x+1, pos.y);
        };
    }

    private static Vector offBasicMap(Set<Pos> map, Pos p, Orientation orientation) {
        Pos np = switch(orientation) {
            case N -> new Pos(p.x, map.stream().filter(m -> m.x == p.x).mapToInt(m -> m.y).max().orElseThrow());
            case S -> new Pos(p.x, map.stream().filter(m -> m.x == p.x).mapToInt(m -> m.y).min().orElseThrow());
            case W -> new Pos(map.stream().filter(m -> m.y == p.y).mapToInt(m -> m.x).max().orElseThrow(), p.y);
            case E -> new Pos(map.stream().filter(m -> m.y == p.y).mapToInt(m -> m.x).min().orElseThrow(), p.y);
        };
        return new Vector(np, orientation);
    }

    private static long resultWithOrientation(Vector vector) {
        return ((1+ vector.position.x)*4L) + ((1+ vector.position.y)*1000L) + vector.orientation.ordinal();
    }

    private static void step(State state, TriFunction fn) {

        Direction d = state.directions.get(state.directionPosition);

        Pos currentPosition = state.vector.position;
        Orientation currentOrientation = state.vector.orientation;

        if(d.turn != null) {
            currentOrientation = applyTurn(currentOrientation, d.turn);
            state.directionPosition++;
        } else if(d.magnitude != null) {
            for(int t = 0; t<d.magnitude; t++) {
                Pos newPos = applyStep(currentPosition, currentOrientation);
                if (state.walls.contains(newPos)) {
                    break;
                } else if (state.map.contains(newPos)) {
                    currentPosition = newPos;
                } else {
                    // gone off end of map, appear on other side
                    Vector newVector = fn.apply(state.map, currentPosition, currentOrientation);
                    if (state.walls.contains(newVector.position)) {
                        break;
                    } else {
                        currentPosition = newVector.position;
                        currentOrientation = newVector.orientation;
                    }
                }
            }
            state.directionPosition++;
        }
        state.vector = new Vector(currentPosition, currentOrientation);
    }

/*    private static void step(State state) {

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
                state.movements = 0;
            } else if (state.map.contains(newPos)) {
                currentPosition = newPos;
            } else {
                // gone off end of map, appear on other side
                newPos = offMap(state.map, currentPosition, currentOrientation);
                if(state.walls.contains(newPos)) {
                    state.directionPosition++;
                    state.movements = 0;
                } else {
                    currentPosition = newPos;
                }
            }
            if(++state.movements == d.magnitude) {
                state.directionPosition++;
                state.movements = 0;
            }
        }
        state.vector = new Vector(currentPosition, currentOrientation);
    }*/

    static class MonkeyMapEngine extends DefaultAppLogic {

        private final PlaneGeneratorSimple planeGeneratorSimple;
        private final Set<Pos> tracePos = new HashSet<>();
        private final State state;
        private final TriFunction fn;

        public MonkeyMapEngine(State state, TriFunction fn) {
            this.state = state;
            this.fn = fn;

            int minx = state.map.stream().mapToInt(m -> m.x).min().orElseThrow();
            int miny = state.map.stream().mapToInt(m -> m.y).min().orElseThrow();
            int maxx = state.map.stream().mapToInt(m -> m.x).max().orElseThrow();
            int maxy = state.map.stream().mapToInt(m -> m.y).max().orElseThrow()+1;

            this.planeGeneratorSimple = new PlaneGeneratorSimple(
                    minx, miny, maxx+10, maxy+10, (x, y) -> {
                    Pos p = new Pos(x, maxy - y-1);

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
            step(state, fn);
            tracePos.add(state.vector.position);

            if(scene != null) {
                scene.addMesh("plane", planeGeneratorSimple.createMesh());
            }
        }
    }

    @Test
    public void testDirectionsTestcase() {
        State state =
                FileReader.readFileForObject("src/test/resources/2022/D22_t.txt",
                        new State(), D22::parseLine);

        Engine gameEng = new Engine(
                "AdventOfCode - D22",
                new Window.WindowOptions().setGui(false).setUps(1).setWidth(500).setHeight(500),
                new MonkeyMapEngine(state, D22::offBasicMap));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(6032, resultWithOrientation(state.vector));
    }

    @Test
    public void testDirectionsWithGUI() {
        State state =
                FileReader.readFileForObject("src/test/resources/2022/D22.txt",
                        new State(), D22::parseLine);

        Engine gameEng = new Engine("AdventOfCode - D22",
                new Window.WindowOptions().setFps(10).setUps(200).setGui(false),
                new MonkeyMapEngine(state, D22::offBasicMap));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(197160, resultWithOrientation(state.vector));
    }

    static final Map<Pos, Integer> faces = Map.of(
                new Pos(2,0), 1,
                new Pos(0,1), 2,
                new Pos(1,1), 3,
                new Pos(2,1), 4,
                new Pos(2,2), 5,
                new Pos(3,2), 6);

    // fixme forget vector
    record CubeMap(Integer i, Orientation o){};
    record CoordMap(BiFunction<Integer, Integer, Integer> xFn, BiFunction<Integer, Integer, Integer> yFn){};
    static final Map<CubeMap, CoordMap> mappingFns = Map.of(
            new CubeMap(1, Orientation.N), new CoordMap((x,s) -> s-x, (y, s) -> s));

    //            new CubeMap(1, Orientation.W), (p, s) -> { return p; },
//            new CubeMap(1, Orientation.E), (p, s) -> { return p; },
//            new CubeMap(2, Orientation.N), (p, s) -> new Vector(new Pos((s*3)-1-p.x, 0), Orientation.S)

  //  );

    private static int getFace(Pos p, int size) {
        return faces.get(new Pos(p.x/size, p.y/size));
    }

    private static Vector offCubeMap(Set<Pos> map, Pos p, Orientation orientation) {
        int size = 4;
        int face = getFace(p, size);
        return mappingFns.get(new CubeMap(face, orientation)).apply(p, 4);
    }

    private static long resultWithFace(Vector vector, int size) {
        return ((1+ vector.position.x)*4L) + ((1+ vector.position.y)*1000L) + getFace(vector.position, size);
    }

    @Test
    public void testDirectionsTestcaseTwo() {
        State state =
                FileReader.readFileForObject("src/test/resources/2022/D22_t.txt",
                        new State(), D22::parseLine);

        Engine gameEng = new Engine(
                "AdventOfCode - D22",
                new Window.WindowOptions().setGui(false).setUps(1).setWidth(500).setHeight(500),
                new MonkeyMapEngine(state, D22::offCubeMap));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(5031, resultWithFace(state.vector, 4));
    }

    @Test
    public void testEdgeCases() {
        assertEquals(new Vector(new Pos(0, 4), Orientation.S), offCubeMap(null, new Pos(11, 0), Orientation.N));
    }

}
