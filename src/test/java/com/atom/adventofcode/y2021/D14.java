package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D14 {

    record Pair(Character a, Character b){
        @Override
        public String toString() {
            return ""  + a + b;
        }
    };

    class Data {
        String polymerTemplate = null;
        Map<Pair, Character> rules = new HashMap<>();

        @Override
        public String toString() {
            return "Data{" +
                    "polymerTemplate='" + polymerTemplate + '\'' +
                    ", rules=" + rules +
                    '}';
        }
    }

    public Data loadData(String filename) {
        return FileReader.readFileForObject(filename, new Data(), (line, state) -> {
            if(!line.isEmpty()) {
                if(state.polymerTemplate == null) {
                    state.polymerTemplate = line;
                } else {
                    String[] parts = line.split(" -> ");
                    state.rules.put(new Pair(parts[0].charAt(0), parts[0].charAt(1)), parts[1].charAt(0));
                }
            }
            return state;
        });
    }

    public List<Character> step(List<Character> c, Map<Pair, Character> rules) {

        // Generate pairs
        Map<Integer, Pair> identifiedPairs = new HashMap<>();
        for(int i=0; i<c.size()-1; i++) {
            Pair p = new Pair(c.get(i), c.get(i+1));
            if(rules.containsKey(p)) {
                identifiedPairs.put(i, p);
            }
        }

        // Write new polymer
        List<Character> polymer = new LinkedList<>();
        for(int i=0; i<c.size(); i++) {
            polymer.add(c.get(i));
            if(identifiedPairs.containsKey(i)) {
                polymer.add(rules.get(identifiedPairs.get(i)));
            }
        }

        return polymer;
    }

    public List<Character> loop(List<Character> polymer, Map<Pair, Character> rules, int loops) {
        for(int i=0; i<loops; i++) {
            polymer = step(polymer, rules);
        }
        return polymer;
    }


    public Map<Character, Long> createFreqMap(List<Character> inp) {
        return inp.stream().collect(Collectors.groupingBy(
                Function.identity(),
                HashMap::new,
                Collectors.counting()));
    }

    public long getAnswer(String polymerIn, Map<Pair, Character> rules, int loops) {
        List<Character> inp = polymerIn.chars().mapToObj(c -> (char) c).toList();
        List<Character> polymer = loop(inp, rules, loops);
        Map<Character, Long> freqMap = createFreqMap(polymer);
        Long max = freqMap.values().stream().max(Comparator.naturalOrder()).orElseThrow(NoSuchElementException::new);
        Long min = freqMap.values().stream().min(Comparator.naturalOrder()).orElseThrow(NoSuchElementException::new);
        return max - min;
    }


    public Map<Pair, Long> countPairs(Map<Pair, Character> rules, List<Character> inp) {
        Map<Pair, Long> c = rules.keySet().stream().collect(Collectors.toMap(Function.identity(), p -> 0L));

        for(int i=0; i<inp.size()-1; i++) {
            Pair p = new Pair(inp.get(i), inp.get(i+1));
            if(c.containsKey(p)) {
                long v = c.getOrDefault(p, 0L);
                c.put(p, v+1);
            }
        }
        return c;
    }

    @Test
    public void testPart1() {
        Data testData = loadData("src/test/resources/2021/D14_t.txt");
        assertEquals(1588, getAnswer(testData.polymerTemplate, testData.rules, 10));

        Data puzzleData = loadData("src/test/resources/2021/D14.txt");
        assertEquals(3906, getAnswer(puzzleData.polymerTemplate, puzzleData.rules, 10));
    }

    /**
     * A lot easier
     */
    public Map<Pair, Long> stepMethod2(Map<Pair, Character> rules, Map<Pair, Long> inputs) {

        Map<Pair, Long> nextCount = new HashMap<>();
        for(Map.Entry<Pair, Character> ee : rules.entrySet()) {
            if(inputs.containsKey(ee.getKey())) {
                Character c = ee.getValue();
                Long times = inputs.get(ee.getKey());
                Pair p1 = new Pair(ee.getKey().a(), c);
                Pair p2 = new Pair(c, ee.getKey().b());
                nextCount.put(p1, nextCount.getOrDefault(p1, 0L) + times);
                nextCount.put(p2, nextCount.getOrDefault(p2, 0L) + times);
            }
        }

        return nextCount;
    }


    public long getAnswer2(String polymerTemplate, Map<Pair, Character> rules, int loops) {

        List<Character> inp = polymerTemplate.chars().mapToObj(c -> (char) c).toList();
        Map<Pair, Long> m = countPairs(rules, inp);

        for(int i=0; i<loops; i++) {
            m = stepMethod2(rules, m);
        }

/*
        Map<Character, Long> freqMap = new HashMap<>();
        var iterator = m.entrySet().iterator();
        var first = iterator.next();
        freqMap.put(first.getKey().a, freqMap.getOrDefault(first.getKey().a, 0L)+first.getValue());
        freqMap.put(first.getKey().b, freqMap.getOrDefault(first.getKey().b, 0L)+first.getValue());

        while(iterator.hasNext()) {
            var item = iterator.next();
            freqMap.put(item.getKey().b, freqMap.getOrDefault(item.getKey().b, 0L)+item.getValue());
        }
*/

        Map<Character, Long> freqMap = m.entrySet().stream().flatMap(e -> {
            List<Map.Entry<Character, Long>> l = new ArrayList<>();
//            l.add(new AbstractMap.SimpleEntry<>(e.getKey().a, e.getValue()));
            l.add(new AbstractMap.SimpleEntry<>(e.getKey().b, e.getValue()));
            return l.stream();
        }).collect(Collectors.groupingBy(
                Map.Entry::getKey,
                HashMap::new,
                Collectors.summingLong(Map.Entry::getValue)));

        Long max = freqMap.values().stream().max(Comparator.naturalOrder()).orElseThrow(NoSuchElementException::new);
        Long min = freqMap.values().stream().min(Comparator.naturalOrder()).orElseThrow(NoSuchElementException::new);
        return max - min;
    }

    @Test
    public void testPart2() {

        Data testData = loadData("src/test/resources/2021/D14_t.txt");

//        List<Character> inp = testData.polymerTemplate.chars().mapToObj(c -> (char) c).toList();
//        Map<Pair, Long> m2 = countPairs(testData.rules, inp);

/*
        for(int i=1; i<=10; i++) {
            Map<Pair, Long> m1 = countPairs(testData.rules, loop(inp, testData.rules, i));
            System.out.println(i+"A: " + m1);

            m2 = stepMethod2(testData.rules, m2);
            System.out.println(i+"B: "+m2);
            System.out.println("");

            for(Map.Entry<Pair, Long> e : m1.entrySet()) {
                if(!e.getValue().equals(m2.get(e.getKey()))) {
                    System.out.println(e.getKey() + " " + e.getValue() + " "+m2.get(e.getKey()));
                }
            }
        }
*/

        assertEquals(1588, getAnswer(testData.polymerTemplate, testData.rules, 10));
        assertEquals(1588, getAnswer2(testData.polymerTemplate, testData.rules, 10));
        assertEquals(2188189693529L, getAnswer2(testData.polymerTemplate, testData.rules, 40));


        Data puzzleData = loadData("src/test/resources/2021/D14.txt");
        // not 4441317262451
        assertEquals(0, getAnswer2(puzzleData.polymerTemplate, puzzleData.rules, 40));


//        List<Character> inp = testData.polymerTemplate.chars().mapToObj(c -> (char) c).toList();
/*
        Map<Pair, Long> c = countPairs(testData.rules, inp);

        for(int i=1; i<10; i++) {
            c = stepMethod2(testData.rules, c);
            System.out.println(c);
        }
        assertEquals(1588, getAnswer2(c));

        for(int i=1; i<40; i++) {
            c = stepMethod2(testData.rules, c);
        }
        assertEquals(2188189693529L, getAnswer2(c));
*/
    }

}
