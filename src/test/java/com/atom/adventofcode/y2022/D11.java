package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D11 {

    enum Operation {MUL, ADD}
    record Monkey(Operation operation, Integer operationAmount, int test, int trueMonkey, int falseMonkey, Queue<Long> items){};
    static class LoadingState {
        List<Long> items;
        Operation operation;
        Integer operationAmount;
        int test;
        int trueMonkey;
        int falseMonkey;
        List<Monkey> monkeys = new ArrayList<>();
    };

    // TODO could make this far nicer, but not now
    private static LoadingState loadFile(String line, LoadingState state) {
        if(line.startsWith("Monkey")) {
            // new monkey
            state.items = new ArrayList<>();
        }
        if(line.contains("Starting items")) {
            String[] split1 = line.split(":");
            String[] split2 = split1[1].split(",");
            for(String s: split2) {
                state.items.add(Long.parseLong(s.trim()));
            }
        }
        if(line.contains("Operation")) {
            char c = line.charAt(23);
            if(c == '+')
                state.operation = Operation.ADD;
            if(c == '*')
                state.operation = Operation.MUL;
            if(line.substring(24).trim().equalsIgnoreCase("old")) {
                state.operationAmount = null;
            } else {
                state.operationAmount = Integer.parseInt(line.substring(24).trim());
            }
        }
        if(line.contains("Test: divisible by")) {
            state.test = Integer.parseInt(line.substring(21).trim());
        }
        if(line.contains("If true: throw to monkey")) {
            state.trueMonkey = Integer.parseInt(line.substring(29).trim());
        }
        if(line.contains("If false: throw to monkey")) {
            state.falseMonkey = Integer.parseInt(line.substring(30).trim());
            state.monkeys.add(new Monkey(
                    state.operation, state.operationAmount, state.test, state.trueMonkey,
            state.falseMonkey, new LinkedList<>(state.items)));
        }
        return state;
    }

    private void monkeyRound(List<Monkey> monkeys, Integer[]inspections, long modValue) {

        for(int j=0; j<monkeys.size(); j++) {
            Monkey monkey = monkeys.get(j);

            while(!monkey.items.isEmpty()) {
                Long i = monkey.items.poll();
                inspections[j]++;

                Long worryLevel = null;
                Long opAmount = monkey.operationAmount == null ? i : monkey.operationAmount;
                switch(monkey.operation) {
                    case MUL -> worryLevel = i * opAmount;
                    case ADD -> worryLevel = i + opAmount;
                }

                if(modValue == -1)
                    worryLevel /= 3;
                else
                    worryLevel %= modValue;

                if(worryLevel%monkey.test == 0) {
                    monkeys.get(monkey.trueMonkey).items.add(worryLevel);
                } else {
                    monkeys.get(monkey.falseMonkey).items.add(worryLevel);
                }
            }
        }
    }

    private BigInteger calMoneyValue(List<Monkey> monkeys, int rounds, boolean divideByThree) {

        long modValue = -1;
        if(!divideByThree)
            modValue = monkeys.stream().map(m -> m.test).reduce(1, (a, b) -> a*b);

        Integer[] inspections =new Integer[monkeys.size()];
        Arrays.fill(inspections, 0);

        for(int i=0; i<rounds; i++)
            monkeyRound(monkeys, inspections, modValue);

        Arrays.sort(inspections, Collections.reverseOrder());
        return BigInteger.valueOf(inspections[0]).multiply(BigInteger.valueOf(inspections[1]));
    }

    @Test
    public void testMonkies() {
        assertEquals(BigInteger.valueOf(118674), calMoneyValue(
                FileReader.readFileForObject("src/test/resources/2022/D11.txt", new LoadingState(), D11::loadFile).monkeys,
                20, true
        ));
    }

    @Test
    public void testMonkies2() {
        assertEquals(BigInteger.valueOf(32333418600L), calMoneyValue(
                FileReader.readFileForObject("src/test/resources/2022/D11.txt", new LoadingState(), D11::loadFile).monkeys
                        ,10000, false
        ));
    }
}
