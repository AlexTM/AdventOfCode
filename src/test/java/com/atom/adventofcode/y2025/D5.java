package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D5 {
    private static final String TEST_INPUT = """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
            """;

    record Range(long min, long max){}
    record Data(List<Range> ranges, List<Long> ids){}

    private Data parseInput(String input) {
        var ranges = new ArrayList<Range>();
        String[] split = input.split("\n");
        int p = 0;
        while (!split[p].isBlank()) {
            String[] split2 = split[p].split("-");
            long min = Long.parseLong(split2[0]);
            long max = Long.parseLong(split2[1]);
            ranges.add(new Range(min, max));
            p++;
        }
        p++;
        var ids = new ArrayList<Long>();
        while (p < split.length) {
            ids.add(Long.parseLong(split[p]));
            p++;
        }
        return new Data(ranges, ids);
    }

    private int countFresh(Data data) {
        int c = 0;
        for(Long idx : data.ids) {
            boolean fresh = false;
            for(Range r :  data.ranges) {
                if(idx >= r.min && idx <= r.max) {
                    fresh = true;
                    break;
                }
            }
            if(fresh)
                c++;
        }
        return c;
    }

    @Test
    public void testPart1() {
        assertEquals(3, countFresh(parseInput(TEST_INPUT)));
        assertEquals(664, countFresh(parseInput(FileReader.readFileString("src/test/resources/2025/D5.txt"))));
    }

    
    private List<Range> condenseRanges2(List<Range> ranges) {
        List<Range> result = new ArrayList<>(ranges);
        boolean changed;

        do {
            changed = false;
            List<Range> newRanges = new ArrayList<>();
            Set<Range> processed = new HashSet<>();

            for (Range r1 : result) {
                if (processed.contains(r1)) continue;

                Range current = r1;
                processed.add(r1);

                for (Range r2 : result) {
                    if (processed.contains(r2)) continue;

                    // Adjacent ranges
                    if (current.max + 1 == r2.min) {
                        current = new Range(current.min, r2.max);
                        processed.add(r2);
                        changed = true;
                    }
                    // Overlapping ranges
                    else if (current.max >= r2.min - 1 && current.min <= r2.max + 1) {
                        current = new Range(Math.min(current.min, r2.min),
                                Math.max(current.max, r2.max));
                        processed.add(r2);
                        changed = true;
                    }
                }
                newRanges.add(current);
            }
            result = newRanges;
        } while (changed);

        return result;
    }


    private long calculateTotalRangeSize(List<Range> ranges) {
        long total = 0;
        for(Range r : ranges) {
            total += r.max - r.min + 1;
        }
        return total;
    }

    @Test
    public void testPart2() {
        assertEquals(14, calculateTotalRangeSize(
                condenseRanges2(parseInput(TEST_INPUT).ranges)));

        long res = calculateTotalRangeSize(
                condenseRanges2(parseInput(FileReader.readFileString("src/test/resources/2025/D5.txt")).ranges));

        assertEquals(350780324308385L, res);

    }
}
