package com.atom.adventofcode.y2024;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D21 {

    private static final String TEST_INPUT = """
            029A
            980A
            179A
            456A
            379A""";

    private static final String INPUT = """
            805A
            682A
            671A
            973A
            319A""";


    record Pos(int x, int y) {}
    record Combo(Character start, Character end) {}
    private static final Character[] NUM_KEYPAD = {
            '7', '8', '9',
            '4', '5', '6',
            '1', '2', '3',
            '*', '0', 'A'};
    private static final Character[] ARROW_KEYPAD = {
            '*', '^', 'A',
            '<', 'v', '>'};

    private List<String> generatePathForCombo(Pos current, Pos end, String path, Character[] keypad) {
        if(current.equals(end))
            return List.of(path+"A");
        if(keypad[current.x + current.y*3] == '*')
            return List.of();

        int diffX = end.x - current.x;
        int diffY = end.y - current.y;
        List<String> paths = new ArrayList<>();
        if(diffX > 0)
            paths.addAll(generatePathForCombo(new Pos(current.x+1, current.y), end, path + ">", keypad));
        if(diffX < 0)
            paths.addAll(generatePathForCombo(new Pos(current.x-1, current.y), end, path + "<", keypad));
        if(diffY > 0)
            paths.addAll(generatePathForCombo(new Pos(current.x, current.y+1), end, path + "v", keypad));
        if (diffY < 0)
            paths.addAll(generatePathForCombo(new Pos(current.x, current.y-1), end, path + "^", keypad));
        return paths;
    }

    private List<String> generatePathForCombo(Character start, Character end, Character[] keypad) {
        int startIdx = IntStream.range(0, keypad.length).filter(i -> keypad[i] == start).findFirst().orElseThrow();
        int endIdx = IntStream.range(0, keypad.length).filter(i -> keypad[i] == end).findFirst().orElseThrow();
        return generatePathForCombo(new Pos(startIdx%3, startIdx/3), new Pos(endIdx%3, endIdx/3), "", keypad);
    }

    private Map<Combo, List<String>> generateComboMapOfAllKeyCombinations(Character[] keypad) {
        Map<Combo, List<String>> comboMap = new HashMap<>();
        for (Character character : keypad) {
            for (Character value : keypad) {
                comboMap.put(new Combo(character, value), generatePathForCombo(character, value, keypad));
            }
        }
        return comboMap;
    }

    private List<String> intSingleCombo(String target, int pos, String combo, final Map<Combo, List<String>> keyPadCombos, final Map<Combo, List<String>> cache) {
        if(pos == target.length()) {
            return List.of(combo);
        }

        Combo c = new Combo(target.charAt(pos-1), target.charAt(pos));

        if(cache != null && cache.containsKey(c)) {
            return cache.get(c);
        }

        List<String> result = new ArrayList<>();
        for(String path : keyPadCombos.get(c)) {
            result.addAll(intSingleCombo(target, pos+1, combo + path, keyPadCombos, cache));
        }

        if(cache != null)
            cache.put(c, result);

        return result;
    }

    record ComboCost(Combo combo, int depth) {}
    private long costOfCombo(Combo combo, int depth, Map<Combo, List<String>> arrowPabCombos, Map<ComboCost, Long> cache) {

        if(depth == 0) {
            return arrowPabCombos.get(combo).stream().mapToLong(String::length).min().orElseThrow();
        }

        ComboCost cc = new ComboCost(combo, depth);
        if(cache.containsKey(cc)) {
            return cache.get(cc);
        }

        long minCost = Long.MAX_VALUE;
        List<String> paths = arrowPabCombos.get(combo);
        for(String path : paths) {
            path = "A" + path;

            long cost = 0;
            for(int i=1; i<path.length(); i++) {
                Combo c = new Combo(path.charAt(i-1), path.charAt(i));
                cost += costOfCombo(c, depth-1, arrowPabCombos, cache);
            }
            minCost = Math.min(minCost, cost);
        }

        cache.put(cc, minCost);

        return minCost;
    }

    private long calculateComplexity4(String target, int depth, Map<Combo, List<String>> numPadCombos, Map<Combo, List<String>> arrowPabCombos) {
        List<String> comboForNumPad = intSingleCombo("AA"+target, 1, "", numPadCombos, null);
        return calculateComplexity5(comboForNumPad, depth, numPadCombos, arrowPabCombos) * Long.parseLong(target.substring(0, 3));
    }

    private long calculateComplexity5(List<String> comboForNumPad, int depth, Map<Combo, List<String>> numPadCombos, Map<Combo, List<String>> arrowPabCombos) {
        long minCost = Long.MAX_VALUE;
        for(String path : comboForNumPad) {
            long cost = 0;
            for(int i=1; i<path.length(); i++) {
                Combo c = new Combo(path.charAt(i-1), path.charAt(i));
                cost += costOfCombo(c, depth-1, arrowPabCombos, new HashMap<>());
            }
            minCost = Math.min(minCost, cost);
        }
        return minCost;
    }

    @Test
    public void testPart1() {
        Map<Combo, List<String>> numPadCombos = generateComboMapOfAllKeyCombinations(NUM_KEYPAD);
        Map<Combo, List<String>> arrowPabCombos = generateComboMapOfAllKeyCombinations(ARROW_KEYPAD);

        assertEquals(126384, TEST_INPUT.lines().mapToLong(s -> calculateComplexity4(s, 2, numPadCombos, arrowPabCombos)).sum());
        assertEquals(242484, INPUT.lines().mapToLong(s -> calculateComplexity4(s, 2, numPadCombos, arrowPabCombos)).sum());
    }

    @Test
    public void testPart2() {
        Map<Combo, List<String>> numPadCombos = generateComboMapOfAllKeyCombinations(NUM_KEYPAD);
        Map<Combo, List<String>> arrowPabCombos = generateComboMapOfAllKeyCombinations(ARROW_KEYPAD);
        assertEquals(294209504640384L,
                INPUT.lines().mapToLong(s -> calculateComplexity4(s, 25, numPadCombos, arrowPabCombos)).sum());
    }
}
