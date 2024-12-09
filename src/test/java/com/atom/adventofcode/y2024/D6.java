package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    record Travel(Position position, Direction direction) { }
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

    private Map<Travel, Integer> patrolLab(final Puzzle puzzle, final Travel travelStart) {
        return patrolLabEnd(puzzle, travelStart,
                (travel, step) -> travel.position.x >= 0 && travel.position.y >= 0 &&
                        travel.position.x < puzzle.size.x && travel.position.y < puzzle.size.y);
    }

    private Travel getNextPosition(Travel travel) {
        return switch (travel.direction) {
            case UP -> new Travel(new Position(travel.position.x, travel.position.y-1), Direction.UP);
            case DOWN -> new Travel(new Position(travel.position.x, travel.position.y+1), Direction.DOWN);
            case LEFT -> new Travel(new Position(travel.position.x-1, travel.position.y), Direction.LEFT);
            case RIGHT -> new Travel(new Position(travel.position.x+1, travel.position.y), Direction.RIGHT);
        };
    }

    private Map<Travel, Integer> patrolLabEnd(Puzzle puzzle, Travel travel, BiFunction<Travel, Integer, Boolean> endCondition) {
        Map<Travel, Integer> visited = new LinkedHashMap<>();

        int step = 0;
        while(endCondition.apply(travel, step)) {
            visited.put(travel, step);
            Travel next = getNextPosition(travel);
            if(puzzle.positions.contains(next.position)) {
                // turn right
                next = new Travel(travel.position, Direction.values()[(travel.direction.ordinal() + 1) % 4]);
            }
            travel = next;
            step++;
        }
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
        assertEquals(41,
                patrolLab(puzzle, new Travel(puzzle.start, Direction.UP))
                        .keySet().stream().map(t -> t.position).distinct().count());

        puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D6.txt"));
        assertEquals(4580,
                patrolLab(puzzle, new Travel(puzzle.start, Direction.UP))
                        .keySet().stream().map(t -> t.position).distinct().count());
    }

    private int countLoopPositions(final Puzzle puzzle, final Map<Travel, Integer> visited) {

        Set<Position> loopPositions = new HashSet<>();

        // for every position in visited, check if turning right will lead to a loop
        for(Travel travel : visited.keySet()) {

            Travel nextTravel = getNextPosition(travel);
            if(puzzle.positions.contains(nextTravel.position)) {
                // already blocks, no need to check
                continue;
            }

            puzzle.positions.add(nextTravel.position);

            // run and see if at any point we reach back track with the same direction
            patrolLabEnd(puzzle, travel, (currentTravel, step) -> {
                if(step > 10000)
                    return false;

                // check boundary conditions
                if(currentTravel.position.x < 0 || currentTravel.position.y < 0 ||
                        currentTravel.position.x >= puzzle.size.x || currentTravel.position.y >= puzzle.size.y)
                    return false;

                // check if we are back at the same position with the same direction with an earlier step
                if(visited.containsKey(currentTravel)) {
                    Integer stepCountLastTime = visited.get(currentTravel);
                    if(stepCountLastTime < step) {
                        loopPositions.add(currentTravel.position);
                        return false;
                    }
                }

                return true;
            });

            puzzle.positions.remove(nextTravel.position);
        }

        System.out.println(loopPositions);
        printVisted(loopPositions, puzzle.size);

        return loopPositions.size();
    }

    @Test
    public void testPart2() {
        Puzzle puzzle = parseInput(TEST_INPUT);
        assertEquals(6,
                countLoopPositions(puzzle, patrolLab(puzzle, new Travel(puzzle.start, Direction.UP))));

//        puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D6.txt"));
//        int total = countLoopPositions(puzzle, patrolLab(puzzle, puzzle.start));
//        assertNotEquals(743, total);
//        assertEquals(0, total);
    }

}
