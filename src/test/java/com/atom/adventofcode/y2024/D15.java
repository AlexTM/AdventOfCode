package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class D15 {

    private static final String TEST_INPUT = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
            
            <^^>>>vv<v>>v<<""";

    private static final String INPUT = """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########
            
            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^""";

    record Pos(int x, int y) {}
    record Data(Set<Pos> walls, Set<Pos> boxes, List<Character> direction, Pos start){}

    private Data parseInput(String input) {
        String[] parts = input.split("\n");

        Set<Pos> walls = new HashSet<>();
        Set<Pos> boxes = new HashSet<>();
        Pos start = null;
        List<Character> direction = new ArrayList<>();

        for(int y=0; y<parts.length; y++) {
            for(int x=0; x<parts[y].length(); x++) {
                char c = parts[y].charAt(x);
                if(c == '#') walls.add(new Pos(x, y));
                else if(c == 'O' || c == '[') boxes.add(new Pos(x, y));
                else if(c == '@') start = new Pos(x, y);
                else if (c== '.' || c == ']') continue;
                else direction.add(c);
            }
        }
        return new Data(walls, boxes, direction, start);
    }

    private Pos getNext(Pos current, Character direction) {
        return switch (direction) {
            case '^' -> new Pos(current.x, current.y-1);
            case 'v' -> new Pos(current.x, current.y+1);
            case '<' -> new Pos(current.x-1, current.y);
            case '>' -> new Pos(current.x+1, current.y);
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    private String preProcessInput(String input) {
        return input.chars().mapToObj(c -> {
            if(c == '#') return "##";
            else if(c == 'O') return "[]";
            else if(c == '@') return "@.";
            else if (c== '.') return "..";
            else return "" + (char)c;
        }).collect(Collectors.joining());
    }

    private Data stepProgram(Data data, int stepNumber, boolean doubleBoxes) {
        Pos current = data.start;
        Character c = data.direction.get(stepNumber);

        // first check for the robot
        Pos next = getNext(current, c);
        if(data.walls.contains(next)) {
            return data;
        }

        // Generate Set of primary and secondary boxes if needed
        Set<Pos> tmpBoxes = !doubleBoxes ? data.boxes : data.boxes.stream()
                .flatMap(b -> Set.of(b, new Pos(b.x+1, b.y)).stream())
                .collect(Collectors.toSet());

        if(tmpBoxes.contains(next)) {
            Collection<Pos> boxes = checkForSpace(data, tmpBoxes, next, c, doubleBoxes);
            if (boxes == null) {
                return data;
            }

            // boxes which are successfully removed from the list are primary boxes
            List<Pos> primaryBoxes = boxes.stream().filter(data.boxes::remove).toList();
            // update primary boxes with the new position
            primaryBoxes.stream().map(box -> getNext(box, c)).forEach(data.boxes::add);
        }
        return new Data(data.walls, data.boxes, data.direction, next);
    }

    private Collection<Pos> checkForSpace(Data data, Set<Pos> tmpBoxes, Pos pos, Character direction, boolean doubleBoxes) {
        List<Pos> boxes = new ArrayList<>();
        Queue<Pos> currentList = new LinkedList<>();
        currentList.add(pos);
        Set<Pos> visited = new HashSet<>();

        while(!currentList.isEmpty()) {
            Pos current = currentList.poll();
            if(visited.contains(current))
                continue;
            visited.add(current);

            if(data.walls.contains(current))
                return null;

             if(tmpBoxes.contains(current)) {
                Pos primaryBox = data.boxes.contains(current) ? current : new Pos(current.x-1, current.y);
                boxes.add(primaryBox);
                currentList.add(getNext(primaryBox, direction));

                if(doubleBoxes) {
                    Pos secondaryBox = new Pos(primaryBox.x+1, primaryBox.y);
                    boxes.add(secondaryBox);
                    currentList.add(getNext(secondaryBox, direction));
                }
            }
        }
        return boxes;
    }

    private Data runProgram2(Data data, boolean doubleBoxes) {
        for (int i = 0; i < data.direction.size(); i++) {
            data = stepProgram(data, i, doubleBoxes);
        }
        return data;
    }

    private long sumGPS(Data data) {
        return data.boxes.stream().mapToLong(p -> p.x+(p.y*100L)).sum();
    }

    @Test
    public void testPartOne() {
        assertEquals(2028,sumGPS(runProgram2(parseInput(TEST_INPUT), false)));
        assertEquals(1490942,sumGPS(runProgram2(parseInput(FileReader.readFileString("src/test/resources/2024/D15.txt")), false)));
    }

    @Test
    public void testPartTwo() {
        assertEquals(9021, sumGPS(runProgram2(
                parseInput(preProcessInput(INPUT)), true)));
        assertEquals(1519202, sumGPS(runProgram2(
                parseInput(preProcessInput(
                FileReader.readFileString("src/test/resources/2024/D15.txt"))), true)));
    }

}
