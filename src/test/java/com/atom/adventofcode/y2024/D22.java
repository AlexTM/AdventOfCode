package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class D22 {

    private long mix(long secret, long given) {
        return secret ^ given;
    }
    private long prune(long given) {
        return given % 16777216;
    }

    private long nextSecretNumberOne(long secretNumber) {
        long tmp = secretNumber*64L;
        secretNumber = mix(secretNumber, tmp);
        secretNumber = prune(secretNumber);

        tmp = secretNumber / 32;
        secretNumber = mix(secretNumber, tmp);
        secretNumber = prune(secretNumber);

        tmp = secretNumber*2048L;
        secretNumber = mix(secretNumber, tmp);
        secretNumber = prune(secretNumber);

        return secretNumber;
    }

    private long generateNthNumber(long secretNumber, int n) {
        for (int i = 0; i < n; i++) {
            secretNumber = nextSecretNumberOne(secretNumber);
        }
        return secretNumber;
    }

    private static final String TEST_INPUT = """
            1
            10
            100
            2024""";

    @Test
    public void testPartOne() {
        assertEquals(37327623L, TEST_INPUT.lines().mapToLong(Long::parseLong).map(i -> generateNthNumber(i, 2000)).sum());
        assertEquals(21147129593L, FileReader.readFileString("src/test/resources/2024/D22.txt")
                .lines().mapToLong(Long::parseLong).map(i -> generateNthNumber(i, 2000)).sum());
    }

    private List<Integer> generateArrayOfNPrices(Long initialSecret, int n) {
        List<Integer> prices = new ArrayList<>();
        String strSec = initialSecret.toString();
        prices.add(Integer.parseInt(strSec.substring(strSec.length()-1)));
        for(int i=0; i<n; i++) {
            initialSecret = nextSecretNumberOne(initialSecret);
            String tmp = initialSecret.toString();
            tmp = tmp.substring(tmp.length()-1);
            prices.add(Integer.parseInt(tmp));
        }
        return prices;
    }

    private List<Integer> calculateDiff(List<Integer> prices) {
        List<Integer> diff = new ArrayList<>();
        for(int i=1; i<prices.size(); i++) {
            diff.add(prices.get(i) - prices.get(i-1));
        }
        return diff;
    }

    record Sequence(int a, int b, int c, int d) { }

    private Map<Sequence, Integer> addToPriceToSequenceMap(final List<Integer> prices, final List<Integer> diffs) {
        Set<Sequence> seen = new HashSet<>();
        Map<Sequence, Integer> sequencePrice = new HashMap<>();
        for(int i=3; i<diffs.size(); i++) {
            Sequence sequence = new Sequence(diffs.get(i-3), diffs.get(i-2), diffs.get(i-1), diffs.get(i));
            if(!seen.contains(sequence)) {
                sequencePrice.put(sequence, prices.get(i+1));
                seen.add(sequence);
            }
        }
        return sequencePrice;
    }

    private Map<Sequence, Integer> goOverSingleBuyer(long initialSecret) {
        List<Integer> prices = generateArrayOfNPrices(initialSecret, 2000);
        List<Integer> diffs = calculateDiff(prices);
        return addToPriceToSequenceMap(prices, diffs);
    }

    private long getBestSequence(String input) {
        Map<Sequence, Integer> sequenceMap = new HashMap<>();
        long[] initialSecrets = input.lines().mapToLong(Long::parseLong).toArray();
        for(long i : initialSecrets) {
            Map<Sequence, Integer> sequencePrice = goOverSingleBuyer(i);
            for(Map.Entry<Sequence, Integer> entry : sequencePrice.entrySet()) {
                sequenceMap.put(entry.getKey(), sequenceMap.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        // get an ordered list based on the value of the sequenceMap
        List<Map.Entry<Sequence, Integer>> sorted = new ArrayList<>(sequenceMap.entrySet());
        sorted.sort(Map.Entry.comparingByValue());
        Collections.reverse(sorted);

        return sorted.get(0).getValue();
    }


    @Test
    public void testPartTwo() {
        Sequence TEST_SEQUENCE = new Sequence(-2,1,-1,3);
        assertEquals(7, goOverSingleBuyer(1).get(TEST_SEQUENCE));
        assertEquals(7, goOverSingleBuyer(2).get(TEST_SEQUENCE));
        assertNull(goOverSingleBuyer(3).get(TEST_SEQUENCE));
        assertEquals(9, goOverSingleBuyer(2024).get(TEST_SEQUENCE));

        assertEquals(2445, getBestSequence(FileReader.readFileString("src/test/resources/2024/D22.txt")));
    }

}
