package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 20: Trench Map ---
 *
 * With the scanners fully deployed, you turn their attention to mapping the floor of the ocean trench.
 *
 * When you get back the image from the scanners, it seems to just be random noise. Perhaps you can combine an image
 * enhancement algorithm and the input image (your puzzle input) to clean it up a little.
 *
 * For example:
 *
 * ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
 * #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
 * .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
 * .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
 * .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
 * ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
 * ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#
 *
 * #..#.
 * #....
 * ##..#
 * ..#..
 * ..###
 *
 * The first section is the image enhancement algorithm. It is normally given on a single line, but it has been wrapped
 * to multiple lines in this example for legibility. The second section is the input image, a two-dimensional grid of
 * light pixels (#) and dark pixels (.).
 *
 * The image enhancement algorithm describes how to enhance an image by simultaneously converting all pixels in the
 * input image into an output image. Each pixel of the output image is determined by looking at a 3x3 square of pixels
 * centered on the corresponding input image pixel. So, to determine the value of the pixel at (5,10) in the output
 * image, nine pixels from the input image need to be considered: (4,9), (4,10), (4,11), (5,9), (5,10), (5,11), (6,9),
 * (6,10), and (6,11). These nine input pixels are combined into a single binary number that is used as an index in the
 * image enhancement algorithm string.
 *
 * For example, to determine the output pixel that corresponds to the very middle pixel of the input image, the nine
 * pixels marked by [...] would need to be considered:
 *
 * # . . # .
 * #[. . .].
 * #[# . .]#
 * .[. # .].
 * . . # # #
 *
 * Starting from the top-left and reading across each row, these pixels are ..., then #.., then .#.; combining these
 * forms ...#...#.. By turning dark pixels (.) into 0 and light pixels (#) into 1, the binary number 000100010 can be
 * formed, which is 34 in decimal.
 *
 * The image enhancement algorithm string is exactly 512 characters long, enough to match every possible 9-bit binary
 * number. The first few characters of the string (numbered starting from zero) are as follows:
 *
 * 0         10        20        30  34    40        50        60        70
 * |         |         |         |   |     |         |         |         |
 * ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
 *
 * In the middle of this first group of characters, the character at index 34 can be found: #. So, the output pixel in
 * the center of the output image should be #, a light pixel.
 *
 * This process can then be repeated to calculate every pixel of the output image.
 *
 * Through advances in imaging technology, the images being operated on here are infinite in size. Every pixel of the
 * infinite output image needs to be calculated exactly based on the relevant pixels of the input image. The small
 * input image you have is only a small region of the actual infinite input image; the rest of the input image consists
 * of dark pixels (.). For the purposes of the example, to save on space, only a portion of the infinite-sized input
 * and output images will be shown.
 *
 * The starting input image, therefore, looks something like this, with more dark pixels (.) extending forever in every
 * direction not shown here:
 *
 * ...............
 * ...............
 * ...............
 * ...............
 * ...............
 * .....#..#......
 * .....#.........
 * .....##..#.....
 * .......#.......
 * .......###.....
 * ...............
 * ...............
 * ...............
 * ...............
 * ...............
 *
 * By applying the image enhancement algorithm to every pixel simultaneously, the following output image can be
 * obtained:
 *
 * ...............
 * ...............
 * ...............
 * ...............
 * .....##.##.....
 * ....#..#.#.....
 * ....##.#..#....
 * ....####..#....
 * .....#..##.....
 * ......##..#....
 * .......#.#.....
 * ...............
 * ...............
 * ...............
 * ...............
 *
 * Through further advances in imaging technology, the above output image can also be used as an input image! This
 * allows it to be enhanced a second time:
 *
 * ...............
 * ...............
 * ...............
 * ..........#....
 * ....#..#.#.....
 * ...#.#...###...
 * ...#...##.#....
 * ...#.....#.#...
 * ....#.#####....
 * .....#.#####...
 * ......##.##....
 * .......###.....
 * ...............
 * ...............
 * ...............
 *
 * Truly incredible - now the small details are really starting to come through. After enhancing the original input
 * image twice, 35 pixels are lit.
 *
 * Start with the original input image and apply the image enhancement algorithm twice, being careful to account for
 * the infinite size of the images. How many pixels are lit in the resulting image?
 *
 */
public class D20 {

    record Pos(int x, int y) {};

    class Data {
        char[] enhancement;
        Map<Pos, Boolean> imageSet = new HashMap<>();
        int rowNumber = 0;
    }

    private Data loadData(String filename) {
        return FileReader.readFileForObject(filename, new Data(), (line, state) -> {
            if(line.isEmpty()) return state;

            if(state.enhancement == null) {
                state.enhancement = line.toCharArray();
            } else {
                for(int x=0; x<line.length(); x++) {
                    state.imageSet.put(new Pos(x, state.rowNumber), line.charAt(x) == '#');
                }
                state.rowNumber++;
            }
            return state;
        });
    };

    private LinkedHashSet<Pos> generateCandidatesForPos(Pos p) {
        LinkedHashSet<Pos> ps = new LinkedHashSet<>();
        for(int y=p.y-1; y<=p.y+1; y++) {
            for(int x=p.x-1; x<=p.x+1; x++) {
                ps.add(new Pos(x, y));
            }
        }
        return ps;
    }

    private Set<Pos> generateCandidatesForEntireImage(Set<Pos> imageSet) {
        // As we are now storing false values in a map and therefore keeping a rectangle of points,
        // no need to generate candidate list, this could be simplified.
        final Set<Pos> ps = new HashSet<>();
        imageSet.forEach(p -> ps.addAll(generateCandidatesForPos(p)));
        return ps;
    }

    private Map<Pos, Boolean> step(Map<Pos, Boolean> imageSet, char[] enhancement, boolean isOdd) {
        boolean specialCase = enhancement[0] == '#' && enhancement[511] == '.';
        Queue<Pos> queue = new ArrayDeque<>(generateCandidatesForEntireImage(imageSet.keySet()));
        Map<Pos, Boolean> newMap = new HashMap<>();
        while(!queue.isEmpty()) {
            Pos p = queue.poll();
            int idx = calculateIndex(imageSet, p, specialCase && isOdd);
            newMap.put(p, enhancement[idx] == '#');
        }
        return newMap;
    }

    private long enhance(Data data, int steps) {
        for(int i=0; i<steps/2; i++) {
            for(int j=0; j<2; j++) {
                data.imageSet = step(data.imageSet, data.enhancement, j%2==1);
            }
        }
        return data.imageSet.entrySet().stream().filter(Map.Entry::getValue).count();
    }

    private int calculateIndex(Map<Pos, Boolean> imageSet, Pos p, boolean isSpecialCase) {
        int idx = 0;
        for(Pos pl : generateCandidatesForPos(p)) {
            idx = idx << 1;
            if(imageSet.containsKey(pl)) {
                if(imageSet.get(pl))
                    idx++;
            } else if(isSpecialCase) {
                // Image outside of known points are continually flashing
                // Therefore if this is an odd iteration set this bit
                idx++;
            }
        }
        return idx;
    }

    @Test
    public void testPart1() {
        Data data = loadData("src/test/resources/2021/D20_t.txt");
        assertEquals(35, enhance(data, 2));

        Data puzzleData = loadData("src/test/resources/2021/D20.txt");
        assertEquals(5437, enhance(puzzleData, 2));
    }

    @Test
    public void testPart2() {
        Data data = loadData("src/test/resources/2021/D20_t.txt");
        assertEquals(3351, enhance(data, 50));

        Data puzzleData = loadData("src/test/resources/2021/D20.txt");
        assertEquals(19340, enhance(puzzleData, 50));
    }

}
