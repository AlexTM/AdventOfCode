package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D7 {

    enum Action {AND, OR, LSHIFT, RSHIFT, NOT, VALUE, LOOKUP_VALUE}

    record Command(Action action, String lhs, String rhs, Integer value){}
    record Instruction(Command command, String assignTo){};

    // Ugly but not spending any more time on it
    private static Instruction parseString(String input) {
        Command command = null;
        String[] split = input.split("->");

        if(split[0].contains("AND")) {
            String[] split2 = split[0].split("AND");
            command = new Command(Action.AND, split2[0].trim(), split2[1].trim(), null);
        } else if(split[0].contains("OR")) {
            String[] split2 = split[0].split("OR");
            command = new Command(Action.OR, split2[0].trim(), split2[1].trim(), null);
        } else if(split[0].contains("LSHIFT")) {
            String[] split2 = split[0].split("LSHIFT");
            command = new Command(Action.LSHIFT, split2[0].trim(), null, Integer.parseInt(split2[1].trim()));
        } else if(split[0].contains("RSHIFT")) {
            String[] split2 = split[0].split("RSHIFT");
            command = new Command(Action.RSHIFT, split2[0].trim(), null, Integer.parseInt(split2[1].trim()));
        } else if(split[0].contains("NOT")) {
            String[] split2 = split[0].split("NOT");
            command = new Command(Action.NOT, split2[1].trim(), null, null);
        } else {
            String lhs = split[0].trim();
            try {
                command = new Command(Action.VALUE, null, null, Integer.parseInt(lhs));
            } catch(NumberFormatException e) {
                // I know, not good
                command = new Command(Action.LOOKUP_VALUE, lhs, null, null);
            }
        }

        return new Instruction(command, split[1].trim());
    }

    private List<Instruction> readInstructions() {
        return FileReader.readFileObjectList("src/test/resources/2015/D7.txt", D7::parseString);
    }

    @Test
    public void testCircuit() {
        String inp1 = "123 -> x\n" +
                "456 -> y\n" +
                "x AND y -> d\n" +
                "x OR y -> e\n" +
                "x LSHIFT 2 -> f\n" +
                "y RSHIFT 2 -> g\n" +
                "NOT x -> h\n" +
                "NOT y -> i";
        
        //        System.out.println(parseString("123 -> x"));
//        System.out.println(parseString("x AND y -> z"));
//        System.out.println(parseString("x OR y -> z"));
//        System.out.println(parseString("x LSHIFT 2 -> f"));
//        System.out.println(parseString("x RSHIFT 2 -> f"));
//        System.out.println(parseString("NOT x -> h"));
//
//        System.out.println(FileReader.readObjectList(inp1, D7::parseString));
        System.out.println(runInstructions(FileReader.readObjectList(inp1, D7::parseString)));

        Map<String, Integer> m = runInstructions(readInstructions());
        System.out.println(m);
        assertEquals(5, m.get("a"));
    }

    private Map<String, Integer> runInstructions(List<Instruction> instructions) {

        Map<String, Integer> variables = new HashMap<>();

        for(Instruction instruction : instructions) {
            process(variables, instruction);
        }

        return variables;
    }

    // TODO what is the correct way to generate a bit mask?
    private static int BIT_MASK_16;
    static {
        BIT_MASK_16 = 0;
        for(int i=0; i<15; i++) {
            BIT_MASK_16 = (BIT_MASK_16 + 1) << 1;
        }
    }

    private int mapInt(int num) {
        return num & BIT_MASK_16;
    }

    private void process(Map<String, Integer> variables, Instruction instruction) {
        switch(instruction.command.action) {
            case VALUE -> {
                variables.put(instruction.assignTo, instruction.command().value);
            }
            case LOOKUP_VALUE -> {
                variables.put(instruction.assignTo, variables.get(instruction.command.lhs));
            }
            case AND -> {
                int lhs = variables.getOrDefault(instruction.command.lhs, 0);
                int rhs = variables.getOrDefault(instruction.command.rhs, 0);
                int value = lhs & rhs;
                variables.put(instruction.assignTo, value);
            }
            case OR -> {
                int lhs = variables.getOrDefault(instruction.command.lhs, 0);
                int rhs = variables.getOrDefault(instruction.command.rhs, 0);
                int value = lhs | rhs;
                variables.put(instruction.assignTo, value);
            }
            case LSHIFT -> {
                int lhs = variables.getOrDefault(instruction.command.lhs, 0);
                int rhs = instruction.command.value;
                int value = lhs << rhs;
                variables.put(instruction.assignTo, value);
            }
            case RSHIFT -> {
                int lhs = variables.getOrDefault(instruction.command.lhs, 0);
                int rhs = instruction.command.value;
                int value = lhs >> rhs;
                variables.put(instruction.assignTo, value);
            }
            case NOT -> {
                int lhs = variables.getOrDefault(instruction.command.lhs, 0);
                int value = mapInt(~lhs);
                variables.put(instruction.assignTo, value);
            }
        }
    }
}
