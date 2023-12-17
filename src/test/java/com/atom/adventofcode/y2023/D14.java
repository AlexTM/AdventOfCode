package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D14 {

    private static final String input = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """;

    record Pos(int x, int y) {}

    class Data {
        Set<Pos> rock = new HashSet<>();
        Set<Pos> bolder = new HashSet<>();
        int maxx=0, maxy=0;
    }

    private static FileReader.TriFunction<Data, String, Integer, Data> tri = (data, line, y) -> {
        for (int x = 0; x < line.length(); x++) {
            char c = line.charAt(x);
            if (c == 'O') {
                data.bolder.add(new Pos(x, y));
            } else if (c == '#') {
                data.rock.add(new Pos(x, y));
            }
            data.maxx = Math.max(data.maxx, x+1);
            data.maxy = Math.max(data.maxy, y+1);
        }
        return data;
    };

    private static boolean inBounds(Data data, Pos p) {
        return p.x >= 0 && p.x < data.maxx && p.y >= 0 && p.y < data.maxy;
    }

/*    private static Map<String, Function<Pos, Pos>> travel = Map.of(
            "N", D14::goNorth,
            "S", D14::goSouth
//            "E", D14::goEast,
//            "W", D14::goWest
    );*/

    private static Pos goNorth(Pos p) {
        return new Pos(p.x, p.y-1);
    }

    private static List<Pos> orderNorth(Collection<Pos> bolders, int row) {
        return bolders.stream().filter(p -> p.x == row).sorted(Comparator.comparingInt(a -> a.y)).toList();
    }

    private static Pos goSouth(Pos p) {
        return new Pos(p.x, p.y+1);
    }
    private static List<Pos> orderSouth(Collection<Pos> bolders, int row) {
        return bolders.stream().filter(p -> p.x == row).sorted((a, b) -> Integer.compare(b.y, a.y)).toList();
    }


    private static Pos goWest(Pos p) {
        return new Pos(p.x-1, p.y);
    }
    private static List<Pos> orderWest(Collection<Pos> bolders, int row) {
        return bolders.stream().filter(p -> p.y == row).sorted((a, b) -> Integer.compare(a.x, b.x)).toList();
    }

    private static Pos goEast(Pos p) {
        return new Pos(p.x+1, p.y);
    }
    private static List<Pos> orderEast(Collection<Pos> bolders, int row) {
        return bolders.stream().filter(p -> p.y == row).sorted((a, b) -> Integer.compare(b.x, a.x)).toList();
    }

    private static void roll(Data data, int row, Function<Pos, Pos> go, BiFunction<Collection<Pos>, Integer, List<Pos>> order) {
        List<Pos> bs = order.apply(data.bolder, row);
        for (Pos b : bs) {
            data.bolder.remove(b);
            while (true) {
                Pos p = go.apply(b);
                if(!inBounds(data, p)) {
                    break;
                }
                if (data.rock.contains(p) || data.bolder.contains(p)) {
                    break;
                }
                b = p;
            }
            data.bolder.add(b);
        }
    }

    private static void allColumnsNorth(Data data) {
        for (int x = 0; x < data.maxx; x++) {
            roll(data, x, D14::goNorth, D14::orderNorth);
        }
    }

    private static long calculateLoad(Data data) {
        return data.bolder.stream().mapToLong(p -> data.maxy - p.y).sum();
    }

    private static String print(Data data) {
        StringBuilder sb = new StringBuilder();
        for(int y=0; y<data.maxy; y++) {
            for(int x=0; x<data.maxx; x++) {
                Pos p = new Pos(x, y);
                if(data.rock.contains(p))
                    sb.append('#');
                else if(data.bolder.contains(p))
                    sb.append('O');
                else
                    sb.append('.');
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Test
    public void partOne() {
        Data data = FileReader.parseStringForObject(input, new Data(), tri);
        System.out.println(print(data));
        allColumnsNorth(data);
        System.out.println(print(data));
        assertEquals(136, calculateLoad(data));

        Data data2 = FileReader.readFileForObject("src/main/resources/2023/D14.txt", new Data(), tri);
        System.out.println(print(data2));
        allColumnsNorth(data2);
        long value = calculateLoad(data2);
        System.out.println(print(data2));
        assertEquals(111339, value);
    }

    private static List<Integer> spinCycle(Data data, int cycles) {
        List<Integer> loads = new ArrayList<>();
        for (int i = 0; i < cycles; i++) {
            if(i % 100 == 0)
                System.out.println("i=" + i + ", load=" + calculateLoad(data));
            for (int x = 0; x < data.maxx; x++) {
                roll(data, x, D14::goNorth, D14::orderNorth);
            }
            for (int y = 0; y < data.maxy; y++) {
                roll(data, y, D14::goWest, D14::orderWest);
            }
            for (int x = 0; x < data.maxx; x++) {
                roll(data, x, D14::goSouth, D14::orderSouth);
            }
            for (int y = 0; y < data.maxy; y++) {
                roll(data, y, D14::goEast, D14::orderEast);
            }
            loads.add((int)calculateLoad(data));
        }
        return loads;
    }

    private static String createString(List<Integer> loads, int start, int length) {
        StringBuilder sb = new StringBuilder();
        for(int i=start; i<start+length; i++) {
            sb.append(loads.get(i));
            sb.append(",");
        }
        return sb.toString();
    }

    private static void findRepeatingSequence(List<Integer> loads) {
        String wholeString = createString(loads, 0, loads.size());
        // start with 5 midway through the string
        String subString = createString(loads, 5, loads.size()/2);
        

    }



    @Test
    public void partTwo() {
/*
        Data data = FileReader.parseStringForObject(input, new Data(), tri);
//        print(data);
        List<Integer> datalist = spinCycle(data, 1000);
        findRepeatingSequence(datalist);

//        print(data);
        assertEquals(64, calculateLoad(data));
*/

        Data data2 = FileReader.readFileForObject("src/main/resources/2023/D14.txt", new Data(), tri);
        List<Integer> datalist = spinCycle(data2, 1000);
        System.out.println(datalist);

        // Pattern starts at position 142
        // Length of pattern is 78
        // This divides into 1000000000 / 78 = 12820511 exactly
        // This has been worked out manually after looking at the output

        // TODO Need to code how to get the pattern start and length

        // This is the correct answer
        long value = 0;
        assertEquals(93736, value);
    }
}
