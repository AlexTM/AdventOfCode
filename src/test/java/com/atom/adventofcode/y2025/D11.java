package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D11 {
    private static final String TEST_INPUT = """
        aaa: you hhh
        you: bbb ccc
        bbb: ddd eee
        ccc: ddd eee fff
        ddd: ggg
        eee: out
        fff: out
        ggg: out
        hhh: ccc fff iii
        iii: out
        """;

    private Map<String, List<String>> parseInput(String input) {
        Map<String, List<String>> results = new HashMap<>();
        String[] split = input.split("\n");

        for(String s : split) {
            String[] split2 = s.split(": ", 2);
            String[] split3 = split2[1].split(" ");
            results.put(split2[0], Arrays.asList(split3));
        }

        return results;
    }

    public List<String> getCandidates(Map<String, List<String>> map, String name) {
        return map.getOrDefault(name, Collections.emptyList());
    }

    public HashMap<String, Integer> doDijkstra(Map<String, List<String>> map, String start) {

        Set<String> visited = new HashSet<>();
        HashMap<String, Integer> distanceFromStart = new HashMap<>();
        distanceFromStart.put(start, 0);

        Queue<String> toVisit = new PriorityQueue<>(Comparator.comparing(distanceFromStart::get));
        toVisit.add(start);

        while(!toVisit.isEmpty()) {

            String currentNode = toVisit.poll();

            // check if we have already visited
            if(visited.contains(currentNode))
                continue;

            visited.add(currentNode);

            // get all edges
            List<String> eList = getCandidates(map, currentNode);
            for (String p : eList) {
                int newDistance = distanceFromStart.get(currentNode) + 1;
                if (newDistance < distanceFromStart.getOrDefault(p, Integer.MAX_VALUE)) {
                    distanceFromStart.put(p, newDistance);
                    toVisit.add(p);
                }
            }
        }

        return distanceFromStart;
    }

    private int countValid(Map<String, List<String>> map, Map<String, Integer> distances, String pos) {

        if(pos.equals("out")) return 1;

        int c = 0;
        for(String p : getCandidates(map, pos)) {
            if(distances.get(p) - distances.get(pos) == 1) {
                // this is valid path
                c += countValid(map, distances, p);
            }
        }

        return c;
    }

    private int countValid(Map<String, List<String>> map, String pos, Set<String> visited) {
        if(pos.equals("out")) return 1;

        int c = 0;
        for(String p : getCandidates(map, pos)) {
            // this is valid path
            if(visited.contains(p)) continue;
            Set<String> v = new HashSet<>(visited);
            v.add(p);
            c += countValid(map, p, v);
        }

        return c;
    }

    @Test
    public void testPart1() {
        var m = parseInput(TEST_INPUT);
        assertEquals(5, countValid(m, "you", Set.of()));
        m = parseInput(FileReader.readFileString("src/test/resources/2025/D11.txt"));
        assertEquals(724, countValid(m, "you", Set.of()));
    }

    private Map<String, List<String>> reverseLinks(Map<String, List<String>> map) {
        Map<String, List<String>> result = new HashMap<>();
        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            for(String p : entry.getValue()) {
                result.computeIfAbsent(p, k -> new ArrayList<>()).add(entry.getKey());
            }
        }
        return result;
    }

    private int countValid2(Map<String, List<String>> map, String pos, Set<String> visited, Map<String, Integer> cache) {
        if (pos.equals("svr")) {
            if(visited.contains("dac") && visited.contains("fft")) {
                return 1;
            }
            return 0;
        }

        if(cache.containsKey(pos)) {
            return cache.get(pos);
        }

        int c = 0;
        for (String p : getCandidates(map, pos)) {
            // this is valid path
            if (visited.contains(p)) continue;
            Set<String> v = new LinkedHashSet<>(visited);
            v.add(p);
            c += countValid2(map, p, v, cache);
        }

        cache.put(pos, c);

        return c;
    }

    @Test
    public void testPart2() {
        var m = parseInput("""
                svr: aaa bbb
                aaa: fft
                fft: ccc
                bbb: tty
                tty: ccc
                ccc: ddd eee
                ddd: hub
                hub: fff
                eee: dac
                dac: fff
                fff: ggg hhh
                ggg: out
                hhh: out
                """);

        m = reverseLinks(m);

        assertEquals(2, countValid2(m, "out", Set.of(), new HashMap<>()));

        m = parseInput(FileReader.readFileString("src/test/resources/2025/D11.txt"));
        assertEquals(0, countValid2(m, "svr", Set.of(), new HashMap<>()));
    }
}
