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
    record Graph(List<String> nodes, HashMap<String, List<Edge>> edges){};

    // TODO redo
    private Graph generateGraph(List<Distance> distances) {
        Set<String> nodes = new HashSet<>();
        for(Distance d : distances) {
            nodes.add(d.start);
            nodes.add(d.end);
        }

        HashMap<String, List<Edge>> edges = new HashMap<>();
        for(Distance d : distances) {
            List<Edge> edgesList = edges.getOrDefault(d.start, new ArrayList<>());
            edgesList.add(new Edge(d.end, d.distance));
            edges.put(d.start, edgesList);

            edgesList = edges.getOrDefault(d.end, new ArrayList<>());
            edgesList.add(new Edge(d.start, d.distance));
            edges.put(d.end, edgesList);
        }

        // Create an extra start node with 1 directional links and zero distance
        // This means we can use the same function to check all the possible
        // start points
        List<Edge> edgesList = new ArrayList<>();
        for(String s : nodes) {
            edgesList.add(new Edge(s, 0));
        }
        edges.put("start", edgesList);
        nodes.add("start");

        return new Graph(new ArrayList<>(nodes), edges);
    }

    // brute force
    private int depthFirst(String start, int cost, int size,
                            HashMap<String, List<Edge>> edges,
                            Set<String> visited, boolean isMin) {

        if(visited.size() == size-1) return cost;

        Set<String> v = new HashSet<>(visited);
        v.add(start);

        int c = isMin ? Integer.MAX_VALUE : 0;
        for(Edge e : edges.getOrDefault(start, new ArrayList<>())) {
            if(!visited.contains(e.to)) {
                if(isMin)
                    c = Math.min(c, depthFirst(e.to, cost + e.distance, size, edges, v, true));
                else
                    c = Math.max(c, depthFirst(e.to, cost + e.distance, size, edges, v, false));
            }
        }
        return c;
    }

    @Test
    public void testGetShortestDistanceFullSearch() {
//        List<Distance> distances = FileReader.readObjectList(testInp, D9::parseString);
        List<Distance> distances = FileReader.readFileObjectList("src/test/resources/2015/D9.txt",
                D9::parseString);

        Graph graph = generateGraph(distances);

        int minCost = depthFirst("start",
                0, graph.nodes.size(), graph.edges, new HashSet<>(), true);
        assertEquals(117, minCost);


        int maxCost = depthFirst("start",
                0, graph.nodes.size(), graph.edges, new HashSet<>(), false);
        assertEquals(909, maxCost);
    }
}
