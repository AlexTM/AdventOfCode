package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

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
        assertEquals(530, countUnique(loadData("src/test/resources/2021/D8.txt")));
    }

    private int sumOutputs(List<Row> loadData) {
        return 0;
    }


    private Set<String> stringToSet(String inp) {
        return Arrays.stream(inp.split("")).collect(Collectors.toSet());
    }


    /**
     * Not getting it
     *
     * Come back to this some other time!
     *
     */
    private void mapToSegment(Row row) {

        Map<String, Map<String, Integer>> count = new HashMap<>();

        Map<Integer, Set<String>> lengthToSegment = new HashMap<>();

        for(Map.Entry<Integer, Set<String>> e : numberMap.entrySet()) {
            Set<String> s = lengthToSegment.getOrDefault(e.getValue().size(), new HashSet<>());
            s.addAll(e.getValue());
            lengthToSegment.put(e.getValue().size(), s);
        }

        System.out.println(lengthToSegment);

        for(String data : row.inputs) {
            int len = data.length();
            Set<String> segments = lengthToSegment.get(len);

            for(String seg : segments) {
                Map<String, Integer> i = count.getOrDefault(seg, new HashMap<>());
                Set<String> ch = stringToSet(data);
                for(String c : ch) {
                    i.put(c, i.getOrDefault(c, 0)+1);
                }
                count.put(seg, i);
            }
        }

        System.out.println(count);

        Map<String, List<String>> candidates = new HashMap<>();
        for(Map.Entry<String, Map<String, Integer>> e : count.entrySet()) {
            Optional<Map.Entry<String, Integer>> maxEntry  = e.getValue().entrySet()
                    .stream().max(Map.Entry.comparingByValue()
            );

            candidates.put(e.getKey(),
                    e.getValue().entrySet().stream().filter(ee->ee.getValue() == maxEntry.get().getValue())
                            .map(Map.Entry::getKey).collect(Collectors.toList())
            );
        }

        System.out.println(candidates);

        Map<String, String> finalMapping = new HashMap<>();

        while(finalMapping.size() != 7) {
            boolean finished = true;
            Iterator<String> it = candidates.keySet().iterator();
            while(it.hasNext()) {
                String s = it.next();
                if(candidates.get(s).size() == 1) {


                    String m = candidates.get(s).get(0);
                    finalMapping.put(s, m);

                    System.out.println(candidates);
                    System.out.println("Removing "+s+" -> "+m);


                    // remove from rest of candidates
                    candidates.remove(s);
                    candidates.forEach((k, v) -> v.remove(m));
                    break;
                }
            }
        }
        System.out.println(finalMapping);
    }


    static final Map<Integer, Set<String>> numberMap;
    static {
        numberMap = new HashMap<>();
        numberMap.put(0, Set.of("abcefg".split("")));
        numberMap.put(1, Set.of("cf".split("")));
        numberMap.put(2, Set.of("acdeg".split("")));
        numberMap.put(3, Set.of("acdfg".split("")));
        numberMap.put(4, Set.of("bcdf".split("")));
        numberMap.put(5, Set.of("abdfg".split("")));
        numberMap.put(6, Set.of("abdefg".split("")));
        numberMap.put(7, Set.of("acf".split("")));
        numberMap.put(8, Set.of("abcdefg".split("")));
        numberMap.put(9, Set.of("abcdfg".split("")));
    }

    @Test
    public void testPart2() {
        mapToSegment(
                new Row(
                        List.of("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab"),
                        List.of("cdfeb", "fcadb", "cdfeb", "cdbaf")));


//        mapToSegment(
//            new Row(
//                    List.of("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab"),
//                    List.of("cdfeb", "fcadb", "cdfeb", "cdbaf")));
        //assertEquals(61229, sumOutputs(loadData("src/test/resources/2021/D8_t.txt")));
        //assertEquals(530, countUnique(loadData("src/test/resources/2021/D8.txt")));
    }

}

