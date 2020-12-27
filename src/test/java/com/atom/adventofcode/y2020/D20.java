package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * --- Day 20: Jurassic Jigsaw ---
 * The high-speed train leaves the forest and quickly carries you south. You can even see a desert in the distance!
 * Since you have some spare time, you might as well see if there was anything interesting in the image the Mythical
 * Information Bureau satellite captured.
 *
 * After decoding the satellite messages, you discover that the data actually contains many small images created by
 * the satellite's camera array. The camera array consists of many cameras; rather than produce a single square
 * image, they produce many smaller square image tiles that need to be reassembled back into a single image.
 *
 * Each camera in the camera array returns a single monochrome image tile with a random unique ID number. The tiles
 * (your puzzle input) arrived in a random order.
 *
 * Worse yet, the camera array appears to be malfunctioning: each image tile has been rotated and flipped to a random
 * orientation. Your first task is to reassemble the original image by orienting the tiles so they fit together.
 *
 * To show how the tiles should be reassembled, each tile's image data includes a border that should line up exactly
 * with its adjacent tiles. All tiles have this border, and the border lines up exactly when the tiles are both
 * oriented correctly. Tiles at the edge of the image also have this border, but the outermost edges won't line up
 * with any other tiles.
 *
 * For example, suppose you have the following nine tiles:
 *
 * Tile 2311:
 * ..##.#..#.
 * ##..#.....
 * #...##..#.
 * ####.#...#
 * ##.##.###.
 * ##...#.###
 * .#.#.#..##
 * ..#....#..
 * ###...#.#.
 * ..###..###
 *
 * Tile 1951:
 * #.##...##.
 * #.####...#
 * .....#..##
 * #...######
 * .##.#....#
 * .###.#####
 * ###.##.##.
 * .###....#.
 * ..#.#..#.#
 * #...##.#..
 *
 * Tile 1171:
 * ####...##.
 * #..##.#..#
 * ##.#..#.#.
 * .###.####.
 * ..###.####
 * .##....##.
 * .#...####.
 * #.##.####.
 * ####..#...
 * .....##...
 *
 * Tile 1427:
 * ###.##.#..
 * .#..#.##..
 * .#.##.#..#
 * #.#.#.##.#
 * ....#...##
 * ...##..##.
 * ...#.#####
 * .#.####.#.
 * ..#..###.#
 * ..##.#..#.
 *
 * Tile 1489:
 * ##.#.#....
 * ..##...#..
 * .##..##...
 * ..#...#...
 * #####...#.
 * #..#.#.#.#
 * ...#.#.#..
 * ##.#...##.
 * ..##.##.##
 * ###.##.#..
 *
 * Tile 2473:
 * #....####.
 * #..#.##...
 * #.##..#...
 * ######.#.#
 * .#...#.#.#
 * .#########
 * .###.#..#.
 * ########.#
 * ##...##.#.
 * ..###.#.#.
 *
 * Tile 2971:
 * ..#.#....#
 * #...###...
 * #.#.###...
 * ##.##..#..
 * .#####..##
 * .#..####.#
 * #..#.#..#.
 * ..####.###
 * ..#.#.###.
 * ...#.#.#.#
 *
 * Tile 2729:
 * ...#.#.#.#
 * ####.#....
 * ..#.#.....
 * ....#..#.#
 * .##..##.#.
 * .#.####...
 * ####.#.#..
 * ##.####...
 * ##..#.##..
 * #.##...##.
 *
 * Tile 3079:
 * #.#.#####.
 * .#..######
 * ..#.......
 * ######....
 * ####.#..#.
 * .#...#.##.
 * #.#####.##
 * ..#.###...
 * ..#.......
 * ..#.###...
 * By rotating, flipping, and rearranging them, you can find a square arrangement that causes all adjacent borders to
 * line up:
 *
 * #...##.#.. ..###..### #.#.#####.
 * ..#.#..#.# ###...#.#. .#..######
 * .###....#. ..#....#.. ..#.......
 * ###.##.##. .#.#.#..## ######....
 * .###.##### ##...#.### ####.#..#.
 * .##.#....# ##.##.###. .#...#.##.
 * #...###### ####.#...# #.#####.##
 * .....#..## #...##..#. ..#.###...
 * #.####...# ##..#..... ..#.......
 * #.##...##. ..##.#..#. ..#.###...
 *
 * #.##...##. ..##.#..#. ..#.###...
 * ##..#.##.. ..#..###.# ##.##....#
 * ##.####... .#.####.#. ..#.###..#
 * ####.#.#.. ...#.##### ###.#..###
 * .#.####... ...##..##. .######.##
 * .##..##.#. ....#...## #.#.#.#...
 * ....#..#.# #.#.#.##.# #.###.###.
 * ..#.#..... .#.##.#..# #.###.##..
 * ####.#.... .#..#.##.. .######...
 * ...#.#.#.# ###.##.#.. .##...####
 *
 * ...#.#.#.# ###.##.#.. .##...####
 * ..#.#.###. ..##.##.## #..#.##..#
 * ..####.### ##.#...##. .#.#..#.##
 * #..#.#..#. ...#.#.#.. .####.###.
 * .#..####.# #..#.#.#.# ####.###..
 * .#####..## #####...#. .##....##.
 * ##.##..#.. ..#...#... .####...#.
 * #.#.###... .##..##... .####.##.#
 * #...###... ..##...#.. ...#..####
 * ..#.#....# ##.#.#.... ...##.....
 * For reference, the IDs of the above tiles are:
 *
 * 1951    2311    3079
 * 2729    1427    2473
 * 2971    1489    1171
 * To check that you've assembled the image correctly, multiply the IDs of the four corner tiles together. If you do
 * this with the assembled tiles from the example above, you get 1951 * 3079 * 2971 * 1171 = 20899048083289.
 *
 * Assemble the tiles into an image. What do you get if you multiply together the IDs of the four corner tiles?
 *
 * Your puzzle answer was 18262194216271.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 *
 * --- Part Two ---
 * Now, you're ready to check the image for sea monsters.
 *
 * The borders of each tile are not part of the actual image; start by removing them.
 *
 * In the example above, the tiles become:
 *
 * .#.#..#. ##...#.# #..#####
 * ###....# .#....#. .#......
 * ##.##.## #.#.#..# #####...
 * ###.#### #...#.## ###.#..#
 * ##.#.... #.##.### #...#.##
 * ...##### ###.#... .#####.#
 * ....#..# ...##..# .#.###..
 * .####... #..#.... .#......
 *
 * #..#.##. .#..###. #.##....
 * #.####.. #.####.# .#.###..
 * ###.#.#. ..#.#### ##.#..##
 * #.####.. ..##..## ######.#
 * ##..##.# ...#...# .#.#.#..
 * ...#..#. .#.#.##. .###.###
 * .#.#.... #.##.#.. .###.##.
 * ###.#... #..#.##. ######..
 *
 * .#.#.### .##.##.# ..#.##..
 * .####.## #.#...## #.#..#.#
 * ..#.#..# ..#.#.#. ####.###
 * #..####. ..#.#.#. ###.###.
 * #####..# ####...# ##....##
 * #.##..#. .#...#.. ####...#
 * .#.###.. ##..##.. ####.##.
 * ...###.. .##...#. ..#..###
 * Remove the gaps to form the actual image:
 *
 * .#.#..#.##...#.##..#####
 * ###....#.#....#..#......
 * ##.##.###.#.#..######...
 * ###.#####...#.#####.#..#
 * ##.#....#.##.####...#.##
 * ...########.#....#####.#
 * ....#..#...##..#.#.###..
 * .####...#..#.....#......
 * #..#.##..#..###.#.##....
 * #.####..#.####.#.#.###..
 * ###.#.#...#.######.#..##
 * #.####....##..########.#
 * ##..##.#...#...#.#.#.#..
 * ...#..#..#.#.##..###.###
 * .#.#....#.##.#...###.##.
 * ###.#...#..#.##.######..
 * .#.#.###.##.##.#..#.##..
 * .####.###.#...###.#..#.#
 * ..#.#..#..#.#.#.####.###
 * #..####...#.#.#.###.###.
 * #####..#####...###....##
 * #.##..#..#...#..####...#
 * .#.###..##..##..####.##.
 * ...###...##...#...#..###
 * Now, you're ready to search for sea monsters! Because your image is monochrome, a sea monster will look like this:
 *
 *                   #
 * #    ##    ##    ###
 *  #  #  #  #  #  #
 * When looking for this pattern in the image, the spaces can be anything; only the # need to match. Also, you might
 * need to rotate or flip your image before it's oriented correctly to find sea monsters. In the above image, after
 * flipping and rotating it to the appropriate orientation, there are two sea monsters (marked with O):
 *
 * .####...#####..#...###..
 * #####..#..#.#.####..#.#.
 * .#.#...#.###...#.##.O#..
 * #.O.##.OO#.#.OO.##.OOO##
 * ..#O.#O#.O##O..O.#O##.##
 * ...#.#..##.##...#..#..##
 * #.##.#..#.#..#..##.#.#..
 * .###.##.....#...###.#...
 * #.####.#.#....##.#..#.#.
 * ##...#..#....#..#...####
 * ..#.##...###..#.#####..#
 * ....#.##.#.#####....#...
 * ..##.##.###.....#.##..#.
 * #...#...###..####....##.
 * .#.##...#.##.#.#.###...#
 * #.###.#..####...##..#...
 * #.###...#.##...#.##O###.
 * .O##.#OO.###OO##..OOO##.
 * ..O#.O..O..O.#O##O##.###
 * #.#..##.########..#..##.
 * #.#####..#.#...##..#....
 * #....##..#.#########..##
 * #...#.....#..##...###.##
 * #..###....##.#...##.##.#
 * Determine how rough the waters are in the sea monsters' habitat by counting the number of # that are not part of a
 * sea monster. In the above example, the habitat's water roughness is 273.
 *
 * How many # are not part of a sea monster?
 */
