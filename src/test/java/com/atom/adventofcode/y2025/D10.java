package com.atom.adventofcode.y2025;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D10 {
    private static final String TEST_INPUT = """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """;

    record Machine(List<Boolean> lights, List<List<Integer>> connections, List<Integer> jolatge) {};

    private List<Boolean> parseLights(String input) {
        return input.chars().filter(i -> i != '[' && i != ']').mapToObj(i -> i == '#').toList();
    }
    private List<Integer> parseInts(String input) {
        return Arrays.stream(input.substring(1, input.length()-1).split(","))
                .map(Integer::parseInt).toList();
    }
    private List<List<Integer>> parseConnections(String[] inputs) {
        List<List<Integer>> results = new ArrayList<>();
        for(int i=1; i<inputs.length-1; i++) {
            results.add(parseInts(inputs[i]));
        }
        return results;
    }
    private List<Machine> parseInput(String input) {
        List<Machine> machines = new ArrayList<>();
        String[] split = input.split("\n");
        for(String s : split) {
            String[] split2 = s.split(" ");
            machines.add(new Machine(
                    parseLights(split2[0]),
                    parseConnections(split2),
                    parseInts(split2[split2.length - 1])));
        }
        return machines;
    }

    List<Boolean> pressButton(Machine m, int button, List<Boolean> state) {
        List<Integer> connections = m.connections.get(button);
        List<Boolean> newState = new ArrayList<>(state);
        for(Integer i : connections) {
            newState.set(i, !newState.get(i));
        }
        return newState;
    }

    private int solve(Machine machine) {
        List<Boolean> state = new ArrayList<>(machine.lights.size());
        for(int i=0; i<machine.lights.size(); i++) {
            state.add(false);
        }

        Set<Integer> available = IntStream.range(0, machine.connections.size())
                .boxed()
                .collect(Collectors.toSet());

        return solve(machine, state, available, Integer.MAX_VALUE);
    }

    private int solve(Machine machine, List<Boolean> state, Set<Integer> available, int currentMin) {
        boolean solved = true;
        for(int i=0; i<machine.lights.size(); i++) {
            if(machine.lights.get(i) != state.get(i)) {
                solved = false;
                break;
            }
        }
        if(solved)
            return machine.connections.size() - available.size();

        // don't look deeper than currentMin
        if(machine.connections.size() - available.size() >= currentMin) {
            return Integer.MAX_VALUE;
        }

        int min = Integer.MAX_VALUE;
        for(Integer i : available) {
            List<Boolean> newState = pressButton(machine, i, state);
            Set<Integer> newSet = new HashSet<>(available);
            newSet.remove(i);
            min = Math.min(min, solve(machine, newState, newSet, min));
        }
        return min;
    }

    private int solveForAll(List<Machine> machines) {
//        int sum = 0;
//        for(int i=0; i<machines.size(); i++) {
//            sum += solve(machines.get(i));
//            System.out.println(i+" = Sum: "+sum);
//        }
//        return sum;

        return machines.parallelStream().mapToInt(m -> {
            System.out.println("Solving: "+m);
            return solve2(m);
        }).sum();
    }



    private int solve2(Machine machine) {
        // Generate candidates from all columns and then check if they are valid
        List<List<Integer>> candidates = new ArrayList<>();

        for(int i=0; i<machine.lights.size(); i++) {
            boolean lightStatus = machine.lights.get(i);
            List<Integer> impactingButtons = getAllButtonsImpactingLight(machine, i);
            if(!lightStatus) {
                // call combinations of 2 and 4 and 6 etc
                candidates.addAll(generateEvenLengthCombinations(impactingButtons));
            } else {
                // if on, all odd combinations
                candidates.addAll(generateOddLengthCombinations(impactingButtons));
            }
        }

        // given all these combinations, check if they are valid
        Map<List<Integer>, Integer> combinationToScore = new HashMap<>();
        for(List<Integer> combination : candidates) {
            combinationToScore.put(combination, solveWithSolution(machine, combination));
        }

        return combinationToScore.values().stream().mapToInt(i -> i).min().getAsInt();

    }

    private int solveWithSolution(Machine m, List<Integer> solution) {
        List<Boolean> state = new ArrayList<>(m.lights.size());
        for(int i=0; i<m.lights.size(); i++) {
            state.add(false);
        }
        for(int i=0; i<solution.size(); i++) {
            List<Integer> lightsToToggle = m.connections.get(solution.get(i));
            for(int j : lightsToToggle) {
                state.set(j, !state.get(j));
            }
        }
        // check if correct state
        for(int i=0; i<m.lights.size(); i++) {
            if(m.lights.get(i) != state.get(i)) {
                return Integer.MAX_VALUE;
            }
        }
        return solution.size();
    }

    private List<Integer> getAllButtonsImpactingLight(Machine machine, int lightIndex) {
        List<Integer> res = new ArrayList<>();
        for(int i=0; i<machine.connections.size(); i++) {
            if(machine.connections.get(i).contains(lightIndex)) {
                res.add(i);
            }
        }
        return res;
    }

    private Collection<? extends List<Integer>> generateEvenLengthCombinations(List<Integer> numbers) {
        List<List<Integer>> res = new ArrayList<>();

        for(int i=0; i<numbers.size(); i = i + 2) {
            res.addAll(generateCombinations(numbers, new ArrayList<>(), i));
        }

        return res;
    }

    private List<List<Integer>> generateOddLengthCombinations(List<Integer> numbers) {
        List<List<Integer>> res = new ArrayList<>();
        for(int i=1; i<numbers.size(); i = i + 2) {
            res.addAll(generateCombinations(numbers, new ArrayList<>(), i));
        }
        return res;
    }

    private List<List<Integer>> generateCombinations(List<Integer> numbers, List<Integer> current, int depth) {
        return generateCombinations(numbers, current, depth, 0);
    }
    private List<List<Integer>> generateCombinations(List<Integer> numbers, List<Integer> current, int depth, int idx) {
        if(depth == 0) return List.of(current);
        List<List<Integer>> res = new ArrayList<>();
        for(int i=idx; i<numbers.size(); i++) {
            if(current.contains(numbers.get(i))) continue;
            List<Integer> newList = new ArrayList<>(current);
            newList.add(numbers.get(i));

            res.addAll(generateCombinations(numbers, newList, depth-1, idx++));
        }
        return res;
    }

    @Test
    public void test() {
        System.out.println(generateCombinations(List.of(1, 2), new ArrayList<>(), 0));
        System.out.println(generateCombinations(List.of(1, 2), new ArrayList<>(), 1));
        System.out.println(generateCombinations(List.of(1, 2, 3), new ArrayList<>(), 1));

        System.out.println(generateCombinations(List.of(1, 2), new ArrayList<>(), 2));
        System.out.println(generateCombinations(List.of(1, 2, 3), new ArrayList<>(), 2));

        System.out.println(generateCombinations(List.of(1, 2, 3), new ArrayList<>(), 3));
    }

    @Test
    public void testPart1() {
        var machines = parseInput(TEST_INPUT);
        assertEquals(2, solve2(machines.get(0)));
//        assertEquals(2, solve(machines.get(0)));
//        assertEquals(3, solve(machines.get(1)));
//        assertEquals(2, solve(machines.get(2)));
        assertEquals(7, solveForAll(machines));
//
//        machines = parseInput(FileReader.readFileString("src/test/resources/2025/D10.txt"));
//        assertEquals(0, solveForAll(machines));
    }

}
