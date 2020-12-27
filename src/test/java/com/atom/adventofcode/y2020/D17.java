package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 17: Conway Cubes ---
 * As your flight slowly drifts through the sky, the Elves at the Mythical Information Bureau at the North Pole
 * contact you. They'd like some help debugging a malfunctioning experimental energy source aboard one of their
 * super-secret imaging satellites.
 *
 * The experimental energy source is based on cutting-edge technology: a set of Conway Cubes contained in a pocket
 * dimension! When you hear it's having problems, you can't help but agree to take a look.
 *
 * The pocket dimension contains an infinite 3-dimensional grid. At every integer 3-dimensional coordinate (x,y,z),
 * there exists a single cube which is either active or inactive.
 *
 * In the initial state of the pocket dimension, almost all cubes start inactive. The only exception to this is a
 * small flat region of cubes (your puzzle input); the cubes in this region start in the specified active (#) or
 * inactive (.) state.
 *
 * The energy source then proceeds to boot up by executing six cycles.
 *
 * Each cube only ever considers its neighbors: any of the 26 other cubes where any of their coordinates differ by at
 * most 1. For example, given the cube at x=1,y=2,z=3, its neighbors include the cube at x=2,y=2,z=2, the cube at
 * x=0,y=2,z=3, and so on.
 *
 * During a cycle, all cubes simultaneously change their state according to the following rules:
 *
 * If a cube is active and exactly 2 or 3 of its neighbors are also active, the cube remains active. Otherwise, the
 * cube becomes inactive.
 * If a cube is inactive but exactly 3 of its neighbors are active, the cube becomes active. Otherwise, the cube
 * remains inactive.
 * The engineers responsible for this experimental energy source would like you to simulate the pocket dimension and
 * determine what the configuration of cubes should be at the end of the six-cycle boot process.
 *
 * For example, consider the following initial state:
 *
 * .#.
 * ..#
 * ###
 * Even though the pocket dimension is 3-dimensional, this initial state represents a small 2-dimensional slice of it
 * . (In particular, this initial state defines a 3x3x1 region of the 3-dimensional space.)
 *
 * Simulating a few cycles from this initial state produces the following configurations, where the result of each
 * cycle is shown layer-by-layer at each given z coordinate (and the frame of view follows the active cells in each
 * cycle):
 *
 * Before any cycles:
 *
 * z=0
 * .#.
 * ..#
 * ###
 *
 *
 * After 1 cycle:
 *
 * z=-1
 * #..
 * ..#
 * .#.
 *
 * z=0
 * #.#
 * .##
 * .#.
 *
 * z=1
 * #..
 * ..#
 * .#.
 *
 *
 * After 2 cycles:
 *
 * z=-2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-1
 * ..#..
 * .#..#
 * ....#
 * .#...
 * .....
 *
 * z=0
 * ##...
 * ##...
 * #....
 * ....#
 * .###.
 *
 * z=1
 * ..#..
 * .#..#
 * ....#
 * .#...
 * .....
 *
 * z=2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 *
 * After 3 cycles:
 *
 * z=-2
 * .......
 * .......
 * ..##...
 * ..###..
 * .......
 * .......
 * .......
 *
 * z=-1
 * ..#....
 * ...#...
 * #......
 * .....##
 * .#...#.
 * ..#.#..
 * ...#...
 *
 * z=0
 * ...#...
 * .......
 * #......
 * .......
 * .....##
 * .##.#..
 * ...#...
 *
 * z=1
 * ..#....
 * ...#...
 * #......
 * .....##
 * .#...#.
 * ..#.#..
 * ...#...
 *
 * z=2
 * .......
 * .......
 * ..##...
 * ..###..
 * .......
 * .......
 * .......
 * After the full six-cycle boot process completes, 112 cubes are left in the active state.
 *
 * Starting with your given initial configuration, simulate six cycles. How many cubes are left in the active state
 * after the sixth cycle?
 *
 * Your puzzle answer was 336.
 *
 * --- Part Two ---
 * For some reason, your simulated results don't match what the experimental energy source engineers expected.
 * Apparently, the pocket dimension actually has four spatial dimensions, not three.
 *
 * The pocket dimension contains an infinite 4-dimensional grid. At every integer 4-dimensional coordinate (x,y,z,w),
 * there exists a single cube (really, a hypercube) which is still either active or inactive.
 *
 * Each cube only ever considers its neighbors: any of the 80 other cubes where any of their coordinates differ by at
 * most 1. For example, given the cube at x=1,y=2,z=3,w=4, its neighbors include the cube at x=2,y=2,z=3,w=3, the
 * cube at x=0,y=2,z=3,w=4, and so on.
 *
 * The initial state of the pocket dimension still consists of a small flat region of cubes. Furthermore, the same
 * rules for cycle updating still apply: during each cycle, consider the number of active neighbors of each cube.
 *
 * For example, consider the same initial state as in the example above. Even though the pocket dimension is
 * 4-dimensional, this initial state represents a small 2-dimensional slice of it. (In particular, this initial state
 * defines a 3x3x1x1 region of the 4-dimensional space.)
 *
 * Simulating a few cycles from this initial state produces the following configurations, where the result of each
 * cycle is shown layer-by-layer at each given z and w coordinate:
 *
 * Before any cycles:
 *
 * z=0, w=0
 * .#.
 * ..#
 * ###
 *
 *
 * After 1 cycle:
 *
 * z=-1, w=-1
 * #..
 * ..#
 * .#.
 *
 * z=0, w=-1
 * #..
 * ..#
 * .#.
 *
 * z=1, w=-1
 * #..
 * ..#
 * .#.
 *
 * z=-1, w=0
 * #..
 * ..#
 * .#.
 *
 * z=0, w=0
 * #.#
 * .##
 * .#.
 *
 * z=1, w=0
 * #..
 * ..#
 * .#.
 *
 * z=-1, w=1
 * #..
 * ..#
 * .#.
 *
 * z=0, w=1
 * #..
 * ..#
 * .#.
 *
 * z=1, w=1
 * #..
 * ..#
 * .#.
 *
 *
 * After 2 cycles:
 *
 * z=-2, w=-2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-1, w=-2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=-2
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=1, w=-2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=-2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-2, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-1, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=1, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=-1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-2, w=0
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=-1, w=0
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=0
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=1, w=0
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=0
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=-2, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-1, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=1, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=1
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=-2, w=2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 *
 * z=-1, w=2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=0, w=2
 * ###..
 * ##.##
 * #...#
 * .#..#
 * .###.
 *
 * z=1, w=2
 * .....
 * .....
 * .....
 * .....
 * .....
 *
 * z=2, w=2
 * .....
 * .....
 * ..#..
 * .....
 * .....
 * After the full six-cycle boot process completes, 848 cubes are left in the active state.
 *
 * Starting with your given initial configuration, simulate six cycles in a 4-dimensional space. How many cubes are
 * left in the active state after the sixth cycle?
 */
public class D17 {

    record Active(int x, int y, int z, int w){}

    private Set<Active> readFile(String filename) throws FileNotFoundException {
        Set<Active> active = new HashSet<>();
        try(Scanner in = new Scanner(new File(filename))) {
            int row = 0;
            while (in.hasNext()) {

                int col = 0;
                for(char c : in.next().toCharArray()) {
                    if(c == '#') {
                        active.add(new Active(col, row, 0, 0));
                    }
                    col++;
                }
                row++;
            }
            in.close();
            return active;
        }
    }

    private Set<Active> initUniverse() {
        Set<Active> active = new HashSet<>();
        active.add(new Active(1, 2,0,0));
        active.add(new Active(2, 1,0,0));
        active.add(new Active(0, 0,0,0));
        active.add(new Active(1, 0,0,0));
        active.add(new Active(2, 0,0,0));
        return active;
    }

    @Test
    public void testPowerSource() throws FileNotFoundException {
        assertEquals(112, activeCubes(initUniverse(), 6, false));
        assertEquals(336, activeCubes(
                readFile("src/test/resources/2020/D17.txt"), 6, false));

        assertEquals(848, activeCubes(initUniverse(), 6, true));
        assertEquals(2620, activeCubes(
                readFile("src/test/resources/2020/D17.txt"), 6, true));

    }

    private int activeCubes(Set<Active> active, int loops, boolean fourD) {
        for(int i=0; i<loops; i++) {
            active = iterateUniverse(active, fourD);
        }
        return active.size();
    }

    private Set<Active> generateCandidates(Active active, boolean fourD) {
        Set<Active> as = new HashSet<>();
        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int m = -1; m < 2; m++) {
                        as.add(new Active(
                                active.x + i, active.y + j, active.z + k, active.w + m
                        ));
                    }
                }
            }
        }
        // Remove center
        as.remove(active);
        // Quick hack, Remove all 4th dimension candidates
        if(!fourD)
            return as.stream().filter(a -> a.w == 0).collect(Collectors.toSet());
        return as;
    }

    private Set<Active> iterateUniverse(Set<Active> actives, boolean fourD) {
        Map<Active, Integer> neighbourCount = new HashMap<>();
        for(Active a: actives) {
            for(Active c : generateCandidates(a, fourD)) {
                int count = neighbourCount.getOrDefault(c, 0);
                neighbourCount.put(c, count + 1);
            }
        }

        Set<Active> nextRound = new HashSet<>();
        neighbourCount.entrySet().stream().forEach(e -> {
            if(actives.contains(e.getKey())) {
                if(e.getValue() == 2 || e.getValue() == 3) {
                    nextRound.add(e.getKey());
                }
            } else {
                if(e.getValue() == 3) {
                    nextRound.add(e.getKey());
                }
            }
        });

        return nextRound;
    }
}