public class D20 {

    class Tile {
        private int number;
        private List<String> data;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public Tile(int number, List<String> data) {
            this.number = number;
            this.data = data;
        }

        public String[] buildEdges() {
            String[] edges = new String[4];
            edges[0] = data.get(0);
            edges[2] = data.get(data.size()-1);

            StringBuilder a = new StringBuilder();
            StringBuilder b = new StringBuilder();
            for(String d : data) {
                a.append(d.charAt(0));
                b.append(d.charAt(d.length()-1));
            }
            edges[3] = a.toString();
            edges[1] = b.toString();
            return edges;
        }

        void flipX() {
            List<String> data2 = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                data2.add(new StringBuilder(data.get(i)).reverse().toString());
            }
            data = data2;
        }

        void flipY() {
            List<String> data2 = new ArrayList<>();
            for (int i = data.size()-1; i >= 0; i--) {
                data2.add(data.get(i));
            }
            data = data2;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(number+"\n");

            for(String s : data)
                sb.append(s+"\n");

            return sb.toString();
        }

        public String[] allPossible() {
            String[] allPossible = new String[4];
            allPossible[0] = String.join(", ", buildEdges());
            flipX();
            allPossible[1] = String.join(", ", buildEdges());
            flipY();
            allPossible[2] = String.join(", ", buildEdges());
            flipX();
            allPossible[3] = String.join(", ", buildEdges());
            return allPossible;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return number == tile.number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(number);
        }
    }

    private Set<Tile> loadTiles(String filename) {
        Set<Tile> tiles = new HashSet<>();

        try(Scanner in = new Scanner(new File(filename))) {
            List<String> data = new ArrayList<>();
            int number = 0;
            while (in.hasNext()) {
                String line = in.nextLine();
                if(line.startsWith("Tile")) {
                    data = new ArrayList<>();
                    number = Integer.parseInt(line.substring(5, line.indexOf(":")));
                } else if(line.isBlank()) {
                    tiles.add(new Tile(number, data));
                } else {
                    data.add(line);
                }
            }
            tiles.add(new Tile(number, data));
            in.close();
            return tiles;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTiles() {
        Set<Tile> tiles  = loadTiles("src/test/resources/2020/D20_t.txt");

        Map<Tile, Integer> m = resolve(tiles);

        Set<Tile> candidates4 =
                m.entrySet().stream().filter(x -> x.getValue() == 4).map(x -> x.getKey()).collect(Collectors.toSet());

        assertTrue(checkFor(List.of(1427), candidates4));

        Set<Tile> candidates3 =
                m.entrySet().stream().filter(x -> x.getValue() == 3).map(x -> x.getKey()).collect(Collectors.toSet());
        assertTrue(checkFor(List.of(2311, 2729, 2473, 1489), candidates3));

        Set<Tile> candidates2 =
                m.entrySet().stream().filter(x -> x.getValue() == 2).map(x -> x.getKey()).collect(Collectors.toSet());

        long mm = 1;
        for(Tile t : candidates2) {
            mm *= t.number;
        }

        assertEquals(20899048083289L, mm);
    }

    @Test
    public void testTilesPuzzle() {
        Set<Tile> tiles  = loadTiles("src/test/resources/2020/D20.txt");

        Map<Tile, Integer> m = resolve(tiles);
        Set<Tile> candidates2 =
                m.entrySet().stream()
                        .filter(x -> x.getValue() == 2)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

        long mm = 1;
        for(Tile t : candidates2) {
            mm *= t.number;
        }

        assertEquals(18262194216271L, mm);
    }

    private boolean checkFor(List<Integer> numbers, Set<Tile> candidates) {
        if(numbers.size() != candidates.size())
            return false;
        for(Tile t : candidates) {
            if(!numbers.contains(t.number)) {
                return false;
            }
        }
        return true;
    }

    // Just need to find 4 tiles with only 2 matches
    private Map<Tile, Integer> resolve(Set<Tile> tiles) {

        Map<Tile, Integer> matches = new HashMap<>();

        for(Tile t1 : tiles) {
            int ecount = 0;
            for(String t1All : t1.allPossible()) {
                for(Tile t2 : tiles) {
                    if(t1 != t2) {
                        for(String t2Edge : t2.buildEdges()) {
                            if (t1All.contains(t2Edge)) {
                                ecount++;
                            }
                        }
                    }
                }
            }
            matches.put(t1, ecount/2);
        }

        return matches;
    }

}
