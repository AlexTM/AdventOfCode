package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D16 {

    record Edge(String name, int rate, List<String> edges){};

    // Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    private static Pattern p = Pattern.compile("Valve (..) has flow rate=(\\d+); tunnel(s?) lead(s?) to valve(s?) (.*)");

    private static Map<String, Edge> parseLine(String line, Map<String, Edge> sensors) {
        Matcher m = p.matcher(line);
        while(m.find()) {
            List<String> cons = Arrays.stream(m.group(6).split(",")).map(String::trim).toList();
            sensors.put(m.group(1), new Edge(m.group(1), Integer.parseInt(m.group(2)), cons));
        }
        return sensors;
    }


    public HashMap<String, Integer> doDijkstra(Map<String, Edge> map, String start) {

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

    private int goDeep(final Map<String, Edge> m,
                       final Map<String, Map<String, Integer>> dist,
                       int remainingTime,
                       String pos,
                       final Set<String> remaining) {

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

        int max = 0;
        for(String p : remaining) {
            if(distFromPos.get(p) < remainingTime) {
                max = Math.max(max,
                                goDeep(m, dist, remainingTime - distFromPos.get(p), p, next));
            }
        }
        return impact+max;
    }


    private int calculateDistanceFromNode(final Map<String, Edge> m) {
        Map<String, Map<String, Integer>> dist = new HashMap<>();

        // Get dist to all nodes from every other node
        for(String s : m.keySet())
            dist.put(s, doDijkstra(m, s));

        Set<String> nonZero = m.values().stream().filter(s -> s.rate != 0).map(s -> s.name).collect(Collectors.toSet());
        nonZero.add("AA");

        // do depth
        return goDeep(m, dist, 30, "AA", nonZero);
    }


    // FIXME not working
    @Test
    public void testValves() {
        Map<String, Edge> m = FileReader.readFileForObject("src/test/resources/2022/D16_t.txt", new HashMap<>(), D16::parseLine);
        System.out.println(m);
        assertEquals(1651, calculateDistanceFromNode(m));
    }
}
