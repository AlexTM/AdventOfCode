package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D1 {
    private static final Map<String, Integer> numbers =
            Map.of("1", 1, "2", 2, "3", 3, "4", 4, "5", 5,
                    "6", 6, "7", 7, "8", 8, "9", 9);
    private static final Map<String, Integer> words =
            Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5,
                    "six", 6, "seven", 7, "eight", 8, "nine", 9);

    private final String input = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;

    private static int indexes(String input, Map<String, Integer> numbers) {
        int minIdx = Integer.MAX_VALUE;
        int maxIdx = Integer.MIN_VALUE;
        int minValue = -1;
        int maxValue = -1;
        for(Map.Entry<String, Integer> entry : numbers.entrySet()) {
            if(input.contains(entry.getKey())) {
                int idx = input.indexOf(entry.getKey());
                if(idx < minIdx) {
                    minIdx = idx;
                    minValue = entry.getValue();
                }
                idx = input.lastIndexOf(entry.getKey());
                if(idx >= maxIdx) {
                    maxIdx = idx;
                    maxValue = entry.getValue();
                }
            }
        }
        return Integer.parseInt(minValue +""+ maxValue);
    }

    @Test
    public void partOne() {
        int res = Arrays.stream(input.split("\n"))
                .mapToInt(s -> indexes(s, numbers))
                .sum();
        assertEquals(142, res);

        res = FileReader.readFileStringList("src/test/resources/2023/D1.txt")
                .stream()
                .mapToInt(s -> indexes(s, numbers))
                .sum();
        assertEquals(55002, res);
    }

    private static final String input2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;

    @Test
    public void partTwo() {
        final Map<String, Integer> newNumbers = new HashMap<>(numbers);
        newNumbers.putAll(words);

        int res = Arrays.stream(input2.split("\n"))
                .mapToInt(s -> indexes(s, newNumbers))
                .sum();
        assertEquals(281, res);

        res = FileReader.readFileStringList("src/test/resources/2023/D1.txt")
                .stream()
                .mapToInt(s -> indexes(s, newNumbers))
                .sum();
        assertEquals(55093, res);
    }
}
