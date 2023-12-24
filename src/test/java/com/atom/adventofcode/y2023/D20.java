package com.atom.adventofcode.y2023;

import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D20 {

    private static final String testInput = """
            broadcaster -> a, b, c
            %a -> b
            %b -> c
            %c -> inv
            &inv -> a
            """;

    private static void parseInput(List<Node> nodes, String line) {
        String[] split = line.split("->");
        String[] tos = split[1].trim().split(",");
        String nodeName = split[0].trim();
        NodeType type = NodeType.START;
        if(nodeName.startsWith("%"))
            type = NodeType.FLIPFLOP;
        else if(nodeName.startsWith("&"))
            type = NodeType.CONJUNCTION;
        nodes.add(
                new Node(type == NodeType.START ? nodeName : nodeName.substring(1),
                        type, Arrays.stream(tos).map(String::trim).collect(Collectors.toList())));
    }

    private enum NodeType { START, FLIPFLOP, CONJUNCTION }
    private enum PulseType { HIGH, LOW }
    record Pulse(PulseType type, String from, String current) {}
    record Node(String name, NodeType type, List<String> tos) {}

    private static void reset(
            Map<String, Boolean> flipFlopState,
            Map<String, Map<String, PulseType>> conjunctionState,
            Map<String, Node> nodeMap) {
        flipFlopState.clear();
        conjunctionState.clear();

        List<String> conjunctionNames = nodeMap.values().stream().filter(n -> n.type.equals(NodeType.CONJUNCTION))
                .map(n -> n.name).toList();

        // Need to reset all conjunctions.
        for(Node node : nodeMap.values()) {
            List<String> tos = new ArrayList<>(node.tos);
            tos.retainAll(conjunctionNames);

            for(String s :  tos) {
                conjunctionState.putIfAbsent(s, new HashMap<>());
                conjunctionState.get(s).put(node.name, PulseType.LOW);
            }
        }
    }

    private static void run(List<Node> nodes) {
        Map<String, Node> map = nodes.stream().collect(Collectors.toMap(Node::name, Function.identity()));

        Map<String, Boolean> flipFlopState = new HashMap<>();
        Map<String, Map<String, PulseType>> conjunctionState = new HashMap<>();
        reset(flipFlopState, conjunctionState, map);

        Queue<Pulse> queue = new LinkedList<>();
        queue.add(new Pulse(PulseType.LOW, null, "broadcaster"));

        while(!queue.isEmpty()) {
            Pulse pulse = queue.poll();

            Node currentNode = map.get(pulse.current);
            switch(currentNode.type) {
                case START -> {
                    for(String to : currentNode.tos) {
                        queue.add(new Pulse(pulse.type, currentNode.name, to));
                    }
                }
                case FLIPFLOP -> {
                    if(pulse.type.equals(PulseType.HIGH)) {
                        for(String to : currentNode.tos) {
                            queue.add(new Pulse(PulseType.HIGH, currentNode.name, to));
                        }
                    } else if(pulse.type.equals(PulseType.LOW)){
                        boolean state = flipFlopState.getOrDefault(pulse.current, false);
                        PulseType pType = !state ? PulseType.HIGH : PulseType.LOW;

                        for(String to : currentNode.tos) {
                            queue.add(new Pulse(pType, currentNode.name, to));
                        }

                        flipFlopState.put(currentNode.name, !state);
                    }
                }
                case CONJUNCTION -> {
                    conjunctionState.putIfAbsent(pulse.current, new HashMap<>());
                    Map<String, PulseType> mmap = conjunctionState.getOrDefault(pulse.current, new HashMap<>());

                    String from = pulse.from;
                    // Check all froms for this conjunction
//                    if()
//
//                    for(String to : currentNode.tos) {
//                        queue.add(new Pulse(state ? PulseType.HIGH : PulseType.LOW, pulse.current, to));
//                    }
                }
            }
            System.out.println(pulse);
        }
    }

    @Test
    public void partOne() {
        List<Node> map = testInput.lines().reduce(new ArrayList<>(), (m, line) -> {
            parseInput(m, line);
            return m;
        }, (a, b) -> a);

        System.out.println(map);
        run(map);
    }
}