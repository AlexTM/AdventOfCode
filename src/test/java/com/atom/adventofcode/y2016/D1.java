package com.atom.adventofcode.y2016;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D1 {

    private static final String data = "R4, R5, L5, L5, L3, R2, R1, R1, L5, R5, R2, L1, L3, L4, R3, L1, L1, R2, R3, R3, R1, L3, L5, R3, R1, L1, R1, R2, L1, L4, L5, R4, R2, L192, R5, L2, R53, R1, L5, R73, R5, L5, R186, L3, L2, R1, R3, L3, L3, R1, L4, L2, R3, L5, R4, R3, R1, L1, R5, R2, R1, R1, R1, R3, R2, L1, R5, R1, L5, R2, L2, L4, R3, L1, R4, L5, R4, R3, L5, L3, R4, R2, L5, L5, R2, R3, R5, R4, R2, R1, L1, L5, L2, L3, L4, L5, L4, L5, L1, R3, R4, R5, R3, L5, L4, L3, L1, L4, R2, R5, R5, R4, L2, L4, R3, R1, L2, R5, L5, R1, R1, L1, L5, L5, L2, L1, R5, R2, L4, L1, R4, R3, L3, R1, R5, L1, L4, R2, L3, R5, R3, R1, L3";

    enum Direction {R, L};
    enum Orientation {N, E, S, W};

    record Instruction(Direction direction, Integer amount){}
    record Pos(int x, int y){}

    private List<Instruction> parseInstructions(String directions) {
        return Arrays.stream(directions.split(",")).map(String::trim)
                .map(s -> new Instruction(Direction.valueOf(s.substring(0,1)), Integer.parseInt(s.substring(1))))
                .collect(Collectors.toList());
    }

    private Orientation updateOrientation(Orientation o, Direction direction) {
        int d = switch (direction) {
            case L -> o.ordinal()-1;
            case R -> o.ordinal()+1;
        };
        if(d < 0) d += 4;
        if(d > 3) d -= 4;
        return Orientation.values()[d];
    }

    private List<Pos> updatePositionGetAllPositions(Pos pos, Orientation o, int magnitude) {
        return switch(o) {
            case N -> IntStream.range(1, magnitude+1).mapToObj(i -> new Pos(pos.x, pos.y - i)).collect(Collectors.toList());
            case S -> IntStream.range(1, magnitude+1).mapToObj(i -> new Pos(pos.x, pos.y + i)).collect(Collectors.toList());
            case W -> IntStream.range(1, magnitude+1).mapToObj(i -> new Pos(pos.x - i, pos.y)).collect(Collectors.toList());
            case E -> IntStream.range(1, magnitude+1).mapToObj(i -> new Pos(pos.x + i, pos.y)).collect(Collectors.toList());
        };
    }


    private int getDistance(List<Instruction> instructions, boolean firstRepeat) {
        Orientation o = Orientation.N;
        Pos pos = new Pos(0,0);

        Set<Pos> posSet = new HashSet<>();

        for(Instruction instruction : instructions) {
            o = updateOrientation(o, instruction.direction);

            List<Pos> posList = updatePositionGetAllPositions(pos, o, instruction.amount);
            for (Pos value : posList) {
                pos = value;
                if (firstRepeat && posSet.contains(pos)) {
                    return Math.abs(pos.x) + Math.abs(pos.y);
                }
                posSet.add(pos);
            }
        }
        return Math.abs(pos.x)+Math.abs(pos.y);
    }

    @Test
    public void test() {
        assertEquals(5, getDistance(parseInstructions("R2, L3"), false));
        assertEquals(2, getDistance(parseInstructions("R2, R2, R2"), false));
        assertEquals(12, getDistance(parseInstructions("R5, L5, R5, R3"), false));
        assertEquals(250, getDistance(parseInstructions(data), false));

        assertEquals(4, getDistance(parseInstructions("R8, R4, R4, R8"), true));
        assertEquals(151, getDistance(parseInstructions(data), true));
    }

}
