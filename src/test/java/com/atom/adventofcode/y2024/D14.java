package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D14 {

    private static final String TEST_INPUT = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
        """;

    record Pos(int x, int y) {}

    private List<Pos[]> parseInput(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> {
                    String[] parts = line.split("[=, ]+");
                    return new Pos[] {new Pos(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])),
                            new Pos(Integer.parseInt(parts[4]), Integer.parseInt(parts[5]))};
                })
                .toList();
    }

    private Pos[] step(Pos[] points, int width, int height) {
        int x = points[0].x + points[1].x;
        int y = points[0].y + points[1].y;
        if(x < 0)
            x += width;
        if(x >= width)
            x -= width;
        if(y < 0)
            y += height;
        if(y >= height)
            y -= height;
        return new Pos[]{new Pos(x, y), points[1]};
    }

    // Rather than stepping each point individually, we can step all points at once by multiplying the steps
    // the distance and taking the modulo of the width and height
    private List<Pos[]> step(List<Pos[]> robots, int steps, int width, int height) {
        for (int i = 0; i < steps; i++) {
            robots = robots.stream().map(p -> step(p, width, height)).toList();
        }
        return robots;
    }

    private long calculateSafetyFactor(List<Pos[]> points, int width, int height) {
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        long c = points.stream().filter(p -> p[0].x > halfWidth && p[0].y < halfHeight).count();
        c *= points.stream().filter(p -> p[0].x < halfWidth && p[0].y < halfHeight).count();
        c *= points.stream().filter(p -> p[0].x > halfWidth && p[0].y > halfHeight).count();
        c *= points.stream().filter(p -> p[0].x < halfWidth && p[0].y > halfHeight).count();

        return c;
    }

    @Test
    public void testPart1() {
        List<Pos[]> points  = parseInput(TEST_INPUT);
        points = step(points, 100, 11, 7);
        assertEquals(12, calculateSafetyFactor(points, 11, 7));

        points  = parseInput(FileReader.readFileString("src/test/resources/2024/D14.txt"));
        points = step(points, 100, 101, 103);
        assertEquals(230435667, calculateSafetyFactor(points, 101, 103));
    }

    private long firstTimeNoOverlap(List<Pos[]> points, int width, int height) {
        int steps = 0;
        while (true) {
            points = step(points, 1, width, height);
            steps++;
            Set<Pos> pp = points.stream().map(p -> p[0]).collect(Collectors.toSet());
            if(pp.size() == points.size())
                return steps;
        }
    }

    @Test
    public void testPart2() {
        List<Pos[]> points  = parseInput(FileReader.readFileString("src/test/resources/2024/D14.txt"));
        assertEquals(7709, firstTimeNoOverlap(points, 101, 103));
    }
}
