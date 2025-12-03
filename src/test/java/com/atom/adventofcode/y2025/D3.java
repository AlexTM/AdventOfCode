package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D3 {
    private static final String TEST_INPUT = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """;

    private List<String> parseInput(String input) {
        return List.of(input.split("\n"));
    }

    private static long findMaxJoltage(String input, int depth) {
        int[] digits = input.chars()
                .map(Character::getNumericValue)
                .toArray();
        return findMaxJoltage(digits, depth, 0, 0, 0);
    }

    private static long findMaxJoltage(int []jolt, int depth, int pos, long max, long value) {

        if(depth == 0) {
            return Math.max(max, value);
        }

        for(int i=pos; i<jolt.length; i++) {
            long tmpValue = jolt[i] * (long)Math.pow(10, depth-1);

            // early return
            if(value + tmpValue < max)
                continue;

            max = findMaxJoltage(jolt, depth-1, i+1, max, value + tmpValue);
        }
        return max;
    }

    private long sumAll(List<String> inputs, int depth) {
        return inputs.stream().mapToLong(i -> findMaxJoltage(i, depth)).sum();
    }

    @Test
    public void testPart1() {
        assertEquals(16887, sumAll(parseInput(FileReader.readFileString("src/test/resources/2025/D3.txt")), 2));
    }

    @Test
    public void testPart2() {
        assertEquals(167302518850275L, sumAll(parseInput(FileReader.readFileString("src/test/resources/2025/D3.txt")), 12));
    }
}
