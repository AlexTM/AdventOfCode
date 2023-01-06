package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D16 {

    record Person(int remainingTime, String pos){};
    record Edge(String name, int rate, List<String> edges){};

    // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    private static Pattern p =
            Pattern.compile("Valve (..) has flow rate=(\\d+); tunnel(s?) lead(s?) to valve(s?) (.*)");

    private static Map<String, Edge> parseLine(String line, Map<String, Edge> sensors) {
        Matcher m = p.matcher(line);
        while(m.find()) {
            List<String> cons = Arrays.stream(m.group(6).split(",")).map(String::trim).toList();
            sensors.put(m.group(1), new Edge(m.group(1), Integer.parseInt(m.group(2)), cons));
        }
        return sensors;
    }


    public HashMap<String, Integer> doDijkstra(final Map<String, Edge> map, final String start) {

        HashMap<String, Integer> distanceFromStart = new HashMap<>();
        distanceFromStart.put(start, 0);

        Queue<String> toVisit = new PriorityQueue<>(
                Comparator.comparing(o -> distanceFromStart.getOrDefault(o, Integer.MAX_VALUE)));
        toVisit.add(start);

        while(!toVisit.isEmpty()) {

            String currentNode = toVisit.poll();
            Edge node = map.get(currentNode);

            List<String> candidates = node.edges;
            for (String e : candidates) {
                int newDistance = 1 + distanceFromStart.getOrDefault(currentNode, Integer.MAX_VALUE);
                if (newDistance < distanceFromStart.getOrDefault(e, Integer.MAX_VALUE)) {
                    distanceFromStart.put(e, newDistance);
                    toVisit.add(e);
                }
            }
        }

        return distanceFromStart;
    }


    private int depthFirstSearch(final Map<String, Edge> map,
                                 final Map<String, Map<String, Integer>> distances,
                                 final List<Person> people,
                                 final Set<String> remaining) {

        int biggerMax = 0;
        for (int i = 0; i < people.size(); i++) {
            final Person current = people.get(i);
            final Edge node = map.get(current.pos);
            final Map<String, Integer> distFromPos = distances.get(current.pos);

            int max = 0;
            for (String p : remaining) {
                if (distFromPos.get(p)+1 < current.remainingTime) {
                    List<Person> newList = new ArrayList<>(people);
                    newList.set(i, new Person(current.remainingTime - distFromPos.get(p) - 1, p));
                    Set<String> next = remaining.stream().filter(s -> !s.equals(p)).collect(Collectors.toSet());
                    max = Math.max(max, depthFirstSearch(map, distances, newList, next));
                }
            }

            biggerMax = Math.max(biggerMax, max+(current.remainingTime * node.rate));
        }
        return biggerMax;
    }


    private int calculateDistanceFromNode(
            final Map<String, Edge> m, int starters, int time) {

        // Get dist to all nodes from every other node
        Map<String, Map<String, Integer>> dist =
                m.keySet().stream().collect(
                        Collectors.toMap(Function.identity(), s -> doDijkstra(m , s)));

        // Only care about the non-zero nodes
        Set<String> nonZero = m.values().stream()
                .filter(s -> s.rate != 0).map(s -> s.name).collect(Collectors.toSet());

        List<Person> people = IntStream.range(0, starters).mapToObj(i ->
                new Person(time, "AA")).toList();

        return depthFirstSearch(m ,dist, people, nonZero);
    }

    @Test
    public void testValves() {

        assertEquals(1651,
                calculateDistanceFromNode(
                        FileReader.readFileForObject("src/test/resources/2022/D16_t.txt", new HashMap<>(), D16::parseLine)
                        , 1, 30
                ));

        assertEquals(1584,
                calculateDistanceFromNode(
                        FileReader.readFileForObject("src/test/resources/2022/D16.txt", new HashMap<>(), D16::parseLine)
                        , 1, 30
                ));
    }

    @Test
    public void testValvesWithTwo() {
        assertEquals(1707,
                calculateDistanceFromNode(
                        FileReader.readFileForObject("src/test/resources/2022/D16_t.txt", new HashMap<>(), D16::parseLine)
                        ,2, 26
                ));

//        assertEquals(0,
//                calculateDistanceFromNode(
//                        FileReader.readFileForObject("src/test/resources/2022/D16.txt", new HashMap<>(), D16::parseLine)
//                        ,2, 26
//                ));

    }

}
