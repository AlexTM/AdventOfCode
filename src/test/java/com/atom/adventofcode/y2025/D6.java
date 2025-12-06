package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D6 {
    private static final String TEST_INPUT = """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s
            """;

    record Sum(long[] values, String operator) {}
    record StringSum(String[] values, String operator) {}

    private long doSum(Sum col) {
        long sum = col.operator.equals("+") ? 0 : 1;
        for (int i = 0; i < col.values.length; i++) {
            if (col.operator.equals("+")) {
                sum += col.values[i];
            } else {
                sum *= col.values[i];
            }
        }
        return sum;
    }

    private long doSums(List<Sum> sums) {
        return sums.stream().mapToLong(this::doSum).sum();
    }

    private List<String> getNumbers(String data, int startIdx, int endIdx) {
        String[] row = data.split("\n");
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < row.length - 1; i++) {
            int endIdxtmp = Math.min(endIdx, row[i].length());
            numbers.add(row[i].substring(startIdx, endIdxtmp));
        }
        return numbers;
    }

    private List<StringSum> parseColumn2(String data) {
        String[] rows = data.split("\n");
        String lastRow = rows[rows.length - 1];
        List<StringSum> sums = new ArrayList<>();

        int lastColumnIdx = 0;
        for (int i = 1; i < lastRow.length(); i++) {
            char c = lastRow.charAt(i);
            if (c == '*' || c == '+') {
                List<String> nums = getNumbers(data, lastColumnIdx, i - 1);
                sums.add(new StringSum(nums.toArray(new String[0]), lastRow.substring(lastColumnIdx, lastColumnIdx + 1)));
                lastColumnIdx = i;
            }
        }
        List<String> nums = getNumbers(data, lastColumnIdx, lastRow.length() + 5);
        sums.add(new StringSum(nums.toArray(new String[0]), lastRow.substring(lastColumnIdx, lastColumnIdx + 1)));

        return sums;
    }

    private static Sum convert(StringSum ss) {
        return new Sum(
                Arrays.stream(ss.values).mapToLong(l -> Long.parseLong(l.strip())).toArray(),
                ss.operator);
    }

    @Test
    public void testPart1() {
        List<Sum> sums = parseColumn2(TEST_INPUT).stream().map(D6::convert).toList();
        assertEquals(4277556, doSums(sums));
        sums = parseColumn2(FileReader.readFileString("src/test/resources/2025/D6.txt")).stream().map(D6::convert).toList();
        assertEquals(4449991244405L, doSums(sums));
    }

    private static Sum convert2(StringSum ss) {
        // find the longest string in number
        int longest = Arrays.stream(ss.values).mapToInt(String::length).max().getAsInt();
        List<Long> numbers = new ArrayList<>();

        for(int j=0; j<longest; j++) {
            StringBuilder num = new StringBuilder();
            for (int i = 0; i < ss.values.length; i++) {
                String tmpNum = ss.values[i];
                char c = tmpNum.charAt(longest - 1 - j);
                if (c != ' ') {
                    num.append(c);
                }
            }
            numbers.add(Long.parseLong(num.toString()));
        }
        return new Sum(numbers.stream().mapToLong(l -> l).toArray(), ss.operator);
    }

    @Test
    public void testPart2() {
        List<Sum> sums = parseColumn2(TEST_INPUT).stream().map(D6::convert2).toList();
        assertEquals(3263827, doSums(sums));
        sums = parseColumn2(FileReader.readFileString("src/test/resources/2025/D6.txt")).stream().map(D6::convert2).toList();
        assertEquals(9348430857627L, doSums(sums));
    }
}