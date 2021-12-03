package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class D3 {

    record Counts(int oneCount, int zeroCount) {}

    private Counts countOccurrences(List<String> inputs, int pos) {
        int oneCount = 0, zeroCount = 0;

        for (String input : inputs) {
            switch (input.charAt(pos)) {
                case '0' -> zeroCount++;
                case '1' -> oneCount++;
            }
        }
        return new Counts(oneCount, zeroCount);
    }

    private int getPowerRating(List<String> inputs, int size) {
        int gammaRate = 0;
        int maxValue = (int)Math.pow(2, size)-1;

        for(int i=0; i<size; i++) {
            Counts c = countOccurrences(inputs, i);
            gammaRate = gammaRate << 1;
            if (c.zeroCount <= c.oneCount)
                gammaRate++;
        }

        int epilsonRate = maxValue - gammaRate;
        return epilsonRate * gammaRate;
    }

    @Test
    public void testPart1() {
        assertEquals(198, getPowerRating(
                FileReader.readFileObjectList("src/test/resources/2021/D3_t.txt", line -> line), 5));

        assertEquals(3895776, getPowerRating(
                FileReader.readFileObjectList("src/test/resources/2021/D3.txt", line -> line), 12));
    }

    private int getLifeSupportRating(List<String> inputs) {
        return getOxygenGeneratorRating(inputs) * getCO2ScrubberRating(inputs);
    }

    private int getOxygenGeneratorRating(List<String> inputs) {
        return convertStringBitToDecimal(recursiveFilterList(inputs, 0, '1'));
    }

    private int getCO2ScrubberRating(List<String> inputs) {
        return convertStringBitToDecimal(recursiveFilterList(inputs, 0, '0'));
    }

    private int convertStringBitToDecimal(String value) {
        return value.chars().reduce(0, (s, e) -> (s << 1) + e - '0');
    }

    private String recursiveFilterList(List<String> inputs, int pos, char initialKeep) {

        Counts c = countOccurrences(inputs, pos);
        final char keep =
                c.zeroCount > c.oneCount ?
                        initialKeep == '1' ? '0' : '1'
                        : initialKeep;

        List<String> nextList =
                inputs.stream().filter(i -> i.charAt(pos) == keep)
                        .collect(Collectors.toList());

        return nextList.size() == 1 ? nextList.get(0) : recursiveFilterList(nextList, pos+1, initialKeep);
    }

    @Test
    public void testPart2() {
        assertEquals(230, getLifeSupportRating(
                FileReader.readFileObjectList("src/test/resources/2021/D3_t.txt", line -> line)));

        assertEquals(7928162, getLifeSupportRating(
                FileReader.readFileObjectList("src/test/resources/2021/D3.txt", line -> line)));
    }

}
