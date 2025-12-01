package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D1 {
    private static final String TEST_INPUT = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82""";

    record Data(String d, int v) { }

    private List<Data> parseInput(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line ->
                        new Data(line.substring(0,1), Integer.parseInt(line.substring(1))))
                .toList();
    }

    private int followInstructions(List<Data> instructions, int startPoint) {
        int c = 0;
        int pos = startPoint;
        for(Data d : instructions) {
            int turn = d.d.equals("L") ? -d.v : d.v;
            pos += turn;
            pos %= 100;

            if(pos < 0) pos += 100;
            if(pos == 0)
                c++;
        }

        return c;
    }

    @Test
    public void testPart1() {
        var data = parseInput(TEST_INPUT);
        assertEquals(3, followInstructions(data, 50));

        data = parseInput(FileReader.readFileString("src/test/resources/2025/D1.txt"));
        assertEquals(1029, followInstructions(data, 50));
    }

    private List<Integer> getPositions(int startPoint, int steps, boolean positiveDirection) {
        List<Integer> positions = new ArrayList<>();
        int pos = startPoint;
        for(int i = 0; i < steps; i++) {
            if(positiveDirection)
                pos++;
            else
                pos--;
            if(pos < 0) pos += 100;
            if(pos > 99) pos -= 100;
            positions.add(pos);
        }
        return positions;
    }

    // Not good, ran out of patient on double counting zero, just doing the simple method
    private int followInstructions2(List<Data> instructions, int startPoint) {
        int c = 0, pos = startPoint;

        for(Data d : instructions) {
            List<Integer> values = getPositions(pos, d.v, d.d.equals("L"));
            c += (int) values.stream().filter(v -> v == 0).count();
            pos = values.get(values.size()-1);
        }

        return c;
    }

    @Test
    public void testPart2() {
        var data = parseInput(TEST_INPUT);
        assertEquals(6, followInstructions2(data, 50));

        data = parseInput(FileReader.readFileString("src/test/resources/2025/D1.txt"));
        assertEquals(5892, followInstructions2(data, 50));
    }

}
