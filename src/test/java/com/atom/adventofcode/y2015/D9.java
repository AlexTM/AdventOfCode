package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D9 {

    private String testInp =
            "London to Dublin = 464\n" +
            "London to Belfast = 518\n" +
            "Dublin to Belfast = 141";

    record Distance(String start, String end, int distance){};

    private static Distance parseString(String s) {
        String[] split = s.split(" ");
        return new Distance(split[0].trim(), split[2].trim(), Integer.parseInt(split[4].trim()));
    }

    record Edge(String to, int distance){};

    public Integer doDijkstra(String start, List<String> nodes, HashMap<String, List<Edge>> edges) {

        Set<String> visited = new HashSet<>();

        // Set all distance to MAX with exception of start
        HashMap<String, Integer> distanceFromStart = new HashMap<>();
        for(String i : nodes) distanceFromStart.put(i, Integer.MAX_VALUE);
        distanceFromStart.put(start, 0);

        // Visit start location
        Queue<String> toVisit = new PriorityQueue<>(
                Comparator.comparing(distanceFromStart::get));

        toVisit.add(start);

        while(!toVisit.isEmpty()) {

            String currentNode = toVisit.poll();

            // check if we have already visited
            if(visited.contains(currentNode))
                continue;

            visited.add(currentNode);

            // get all edges
            List<Edge> eList = edges.get(currentNode);
            if(eList != null) {
                for (Edge e : eList) {
                    int newDistance = e.distance + distanceFromStart.get(currentNode);
                    if (newDistance < distanceFromStart.get(e.to)) {
                        distanceFromStart.put(e.to, newDistance);
                        toVisit.add(e.to);
                    }
                }
            }
        }

        distanceFromStart.forEach((key, value) -> System.out.println(key + ": " + value));

        return Collections.max(distanceFromStart.entrySet(),
                Map.Entry.comparingByValue()).getValue();
    }

    @Test
    public void testGetShortestDistance() {
        List<Distance> distances = FileReader.readObjectList(testInp, D9::parseString);

        Set<String> nodes = new HashSet<>();
        for(Distance d : distances) {
            nodes.add(d.start);
            nodes.add(d.end);
        }
        HashMap<String, List<Edge>> edges = new HashMap<>();
        for(Distance d : distances) {
            var edgesList = edges.getOrDefault(d.start, new ArrayList<>());
            edgesList.add(new Edge(d.end, d.distance));
            edges.put(d.start, edgesList);

            edgesList = edges.getOrDefault(d.end, new ArrayList<>());
            edgesList.add(new Edge(d.start, d.distance));
            edges.put(d.end, edgesList);
        }

        assertEquals(605, doDijkstra("London", new ArrayList<>(nodes), edges));
    }
}
