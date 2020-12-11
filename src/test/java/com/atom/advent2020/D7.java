package com.atom.advent2020;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 7: Handy Haversacks ---
 * You land at the regional airport in time for your next flight. In fact, it looks like you'll even have time to grab
 * some food: all flights are currently delayed due to issues in luggage processing.
 *
 * Due to recent aviation regulations, many rules (your puzzle input) are being enforced about bags and their contents;
 * bags must be color-coded and must contain specific quantities of other color-coded bags. Apparently, nobody
 * responsible for these regulations considered how long they would take to enforce!
 *
 * For example, consider the following rules:
 *
 * light red bags contain 1 bright white bag, 2 muted yellow bags.
 * dark orange bags contain 3 bright white bags, 4 muted yellow bags.
 * bright white bags contain 1 shiny gold bag.
 * muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
 * shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
 * dark olive bags contain 3 faded blue bags, 4 dotted black bags.
 * vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
 * faded blue bags contain no other bags.
 * dotted black bags contain no other bags.
 * These rules specify the required contents for 9 bag types. In this example, every faded blue bag is empty, every
 * vibrant plum bag contains 11 bags (5 faded blue and 6 dotted black), and so on.
 *
 * You have a shiny gold bag. If you wanted to carry it in at least one other bag, how many different bag colors would
 * be valid for the outermost bag? (In other words: how many colors can, eventually, contain at least one shiny gold
 * bag?)
 *
 * In the above rules, the following options would be available to you:
 *
 * A bright white bag, which can hold your shiny gold bag directly.
 * A muted yellow bag, which can hold your shiny gold bag directly, plus some other bags.
 * A dark orange bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny
 * gold bag.
 * A light red bag, which can hold bright white and muted yellow bags, either of which could then hold your shiny
 * gold bag.
 * So, in this example, the number of bag colors that can eventually contain at least one shiny gold bag is 4.
 *
 * How many bag colors can eventually contain at least one shiny gold bag? (The list of rules is quite long; make sure
 * you get all of it.)
 *
 * --- Part Two ---
 * It's getting pretty expensive to fly these days - not because of ticket prices, but because of the ridiculous number
 * of bags you need to buy!
 *
 * Consider again your shiny gold bag and the rules from the above example:
 *
 * faded blue bags contain 0 other bags.
 * dotted black bags contain 0 other bags.
 * vibrant plum bags contain 11 other bags: 5 faded blue bags and 6 dotted black bags.
 * dark olive bags contain 7 other bags: 3 faded blue bags and 4 dotted black bags.
 * So, a single shiny gold bag must contain 1 dark olive bag (and the 7 bags within it) plus 2 vibrant plum bags (and
 * the 11 bags within each of those): 1 + 1*7 + 2 + 2*11 = 32 bags!
 *
 * Of course, the actual rules have a small chance of going several levels deeper than this example; be sure to count
 * all of the bags, even if the nesting becomes topologically impractical!
 *
 * Here's another example:
 *
 * shiny gold bags contain 2 dark red bags.
 * dark red bags contain 2 dark orange bags.
 * dark orange bags contain 2 dark yellow bags.
 * dark yellow bags contain 2 dark green bags.
 * dark green bags contain 2 dark blue bags.
 * dark blue bags contain 2 dark violet bags.
 * dark violet bags contain no other bags.
 * In this example, a single shiny gold bag must contain 126 other bags.
 *
 * How many individual bags are required inside your single shiny gold bag?
 *
 */
public class D7 {

    class Rule {
        private String name;
        private Map<String, Integer> children = new HashMap<>();

