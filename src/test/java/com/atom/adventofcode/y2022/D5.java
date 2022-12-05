package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D5 {

    record Move(int amount, int source, int dest){};

    // Ugly state class
    static class State {
        List<Move> moves;
        Stack<Character>[] stacks;
        boolean readingStacks = true;

        public State(int stackSize) {
            stacks = new Stack[stackSize];
            for(int i=0; i<stackSize; i++) {
                stacks[i] = new Stack<>();
            }
            moves = new ArrayList<>();
        }

        public void reverseStacks() {
            for(int i=0; i<stacks.length; i++) {
                Stack<Character> tmp = new Stack<>();
                while(!stacks[i].isEmpty()) {
                    Character c = stacks[i].pop();
                    if(Character.isAlphabetic(c)) {
                        tmp.push(c);
                    }
                }
                stacks[i] = tmp;
            }
        }
    }

    // read into ugly state class
    private static State readLine(String line, State state) {
        if(state.readingStacks) {
            if(line.isEmpty()) {
                state.readingStacks = false;
                return state;
            }
            int size = (line.length() / 4)+1;
            for(int i=0; i<size; i++) {
                state.stacks[i].push(line.charAt(1+(i*4)));
            }
        } else {
            String[] split = line.split(" ");
            state.moves.add(
                    new Move(Integer.parseInt(split[1]),
                            Integer.parseInt(split[3]),
                            Integer.parseInt(split[5])));
        }
        return state;
    }

    private static Void crateMover9000(State state, Move move) {
        for(int i=0; i<move.amount; i++)
            state.stacks[move.dest-1].push(state.stacks[move.source-1].pop());

        return null;
    }

    private static Void crateMover9001(State state, Move move) {
        Stack<Character> tmp = new Stack<>();
        for(int i=0; i<move.amount; i++)
            tmp.add(state.stacks[move.source-1].pop());

        while(!tmp.isEmpty())
            state.stacks[move.dest-1].push(tmp.pop());

        return null;
    }

    private static void runMoves(State state, BiFunction<State, Move, Void> func) {
        for(Move move : state.moves) {
            func.apply(state, move);
        }
    }

    private static String getTopCrate(State state) {
        return Arrays.stream(state.stacks)
                .filter(s -> !s.isEmpty())
                .map(s -> Character.toString(s.pop()))
                .reduce("", (a, b) -> a+b);
    }

    @Test
    public void testCrateMover9000() {
        State state = FileReader.readFileForObject("src/test/resources/2022/D5.txt", new State(10), D5::readLine);
        state.reverseStacks();
        runMoves(state, D5::crateMover9000);
        assertEquals("TWSGQHNHL", getTopCrate(state));
    }


    @Test
    public void testCrateMover9001() {
        State state = FileReader.readFileForObject("src/test/resources/2022/D5.txt", new State(10), D5::readLine);
        state.reverseStacks();
        runMoves(state, D5::crateMover9001);
        assertEquals("JNRSCDWPP", getTopCrate(state));
    }

}
