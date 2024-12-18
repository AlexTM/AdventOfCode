package com.atom.adventofcode.y2024;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D17 {
    private static final String TEST_INPUT = """
            Register A: 729
            Register B: 0
            Register C: 0
            
            Program: 0,1,5,4,3,0""";

    private static final String INPUT = """
            Register A: 47719761
            Register B: 0
            Register C: 0
            
            Program: 2,4,1,5,7,5,0,3,4,1,1,6,5,5,3,0""";

    record State(int regA, int regB, int regC, int[] program, int pointer, List<Integer> output) {
        public State(int regA, int regB, int regC, int[] program) {
            this(regA, regB, regC, program, 0, new ArrayList<>());
        }
    }

    private int lowest3Bits(int n) {
        return n & 0b111;
    }

    private State parseInput(String input) {
        String[] parts = input.split("\n");
        int a = Integer.parseInt(parts[0].split(": ")[1]);
        int b = Integer.parseInt(parts[1].split(": ")[1]);
        int c = Integer.parseInt(parts[2].split(": ")[1]);
        int[] program = Stream.of(parts[4].split(": ")[1].split(",")).mapToInt(Integer::parseInt).toArray();
        return new State(a, b, c, program);
    }

    private int getComboOperand(int operand, State program) {
        return switch (operand) {
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> program.regA;
            case 5 -> program.regB;
            case 6 -> program.regC;
            default -> throw new IllegalArgumentException("Invalid operand: " + operand);
        };
    }

    private State doInstruction(State state) {

        int j = state.pointer;

        int opcode = state.program[j];
        int operand = lowest3Bits(state.program[j + 1]);


        int regA = state.regA;
        int regB = state.regB;
        int regC = state.regC;
        int pointer = state.pointer;
        boolean jumped = false;

        switch (opcode) {
            case 0:
                regA = (int) (state.regA / Math.pow(2, getComboOperand(operand, state)));
                break;
            case 1:
                regB = state.regB ^ operand;
                break;
            case 2:
                regB = lowest3Bits(getComboOperand(operand, state) % 8);
                break;
            case 3:
                if(state.regA != 0) {
                    pointer = operand;
                    jumped = true;
                }
                break;
            case 4:
                regB = state.regB ^ state.regC;
                break;
            case 5:
                int i = getComboOperand(operand, state) % 8;
                state.output.add(i);
                break;
            case 6:
                regB = (int) (state.regA / Math.pow(2, getComboOperand(operand, state)));
                break;
            case 7:
                regC = (int) (state.regA / Math.pow(2, getComboOperand(operand, state)));
                break;
            default:
                throw new IllegalArgumentException("Invalid opcode: " + opcode);
        }
        pointer = jumped ? pointer : pointer + 2;
        return new State(regA, regB, regC, state.program, pointer, state.output);
    }

    private State run(State state) {
        while (state.pointer < state.program.length) {
            state = doInstruction(state);
        }
        return state;
    }

    @Test
    public void testPartOne() {
        assertEquals(1, run(new State(0, 0, 9, new int[]{2, 6})).regB);
        assertEquals(List.of(0,1,2), run(new State(10, 0, 0, new int[]{5,0,5,1,5,4})).output);
        assertEquals(26, run(new State(0, 29, 0, new int[]{1, 7})).regB);
        assertEquals(44354, run(new State(0, 2024, 43690, new int[]{4, 0})).regB);

        assertEquals(0, run(new State(2024, 0, 0, new int[]{0,1,5,4,3,0})).regA);
        assertEquals(List.of(4,2,5,6,7,7,7,7,3,1,0), run(new State(2024, 0, 0, new int[]{0,1,5,4,3,0})).output);

        assertEquals("4,6,3,5,6,3,5,2,1,0", run(parseInput(TEST_INPUT)).output.stream().map(Object::toString).collect(Collectors.joining(",")));
        assertEquals("7,0,3,1,2,6,3,7,1", run(parseInput(INPUT)).output.stream().map(Object::toString).collect(Collectors.joining(",")));
    }

    private State modifyRegA(State state, int value) {
        return new State(value, state.regB, state.regC, state.program, state.pointer, state.output);
    }

    private long findValue(final State state) {
        final List<Integer> target = Arrays.stream(state.program).boxed().toList();
        for(int i=0; i<1_000_0000; i++) {
            if(i % 10_000 == 0)
                System.out.println(i);
            if(run(modifyRegA(state, i)).output.equals(target)) {
                return i;
            }
        }
        return -1;
    }

    @Test
    public void testPartTwo() {
        String inp = """
                Register A: 2024
                Register B: 0
                Register C: 0
                
                Program: 0,3,5,4,3,0""";
        State s = parseInput(inp);
//        assertEquals(Arrays.stream(s.program).boxed().toList(), run(modifyRegA(s, 117440)).output);

        assertEquals(117440, findValue(parseInput(inp)));
    }
}
