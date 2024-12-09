package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D9 {

    private String expandFormat(String input) {

        int pos = 0, index = 0;
        StringBuffer sb = new StringBuffer();
        while(pos < input.length()) {
            int blocks = Character.getNumericValue(input.charAt(pos++));
            int freeSpace = 0;
            if(pos < input.length())
                freeSpace = Character.getNumericValue(input.charAt(pos++));

            sb.append(String.valueOf(index).repeat(blocks) );
            sb.append(".".repeat(freeSpace));

            index++;
        }
        return sb.toString();
    }

    private String compactFile(String input) {
        StringBuilder output = new StringBuilder();
        int start = 0;
        int end = input.length()-1;
        while(start <= end) {
            char c = input.charAt(start);
            while (c == '.' && start <= end) {
                c = input.charAt(end);
                end--;
            }
            output.append(c);
            start++;
        }
        output.append(".".repeat(input.length()-start));
        return output.toString();
    }

    private long computeCheckSum(String input) {
        long sum = 0;
        for(int i=0; i<input.length(); i++) {
            char c = input.charAt(i);
            if(Character.isDigit(c)) {
                sum += (long) Character.getNumericValue(c) * i;
            } else
                break;
        }
        return sum;
    }

    private static final String TEST_INPUT = "2333133121414131402";

    @Test
    public void testPartOne() {
        assertEquals("0..111....22222", expandFormat("12345"));
        assertEquals("00...111...2...333.44.5555.6666.777.888899", expandFormat(TEST_INPUT));

        assertEquals("022111222......", compactFile(expandFormat("12345")));
        assertEquals("0099811188827773336446555566..............", compactFile(expandFormat(TEST_INPUT)));

        assertEquals(1928, computeCheckSum(compactFile(expandFormat(TEST_INPUT))));

        long res = computeCheckSum(compactFile(expandFormat(
                FileReader.readFileString("src/test/resources/2024/D9.txt"))));

//        String in = FileReader.readFileString("src/test/resources/2024/D9.txt");
//        System.out.println(expandFormat(in));
//        System.out.println(compactFile(expandFormat(in)));
//        System.out.println(computeCheckSum(compactFile(expandFormat(in))));


        assertNotEquals(91111740342L, res);
        assertEquals(0, res);
    }
}
