package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D4 {

    record Range(int min, int max){};
    record Pair(Range a, Range b){};

    private static Pair loadData(String i) {
        String splita[] = i.split(",");
        String splitb[] = splita[0].split("-");
        String splitc[] = splita[1].split("-");
        return new Pair(
                new Range(Integer.parseInt(splitb[0]), Integer.parseInt(splitb[1])),
                new Range(Integer.parseInt(splitc[0]), Integer.parseInt(splitc[1])));
    }

    private static boolean isTotalOverlap(Range a, Range b) {
        return a.min >= b.min && a.max <= b.max;
    }

    private static boolean isPartialOverlap(Range a, Range b) {
        return b.min >= a.min && b.min <= a.max;
    }

    private static int findOverLap(final List<Pair> rangeList, final BiFunction<Range, Range, Boolean> func) {
        return (int)rangeList.stream()
                .map(p -> func.apply(p.a, p.b) || func.apply(p.b, p.a))
                .filter(p -> p).count();
    }

    @Test
    public void testOverlap() {
        assertEquals(466,
                findOverLap(FileReader.readFileObjectList("src/test/resources/2022/D4.txt", D4::loadData),
                        D4::isTotalOverlap));
    }

    @Test
    public void testOverlap2() {
        assertEquals(865,
                findOverLap(
                        FileReader.readFileObjectList("src/test/resources/2022/D4.txt", D4::loadData),
                        D4::isPartialOverlap));
    }

}
