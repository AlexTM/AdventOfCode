package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D20 {
    private static final String TEST_INPUT = """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############""";

    record Pos(int x, int y) {}
    record Track(Set<Pos> path, Pos start, Pos end) {}

    private Track parseInput(String input) {
        String[] parts = input.split("\n");

        Set<Pos> path = new HashSet<>();
        Pos start = null;
        Pos end = null;

        for(int y=0; y<parts.length; y++) {
            for(int x=0; x<parts[y].length(); x++) {
                char c = parts[y].charAt(x);
                if(c == '.') {
                    path.add(new Pos(x, y));
                } else if(c == 'S') {
                    start = new Pos(x, y);
                    path.add(new Pos(x, y));
                } else if(c == 'E') {
                    end = new Pos(x, y);
                    path.add(new Pos(x, y));
                }
            }
        }
        return new Track(path, start, end);
    }

    private Set<Pos> getCandidates(final Set<Pos> path, final Pos current) {
        return Stream.of(
                        new Pos(current.x-1, current.y),
                        new Pos(current.x+1, current.y),
                        new Pos(current.x, current.y-1),
                        new Pos(current.x, current.y+1))
                .filter(path::contains)
                .collect(Collectors.toSet());
    }

    private List<Pos> getFollowedPath(final Map<Pos, Long> distances, Pos current) {
        List<Pos> path = new ArrayList<>();
        while(distances.get(current) != 0) {
            for(Pos next : getCandidates(distances.keySet(), current)) {
                if(distances.get(next) == distances.get(current) - 1) {
                    path.add(current);
                    current = next;
                    break;
                }
            }
        }
        path.add(current);
        Collections.reverse(path);
        return path;
    }

    private Map<Pos, Long> solve(final Set<Pos> path, final Pos start, final Pos end) {

        final Map<Pos, Long> distanceMap = new HashMap<>();
        distanceMap.put(start, 0L);

        PriorityQueue<Pos> queue = new PriorityQueue<>(Comparator.comparingLong(distanceMap::get));
        queue.add(start);

        while(!queue.isEmpty()) {
            Pos current = queue.poll();

            Set<Pos> candidates = getCandidates(path, current);
            for(Pos e : candidates) {
                long oldDistance = distanceMap.getOrDefault(e, Long.MAX_VALUE);
                long newDistance = distanceMap.get(current) + 1;
                if(newDistance < oldDistance) {
                    distanceMap.put(e, newDistance);
                    queue.add(e);
                }
            }
            if(current.equals(end)) {
                break;
            }
        }
        return distanceMap;
    }

    private Set<Pos> createBox(final Pos initial, int n) {
        Set<Pos> box = new HashSet<>();
        for(int x=0; x<n; x++) {
            for(int y=0; y<n-x; y++) {
                box.add(new Pos(initial.x + x, initial.y + y));
                box.add(new Pos(initial.x - x, initial.y + y));
                box.add(new Pos(initial.x + x, initial.y - y));
                box.add(new Pos(initial.x - x, initial.y - y));
            }
        }
        return box;
    }

    private List<Long> givenNStepsWhatIsHighestPositionReachable(final Track track, int n) {

        Map<Pos, Long> distances = solve(track.path, track.start, track.end);
        long firstTime = distances.get(track.end);
        List<Pos> path = getFollowedPath(distances, track.end);

        List<Long> timeSaved = new ArrayList<>();

        for(Pos current : path) {
            // create a box of nxn and check for highest path number in there.
            Set<Pos> box = createBox(current, n);
            Set<Pos> tmpPath = new HashSet<>(path);
            tmpPath.retainAll(box);

            // find the highest position from distances
            for(Pos p : tmpPath) {
                long timeCost = distances.get(current) + firstTime - distances.get(p) + distance(current, p);
                timeSaved.add(firstTime - timeCost);
            }
        }
        return timeSaved;
    }

    private long distance(final Pos current, final Pos next) {
        return Math.abs(current.x - next.x) + Math.abs(current.y - next.y);
    }

    @Test
    public void testPart1() {
        List<Long> timeSaved = givenNStepsWhatIsHighestPositionReachable(parseInput(TEST_INPUT), 3);
        assertEquals(14, timeSaved.stream().filter(t -> t == 2).count());
        assertEquals(14, timeSaved.stream().filter(t -> t == 4).count());
        assertEquals(2, timeSaved.stream().filter(t -> t == 6).count());
        assertEquals(4, timeSaved.stream().filter(t -> t == 8).count());
        assertEquals(2, timeSaved.stream().filter(t -> t == 10).count());
        assertEquals(3, timeSaved.stream().filter(t -> t == 12).count());
        assertEquals(1, timeSaved.stream().filter(t -> t == 20).count());
        assertEquals(1, timeSaved.stream().filter(t -> t == 36).count());
        assertEquals(1, timeSaved.stream().filter(t -> t == 38).count());
        assertEquals(1, timeSaved.stream().filter(t -> t == 40).count());
        assertEquals(1, timeSaved.stream().filter(t -> t == 64).count());

        timeSaved = givenNStepsWhatIsHighestPositionReachable(
                parseInput(FileReader.readFileString("src/test/resources/2024/D20.txt")), 3);
        assertEquals(1502, timeSaved.stream().filter(t -> t >= 100).count());
    }

    @Test
    public void testPart2() {
        List<Long> timeSaved = givenNStepsWhatIsHighestPositionReachable(parseInput(TEST_INPUT), 21);
        assertEquals(32, timeSaved.stream().filter(t -> t == 50).count());
        assertEquals(31, timeSaved.stream().filter(t -> t == 52).count());

        timeSaved = givenNStepsWhatIsHighestPositionReachable(
                parseInput(FileReader.readFileString("src/test/resources/2024/D20.txt")), 21);
        assertEquals(1028136, timeSaved.stream().filter(t -> t >= 100).count());
    }
}
