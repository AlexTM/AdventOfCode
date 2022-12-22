package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D22 {

    enum Orientation {E, S, W, N}
    enum Turn {L, R}
    record Pos(int x, int y){}
    record Direction(int magnitude, Turn turn){}

    static class LoadingState {
        boolean loadingMap = true;
        int y = 0;
        Set<Pos> map = new HashSet<>();
        Set<Pos> walls = new HashSet<>();
        List<Direction> directions = new ArrayList<>();
    }

    private static LoadingState parseLine(LoadingState loadingState, String line, Integer integer) {
        if(line.isEmpty()) {
            loadingState.loadingMap = false;
            return loadingState;
        }
        if(loadingState.loadingMap) {
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == '.') {
                    loadingState.map.add(new Pos(x, loadingState.y));
                } else if (c == '#') {
                    loadingState.walls.add(new Pos(x, loadingState.y));
                }
            }
            loadingState.y++;
        } else {
            Pattern p = Pattern.compile("([RL]\\d+)");
            Matcher m = p.matcher("R"+line);
            while(m.find()) {
                Turn t = Turn.valueOf(m.group(1).substring(0,1));
                int mag = Integer.parseInt(m.group(1).substring(1));
                loadingState.directions.add(new Direction(mag, t));
            }
        }
        return loadingState;
    }


    private Orientation applyTurn(Orientation orientation, Turn turn) {
        return applyTurn(orientation, turn, 1);
    }
    private Orientation applyTurn(Orientation orientation, Turn turn, int amount) {
        return Orientation.values()[
                Math.abs((orientation.ordinal() + (turn.equals(Turn.R) ? amount : -amount))) % Orientation.values().length];
    }

    private Pos applyStep(Pos pos, Orientation orientation) {
        return switch(orientation) {
            case N -> new Pos(pos.x, pos.y-1);
            case S -> new Pos(pos.x, pos.y+1);
            case W -> new Pos(pos.x-1, pos.y);
            case E -> new Pos(pos.x+1, pos.y);
        };
    }

    static final Map<Orientation, BiFunction<Set<Pos>, Pos, Pos>> getNextPos = Map.of(
            Orientation.N, (map, p) -> new Pos(p.x, map.stream().filter(m -> m.x == p.x).mapToInt(m -> m.y).max().orElseThrow()),
            Orientation.S, (map, p) -> new Pos(p.x, map.stream().filter(m -> m.x == p.x).mapToInt(m -> m.y).min().orElseThrow()),
            Orientation.W, (map, p) -> new Pos(map.stream().filter(m -> m.y == p.y).mapToInt(m -> m.x).max().orElseThrow(), p.y),
            Orientation.E, (map, p) -> new Pos(map.stream().filter(m -> m.y == p.y).mapToInt(m -> m.x).min().orElseThrow(), p.y)
    );

    /**
     * Check if wall is on the edge of a map, if so, create a new wall off map on the
     * opposite side (this preventing walking off the edge of the map)
     */
    private void addMoreWalls(final Set<Pos> map, final Set<Pos> walls) {
        Set<Pos> newWalls = new HashSet<>();
        for(Pos wall : walls) {
            Map<Pos, Orientation> checks = Arrays.stream(Orientation.values())
                    .collect(Collectors.toMap(o -> applyStep(wall, o), Function.identity()));

            for(Map.Entry<Pos, Orientation> e : checks.entrySet()) {
                if (!map.contains(e.getKey())) {
                    // this on the edge in this direction, need to create a wall on it opposite side
                    Pos opp = getNextPos.get(e.getValue()).apply(map, e.getKey());
                    // Need to subtract one square, do this by heading in the opposite direction
                    newWalls.add(applyStep(opp, applyTurn(e.getValue(), Turn.R, 2)));
                }
            }
        }
        walls.addAll(newWalls);
    }


    private long run(Set<Pos> map, Set<Pos> walls, List<Direction> directions, Map<Pos, Orientation> trace) {
        addMoreWalls(map, walls);

        // first direction will point correct Orientation of east
        Orientation currentOrientation = Orientation.N;
        // find the start position
        Pos currentPosition = getNextPos.get(Orientation.E).apply(map, new Pos(0,0));

        for(Direction d : directions) {
            currentOrientation = applyTurn(currentOrientation, d.turn);
            for(int s=0; s<d.magnitude; s++) {

                Pos newPos = applyStep(currentPosition, currentOrientation);
                if(map.contains(newPos)) {
                    currentPosition = newPos;
                } else if(walls.contains(newPos)) {
                    break;
                } else {
                    // gone off end of map, appear on other side
                    currentPosition = getNextPos.get(currentOrientation).apply(map, currentPosition);
                }
                if(trace != null)
                    trace.put(currentPosition, currentOrientation);
            }
            System.out.println(d+" "+currentPosition+" "+currentOrientation);
        }
        return ((1+currentPosition.x)*4L)+((1+currentPosition.y)*1000L)+currentOrientation.ordinal();
    }

    private void print(Set<Pos> map, Set<Pos> walls, Map<Pos, Orientation> trace) {
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

                } else if(map.contains(p)) {
                    System.out.print(".");
                } else if(walls.contains(p)) {
                    System.out.print("#");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }

    @Test
    public void testDirections1() {

        Map<Pos, Orientation> trace = new HashMap<>();
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22_t1.txt", new LoadingState(), D22::parseLine);
        long s = run(loadingState.map, loadingState.walls, loadingState.directions, trace);
        print(loadingState.map, loadingState.walls, trace);
        assertEquals(0, s);
    }


    // 44556 incorrect
    @Test
    public void testDirections() {

        Map<Pos, Orientation> trace = new HashMap<>();

        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22_t.txt", new LoadingState(), D22::parseLine);
        assertEquals(6032, run(loadingState.map, loadingState.walls, loadingState.directions, trace));
        print(loadingState.map, loadingState.walls, trace);
    }

    @Test
    public void testProblem() {
        Map<Pos, Orientation> trace = new HashMap<>();
        // 44556
        // 8479
        LoadingState loadingState =
                FileReader.readFileForObject("src/test/resources/2022/D22.txt", new LoadingState(), D22::parseLine);
        run(loadingState.map, loadingState.walls, loadingState.directions, trace);
//        assertEquals(0, run(loadingState.map, loadingState.walls, loadingState.directions, trace));
        print(loadingState.map, loadingState.walls, trace);
    }

}
