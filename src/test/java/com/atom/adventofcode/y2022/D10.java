package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D10 {

    enum Instruction {noop(0), addx(1);
        public final int time;
        Instruction(int time) {
            this.time = time;
        }
    }

    record Command(Instruction name, Integer value){};
    record State(int registerX, Command command, int processing){}

    private static Command readData(String line) {
        String[] split = line.split(" ");
        if(split.length == 1)
            return new Command(Instruction.valueOf(split[0].trim()), null);
        return new Command(Instruction.valueOf(split[0].trim()), Integer.parseInt(split[1]));
    }

    public State cycle(State state, Queue<Command> commands) {
        int registerX = state.registerX;
        int processing = state.processing;
        Command currentCommand = state.command;

        if(processing == currentCommand.name.time) {
            switch (currentCommand.name) {
                case noop -> {}
                case addx -> registerX += currentCommand.value;
            }
            processing = 0;
            currentCommand = commands.poll();
        } else {
            processing++;
        }

        return new State(registerX, currentCommand, processing);
    }

    private List<Integer> getSignalStrengths(Queue<Command> commandQueue) {
        int counter = 0;
        List<Integer> signalStrengths = new ArrayList<>();

        State state = new State(1, commandQueue.poll(), 0);
        while(state.command != null) {
            if((++counter+20)%40==0)
                signalStrengths.add(state.registerX*counter);
            state = cycle(state, commandQueue);
        }
        return signalStrengths;
    }

    private String drawScreen(Queue<Command> commandQueue) {
        int counter = 0;

        StringBuilder sb = new StringBuilder();
        State state = new State(1, commandQueue.poll(), 0);

        while(state.command != null) {
            int crtPos = counter%40;
            if(crtPos==0 && counter !=0)
                sb.append("\n");

            counter++;
            int spritePosition = state.registerX;
            if(crtPos >= spritePosition-1 && crtPos <= spritePosition+1)
                sb.append("#");
            else
                sb.append(".");

            state = cycle(state, commandQueue);
        }
        return sb.toString();
    }

    @Test
    public void testCPU4() {
        List<Command> commandList = FileReader.readFileObjectList("src/test/resources/2022/D10.txt", D10::readData);
        Queue<Command> commandQueue = new LinkedList<>(commandList);

        // FJUBULRZ
        String screen = drawScreen(commandQueue);
        assertEquals(
                "####...##.#..#.###..#..#.#....###..####.\n" +
                        "#.......#.#..#.#..#.#..#.#....#..#....#.\n" +
                        "###.....#.#..#.###..#..#.#....#..#...#..\n" +
                        "#.......#.#..#.#..#.#..#.#....###...#...\n" +
                        "#....#..#.#..#.#..#.#..#.#....#.#..#....\n" +
                        "#.....##...##..###...##..####.#..#.####.",
                screen);
    }

    @Test
    public void testCPU2() {
        List<Command> commandList = FileReader.readFileObjectList("src/test/resources/2022/D10.txt", D10::readData);
        Queue<Command> commandQueue = new LinkedList<>(commandList);
        List<Integer> signalStrengths = getSignalStrengths(commandQueue);
        assertEquals(13060, signalStrengths.stream().reduce(0, Integer::sum));
    }
}
