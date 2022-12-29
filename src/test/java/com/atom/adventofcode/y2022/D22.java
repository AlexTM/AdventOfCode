package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
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
    record Result(Pos position, Orientation orientation){}

    static class LoadingState {
        boolean loadingMap = true;
        Set<Pos> map = new HashSet<>();
        Set<Pos> walls = new HashSet<>();
        List<Direction> directions = new ArrayList<>();
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

    private static long setUpAndRun(
            final Set<Pos> map,
            final Set<Pos> walls,
            final List<Direction> directions,
            final Map<Pos, Orientation> trace) {

        // find the start position
        Pos position = offMap(map, new Pos(0,0), Orientation.E);

        // first direction will point correct Orientation of east
        Result result = run(map, walls, directions, position, Orientation.E, trace);

        return ((1+result.position.x)*4L)+((1+result.position.y)*1000L)+result.orientation.ordinal();
    }


    private static Result run(
            final Set<Pos> map,
            final Set<Pos> walls,
            final List<Direction> directions,
            Pos currentPosition,
            Orientation currentOrientation,
            final Map<Pos, Orientation> trace) {

        for(Direction d : directions) {
            if(d.turn != null) {
                currentOrientation = applyTurn(currentOrientation, d.turn);
                if (trace != null)
                    trace.put(currentPosition, currentOrientation);
            } else if(d.magnitude != null) {
                for (int s = 0; s < d.magnitude; s++) {
                    Pos newPos = applyStep(currentPosition, currentOrientation);

                    if (walls.contains(newPos)) {
                        break;
                    } else if (map.contains(newPos)) {
                        currentPosition = newPos;
                    } else {
                        // gone off end of map, appear on other side
                        Pos p = offMap(map, currentPosition, currentOrientation);
                        if(walls.contains(p)) {
                            break;
                        } else {
                            currentPosition = p;
                        }
                    }
                    if (trace != null)
                        trace.put(currentPosition, currentOrientation);
                }
            }
        }
        return new Result(currentPosition, currentOrientation);
    }

    private static void print(final Set<Pos> map, final Set<Pos> walls, final Map<Pos, Orientation> trace) {
        int minx = Stream.of(walls, map).flatMap(Collection::stream).mapToInt(m -> m.x).min().orElseThrow();
        int miny = Stream.of(walls, map).flatMap(Collection::stream).mapToInt(m -> m.y).min().orElseThrow();
        int maxx = Stream.of(walls, map).flatMap(Collection::stream).mapToInt(m -> m.x).max().orElseThrow();
        int maxy = Stream.of(walls, map).flatMap(Collection::stream).mapToInt(m -> m.y).max().orElseThrow();

        for(int y=miny; y<maxy+1; y++) {
            for (int x = minx; x <= maxx; x++) {
                Pos p = new Pos(x, y);
                if(trace.containsKey(p)) {
                    switch(trace.get(p)) {
                        case E -> System.out.print(">");
                        case W -> System.out.print("<");
                        case N -> System.out.print("^");
                        case S -> System.out.print("v");
                    }
                } else if(walls.contains(p)) {
                    System.out.print("#");
                } else if(map.contains(p)) {
                    System.out.print(".");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }

    @Test
    public void testDirectionsOffMap() {

        Map<Pos, Orientation> trace = new HashMap<>();
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22_t1.txt", new LoadingState(), D22::parseLine);
        Result r = run(loadingState.map, loadingState.walls, loadingState.directions,
                new Pos(2,2), Orientation.N, trace);
        print(loadingState.map, loadingState.walls, trace);
        System.out.println(r);
        assertEquals(new Pos(2,4), r.position);

        trace = new HashMap<>();
        r = run(loadingState.map, loadingState.walls, loadingState.directions,
                new Pos(2,2), Orientation.S, trace);
        print(loadingState.map, loadingState.walls, trace);
        System.out.println(r);
        assertEquals(new Pos(2, 0), r.position);

        trace = new HashMap<>();
        r = run(loadingState.map, loadingState.walls, loadingState.directions,
                new Pos(2,2), Orientation.E, trace);
        print(loadingState.map, loadingState.walls, trace);
        System.out.println(r);
        assertEquals(new Pos(0, 2), r.position);

        trace = new HashMap<>();
        r = run(loadingState.map, loadingState.walls, loadingState.directions,
                new Pos(2,2), Orientation.W, trace);
        print(loadingState.map, loadingState.walls, trace);
        System.out.println(r);
        assertEquals(new Pos(4, 2), r.position);
    }

    @Test
    public void testDirections() {
        Map<Pos, Orientation> trace = new HashMap<>();
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22_t.txt", new LoadingState(), D22::parseLine);
        long res = setUpAndRun(loadingState.map, loadingState.walls, loadingState.directions, trace);
        print(loadingState.map, loadingState.walls, trace);
        assertEquals(6032, res);
    }

    @Test
    public void testProblem() {
        Map<Pos, Orientation> trace = new HashMap<>();
        // 44556
        // 8479
        // 89220
        // 89220
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22.txt",
                        new LoadingState(), D22::parseLine);
        long res = setUpAndRun(loadingState.map, loadingState.walls, loadingState.directions, trace);
//        print(loadingState.map, loadingState.walls, trace);
        assertNotEquals(89220, res);
        System.out.println("Res "+res);
    }
}
