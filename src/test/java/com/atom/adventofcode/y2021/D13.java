package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 13: Transparent Origami ---
 *
 * You reach another volcanically active part of the cave. It would be nice if you could do some kind of thermal
 * imaging so you could tell ahead of time which caves are too hot to safely enter.
 *
 * Fortunately, the submarine seems to be equipped with a thermal camera! When you activate it, you are greeted with:
 *
 * Congratulations on your purchase! To activate this infrared thermal imaging
 * camera system, please enter the code found on page 1 of the manual.
 *
 * Apparently, the Elves have never used this feature. To your surprise, you manage to find the manual; as you go to
 * open it, page 1 falls out. It's a large sheet of transparent paper! The transparent paper is marked with random
 * dots and includes instructions on how to fold it up (your puzzle input). For example:
 *
 * 6,10
 * 0,14
 * 9,10
 * 0,3
 * 10,4
 * 4,11
 * 6,0
 * 6,12
 * 4,1
 * 0,13
 * 10,12
 * 3,4
 * 3,0
 * 8,4
 * 1,10
 * 2,14
 * 8,10
 * 9,0
 *
 * fold along y=7
 * fold along x=5
 *
 * The first section is a list of dots on the transparent paper. 0,0 represents the top-left coordinate. The first
 * value, x, increases to the right. The second value, y, increases downward. So, the coordinate 3,0 is to the right
 * of 0,0, and the coordinate 0,7 is below 0,0. The coordinates in this example form the following pattern, where #
 * is a dot on the paper and . is an empty, unmarked position:
 *
 * ...#..#..#.
 * ....#......
 * ...........
 * #..........
 * ...#....#.#
 * ...........
 * ...........
 * ...........
 * ...........
 * ...........
 * .#....#.##.
 * ....#......
 * ......#...#
 * #..........
 * #.#........
 *
 * Then, there is a list of fold instructions. Each instruction indicates a line on the transparent paper and wants
 * you to fold the paper up (for horizontal y=... lines) or left (for vertical x=... lines). In this example, the first
 * fold instruction is fold along y=7, which designates the line formed by all of the positions where y is 7 (marked
 * here with -):
 *
 * ...#..#..#.
 * ....#......
 * ...........
 * #..........
 * ...#....#.#
 * ...........
 * ...........
 * -----------
 * ...........
 * ...........
 * .#....#.##.
 * ....#......
 * ......#...#
 * #..........
 * #.#........
 *
 * Because this is a horizontal line, fold the bottom half up. Some of the dots might end up overlapping after the
 * fold is complete, but dots will never appear exactly on a fold line. The result of doing this fold looks like this:
 *
 * #.##..#..#.
 * #...#......
 * ......#...#
 * #...#......
 * .#.#..#.###
 * ...........
 * ...........
 *
 * Now, only 17 dots are visible.
 *
 * Notice, for example, the two dots in the bottom left corner before the transparent paper is folded; after the fold
 * is complete, those dots appear in the top left corner (at 0,0 and 0,1). Because the paper is transparent, the dot
 * just below them in the result (at 0,3) remains visible, as it can be seen through the transparent paper.
 *
 * Also notice that some dots can end up overlapping; in this case, the dots merge together and become a single dot.
 *
 * The second fold instruction is fold along x=5, which indicates this line:
 *
 * #.##.|#..#.
 * #...#|.....
 * .....|#...#
 * #...#|.....
 * .#.#.|#.###
 * .....|.....
 * .....|.....
 *
 * Because this is a vertical line, fold left:
 *
 * #####
 * #...#
 * #...#
 * #...#
 * #####
 * .....
 * .....
 *
 * The instructions made a square!
 *
 * The transparent paper is pretty big, so for now, focus on just completing the first fold. After the first fold in
 * the example above, 17 dots are visible - dots that end up overlapping after the fold is completed count as a single
 * dot.
 *
 * How many dots are visible after completing just the first fold instruction on your transparent paper?
 *
 * --- Part Two ---
 *
 * Finish folding the transparent paper according to the instructions. The manual says the code is always eight capital
 * letters.
 *
 * What code do you use to activate the infrared thermal imaging camera system?
 */
public class D13 {

    record Pos(int x, int y){};
    record Fold(char axis, int line){};

    class Data {
        Set<Pos> pos = new HashSet<>();
        List<Fold> folds = new ArrayList<>();
        boolean loadFolds = false;
    }

    public Data loadData(String filename) {
        return FileReader.readFileForObject(filename, new Data(), (line, state) -> {
            if(line.isEmpty()) {
                state.folds = new ArrayList<>();
                state.loadFolds = true;
                return state;
            }
            if(!state.loadFolds) {
                String[] parts = line.split(",");
                state.pos.add(new Pos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            } else {
                line = line.replace("fold along ", "");
                String[] parts = line.split("=");
                state.folds.add(new Fold(parts[0].toCharArray()[0], Integer.parseInt(parts[1])));
            }
            return state;
        });
    }

    public Set<Pos> fold(Set<Pos> points, Fold fold) {
        Set<Pos> newset = new HashSet<>();
        if(fold.axis == 'y') {
            for(Pos p : points) {
                if(p.y < fold.line) {
                    newset.add(p);
                } else {
                    int d = fold.line+(fold.line-p.y);
                    if(d >= 0)
                        newset.add(new Pos(p.x, d));
                }
            }
        } else if(fold.axis == 'x') {
            for(Pos p : points) {
                if(p.x < fold.line) {
                    newset.add(p);
                } else {
                    int d = fold.line+(fold.line-p.x);
                    if(d >= 0)
                        newset.add(new Pos(d, p.y));
                }
            }
        }
        return newset;
    }

    public void print(Set<Pos> pos) {
        int max = pos.stream().mapToInt(p -> Math.max(p.x, p.y)).max().orElseThrow(NoSuchElementException::new);
        max++;

        int[][] m = new int[max][max];
        pos.forEach(p -> m[p.x][p.y] = 1);

        for(int i=0; i<max; i++) {
            System.out.print("- ");
            for(int j=0; j<max; j++) {
                System.out.print(m[j][i] == 1 ? "*" : " ");
            }
            System.out.println("");
        }
    }

    public Set<Pos> foldAll(Data data) {
        Set<Pos> pos = data.pos;
        for(Fold fold : data.folds) {
            pos = fold(pos, fold);
        }
        return pos;
    }

    @Test
    public void testPartA() {
        Data data = loadData("src/test/resources/2021/D13_t.txt");
        data.pos = fold(data.pos, data.folds.get(0));
        assertEquals(17, fold(data.pos, data.folds.get(0)).size());

        data = loadData("src/test/resources/2021/D13.txt");
        assertEquals(687, fold(data.pos, data.folds.get(0)).size());

        print(foldAll(data));
    }
}
