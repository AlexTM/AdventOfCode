package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D16 {

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

    record Person(int remainingTime, String pos){};

    private int goDeep(final Map<String, Edge> m,
                       final Map<String, Map<String, Integer>> dist,
                       final Person me,
                       final Person elephant,
                       final Set<String> remaining) {

//        Person current = me.remainingTime > elephant.remainingTime ? me : elephant;
        Person current = me;

        String pos = current.pos;
        int remainingTime = current.remainingTime;

        Map<String, Integer> distFromPos = dist.get(pos);
        Edge node = m.get(pos);

        Set<String> next = new HashSet<>(remaining);
        next.remove(pos);

        int impact = 0;
        // must account for speical case "AA"
        if(node.rate != 0) {
            remainingTime--;
            impact += remainingTime * node.rate;
        }
        final int rem = remainingTime;

        return impact + next.stream().filter(p -> distFromPos.get(p) < rem)
                .mapToInt(p -> goDeep(m, dist,
                        new Person(rem - distFromPos.get(p), p),
                        elephant, next))
                .max().orElse(0);
    }


    private int calculateDistanceFromNode(final Map<String, Edge> m, final int remainingTime) {

        // Get dist to all nodes from every other node
        Map<String, Map<String, Integer>> dist =
                m.keySet().stream().collect(
                        Collectors.toMap(Function.identity(), s -> doDijkstra(m , s)));

        Set<String> nonZero = m.values().stream()
                .filter(s -> s.rate != 0).map(s -> s.name).collect(Collectors.toSet());
        nonZero.add("AA");

        // do depth
        return goDeep(m, dist, new Person(remainingTime, "AA"),
                new Person(remainingTime, "AA"), nonZero);
    }


    @Test
    public void testValves() {
        assertEquals(1651,
                calculateDistanceFromNode(
                        FileReader.readFileForObject("src/test/resources/2022/D16_t.txt", new HashMap<>(), D16::parseLine)
                        ,30
                ));

        assertEquals(1584,
                calculateDistanceFromNode(
                        FileReader.readFileForObject("src/test/resources/2022/D16.txt", new HashMap<>(), D16::parseLine)
                        ,30
                ));
    }

    @Test
    public void testValvesWithTwo() {
        assertEquals(1707,
                calculateDistanceFromNode(
                        FileReader.readFileForObject("src/test/resources/2022/D16_t.txt", new HashMap<>(), D16::parseLine)
                        ,26
                ));

//        assertEquals(1584,
//                calculateDistanceFromNode(
//                        FileReader.readFileForObject("src/test/resources/2022/D16.txt", new HashMap<>(), D16::parseLine)
//                ));
    }

}
