package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * With the toboggan login problems resolved, you set off toward the airport. While travel by toboggan might be easy,
 * it's certainly not safe: there's very minimal steering and the area is covered in trees. You'll need to see which
 * angles will take you near the fewest trees.
 *
 * Due to the local geology, trees in this area only grow on exact integer coordinates in a grid. You make a map (your
 * puzzle input) of the open squares (.) and trees (#) you can see. For example:
 *
 * ..##.......
 * #...#...#..
 * .#....#..#.
 * ..#.#...#.#
 * .#...##..#.
 * ..#.##.....
 * .#.#.#....#
 * .#........#
 * #.##...#...
 * #...##....#
 * .#..#...#.#
 * These aren't the only trees, though; due to something you read about once involving arboreal genetics and biome
 * stability, the same pattern repeats to the right many times:
 *
 * ..##.........##.........##.........##.........##.........##.......  --->
 * #...#...#..#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..
 * .#....#..#..#....#..#..#....#..#..#....#..#..#....#..#..#....#..#.
 * ..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#
 * .#...##..#..#...##..#..#...##..#..#...##..#..#...##..#..#...##..#.
 * ..#.##.......#.##.......#.##.......#.##.......#.##.......#.##.....  --->
 * .#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#
 * .#........#.#........#.#........#.#........#.#........#.#........#
 * #.##...#...#.##...#...#.##...#...#.##...#...#.##...#...#.##...#...
 * #...##....##...##....##...##....##...##....##...##....##...##....#
 * .#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#.#..#...#.#  --->
 * You start on the open square (.) in the top-left corner and need to reach the bottom (below the bottom-most row on
 * your map).
 *
 * The toboggan can only follow a few specific slopes (you opted for a cheaper model that prefers rational numbers);
 * start by counting all the trees you would encounter for the slope right 3, down 1:
 *
 * From your starting position at the top-left, check the position that is right 3 and down 1. Then, check the position
 * that is right 3 and down 1 from there, and so on until you go past the bottom of the map.
 *
 * The locations you'd check in the above example are marked here with O where there was an open square and X where
 * there was a tree:
 *
 * ..##.........##.........##.........##.........##.........##.......  --->
 * #..O#...#..#...#...#..#...#...#..#...#...#..#...#...#..#...#...#..
 * .#....X..#..#....#..#..#....#..#..#....#..#..#....#..#..#....#..#.
 * ..#.#...#O#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#..#.#...#.#
 * .#...##..#..X...##..#..#...##..#..#...##..#..#...##..#..#...##..#.
 * ..#.##.......#.X#.......#.##.......#.##.......#.##.......#.##.....  --->
 * .#.#.#....#.#.#.#.O..#.#.#.#....#.#.#.#....#.#.#.#....#.#.#.#....#
 * .#........#.#........X.#........#.#........#.#........#.#........#
 * #.##...#...#.##...#...#.X#...#...#.##...#...#.##...#...#.##...#...
 * #...##....##...##....##...#X....##...##....##...##....##...##....#
 * .#..#...#.#.#..#...#.#.#..#...X.#.#..#...#.#.#..#...#.#.#..#...#.#  --->
 * In this example, traversing the map using this slope would cause you to encounter 7 trees.
 *
 * Starting at the top-left corner of your map and following a slope of right 3 and down 1, how many trees would you
 * encounter?
 *
 * --- Part Two ---
 * Time to check the rest of the slopes - you need to minimize the probability of a sudden arboreal stop, after all.
 *
 * Determine the number of trees you would encounter if, for each of the following slopes, you start at the top-left corner and traverse the map all the way to the bottom:
 *
 * Right 1, down 1.
 * Right 3, down 1. (This is the slope you already checked.)
 * Right 5, down 1.
 * Right 7, down 1.
 * Right 1, down 2.
 * In the above example, these slopes would find 2, 7, 3, 4, and 2 tree(s) respectively; multiplied together, these produce the answer 336.
 *
 */
public class D3 {

    private List<char[]> readFile(String filename) throws FileNotFoundException {
        List<char[]> values = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            while (in.hasNext()) {
                String line = in.nextLine();
                values.add(line.toCharArray());
            }
            in.close();
            return values;
        }
    }

    @Test
    public void testTrees() throws FileNotFoundException {
        List<char[]> inp = readFile("src/test/resources/2020/D3_t.txt");
        assertEquals(7, countTrees(inp, 3, 1));

        System.out.println("Trees: "+countTrees(readFile("src/test/resources/2020/D3.txt"), 3, 1));

    }

    @Test
    public void testTrees2() throws FileNotFoundException {
        List<char[]> inp = readFile("src/test/resources/2020/D3_t.txt");
        assertEquals(2, countTrees(inp, 1, 1));
        assertEquals(7, countTrees(inp, 3, 1));
        assertEquals(3, countTrees(inp, 5, 1));
        assertEquals(4, countTrees(inp, 7, 1));
        assertEquals(2, countTrees(inp, 1, 2));

        int[][] inputs = new int[][] { {1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2} };
        long res = 1;
        for(int [] steps : inputs) {
            res *= countTrees(inp, steps[0], steps[1]);
        }
        assertEquals(336, res);

        inp = readFile("src/test/resources/2020/D3.txt");
        res = 1;
        for(int [] steps : inputs) {
            res *= countTrees(inp, steps[0], steps[1]);
        }
        System.out.println("Trees: "+res);

    }


    public int countTrees(List<char[]> inp, int right, int down) {
        int count = 0;
        int x = 0;
        for(int i=0; i<inp.size(); i += down) {
            char[] row = inp.get(i);
            if(row[x] == '#') {
                count++;
            }
            x += right;
            if(x >= row.length)
                x -= row.length;
        }
        return count;
    }
}
