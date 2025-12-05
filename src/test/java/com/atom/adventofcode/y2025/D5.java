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
        List<Range> result = null;
        Set<Range> merged;

        while(result == null || result.size() != ranges.size()) {
            merged = new HashSet<>();
            if(result != null) {
                ranges = result;
            }
            result = new ArrayList<>();

            for (Range r : ranges) {
                Range toAdd = null;
                Range toRemove = null;

                if (merged.contains(r))
                    continue;

                for (Range r2 : ranges) {

                    if (merged.contains(r2))
                        continue;

                    if (r.min < r2.min && r.max >= r2.min && r.max <= r2.max) {
                        toAdd = new Range(r.min, r2.max);
                        toRemove = r2;
                        break;
                    }
                    // total overlap
                    if (r.min < r2.min && r.max > r2.max) {
                        toAdd = new Range(r.min, r.max);
                        toRemove = r2;
                        break;
                    }
                    // partial overlap
                    if (r.min > r2.min && r.min <= r2.max && r.max >= r2.max) {
                        toAdd = new Range(r2.min, r.max);
                        toRemove = r2;
                        break;
                    }

                }
                if (toRemove != null) {
                    result.add(toAdd);
                    merged.add(r);
                    merged.add(toRemove);
                } else {
                    result.add(r);
                    merged.add(r);
                }

            }
        }
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

        assertNotEquals(415321722881192L, res);
        assertNotEquals(416375886268456L, res); // too high
        assertNotEquals(363340107255412L, res); // too high
        assertNotEquals(364904969506891L, res);
        assertNotEquals(364634303056893L, res);

        assertEquals(0, res);

    }


    private List<Range> condenseRanges(List<Range> ranges) {
        List<Range> result = new ArrayList<>();

        boolean overlap = true;
        while(overlap) {
            result = new ArrayList<>();
            overlap = false;
            for (Range r : ranges) {
                Range toAdd = null;
                Range toRemove = null;
                for (Range r2 : result) {
                    // partial overlap
                    if (r.min < r2.min && r.max >= r2.min && r.max <= r2.max) {
                        toAdd = new Range(r.min, r2.max);
                        toRemove = r2;
                        break;
                    }
                    // total overlap
                    if (r.min < r2.min && r.max > r2.max) {
                        toAdd = new Range(r.min, r.max);
                        toRemove = r2;
                        break;
                    }
                    // partial overlap
                    if (r.min > r2.min && r.min <= r2.max && r.max >= r2.max) {
                        toAdd = new Range(r2.min, r.max);
                        toRemove = r2;
                    }
                }
                if (toRemove != null) {
                    result.remove(toRemove);
                    result.add(toAdd);
                    overlap = true;
                } else {
                    result.add(r);
                }
            }
            ranges = new ArrayList<>(result);
        }

        return result;
    }

}
