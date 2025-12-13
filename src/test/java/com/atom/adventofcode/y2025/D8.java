package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D8 {
    private static final String TEST_INPUT = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """;

    record Pos(long x, long y, long z){}

    private List<Pos> parseInput(String input) {
        String[] rows = input.split("\n");
        List<Pos> positions = new ArrayList<>();
        for(String row : rows) {
            String[] split = row.split(",");
            positions.add(new Pos(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2])));
        }
        return positions;
    }

    private long calculateEuclideanDistance(Pos p1, Pos p2) {
        return (long)Math.sqrt(Math.pow(p1.x - p2.x, 2)
                        + Math.pow(p1.y - p2.y, 2)
                        + Math.pow(p1.z - p2.z, 2));
    }

    private long[][] calculateAllDistances(List<Pos> positions) {
        long[][] distances = new long[positions.size()][positions.size()];
        for(int i=0; i<positions.size(); i++) {
            for(int j=i+1; j<positions.size(); j++) {
                distances[i][j] = calculateEuclideanDistance(positions.get(i), positions.get(j));
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }

    record Pair(Pos a, Pos b){};

    private Map<Long, List<Pair>> orderByDistance(List<Pos> positions) {

        var map = new TreeMap<Long, List<Pair>>();
        for (var i = 0; i < positions.size(); i++) {
            for (var j = i + 1; j < positions.size(); j++) {
                var d = calculateEuclideanDistance(positions.get(i), positions.get(j));
                map.computeIfAbsent(d, k -> new ArrayList<>())
                        .add(new Pair(positions.get(i), positions.get(j)));
            }
        }
//        System.out.println(map);
        return map;
    }

    private Map<Pos, Integer> connectUpCircuits(Map<Long, List<Pair>> order) {
        int nextCircuit = 0;
        Map<Pos, Integer> posToCircuit = new HashMap<>();
        List<Long> keys = new ArrayList<>(order.keySet());
        for(int i=0; i<10; i++) {
            List<Pair> pairs = order.get(keys.get(i));
            if(pairs.size() > 1) {
                System.out.println("Multiple pairs!!!!!");
                System.exit(1);
            }
            Pair p = pairs.get(0);
            System.out.println(p);

            if(posToCircuit.containsKey(p.a) && posToCircuit.containsKey(p.b))
                continue;

            // find circuit
            int nc = posToCircuit.getOrDefault(p.a, posToCircuit.getOrDefault(p.b, -1));
            if(nc == -1) {
                nc = nextCircuit++;
            }
            posToCircuit.put(p.a, nc);
            posToCircuit.put(p.b, nc);
            System.out.println(p.a +" -> " + nc);
            System.out.println(p.b +" -> " + nc);
        }

        System.out.println(posToCircuit);
        return posToCircuit;
    }

    private long calculateCableNeeded(Map<Pos, Integer> posToCircuit) {
        // frequency map
        Map<Integer, Long> freqMap = new HashMap<>();
        for(int v : posToCircuit.values()) {
            freqMap.put(v, freqMap.getOrDefault(v, 0L) + 1);
        }
        Map<Integer, Long> sortedMap = new TreeMap<>(freqMap);

        int c = 0;
        long result = 1;
        for(Map.Entry<Integer, Long> e : sortedMap.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
            result *= e.getValue();
            if(++c == 3)
                break;
        }

        return result;
    }

    @Test
    public void testDay8() {
        List<Pos> positions = parseInput(TEST_INPUT);
        Map<Long, List<Pair>> order = orderByDistance(positions);
        Map<Pos, Integer> ci = connectUpCircuits(order);
        assertEquals(40, calculateCableNeeded(ci));


        //distances = calculateAllDistances(parseInput(FileReader.readFileString("src/test/resources/2025/D8.txt")));
    }
}
