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

    private List<String> intPath(Pos current, Pos end, String path, Character[] keypad) {
        if(current.equals(end)) {
            return List.of(path+"A");
        }
        if(keypad[current.x + current.y*3] == '*') {
            return List.of();
        }

        int diffx = end.x - current.x;
        int diffy = end.y - current.y;
        List<String> paths = new ArrayList<>();
        if(diffx > 0) {
            paths.addAll(intPath(new Pos(current.x+1, current.y), end, path + ">", keypad));
        }
        if(diffx < 0) {
            paths.addAll(intPath(new Pos(current.x-1, current.y), end, path + "<", keypad));
        }
        if(diffy > 0) {
            paths.addAll(intPath(new Pos(current.x, current.y+1), end, path + "v", keypad));
        }
        if (diffy < 0){
            paths.addAll(intPath(new Pos(current.x, current.y-1), end, path + "^", keypad));
        }
        return paths;
    }

    private List<String> shortestPath(Character start, Character end, Character[] keypad) {
        int startIdx = IntStream.range(0, keypad.length).filter(i -> keypad[i] == start).findFirst().orElseThrow();
        int endIdx = IntStream.range(0, keypad.length).filter(i -> keypad[i] == end).findFirst().orElseThrow();
        return intPath(new Pos(startIdx%3, startIdx/3), new Pos(endIdx%3, endIdx/3), "", keypad);
    }

    private Map<Combo, List<String>> generateComboMapOfAllKeyCombinations(Character[] keypad) {
        Map<Combo, List<String>> comboMap = new HashMap<>();
        for (Character character : keypad) {
            for (Character value : keypad) {
                comboMap.put(new Combo(character, value), shortestPath(character, value, keypad));
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

    private List<String> singleCombo(String target, final Map<Combo, List<String>> shortestPathKeyPad, final Map<Combo, List<String>> cache) {
        return intSingleCombo("A"+target, 1, "", shortestPathKeyPad, cache);
    }

    private int calculateComplexity(String target, Map<Combo, List<String>> numPadCombos, Map<Combo, List<String>> arrowPabCombos) {
        List<String> comboForNumPad = singleCombo(target, numPadCombos, null);

        List<String> result = new ArrayList<>();
        for(String combo : comboForNumPad) {
            List<String> combosForArrowPad1 = singleCombo(combo, arrowPabCombos, null);
            for(String combo1 : combosForArrowPad1) {
                List<String> combosForArrowPad2 = singleCombo(combo1, arrowPabCombos, new HashMap<>());
                result.addAll(combosForArrowPad2);
            }
        }

        result.sort(Comparator.comparingInt(String::length));
        int len = result.get(0).length();
        int num = Integer.parseInt(target.substring(0, 3));
        System.out.println("len: " + len + " num: " + num);
        return len*num;
    }

    @Test
    public void testPart1() {
        Map<Combo, List<String>> numPadCombos = generateComboMapOfAllKeyCombinations(NUM_KEYPAD);
        Map<Combo, List<String>> arrowPabCombos = generateComboMapOfAllKeyCombinations(ARROW_KEYPAD);

        assertEquals(126384, TEST_INPUT.lines().mapToInt(s -> calculateComplexity(s, numPadCombos, arrowPabCombos)).sum());
        assertEquals(242484, INPUT.lines().mapToInt(s -> calculateComplexity(s, numPadCombos, arrowPabCombos)).sum());
    }

    @Test
    public void testPart2() {
        Map<Combo, List<String>> numPadCombos = generateComboMapOfAllKeyCombinations(NUM_KEYPAD);
        Map<Combo, List<String>> arrowPabCombos = generateComboMapOfAllKeyCombinations(ARROW_KEYPAD);

        assertEquals(126384, TEST_INPUT.lines().mapToInt(s -> calculateComplexity(s, numPadCombos, arrowPabCombos)).sum());
//        assertEquals(242484, INPUT.lines().mapToInt(s -> calculateComplexity(s, numPadCombos, arrowPabCombos)).sum());
    }
}
