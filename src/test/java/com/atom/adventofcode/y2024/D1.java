package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D1 {
    private static final String TEST_INPUT = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3""";

    record Data(List<Integer> lista, List<Integer> listb) { }

    private Data parseInput(String input) {
        List<Integer> listA = new ArrayList<>();
        List<Integer> listB = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            listA.add(a);
            listB.add(b);
        }
        return new Data(listA, listB);
    }

    private int getDiff(List<Integer> lista, List<Integer> listb) {
        Collections.sort(lista);
        Collections.sort(listb);
        int sum = 0;
        for(int i = 0; i < lista.size(); i++) {
            sum += Math.abs(lista.get(i) - listb.get(i));
        }
        return sum;
    }

    @Test
    public void testPart1() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(11, getDiff(data.lista, data.listb));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D1.txt"));
        assertEquals(2196996, getDiff(data.lista, data.listb));
    }

    private long getSimilarityScore(List<Integer> lista, List<Integer> listb) {
        final Map<Integer, Long> frequencyMap = listb.stream()
                .collect(Collectors.groupingBy(number -> number, Collectors.counting()));

        return lista.stream().filter(frequencyMap::containsKey).map(n -> n * frequencyMap.get(n))
                .reduce(0L, Long::sum);
    }

    @Test
    public void testPart2() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(31, getSimilarityScore(data.lista, data.listb));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D1.txt"));
        assertEquals(23655822, getSimilarityScore(data.lista, data.listb));
    }

}
