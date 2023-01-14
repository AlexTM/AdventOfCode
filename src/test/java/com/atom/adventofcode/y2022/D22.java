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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D22 {

    enum Orientation {E, S, W, N}
    enum Turn {L, R}
    record Pos(int x, int y){}
    record Direction(Integer magnitude, Turn turn){}
    record Vector(Pos position, Orientation orientation){
        public Vector(int x, int y, Orientation orientation) {
            this(new Pos(x, y), orientation);
        }
    }

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

    static class MonkeyMapTwoD extends DefaultAppLogic {

        private final PlaneGeneratorSimple planeGeneratorSimple;
        private final Set<Pos> tracePos = new HashSet<>();
        private final State state;
        private final TriFunction fn;

        public MonkeyMapTwoD(State state, TriFunction fn) {
            this.state = state;
            this.fn = fn;

            int minx = state.map.stream().mapToInt(m -> m.x).min().orElseThrow();
            int miny = state.map.stream().mapToInt(m -> m.y).min().orElseThrow();
            int maxx = state.map.stream().mapToInt(m -> m.x).max().orElseThrow();
            int maxy = state.map.stream().mapToInt(m -> m.y).max().orElseThrow()+1;

            this.planeGeneratorSimple = new PlaneGeneratorSimple(
                    minx, miny, maxx+10, maxy+10, (x, y) -> {
                    Pos p = new Pos(x, maxy - y-1);
                    if(tracePos.contains(p))
                        return new Vector3f(1.0f, 0.0f, 0.0f);
                    if(state.walls.contains(p))
                        return new Vector3f(0.8f, 0.8f, 0.8f);
                    if(state.map.contains(p))
                        return new Vector3f(0.2f, 0.2f, 0.2f);
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
                new MonkeyMapTwoD(state, D22::offBasicMap));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(6032, resultWithOrientation(state.vector));
    }

    @Test
    public void testDirectionsWithGUI() {
        State state =
                FileReader.readFileForObject("src/test/resources/2022/D22.txt",
                        new State(), D22::parseLine);

        Engine gameEng = new Engine("AdventOfCode - D22",
                new Window.WindowOptions().setFps(10).setUps(200).setGui(true),
                new MonkeyMapTwoD(state, D22::offBasicMap));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(197160, resultWithOrientation(state.vector));
    }

    static final Map<Pos, Integer> faces = Map.of(
                new Pos(2,0), 0,
                new Pos(0,1), 1,
                new Pos(1,1), 2,
                new Pos(2,1), 3,
                new Pos(2,2), 4,
                new Pos(3,2), 5);

    /*
     *     N    E    S    W
     * 1  2S2  6W2  4S0  3S1
     * 2  1S  3E  5N  6N
     * 3  1E  4E  5E  2W
     * 4  1N  6S  5S  3W
     * 5  4N  6E  2N  3N
     * 6  4W  1W  2E  5W
     */

    record WrapInfo(int face, Orientation o){
        public WrapInfo(String s) {
            this(s.charAt(0)-'0', Orientation.valueOf(s.substring(1,2)));
        }
    };
    record WrapInfoB(int face, Orientation o, int rotations){
        public WrapInfoB(String s) {
            this(s.charAt(0)-'0', Orientation.valueOf(s.substring(1,2)), s.charAt(0)-'0');
        }
    };
    static final Map<WrapInfo, WrapInfoB> wrapInfo = Map.of(
            new WrapInfo("1N"), new WrapInfoB("2S2")
            );

    private Pos mapToFaces(final Pos pos, final int size, final Map<Integer, Integer> rotations) {
        int face = getFace(pos, size);
        Pos posOnFace = new Pos(pos.x%size, pos.y%size);

        // TODO cache it / take it out of here!
        // reverse faceMapping
        final Map<Integer, Pos> reverseFaces = new HashMap<>();
        for(Map.Entry<Pos, Integer> e : faces.entrySet()) {
            reverseFaces.put(e.getValue(), new Pos(e.getKey().x*size, e.getKey().y*size));
        }

        Pos tmpP = rotateAntiClockwiseAroundOrigin(pos, rotations.get(face));
        return new Pos(tmpP.x + reverseFaces.get(face).x, tmpP.y + reverseFaces.get(face).y);

    }

/*    private Set<Pos> mapToFaces(final Set<Pos> posSet, final int size, final Map<Integer, Integer> rotations) {
        // create a list of faces
        List<Set<Pos>> faceList = List.of(
                new HashSet<>(), new HashSet<>(), new HashSet<>(),
                new HashSet<>(), new HashSet<>(), new HashSet<>());
        for(Pos p : posSet)
            faceList.get(getFace(p, size)).add(new Pos(p.x%size, p.y%size));

        // reverse faceMapping
        final Map<Integer, Pos> reverseFaces = new HashMap<>();
        for(Map.Entry<Pos, Integer> e : faces.entrySet()) {
            reverseFaces.put(e.getValue(), new Pos(e.getKey().x*size, e.getKey().y*size));
        }

        // rotate all to same orientation and recombine into single map
        Set<Pos> result = new HashSet<>();
        for(int i=0; i<6; i++) {
            for(Pos p : faceList.get(i)) {
                Pos tmpP = rotateAntiClockwiseAroundOrigin(p, 1);
                result.add(new Pos(tmpP.x + reverseFaces.get(i).x, tmpP.y + reverseFaces.get(i).y));
            }
        }

        return result;
    }*/

    private static int getFace(Pos p, int size) {
        return faces.get(new Pos(p.x/size, p.y/size));
    }

    private static Vector offCubeMap(Set<Pos> map, Pos p, Orientation orientation) {
        int size = 4;

        int face = getFace(p, size);

//        return mappingFns.get(new CubeMap(getFace(p, size), orientation)).apply(p, size);
        return null;
    }


    private static long resultWithFace(Vector vector, int size) {
        return ((1+ vector.position.x)*4L) + ((1+ vector.position.y)*1000L) + getFace(vector.position, size);
    }

    @Test
    public void testDirectionsTestcaseTwo() {
        State state =
                FileReader.readFileForObject("src/test/resources/2022/D22_t.txt",
                        new State(), D22::parseLine);

//        state.map = mapToFaces(state.map, 4, rotations);
//        state.walls = mapToFaces(state.walls, 4, rotations);

        Engine gameEng = new Engine(
                "AdventOfCode - D22",
                new Window.WindowOptions().setGui(true).setUps(1).setWidth(500).setHeight(500),
                new MonkeyMapTwoD(state, D22::offCubeMap));

        gameEng.start(() -> state.directions.size() == state.directionPosition);
        assertEquals(5031, resultWithFace(state.vector, 4));
    }

    private static final Map<Integer, Integer> rotations = Map.of(
            0, 0,
            1, 1,
            2, 1,
            3, 1,
            4, 1,
            5, 1
    );

    private static Vector rotateAntiClockwiseAroundOrigin(Vector p, int times) {
        for(int i=0; i<times; i++)
            p = new Vector(-p.position.y, p.position.x, applyTurn(p.orientation, Turn.L));
        return p;
    }

    private static Pos rotateAntiClockwiseAroundOrigin(Pos p, int times) {
        for(int i=0; i<times; i++)
            p = new Pos(-p.y, p.x);
        return p;
    }


    @Test
    public void testRotation() {
        Vector v = new Vector(4,0, Orientation.N);
        assertEquals(new Vector(0,4, Orientation.W), rotateAntiClockwiseAroundOrigin(v, 1));
        assertEquals(new Vector(-4,0, Orientation.S), rotateAntiClockwiseAroundOrigin(v, 2));
        assertEquals(new Vector(0,-4, Orientation.E), rotateAntiClockwiseAroundOrigin(v, 3));
    }

    /**
     *              0123456
     *    01234567891111111
     *
     * 0          1111
     * 1          1111
     * 2          1111
     * 3          1111
     * 4  222233334444
     * 5  222233334444
     * 6  222233334444
     * 7  222233334444
     * 8          55556666
     * 9          55556666
     * 10         55556666
     * 11         55556666
     *
     *
     *     2
     *    316
     *     4
     *
     *  rotations
     *  1 = [2,1,3,0]
     */
    @Test
    public void testEdgeCases() {
//        assertEquals(new Vector(0, 4, Orientation.S), offCubeMap(null, new Pos(11, 0), Orientation.N));
//        assertEquals(new Vector(3, 4, Orientation.S), offCubeMap(null, new Pos(8, 0), Orientation.N));

//        assertEquals(new Vector(new Pos(4, 4), Orientation.S), offCubeMap(null, new Pos(8, 0), Orientation.W));
//        assertEquals(new Vector(new Pos(8, 0), Orientation.E), offCubeMap(null, new Pos(4, 4), Orientation.N));
//
//        assertEquals(new Vector(new Pos(7, 4), Orientation.S), offCubeMap(null, new Pos(8, 3), Orientation.W));
//        assertEquals(new Vector(new Pos(4, 4), Orientation.S), offCubeMap(null, new Pos(8, 0), Orientation.W));
//
//        assertEquals(new Vector(new Pos(8, 3), Orientation.E), offCubeMap(null, new Pos(7, 4), Orientation.N));
//        assertEquals(new Vector(new Pos(8, 0), Orientation.E), offCubeMap(null, new Pos(4, 4), Orientation.N));
    }

}
