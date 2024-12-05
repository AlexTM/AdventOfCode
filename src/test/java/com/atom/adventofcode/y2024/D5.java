package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D5 {

    private final String TEST_INPUT = """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
            
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47""";



    private static class Data {
        public final Map<Integer, List<Integer>> order = new HashMap<>();
        public final List<List<Integer>> updates = new ArrayList<>();
    }

    private Data parseInput(final String input) {
        return FileReader.parseStringForObject(input, new Data(), (a, line) -> {
            if (line.isBlank()) {
                return a;
            }
            if (line.contains(",")) {
                a.updates.add(Stream.of(line.split(",")).map(Integer::parseInt).toList());
            } else {
                String[] parts = line.split("\\|");
                int key = Integer.parseInt(parts[0]);
                if(!a.order.containsKey(key)) {
                    a.order.put(key, new ArrayList<>());
                }
                a.order.get(key).add(Integer.parseInt(parts[1]));
            }
            return a;
        });
    }

    public static boolean isCorrectOrder(Map<Integer, List<Integer>> order, List<Integer> update) {
        for (int i=0; i<update.size(); i++) {
            int v = update.get(i);
            if (!order.containsKey(v)) {
                continue;
            }
            List<Integer> list = order.get(v);
            for(int check : list) {
                for(int j=i-1; j>=0; j--) {
                    if(update.get(j) == check) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private long getScore(final Data data, boolean filterResult, BiFunction<Map<Integer, List<Integer>>, List<Integer>, List<Integer>> fn) {
        return data.updates.stream().filter(update -> filterResult == isCorrectOrder(data.order, update))
                .map(update -> fn.apply(data.order, update))
                .mapToLong(update -> update.get(update.size()/2)).sum();
    }

    @Test
    public void testPart1() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(143, getScore(data, true, (a,b) -> b));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D5.txt"));
        assertEquals(7198, getScore(data, true, (a,b) -> b));
    }

    private static List<Integer> getCorrectedOrder(Map<Integer, List<Integer>> order, List<Integer> update) {
        final List<Integer> newOrder = new LinkedList<>();
        for (int v : update) {
            if (!order.containsKey(v)) {
                newOrder.add(v);
                continue;
            }

            int index = Integer.MAX_VALUE;
            List<Integer> list = order.get(v);
            for (int check : list) {
                for (int j = 0; j < newOrder.size(); j++) {
                    if (newOrder.get(j) == check) {
                        index = Math.min(index, newOrder.indexOf(check));
                    }
                }
            }
            if (index == Integer.MAX_VALUE) {
                newOrder.add(v);
            } else {
                newOrder.add(index, v);
            }
        }
        return newOrder;
    }

    @Test
    public void testPart2() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(123, getScore(data, false, D5::getCorrectedOrder));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D5.txt"));
        assertEquals(4230, getScore(data, false, D5::getCorrectedOrder));
    }
}
