package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D2 {

    private final String TEST_INPUT = """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9""";

    private List<List<Integer>> parseInput(String input) {
        List<List<Integer>> data = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            List<Integer> list = new ArrayList<>();
            for (String part : parts) {
                list.add(Integer.parseInt(part));
            }
            data.add(list);
        }
        return data;
    }

    private boolean isSafe(List<Integer> list) {
        int direction = list.get(0) < list.get(1) ? 1 : -1;
        for(int i=1; i<list.size(); i++) {
            int diff = Math.abs(list.get(i-1) - list.get(i));
            if(diff > 3 || diff == 0) {
                return false;
            }
            if(list.get(i-1) < list.get(i)) {
                if(direction == -1) {
                    return false;
                }
            } else {
                if(direction == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void testPart1() {
        List<List<Integer>> data = parseInput(TEST_INPUT);
        assertEquals(2, data.stream().filter(this::isSafe).count());

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D2.txt"));
        assertEquals(220, data.stream().filter(this::isSafe).count());
    }

    private boolean isSafeWithDroppedValue(List<Integer> list) {
        if(isSafe(list)) {
            return true;
        }

        for(int i=0; i<list.size(); i++) {
            List<Integer> copy = new ArrayList<>(list);
            copy.remove(i);
            if(isSafe(copy)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testPart2() {
        List<List<Integer>> data = parseInput(TEST_INPUT);
        assertEquals(4, data.stream().filter(this::isSafeWithDroppedValue).count());

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D2.txt"));
        assertEquals(296, data.stream().filter(this::isSafeWithDroppedValue).count());
    }

}
