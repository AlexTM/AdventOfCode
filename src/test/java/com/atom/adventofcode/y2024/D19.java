package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D19 {
    private static final String TEST_INPUT = """
            r, wr, b, g, bwu, rb, gb, br
            
            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb""";

    record Data(List<String> towels, List<String> patterns) {}

    private Data parseInput(final String input) {
        String[] parts = input.split("\n");
        String[] towels = parts[0].split(", ");
        List<String> patterns = new ArrayList<>();
        for(int i=1; i<parts.length; i++) {
            if(StringUtils.isBlank(parts[i])) continue;
            patterns.add(parts[i]);
        }
        return new Data(Arrays.asList(towels), patterns);
    }

    private long matchPattern(final List<String> towels, final String pattern, final Map<String, Long> cache) {

        if(cache.containsKey(pattern)) {
            return cache.get(pattern);
        }

        if(pattern.isEmpty()) {
            return 1;
        }

        // find all towels that match the pattern given their length and starting index
        long matches = 0;
        for(int i=0; i<towels.size(); i++) {
            String towel = towels.get(i);

            if(towel.length() > pattern.length()) {
                continue;
            }

            String match = pattern.substring(0, towel.length());
            if(match.equals(towel)) {
                matches += matchPattern(towels, pattern.substring(towel.length()), cache);
            }
        }

        cache.put(pattern, matches);
        return matches;
    }

    private long countMatches(final List<String> towels, final List<String> patterns) {
        final Map<String, Long> cache = new HashMap<>();
        return patterns.stream().filter(pattern -> matchPattern(towels, pattern, cache) > 0).count();
    }

    @Test
    public void testPart1() {
        Data data = parseInput(TEST_INPUT);
        System.out.println(data);
        assertEquals(6, countMatches(data.towels, data.patterns));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D19.txt"));
        assertEquals(209, countMatches(data.towels, data.patterns));
    }

    private long sumMatches(final List<String> towels, final List<String> patterns) {
        final Map<String, Long> cache = new HashMap<>();
        return patterns.stream().mapToLong(pattern -> matchPattern(towels, pattern, cache)).sum();
    }

    @Test
    public void testPart2() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(16, sumMatches(data.towels, data.patterns));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D19.txt"));
        long res = sumMatches(data.towels, data.patterns);
        assertEquals(777669668613191L, res);
    }
}
