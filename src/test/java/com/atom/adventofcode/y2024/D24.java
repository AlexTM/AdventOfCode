package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D24 {

    record Gate(String operation, String input1, String input2, String output) { }
    record Puzzle(Map<String, Boolean> initialInput, List<Gate> operations) { }

    private Puzzle parseInput(String input) {
        String[] parts = input.split("\n");
        Map<String, Boolean> initialInput = new HashMap<>();
        List<Gate> operations = new ArrayList<>();

        for(String part : parts) {
            if(part.isBlank()) {
                continue;
            }
            if(part.contains(":")) {
                String[] pair = part.split(":");
                String key = pair[0].trim();
                boolean value = pair[1].trim().equals("1");
                initialInput.put(key, value);
            } else {
                String[] pair = part.split(" ");
                String operation = pair[1];
                String input1 = pair[0];
                String input2 = pair[2];
                String output = pair[4];
                operations.add(new Gate(operation, input1, input2, output));
            }
        }

        return new Puzzle(initialInput, operations);
    }

    private void runOperation(Map<String, Boolean> inputs, Gate operation) {
        boolean input1 = inputs.get(operation.input1);
        boolean input2 = inputs.get(operation.input2);
        boolean result = switch (operation.operation) {
            case "AND" -> input1 & input2;
            case "OR" -> input1 | input2;
            case "XOR" -> input1 ^ input2;
            default -> throw new IllegalArgumentException("Invalid operation: " + operation.operation);
        };
        inputs.put(operation.output, result);
    }

    private Map<String, Boolean> runOperations(Puzzle puzzle) {
        Map<String, Boolean> inputs = new HashMap<>(puzzle.initialInput);
        Set<Gate> gates = new HashSet<>(puzzle.operations);

        while(!gates.isEmpty()) {
            int s = gates.size();
            for(Gate gate : gates) {
                if(inputs.containsKey(gate.input1) && inputs.containsKey(gate.input2)) {
                    runOperation(inputs, gate);
                    gates.remove(gate);
                    break;
                }
            }
            if(s == gates.size()) {
                throw new RuntimeException("Stuck in a loop");
            }
        }
        return inputs;
    }

    private BigInteger convertToBinary(Map<String, Boolean> input, String letter) {
        List<String> names = new ArrayList<>(input.keySet().stream().filter(key -> key.startsWith(letter)).toList());
        Collections.sort(names);
        Collections.reverse(names);
        BigInteger value = BigInteger.ZERO;
        for(String name : names) {
            value = value.or(input.get(name) ? BigInteger.ONE : BigInteger.ZERO);
            value = value.shiftLeft(1);
        }
        value = value.shiftRight(1);
        return value;
    }

    @Test
    public void testPartOne() {
        Puzzle puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D24.txt"));
        Map<String, Boolean> res = runOperations(puzzle);
        assertEquals("51715173446832", convertToBinary(res, "z").toString());
    }

    private Set<String> findErrors(Puzzle puzzle, String toggleInput) {
        Set<String> errors = new HashSet<>();
        for(int j=0; j<45; j++) {
            // try each bit 1 at a time
            Map<String, Boolean> inputs = new HashMap<>();
            for(int i=0; i<45; i++) {
                inputs.put(String.format("x%02d", i), false);
                inputs.put(String.format("y%02d", i), false);
            }
            inputs.put(String.format(toggleInput+"%02d", j), true);

            Map<String, Boolean> res = runOperations(new Puzzle(inputs, puzzle.operations));
            BigInteger actual = convertToBinary(res, "z");

            BigInteger target = BigInteger.ONE.shiftLeft(j);
            if(!actual.equals(target)) {
                errors.add(String.format(toggleInput+"%02d", j));
            }
        }
        return errors;
    }

    private Set<String> getAllWires(List<Gate> gates, String wire) {
        Set<String> moreWires = gates.stream().filter(g -> g.input1.contains(wire) || g.input2.contains(wire))
                .map(g -> g.output).collect(Collectors.toSet());

        Map<String, Set<String>> subWires = new HashMap<>();
        System.out.println("More wires: "+moreWires);
        for(String w : moreWires) {
            moreWires.addAll(getAllWires(gates, w));
        }
        return moreWires;
    }

    @Test
    public void testPartTwo() {
        Puzzle puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D24.txt"));
        Set<String> xErros = findErrors(puzzle, "x");
        Set<String> yErros = findErrors(puzzle, "y");

        System.out.println("x errors: "+xErros);
        System.out.println("y errors: "+yErros);

        String out = xErros.stream().flatMap(s -> puzzle.operations.stream().filter(g -> g.input1.contains(s) || g.input2.contains(s)))
                .map(g -> g.output).sorted().collect(Collectors.joining(","));
        System.out.println("Output: "+out);

        out = yErros.stream().flatMap(s -> puzzle.operations.stream().filter(g -> g.input1.contains(s) || g.input2.contains(s)))
                .map(g -> g.output).sorted().collect(Collectors.joining(","));
        System.out.println("Output: "+out);

//        assertNotEquals("bpw,fsh,kks,kvg,mmf,vdk,vrn,z25", out);

        System.out.println(getAllWires(puzzle.operations, "bpw"));

    }


}
