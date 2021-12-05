package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 5: Hydrothermal Venture ---
 *
 * You come across a field of hydrothermal vents on the ocean floor! These vents constantly produce large, opaque
 * clouds, so it would be best to avoid them if possible.
 *
 * They tend to form in lines; the submarine helpfully produces a list of nearby lines of vents (your puzzle input) for
 * you to review. For example:
 *
 * 0,9 -> 5,9
 * 8,0 -> 0,8
 * 9,4 -> 3,4
 * 2,2 -> 2,1
 * 7,0 -> 7,4
 * 6,4 -> 2,0
 * 0,9 -> 2,9
 * 3,4 -> 1,4
 * 0,0 -> 8,8
 * 5,5 -> 8,2
 *
 * Each line of vents is given as a line segment in the format x1,y1 -> x2,y2 where x1,y1 are the coordinates of one
 * end the line segment and x2,y2 are the coordinates of the other end. These line segments include the points at both
 * ends. In other words:
 *
 *     An entry like 1,1 -> 1,3 covers points 1,1, 1,2, and 1,3.
 *     An entry like 9,7 -> 7,7 covers points 9,7, 8,7, and 7,7.
 *
 * For now, only consider horizontal and vertical lines: lines where either x1 = x2 or y1 = y2.
 *
 * So, the horizontal and vertical lines from the above list would produce the following diagram:
 *
 * .......1..
 * ..1....1..
 * ..1....1..
 * .......1..
 * .112111211
 * ..........
 * ..........
 * ..........
 * ..........
 * 222111....
 *
 * In this diagram, the top left corner is 0,0 and the bottom right corner is 9,9. Each position is shown as the number
 * of lines which cover that point or . if no line covers that point. The top-left pair of 1s, for example, comes
 * from 2,2 -> 2,1; the very bottom row is formed by the overlapping lines 0,9 -> 5,9 and 0,9 -> 2,9.
 *
 * To avoid the most dangerous areas, you need to determine the number of points where at least two lines overlap. In
 * the above example, this is anywhere in the diagram with a 2 or larger - a total of 5 points.
 *
 * Consider only horizontal and vertical lines. At how many points do at least two lines overlap?
 *
 * --- Part Two ---
 *
 * Unfortunately, considering only horizontal and vertical lines doesn't give you the full picture; you need to also
 * consider diagonal lines.
 *
 * Because of the limits of the hydrothermal vent mapping system, the lines in your list will only ever be horizontal,
 * vertical, or a diagonal line at exactly 45 degrees. In other words:
 *
 *     An entry like 1,1 -> 3,3 covers points 1,1, 2,2, and 3,3.
 *     An entry like 9,7 -> 7,9 covers points 9,7, 8,8, and 7,9.
 *
 * Considering all lines from the above example would now produce the following diagram:
 *
 * 1.1....11.
 * .111...2..
 * ..2.1.111.
 * ...1.2.2..
 * .112313211
 * ...1.2....
 * ..1...1...
 * .1.....1..
 * 1.......1.
 * 222111....
 *
 * You still need to determine the number of points where at least two lines overlap. In the above example, this is
 * still anywhere in the diagram with a 2 or larger - now a total of 12 points.
 *
 * Consider all of the lines. At how many points do at least two lines overlap?
 *
 */
public class D5 {

    record Coord(int x, int y){};
    record Cloud(int x1, int y1, int x2, int y2) { };

    public List<Cloud> loadData(String filename) {
        return FileReader.readFileObjectList(filename, line -> {
            String[] parts = line.split(" -> ");
            String[] l = parts[0].split(",");
            int x1 = Integer.parseInt(l[0]);
            int y1 = Integer.parseInt(l[1]);
            String[] r = parts[1].split(",");
            int x2 = Integer.parseInt(r[0]);
            int y2 = Integer.parseInt(r[1]);
            return new Cloud(x1, y1, x2, y2);
        });
    }

    private List<Coord> generateCoordsFromCloud(Cloud c) {
        List<Coord> coords = new ArrayList<>();
        int posx = c.x1;
        int posy = c.y1;
        coords.add(new Coord(posx, posy));
        while(posx != c.x2 || posy != c.y2) {
            if(posx > c.x2) posx--;
            if(posx < c.x2) posx++;
            if(posy > c.y2) posy--;
            if(posy < c.y2) posy++;
            coords.add(new Coord(posx, posy));
        }
        return coords;
    }

    private long countCoords(List<Cloud> clouds) {
        return clouds.stream().flatMap(c -> generateCoordsFromCloud(c).stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() >= 2).count();
    }

    @Test
    public void testPart1() {
        List<Cloud> clouds = loadData("src/test/resources/2021/D5_t.txt");
        List<Cloud> straightLines = clouds.stream().filter(c -> c.x1 == c.x2 || c.y1 == c.y2).collect(Collectors.toList());
        assertEquals(5, countCoords(straightLines));

        List<Cloud> clouds2 = loadData("src/test/resources/2021/D5.txt");
        List<Cloud> straightLines2 = clouds2.stream().filter(c -> c.x1 == c.x2 || c.y1 == c.y2).collect(Collectors.toList());
        assertEquals(7473, countCoords(straightLines2));
    }

    @Test
    public void testPart2() {
        List<Cloud> clouds = loadData("src/test/resources/2021/D5_t.txt");
        assertEquals(12, countCoords(clouds));

        List<Cloud> clouds2 = loadData("src/test/resources/2021/D5.txt");
        assertEquals(24164, countCoords(clouds2));
    }
}
