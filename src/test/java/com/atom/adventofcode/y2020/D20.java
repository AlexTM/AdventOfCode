package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
