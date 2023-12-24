package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import com.atom.adventofcode.y2022.D24;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D10 {

    private static final String input1 = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
            """;

    record Position(int x, int y) {}

    private FileReader.TriFunction<HashMap<Position, Character>, String, Integer, HashMap<Position, Character>> tri = (map, line, yy) -> {
        for(int xx=0; xx<line.length(); xx++) {
            map.put(new Position(xx, yy), line.charAt(xx));
        }
        return map;
    };

    private Set<Position> getCandidates(Map<Position, Character> map, Position position) {
        Set<Position> options = new HashSet<>();
        Position p = new Position(position.x, position.y-1);
        Character c = map.get(p);
        if(c != null && (c == '|' || c == '7' || c == 'F')) {
            options.add(p);
        }
        p = new Position(position.x, position.y+1);
        c = map.get(p);
        if(c != null && (c == '|' || c == 'L' || c == 'J')) {
            options.add(p);
        }
        p = new Position(position.x+1, position.y);
        c = map.get(p);
        if(c != null && (c == '-' || c == '7' || c == 'J')) {
            options.add(p);
        }
        p = new Position(position.x-1, position.y);
        c = map.get(p);
        if(c != null && (c == '-' || c == 'F' || c == 'L')) {
            options.add(p);
        }
        return options;
    }

    private HashMap<Position, Integer> walk(Map<Position, Character> map) {
        // find start 'S'
        Position start =
                map.entrySet().stream().filter(e -> e.getValue().equals('S')).map(Map.Entry::getKey).findFirst().orElseThrow();

        HashMap<Position, Integer> visited = new HashMap<>();
        visited.put(start, 0);

        Set<Position> candidates = getCandidates(map, start);
        int loops = 1;
        while(!candidates.isEmpty()) {
            Set<Position> newCandidates = new HashSet<>();
            for(Position p : candidates) {
                visited.put(p, loops);
                newCandidates.addAll(getCandidates(map, p));
            }
            newCandidates.removeAll(visited.keySet());
            candidates = newCandidates;
            loops++;
        }
        return visited;
    }

    @Test
    public void partOne() {
        HashMap<Position, Character> map = FileReader.parseStringForObject(input1, new HashMap<>(), tri);
        HashMap<Position, Integer> visited = walk(map);
        assertEquals(4, visited.values().stream().mapToInt(i -> i).max().orElseThrow());

        HashMap<Position, Character> map2 = FileReader.readFileForObject("src/main/resources/2023/D10.txt", new HashMap<>(), tri);
        HashMap<Position, Integer> visited2 = walk(map2);
        assertEquals(6812, visited2.values().stream().mapToInt(i -> i).max().orElseThrow());
    }

    private static final String input2 = """
            ..........
            .S------7.
            .|F----7|.
            .||OOOO||.
            .||OOOO||.
            .|L-7F-J|.
            .|II||II|.
            .L--JL--J.
            ..........
            """;

    private int enclosed(HashMap<Position, Integer> visited) {
        int max = 10, maxy = 10;
        int count = 0;
        for(int y=0; y<maxy; y++) {
            for(int x=0; x<max; x++) {
                Position p = new Position(x, y);
                if(isEnclosed(visited, p)) {
                    count++;
                }
            }
        }
        return 0;
    }

    private boolean isEnclosed(HashMap<Position, Integer> visited, Position p) {
        // current be enclosed it must be surrounded by 4 walls
        // the walls must be connected current each other
        return false;
    }

    @Test
    public void partTwo() {
        HashMap<Position, Character> map = FileReader.parseStringForObject(input2, new HashMap<>(), tri);
        HashMap<Position, Integer> visited = walk(map);
        assertEquals(4, enclosed(visited));
    }

}