        @Override
        public String toString() {
            return "Node{" +
                    "name='" + name + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    private String stripString(String inp) {
        return inp.replace("bags", "").replace("bag", "").replace(".", "").trim();
    }

    private List<Rule> readFile(String filename) throws FileNotFoundException {
        List<Rule> values = new ArrayList<>();
        try(Scanner in = new Scanner(new FileReader(filename))) {
            in.useDelimiter(System.getProperty("line.separator"));
            while (in.hasNext()) {
                String line = stripString(in.next());
                String parts[] = line.split("contain");

                Rule n = new Rule();
                n.name = parts[0].trim();

                if(!parts[1].trim().startsWith("no other")) {
                    String parts2[] = parts[1].split(",");
                    for(int i=0; i< parts2.length; i++) {
                        String trimString = parts2[i].trim();
                        int amount = Integer.parseInt(trimString.substring(0,1));
                        n.children.put(trimString.substring(1).trim(), amount);
                    }
                }

                values.add(n);
            }
            in.close();
            return values;
        }
    }

    private Map<String, Integer> getLookupMap(List<Rule> rules) {
        Map<String, Integer> lookup = new HashMap<>();
        for(int i=0; i<rules.size(); i++) {
            lookup.put(rules.get(i).name, i);
        }
        return lookup;
    }

    private Map<Integer, String> getReverseLookupMap(List<Rule> rules) {
        Map<Integer, String> lookup = new HashMap<>();
        for(int i=0; i<rules.size(); i++) {
            lookup.put(i, rules.get(i).name);
        }
        return lookup;
    }

    private int[][] buildGraph(List<Rule> rules) {
        int[][] m = new int[rules.size()][rules.size()];
        Map<String, Integer> l = getLookupMap(rules);

        for(Rule r : rules) {
            for(Map.Entry<String, Integer> e : r.children.entrySet()) {
                m[l.get(r.name)][l.get(e.getKey())] = e.getValue();
            }
        }
        return m;
    }

    @Test
    public void testLoadingBags() throws FileNotFoundException {
        List<Rule> rules = readFile("src/test/resources/dayseven_test.txt");
        rules.stream().forEach(System.out::println);
        assertEquals(9, rules.size());
        assertEquals(9, rules.get(3).children.get("faded blue"));

        int[][] m = buildGraph(rules);
        // output rule set from matrix
        Map<Integer, String> rev = getReverseLookupMap(rules);
        for(int i=0; i<m.length; i++) {
            System.out.print(rev.get(i)+" :");
            for(int j=0; j<m[0].length; j++) {
                if(m[i][j] != 0) {
                    System.out.print(rev.get(j)+" "+m[i][j]+", ");
                }
            }
            System.out.println("");
        }

    }

    @Test
    public void testIterateOverTreeBags() throws FileNotFoundException {
        List<Rule> rules = readFile("src/test/resources/dayseven_test.txt");
        int[][] m = buildGraph(rules);
        Map<String, Integer> l = getLookupMap(rules);
        Map<Integer, String> rev = getReverseLookupMap(rules);

        Integer sg = l.get("shiny gold");

        Set<String> results = new HashSet<>();
        keepGoing(m, sg, rev, results);
        assertEquals(4, results.size());


        rules = readFile("src/test/resources/dayseven.txt");
        m = buildGraph(rules);
        l = getLookupMap(rules);
        rev = getReverseLookupMap(rules);

        sg = l.get("shiny gold");

        results = new HashSet<>();
        keepGoing(m, sg, rev, results);
        System.out.println(results.size());

    }

    private void keepGoing(int[][] m, int sg, Map<Integer, String> rev, Set<String> results) {
        for(int i=0; i<m[sg].length; i++) {
            if(m[i][sg] > 0) {
                results.add(rev.get(i));
                keepGoing(m, i, rev, results);
            }
        }
    }


    @Test
    public void testIterateOverTreeBags2() throws FileNotFoundException {
        List<Rule> rules = readFile("src/test/resources/dayseven_test.txt");
        int[][] m = buildGraph(rules);
        Map<String, Integer> l = getLookupMap(rules);
        Map<Integer, String> rev = getReverseLookupMap(rules);

        Integer sg = l.get("shiny gold");

        int bags = keepGoing2(m, sg)-1;
        assertEquals(32, bags);


        rules = readFile("src/test/resources/dayseven.txt");
        m = buildGraph(rules);
        l = getLookupMap(rules);

        sg = l.get("shiny gold");
        bags = keepGoing2(m, sg)-1;
        System.out.println(bags);
    }

    private int keepGoing2(int[][] m, int sg) {
        int bags = 1;
        for(int i=0; i<m[sg].length; i++) {
            if(m[sg][i] > 0) {
                bags += keepGoing2(m, i) * m[sg][i];
            }
        }
        return bags;
    }

}
