package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D9 {

    enum Dir {U, D, L, R}
    record Pos(int x, int y){};
    record Command(Dir dir, int mag){};

    private static Command readData(String line) {
        String[] split = line.split(" ");
        return new Command(Dir.valueOf(split[0]), Integer.parseInt(split[1]));
    }

    private int countTailMoves(final List<Command> commandList, final Pos[] rope) {
        Set<Pos> tailPositions = new HashSet<>();
        for(Command command : commandList) {
            doCommand(command, rope, tailPositions);
        }
        return tailPositions.size();
    }

    private void doCommand(final Command command, final Pos[] rope, final Set<Pos> tailPositions) {

        for(int i=0; i<command.mag; i++) {
            switch (command.dir) {
                case U -> rope[0] = new Pos(rope[0].x, rope[0].y - 1);
                case D -> rope[0] = new Pos(rope[0].x, rope[0].y + 1);
                case L -> rope[0] = new Pos(rope[0].x - 1, rope[0].y);
                case R -> rope[0] = new Pos(rope[0].x + 1, rope[0].y);
            }

            for(int j=1; j<rope.length; j++) {
                rope[j] = moveTail(rope[j-1], rope[j]);
            }
            tailPositions.add(rope[rope.length-1]);
        }
    }

    // TODO probably a far tidier way to update tail
    private Pos moveTail(Pos head, Pos tail) {
        int difx = head.x - tail.x;
        int dify = head.y - tail.y;
        int tx = tail.x, ty = tail.y;

        if(head.x == tail.x) {
            if(dify == 2) {
                ty = tail.y + 1;
            }
            if(dify == -2) {
                ty = tail.y - 1;
            }
        } else if(head.y == tail.y) {
            if(difx == 2) {
                tx = tail.x + 1;
            }
            if(difx == -2) {
                tx = tail.x - 1;
            }
        } else if(Math.abs(difx) == 2 || Math.abs(dify) == 2){
            if(difx < 0)
                tx--;
            else
                tx++;
            if(dify < 0)
                ty--;
            else
                ty++;
        }
        return new Pos(tx, ty);
    }


    @Test
    public void testTailMovements() {
        assertEquals(6044, countTailMoves(
                FileReader.readFileObjectList("src/test/resources/2022/D9.txt", D9::readData), rope(2)));
    }

    private Pos[] rope(int size) {
        Pos[] rope = new Pos[size];
        for(int i=0; i<size; i++)
            rope[i] = new Pos(0,0);
        return rope;
    }

    @Test
    public void testTailMovements2() {
        assertEquals(2384,
                countTailMoves(
                        FileReader.readFileObjectList("src/test/resources/2022/D9.txt", D9::readData),
                        rope(10)));
    }

}
