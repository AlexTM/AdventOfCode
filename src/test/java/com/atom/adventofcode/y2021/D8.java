package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Only Part A done
 *
 * --- Day 8: Seven Segment Search ---
 *
 * You barely reach the safety of the cave when the whale smashes into the cave mouth, collapsing it. Sensors indicate
 * another exit to this cave at a much greater depth, so you have no choice but to press on.
 *
 * As your submarine slowly makes its way through the cave system, you notice that the four-digit seven-segment displays
 * in your submarine are malfunctioning; they must have been damaged during the escape. You'll be in a lot of trouble
 * without them, so you'd better figure out what's wrong.
 *
 * Each digit of a seven-segment display is rendered by turning on or off any of seven segments named a through g:
 *
 *   0:      1:      2:      3:      4:
 *  aaaa    ....    aaaa    aaaa    ....
 * b    c  .    c  .    c  .    c  b    c
 * b    c  .    c  .    c  .    c  b    c
 *  ....    ....    dddd    dddd    dddd
 * e    f  .    f  e    .  .    f  .    f
 * e    f  .    f  e    .  .    f  .    f
 *  gggg    ....    gggg    gggg    ....
 *
 *   5:      6:      7:      8:      9:
 *  aaaa    aaaa    aaaa    aaaa    aaaa
 * b    .  b    .  .    c  b    c  b    c
 * b    .  b    .  .    c  b    c  b    c
 *  dddd    dddd    ....    dddd    dddd
 * .    f  e    f  .    f  e    f  .    f
 * .    f  e    f  .    f  e    f  .    f
 *  gggg    gggg    ....    gggg    gggg
 *
 * So, to render a 1, only segments c and f would be turned on; the rest would be off. To render a 7, only segments
 * a, c, and f would be turned on.
 *
 * The problem is that the signals which control the segments have been mixed up on each display. The submarine is
 * still trying to display numbers by producing output on signal wires a through g, but those wires are connected to
 * segments randomly. Worse, the wire/segment connections are mixed up separately for each four-digit display! (All of
 * the digits within a display use the same connections, though.)
 *
 * So, you might know that only signal wires b and g are turned on, but that doesn't mean segments b and g are turned
 * on: the only digit that uses two segments is 1, so it must mean segments c and f are meant to be on. With just that
 * information, you still can't tell which wire (b/g) goes to which segment (c/f). For that, you'll need to collect
 * more information.
 *
 * For each display, you watch the changing signals for a while, make a note of all ten unique signal patterns you see,
 * and then write down a single four digit output value (your puzzle input). Using the signal patterns, you should be
 * able to work out which pattern corresponds to which digit.
 *
 * For example, here is what you might see in a single entry in your notes:
 *
 * acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |
 * cdfeb fcadb cdfeb cdbaf
 *
 * (The entry is wrapped here to two lines so it fits; in your notes, it will all be on a single line.)
 *
 * Each entry consists of ten unique signal patterns, a | delimiter, and finally the four digit output value. Within an
 * entry, the same wire/segment connections are used (but you don't know what the connections actually are). The unique
 * signal patterns correspond to the ten different ways the submarine tries to render a digit using the current
 * wire/segment connections. Because 7 is the only digit that uses three segments, dab in the above example means that
 * to render a 7, signal lines d, a, and b are on. Because 4 is the only digit that uses four segments, eafb means that
 * to render a 4, signal lines e, a, f, and b are on.
 *
 * Using this information, you should be able to work out which combination of signal wires corresponds to each of the
 * ten digits. Then, you can decode the four digit output value. Unfortunately, in the above example, all of the digits
 * in the output value (cdfeb fcadb cdfeb cdbaf) use five segments and are more difficult to deduce.
 *
 * For now, focus on the easy digits. Consider this larger example:
 *
 * be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |
 * fdgacbe cefdb cefbgd gcbe
 * edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |
 * fcgedb cgb dgebacf gc
 * fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |
 * cg cg fdcagb cbg
 * fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |
 * efabcd cedba gadfec cb
 * aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |
 * gecf egdcabf bgf bfgea
 * fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |
 * gebdcfa ecba ca fadegcb
 * dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |
 * cefg dcbef fcge gbcadfe
 * bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |
 * ed bcgafe cdgba cbgef
 * egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |
 * gbdfcae bgc cg cgb
 * gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |
 * fgae cfgab fg bagce
 *
 * Because the digits 1, 4, 7, and 8 each use a unique number of segments, you should be able to tell which combinations
 * of signals correspond to those digits. Counting only digits in the output values (the part after | on each line), in
 * the above example, there are 26 instances of digits that use a unique number of segments (highlighted above).
 *
 * In the output values, how many times do digits 1, 4, 7, or 8 appear?
 */
public class D8 {

    record Row(List<String> inputs, List<String> outputs){};

    private List<Row> loadData(String filename) {
        return FileReader.readFileObjectList(filename, line -> {
            String[] parts = line.split(" \\| ");
            List<String> patterns = Arrays.stream(parts[0].split(" ")).toList();
            List<String> outputs = Arrays.stream(parts[1].split(" ")).toList();
            return new Row(patterns, outputs);
        });
    }

    private long countUnique(List<Row> rows) {
        return rows.stream().flatMap(r -> r.outputs.stream())
                .map(String::length)
                .filter(s -> s == 2 || s ==7 || s == 4 || s == 3)
                .count();
    }

    @Test
    public void testPart1() {
        assertEquals(26, countUnique(loadData("src/test/resources/2021/D8_t.txt")));
        assertEquals(261, countUnique(loadData("src/test/resources/2021/D8.txt")));
    }


    static final Map<String, Set<String>> numberMap;
    static {
        numberMap = new HashMap<>();
        numberMap.put("0", Set.of("abcefg".split("")));
        numberMap.put("1", Set.of("cf".split("")));
        numberMap.put("2", Set.of("acdeg".split("")));
        numberMap.put("3", Set.of("acdfg".split("")));
        numberMap.put("4", Set.of("bcdf".split("")));
        numberMap.put("5", Set.of("abdfg".split("")));
        numberMap.put("6", Set.of("abdefg".split("")));
        numberMap.put("7", Set.of("acf".split("")));
        numberMap.put("8", Set.of("abcdefg".split("")));
        numberMap.put("9", Set.of("abcdfg".split("")));
    }

    private String remainder(String s1, String s2) {
        Set<String> one = new HashSet<>(Arrays.asList(s1.split("")));
        Set<String> two = new HashSet<>(Arrays.asList(s2.split("")));
        one.removeAll(two);
        return one.stream().reduce("", (res, s) -> res+s);
    }

    private Map<String, Integer> countOccurances(List<String> input) {
        Map<String, Integer> res = new HashMap<>();
        for(String inp : input) {
            for(String s : inp.split("")) {
                int v = res.getOrDefault(s, 0)+1;
                res.put(s, v);
            }
        }
        return res;
    }

    public String getKey(Map<String, Integer> m, Integer value) {
        for(Map.Entry<String, Integer> e : m.entrySet()){
            if(e.getValue() == value)
                return e.getKey();
        }
        return null;
    }

    private Map<String, String> mapToSegmentTakeTwo(List<String> inputs) {
        Map<String, String> finalMapping = new HashMap<>();

        Map<Integer, List<String>> lengthMapping = new HashMap<>();
        for (String input : inputs) {
            int l = input.length();
            List<String> s = lengthMapping.getOrDefault(l, new ArrayList<>());
            s.add(input);
            lengthMapping.put(l, s);
        }

        // get the 1 and 7 to work out segment a
        String ais = remainder(lengthMapping.get(3).get(0), lengthMapping.get(2).get(0));
        finalMapping.put(ais, "a");

        // workout segment c
        String cIs = null;
        for(String s : lengthMapping.get(6)) {
            String remainder = remainder(lengthMapping.get(2).get(0), s);
            if(remainder.length() == 1) {
                cIs = remainder;
                finalMapping.put(cIs, "c");
                break;
            }
        }

        // segment f
        finalMapping.put(remainder(lengthMapping.get(2).get(0), cIs), "f");

        // Other segments appear a unique number of times in reminding characters
        List<String> all = new ArrayList<>();
        all.addAll(lengthMapping.get(5));
        all.addAll(lengthMapping.get(6));

        Map<String, Integer> count3 = countOccurances(all);
        count3.keySet().removeAll(finalMapping.keySet());
        finalMapping.put(getKey(count3, 3), "e");
        finalMapping.put(getKey(count3, 5), "d");
        finalMapping.put(getKey(count3, 4), "b");
        finalMapping.put(getKey(count3, 6), "g");

        return finalMapping;
    }

    public int computeCorrectOutput(Map<String, String> mapping, List<String> inputs) {
        String result = "";

        for(String inp : inputs) {
            String number = "";
            for(String seg : inp.split("")) {
                number += mapping.get(seg);
            }
            result += segsToNumber(number);
        }

        return Integer.parseInt(result);
    }

    private String segsToNumber(String number) {

        Set<String> segs = Set.of(number.split(""));

        for(Map.Entry<String, Set<String>> e : numberMap.entrySet()) {
            if(e.getValue().size() == segs.size()) {
                if(segs.containsAll(e.getValue()))
                    return e.getKey();
            }
        }
        return null;
    }

    private Integer addAll(List<Row> rows) {
        return rows.stream()
                .map(row -> computeCorrectOutput(mapToSegmentTakeTwo(row.inputs), row.outputs))
                .reduce(0, Integer::sum);
    }

    /**
     * Finally got it working, not going to re-do the code, already spent too much time on it!
     */
    @Test
    public void testPart2() {
        Row row = new Row(
                List.of("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab"),
                List.of("cdfeb", "fcadb", "cdfeb", "cdbaf"));

        assertEquals(5353, computeCorrectOutput(mapToSegmentTakeTwo(row.inputs), row.outputs));

        assertEquals(61229, addAll(loadData("src/test/resources/2021/D8_t.txt")));
        assertEquals(987553, addAll(loadData("src/test/resources/2021/D8.txt")));
    }

}

