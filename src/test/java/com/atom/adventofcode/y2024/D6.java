package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D6 {
    private static final String TEST_INPUT = """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...""";

    record Position(int x, int y) { }
    record TravelData(int step, Direction direction) { }
    record Puzzle(List<Position> positions, Position size, Position start) { }

    private Puzzle parseInput(String input) {
        Position start = null;
        List<Position> positionList = new ArrayList<>();
        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    positionList.add(new Position(x, y));
                }
                if (line.charAt(x) == '^') {
                    start = new Position(x, y);
                }
            }
        }
        return new Puzzle(positionList, new Position(lines[0].length(), lines.length), start);
    }

    private enum Direction {UP, RIGHT, DOWN, LEFT}

    private Map<Position, TravelData> patrolLab(final Puzzle puzzle, final Position position) {
        return patrolLabEnd(puzzle, Direction.UP, position, (currentPosition, step) -> currentPosition.x >= 0 && currentPosition.y >= 0 &&
                currentPosition.x < puzzle.size.x && currentPosition.y < puzzle.size.y);
    }

    private Position getNextPosition(Position position, Direction direction) {
        return switch (direction) {
            case UP -> new Position(position.x, position.y-1);
            case DOWN -> new Position(position.x, position.y+1);
            case LEFT -> new Position(position.x-1, position.y);
            case RIGHT -> new Position(position.x+1, position.y);
        };
    }

    private Map<Position, TravelData> patrolLabEnd(Puzzle puzzle, Direction direction, Position position, BiFunction<Position, Integer, Boolean> endCondition) {
        Map<Position, TravelData> visited = new HashMap<>();

        int step = 0;
        while(endCondition.apply(position, step)) {
            visited.put(position, new TravelData(step, direction));
            Position next = getNextPosition(position, direction);
            if(puzzle.positions.contains(next)) {
                // turn right
                direction = Direction.values()[(direction.ordinal() + 1) % 4];
            } else {
                position = next;
            }
            step++;
        }

        printVisted(visited.keySet(), puzzle.size);
        return visited;
    }

    private void printVisted(Collection<Position> visited, Position size) {
        for(int y = 0; y < size.y; y++) {
            for(int x = 0; x < size.x; x++) {
                if(visited.contains(new Position(x, y))) {
                    System.out.print("X");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testPart1() {
        Puzzle puzzle = parseInput(TEST_INPUT);
        assertEquals(41, patrolLab(puzzle, puzzle.start).size());

        puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D6.txt"));
        assertEquals(4580, patrolLab(puzzle, puzzle.start).size());
    }

    private int countLoopPositions(final Puzzle puzzle, final Map<Position, TravelData> visited) {

        // for every position in visited, check if turning right will lead to a loop
        for(Position position : visited.keySet()) {
            TravelData travelData = visited.get(position);

            Position nextPosition = getNextPosition(position, travelData.direction);
            if(puzzle.positions.contains(nextPosition)) {
                // already blocks, no need to check
                continue;
            }

            puzzle.positions.add(nextPosition);
/*

            // run and see if at any point we reach back track with the same direction
            patrolLabEnd(puzzle, travelData.direction, position, (currentPosition, step) -> {
                // check boundary conditions
                if(currentPosition.x < 0 || currentPosition.y < 0 ||
                        currentPosition.x >= puzzle.size.x || currentPosition.y >= puzzle.size.y)
                    return false;

                // check if we are back at the same position with the same direction with an earlier step
                if(visited.containsKey(currentPosition)) {
                    TravelData data = visited.get(currentPosition);
                    return data.direction == travelData.direction && data.step == travelData.step;
                }
            });
*/

            puzzle.positions.remove(nextPosition);



        }

        return 0;
    }

    @Test
    public void testPart2() {
        Puzzle puzzle = parseInput(TEST_INPUT);
        assertEquals(6, countLoopPositions(puzzle, patrolLab(puzzle, puzzle.start)));
    }

}
