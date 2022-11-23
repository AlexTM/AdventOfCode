package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D13 {

    private final String testInput =
            "Alice would gain 54 happiness units by sitting next to Bob.\n" +
            "Alice would lose 79 happiness units by sitting next to Carol.\n" +
            "Alice would lose 2 happiness units by sitting next to David.\n" +
            "Bob would gain 83 happiness units by sitting next to Alice.\n" +
            "Bob would lose 7 happiness units by sitting next to Carol.\n" +
            "Bob would lose 63 happiness units by sitting next to David.\n" +
            "Carol would lose 62 happiness units by sitting next to Alice.\n" +
            "Carol would gain 60 happiness units by sitting next to Bob.\n" +
            "Carol would gain 55 happiness units by sitting next to David.\n" +
            "David would gain 46 happiness units by sitting next to Alice.\n" +
            "David would lose 7 happiness units by sitting next to Bob.\n" +
            "David would gain 41 happiness units by sitting next to Carol.\n";

    record Rule(String persona, String personb, int happiness){};

    private static Rule parseRules(String input) {
        String[] split = input.split(" ");
        return new Rule(
                split[0],
                split[10].substring(0, split[10].length()-1),
                split[2].equalsIgnoreCase("lose") ? -1*Integer.parseInt(split[3]) : Integer.parseInt(split[3])
        );
    }

    private Set<String> getNeighbours(List<String> people, int i) {
        Set<String> neighbours = new HashSet<>();
        if(i == 0)
            neighbours.add(people.get(people.size()-1));
        else
            neighbours.add(people.get(i-1));

        if(i == people.size()-1)
            neighbours.add(people.get(0));
        else
            neighbours.add(people.get(i+1));

        return neighbours;
    }

    private int computeHappiness(List<String> people, Map<String, List<Rule>> ruleMap) {

        int happiness = 0;
        for(int i=0; i<people.size(); i++) {

            Set<String> neighbours = getNeighbours(people, i);

            for (Rule rule : ruleMap.get(people.get(i))) {
                if(neighbours.contains(rule.personb)) {
                    happiness += rule.happiness;
                }
            }
        }
        return happiness;
    }

    private Map<String, List<Rule>> ruleMap(List<Rule> ruleList) {
        Map<String, List<Rule>> ruleMap = new HashMap<>();
        for(Rule rule : ruleList) {
            List<Rule> m = ruleMap.getOrDefault(rule.persona, new ArrayList<>());
            m.add(rule);
            ruleMap.put(rule.persona, m);
        }
        return ruleMap;
    }

    private int findOptimalSeating(Map<String, List<Rule>> ruleMap) {
        return findOptimalSeatingRec(ruleMap,
                new ArrayList<>(), new ArrayList<>(ruleMap.keySet()));
    }

    private int findOptimalSeatingRec(Map<String, List<Rule>> ruleMap, List<String> order, List<String> guests) {
        if(guests.isEmpty()) {
            return computeHappiness(order, ruleMap);
        }

        // TODO change to more efficient (not creating lists)
        int max = 0;
        for(String guest : guests) {
            List<String> newGuestList = new ArrayList<>(guests);
            newGuestList.remove(guest);
            List<String> newOrder = new ArrayList<>(order);
            newOrder.add(guest);
            max = Math.max(max, findOptimalSeatingRec(ruleMap, newOrder, newGuestList));
        }
        return max;
    }

    @Test
    public void testHappiness() {
        List<Rule> ruleList = FileReader.readObjectList(testInput, D13::parseRules);
        Map<String, List<Rule>> ruleMap = ruleMap(ruleList);
        assertEquals(330, computeHappiness(List.of("David", "Alice", "Bob", "Carol"), ruleMap));
        assertEquals(330, findOptimalSeating(ruleMap));

        List<Rule> ruleList2 = FileReader.readFileObjectList("src/test/resources/2015/D13.txt", D13::parseRules);
        Map<String, List<Rule>> ruleMap2 = ruleMap(ruleList2);
        assertEquals(664, findOptimalSeating(ruleMap2));

        ruleList2.add(new Rule("me", "me", 0));
        ruleMap2 = ruleMap(ruleList2);
        assertEquals(640, findOptimalSeating(ruleMap2));
    }

}
