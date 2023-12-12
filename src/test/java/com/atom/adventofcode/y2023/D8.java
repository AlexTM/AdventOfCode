package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class D8 {
    record Node(String name, String left, String right) {}

    class Data {
        private List<String> directions = new ArrayList<>();
        private Map<String, Node> nodes = new HashMap<>();
    }

    private static FileReader.TriFunction<Data, String, Integer, Data> tri = (data, line, i) -> {
        if (line.isBlank())
            return data;

        if (i == 0) {
            data.directions = Arrays.stream(line.split("")).toList();
        } else {
            String[] split = line.split("(\\W+)");
            data.nodes.put(split[0].trim(), new Node(split[0].trim(), split[1].trim(), split[2].trim()));
        }
        return data;
    };

    private static int followingInstructions(Map<String, Node> nodes, List<String> instructions) {
        return followingInstructions(nodes, instructions, nodes.get("AAA"), s -> s.equals("ZZZ"));
    }
    private static int followingInstructions(Map<String, Node> nodes, List<String> instructions, Node currentNode,
                                             Function<String, Boolean> endCondition) {
        int steps = 0;
        while(true) {
            for (String direction : instructions) {
                steps++;
                if (direction.equals("R")) {
                    currentNode = nodes.get(currentNode.right());
                } else {
                    currentNode = nodes.get(currentNode.left());
                }
                if (endCondition.apply(currentNode.name)) {
                    return steps;
                }
            }
        }
    }

    @Test
    public void partOne() {
        Data data = FileReader.parseStringForObject("""
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """, new Data(), tri);
        assertEquals(2, followingInstructions(data.nodes, data.directions));

        data = FileReader.parseStringForObject("""
                LLR
                                
                AAA = (BBB, BBB)
                BBB = (AAA, ZZZ)
                ZZZ = (ZZZ, ZZZ)
                """, new Data(), tri);
        assertEquals(6, followingInstructions(data.nodes, data.directions));

        data = FileReader.readFileForObject("src/main/resources/2023/D8.txt", new Data(), tri);
        assertEquals(21883, followingInstructions(data.nodes, data.directions));
    }

    private static int followingInstructions2(Map<String, Node> nodes, List<String> instructions) {

        Set<Node> currentNodes =
                nodes.values().stream().filter(node -> node.name.endsWith("A"))
                        .collect(Collectors.toSet());

        int steps = 0;
        while (true) {
            for (String direction : instructions) {
                int foundZ = 0;
                steps++;
                Set<Node> nextNodes = new HashSet<>();
                for (Node currentNode : currentNodes) {
                    if (direction.equals("R")) {
                        currentNode = nodes.get(currentNode.right());
                    } else {
                        currentNode = nodes.get(currentNode.left());
                    }
                    if (currentNode.name.endsWith("Z")) {
                        foundZ++;
                    }
                    nextNodes.add(currentNode);
                }
                if (foundZ == currentNodes.size()) {
                    return steps;
                }
                currentNodes = nextNodes;
            }
            if(steps % 100000 == 0)
                System.out.println("Steps: " + steps + " Nodes: " + currentNodes.size());
        }
    }

    private static int followingInstructions3(Map<String, Node> nodes, List<String> instructions) {
        Set<Node> currentNodes =
                nodes.values().stream().filter(node -> node.name.endsWith("A"))
                        .collect(Collectors.toSet());

        // For each node that starts with A find the shortest path to a node that ends with Z for every start position


        return 0;
    }

    @Test
    public void partTwo() {
        Data data = FileReader.parseStringForObject("""
                LR

                11A = (11B, XXX)
                11B = (XXX, 11Z)
                11Z = (11B, XXX)
                22A = (22B, XXX)
                22B = (22C, 22C)
                22C = (22Z, 22Z)
                22Z = (22B, 22B)
                XXX = (XXX, XXX)
                """, new Data(), tri);
        assertEquals(6, followingInstructions2(data.nodes, data.directions));

//        data = FileReader.readFileForObject("src/main/resources/2023/D8.txt", new Data(), tri);
//        assertEquals(0, followingInstructions2(data.nodes, data.directions));
    }
}
