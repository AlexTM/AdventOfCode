package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D2 {

    private static final String TEST_INPUT = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";

    record Range(long min, long max){}

    private List<Range> parseInput(String input) {
        String[] split = input.split(",");
        var r = new ArrayList<Range>();
        for(String s : split) {
            String[] split2 = s.split("-");
            r.add(new Range(Long.parseLong(split2[0]), Long.parseLong(split2[1])));
        }
        return r;
    }

    // could use regex
    private static boolean isValid(String s) {
        int l = s.length()/2;
        return !s.substring(0, l).equals(s.substring(l));
    }

    private long countValid(List<Range> ranges, Function<String, Boolean> validFns) {
        long res = 0;
        for(Range r : ranges) {
            for(long i = r.min; i <= r.max; i++) {
                if(!isValid(String.valueOf(i))) {
                   res += i;
                }
            }
        }
        return res;
    }

    @Test
    public void testPart1() {
        assertEquals(1227775554L, countValid(parseInput(TEST_INPUT), D2::isValid));
        assertEquals(15873079081L, countValid(parseInput(FileReader.readFileString("src/test/resources/2025/D2.txt")), D2::isValid));
    }

    // could use regex
    private static boolean isValid2(String s) {
        int l = s.length()/2;
        for(int i = 2; i < l; i = i + 2) {
            String sub = s.substring(0, i);
            if(!isValid(sub))
                return false;
        }
        return true;
    }

    private List<Long> generateInvalids(Range r) {
        String sMin = String.valueOf(r.min);
        String sMax = String.valueOf(r.max);
        List<Long> res = new ArrayList<>();

        for(int i=1; i<sMax.length(); i++) {
            String stub = sMin.substring(0, i);
            StringBuilder sb = new StringBuilder();
            while(sb.length() < sMax.length()) {
                sb.append(stub);
            }
            res.add(Long.parseLong(sb.toString()));
        }

        return res;
    }

    @Test
    public void testPart2() {
        generateInvalids(new Range(11, 22)).forEach(System.out::println);
//        assertEquals(4174379265L, countValid(parseInput(TEST_INPUT), D2::isValid2));
    }
}
