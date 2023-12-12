package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D9 {

    private static final String input = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;

    private static long getDiff(List<Long> data) {

        List<LinkedList<Long>> diffs = new ArrayList<>();
        diffs.add(new LinkedList<>(data));
        while(data.stream().anyMatch(l -> l != 0)) {
            LinkedList<Long> diff = new LinkedList<>();
            for (int i = 0; i < data.size() - 1; i++) {
                diff.add(data.get(i + 1) - data.get(i));
            }
            diffs.add(diff);
            data = diff;
        }

        long lastValue = 0;
        diffs.get(diffs.size()-1).addLast(0L);
        for(int i=diffs.size()-1; i>=1; i--) {
            lastValue = diffs.get(i).getLast() + diffs.get(i-1).getLast();
            diffs.get(i-1).addLast(lastValue);
        }

        return lastValue;
    }

    private List<Long> parseLine(String line) {
        return Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
    }

    @Test
    public void partOne() {
        List<String> data = Arrays.stream(input.split("\n")).toList();
        assertEquals(114, data.stream().mapToLong(s -> getDiff(parseLine(s))).sum());

        List<String> data2 = FileReader.readFileStringList("src/main/resources/2023/D9.txt");
        assertEquals(2075724761L, data2.stream().mapToLong(s -> getDiff(parseLine(s))).sum());
    }


    private static long getDiff2(List<Long> data) {

        List<LinkedList<Long>> diffs = new ArrayList<>();
        diffs.add(new LinkedList<>(data));
        while(data.stream().anyMatch(l -> l != 0)) {
            LinkedList<Long> diff = new LinkedList<>();
            for (int i = 0; i < data.size() - 1; i++) {
                diff.add(data.get(i + 1) - data.get(i));
            }
            diffs.add(diff);
            data = diff;
        }

        diffs.get(diffs.size()-1).addFirst(0L);
        for(int i=diffs.size()-1; i>=1; i--) {
            diffs.get(i-1).addFirst(
                    diffs.get(i-1).getFirst() - diffs.get(i).getFirst());
        }

        return diffs.get(0).getFirst();
    }

    @Test
    public void partTwo() {
        List<String> data = Arrays.stream(input.split("\n")).toList();
        assertEquals(2, data.stream().mapToLong(s -> getDiff2(parseLine(s))).sum());

        List<String> data2 = FileReader.readFileStringList("src/main/resources/2023/D9.txt");
        assertEquals(1072, data2.stream().mapToLong(s -> getDiff2(parseLine(s))).sum());
    }

}
