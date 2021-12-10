package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 9: Smoke Basin ---
 *
 * These caves seem to be lava tubes. Parts are even still volcanically active; small hydrothermal vents release smoke
 * into the caves that slowly settles like rain.
 *
 * If you can model how the smoke flows through the caves, you might be able to avoid it and be that much safer. The
 * submarine generates a heightmap of the floor of the nearby caves for you (your puzzle input).
 *
 * Smoke flows to the lowest point of the area it's in. For example, consider the following heightmap:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 *
 * Each number corresponds to the height of a particular location, where 9 is the highest and 0 is the lowest a
 * location can be.
 *
 * Your first goal is to find the low points - the locations that are lower than any of its adjacent locations. Most
 * locations have four adjacent locations (up, down, left, and right); locations on the edge or corner of the map have
 * three or two adjacent locations, respectively. (Diagonal locations do not count as adjacent.)
 *
 * In the above example, there are four low points, all highlighted: two are in the first row (a 1 and a 0), one is in
 * the third row (a 5), and one is in the bottom row (also a 5). All other locations on the heightmap have some lower
 * adjacent location, and so are not low points.
 *
 * The risk level of a low point is 1 plus its height. In the above example, the risk levels of the low points are 2,
 * 1, 6, and 6. The sum of the risk levels of all low points in the heightmap is therefore 15.
 *
 * Find all of the low points on your heightmap. What is the sum of the risk levels of all low points on your heightmap?
 *
 * --- Part Two ---
 *
 * Next, you need to find the largest basins so you know what areas are most important to avoid.
 *
 * A basin is all locations that eventually flow downward to a single low point. Therefore, every low point has a basin,
 * although some basins are very small. Locations of height 9 do not count as being in any basin, and all other
 * locations will always be part of exactly one basin.
 *
 * The size of a basin is the number of locations within the basin, including the low point. The example above has four
 * basins.
 *
 * The top-left basin, size 3:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 *
 * The top-right basin, size 9:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 *
 * The middle basin, size 14:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 *
 * The bottom-right basin, size 9:
 *
 * 2199943210
 * 3987894921
 * 9856789892
 * 8767896789
 * 9899965678
 *
 * Find the three largest basins and multiply their sizes together. In the above example, this is 9 * 14 * 9 = 1134.
 *
 * What do you get if you multiply together the sizes of the three largest basins?
 *
 */
public class D9 {

    record Pos(int i, int j) {};

    // Could do in a matrix, but messing around
    class Caves {
        int dimx, dimy;
        Map<Pos, Integer> floor = new HashMap<>();
    }

    public Caves loadFile(String filename) {
        List<List<Integer>> values =
                FileReader.readFileObjectList(filename, line -> Arrays.stream(line.split(""))
                .map(Integer::parseInt).collect(Collectors.toList()));
        Caves caves = new Caves();
        caves.dimx = values.size();
        caves.dimy = values.get(0).size();
        for(int i=0; i<values.size(); i++) {
            for(int j=0; j<values.get(0).size(); j++) {
                caves.floor.put(new Pos(i, j), values.get(i).get(j));
            }
        }
        return caves;
    }

    public Set<Pos> getAdjacent(Caves caves, Pos p) {
        Set<Pos> adjacent = new HashSet<>();
        if(p.i > 0) adjacent.add(new Pos(p.i-1, p.j));
        if(p.i < caves.dimx-1) adjacent.add(new Pos(p.i+1, p.j));
        if(p.j > 0) adjacent.add(new Pos(p.i, p.j-1));
        if(p. j <caves.dimy-1) adjacent.add(new Pos(p.i, p.j+1));
        return adjacent;
    }

    private boolean checkIfLow(Caves caves, Pos p) {
        int v = caves.floor.get(p);
        Set<Pos> adjacent = getAdjacent(caves, p);
        for(Pos ad : adjacent) {
            if(v >= caves.floor.get(ad))
                return false;
        }
        return true;
    }

    public long riskLevel(Caves caves) {
        return caves.floor.entrySet().stream().filter(e -> checkIfLow(caves, e.getKey()))
                .map(Map.Entry::getValue).reduce(0, (res, i) -> res + i + 1);
    }

    @Test
    public void testPart1() {
        assertEquals(15, riskLevel(loadFile("src/test/resources/2021/D9_t.txt")));
        assertEquals(600, riskLevel(loadFile("src/test/resources/2021/D9.txt")));
    }

    class Basin {
        Set<Pos> points = new HashSet<>();
    }

    public Basin mapBasin(final Caves caves, final Pos lowPoint) {
        Basin basin = new Basin();

        final Queue<Pos> queue = new LinkedList<>();
        final Set<Pos> visited = new HashSet<>();

        queue.add(lowPoint);
        while(!queue.isEmpty()) {
            Pos pos = queue.poll();
            visited.add(pos);
            if(caves.floor.get(pos) != 9) {
                Set<Pos> candidates = getAdjacent(caves, pos);
                candidates.removeAll(visited);
                queue.addAll(candidates);
            }
        }
        basin.points = visited.stream()
                .filter(p -> caves.floor.get(p) != 9)
                .collect(Collectors.toSet());

        return basin;
    }

    private int topThree(Caves caves) {
        final Set<Pos> inBasin = new HashSet<>();
        final List<Basin> basins = new ArrayList<>();

        caves.floor.keySet().stream()
                .filter(p -> !inBasin.contains(p))
                .filter(p -> checkIfLow(caves, p))
                .forEach(p -> {
                    Basin b = mapBasin(caves, p);
                    basins.add(b);
                    inBasin.addAll(b.points);
                });
        
        basins.sort(Comparator.comparingInt(a -> a.points.size()));
        Collections.reverse(basins);

        return IntStream.range(0, 3).reduce(1, (res, i) -> res * basins.get(i).points.size());
    }

    @Test
    public void testPart2() {
        assertEquals(1134, topThree(loadFile("src/test/resources/2021/D9_t.txt")));
        assertEquals(987840, topThree(loadFile("src/test/resources/2021/D9.txt")));
    }

}
