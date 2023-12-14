package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

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

    private Pos updatePos(Pos p) {
        return new Pos(p.x, p.y-1);
    }

    private static void rollNorth(Data data, int row) {
        List<Pos> bs = new ArrayList<>(data.bolder.stream().filter(p -> p.x == row).sorted(Comparator.comparingInt(a -> a.y)).toList());

        // foreach bs ensure they are as north as possible
        for (Pos b : bs) {
            //int y = b.y;
            //Pos p = new Pos(b.x, b.y);
            data.bolder.remove(b);
            while (y > 0) {
                Pos p = new Pos(b.x, y - 1);
                if (data.rock.contains(p) || data.bolder.contains(p)) {
                    break;
                }
                y--;
            }
            data.bolder.add(new Pos(b.x, y));
        }
    }

    private static void allColumnsNorth(Data data) {
        for (int x = 0; x < data.maxx; x++) {
            rollNorth(data, x);
        }
    }

    private static long calculateLoad(Data data) {
        return data.bolder.stream().mapToLong(p -> data.maxy - p.y).sum();
    }

    private static void print(Data data) {
        for(int y=0; y<data.maxy; y++) {
            for(int x=0; x<data.maxx; x++) {
                Pos p = new Pos(x, y);
                if(data.rock.contains(p))
                    System.out.print('#');
                else if(data.bolder.contains(p))
                    System.out.print('O');
                else
                    System.out.print('.');
            }
            System.out.println();
        }
        System.out.println();
    }

    @Test
    public void partOne() {
        Data data = FileReader.parseStringForObject(input, new Data(), tri);
        print(data);
        allColumnsNorth(data);
        print(data);
        assertEquals(136, calculateLoad(data));

        Data data2 = FileReader.readFileForObject("src/main/resources/2023/D14.txt", new Data(), tri);
        print(data2);
        allColumnsNorth(data2);
        long value = calculateLoad(data2);
        print(data2);
        assertEquals(111339, value);
    }

    private static void spinCycle(Data data) {

    }

    @Test
    public void partTwo() {
        Data data = FileReader.parseStringForObject(input, new Data(), tri);
        print(data);
        spinCycle(data);
        print(data);
        assertEquals(136, calculateLoad(data));

    }
}
