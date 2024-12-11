package com.atom.adventofcode.y2024;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D11 {

    private static final String INPUT = "475449 2599064 213 0 2 65 5755 51149";

    record Stones(long num, int depth){}

    private List<Long> parseInput(String input) {
        return Arrays.stream(input.split(" ")).map(Long::parseLong).collect(Collectors.toList());
    }

    private List<Long> singleNumber(Long num) {
        List<Long> res = new ArrayList<>();
        if (num == 0) {
            res.add(1L);
        } else if (num.toString().length() % 2 == 0) {
            int l = num.toString().length();
            res.add(Long.parseLong(num.toString().substring(0, l / 2)));
            res.add(Long.parseLong(num.toString().substring(l / 2)));
        } else {
            res.add(num * 2024);
        }
        return res;
    }

    private long blink(List<Long> input, int n) {
        long count = 0;
        Map<Stones, Long> cache = new HashMap<>();
        for (Long aLong : input) {
            count += blinkRec(aLong, n, cache);
        }
        return count;
    }

    private long blinkRec(Long input, int depth, Map<Stones, Long> cache) {
        if(depth == 0) {
            return 1;
        }

        Stones key = new Stones(input, depth);
        if(cache.containsKey(key)) {
            return cache.get(key);
        }

        long size = 0;
        List<Long> res = singleNumber(input);
        for (Long i : res) {
            size += blinkRec(i, depth-1, cache);
        }

        cache.put(key, size);

        return size;
    }

    @Test
    public void testPartOne() {
        assertEquals(55312, blink(parseInput("125 17"), 25));
        assertEquals(193269, blink(parseInput(INPUT), 25));
    }

    @Test
    public void testPartTwo() {
        assertEquals(228449040027793L, blink(parseInput(INPUT), 75));
    }
}
