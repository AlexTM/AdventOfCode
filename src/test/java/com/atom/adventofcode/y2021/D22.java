package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D22 {

    record Pos(int x, int y, int z){};
    record Cube(Pos start, Pos end){};
    record Instruction(Boolean on, Pos start, Pos stop){};

    private static Pattern p = Pattern.compile("(.*) x=(.*)\\.\\.(.*),y=(.*)\\.\\.(.*),z=(.*)\\.\\.(.*)");

    // on x=-20..26,y=-36..17,z=-47..7
    private List<Instruction> loadData(String filename) {
        return FileReader.readFileObjectList(filename, line -> {
            Matcher m = p.matcher(line);
            if(m.find()) {
                return new Instruction(
                        m.group(1).equals("on"),
                        new Pos(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(6))),
                        new Pos(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(5)), Integer.parseInt(m.group(7)))
                        );
            }
            return null;
        });
    };

    private List<Instruction> addConstraint(List<Instruction> instructions) {
        return instructions.stream().map(i -> {
            int startx = Math.max(i.start.x, -50);
            int endx = Math.min(i.stop.x, 50);
            int starty = Math.max(i.start.y, -50);
            int endy = Math.min(i.stop.y, 50);
            int startz = Math.max(i.start.z, -50);
            int endz = Math.min(i.stop.z, 50);
            return new Instruction(i.on, new Pos(startx, starty, startz), new Pos(endx, endy, endz));
        }).collect(Collectors.toList());
    }

    private int evaluateAllRules(List<Instruction> instructions) {
        Set<Pos> state = new HashSet<>();
        for(Instruction i : instructions) {
            state = evaluateInstruction(i, state);
        }
        return state.size();
    }

    private Set<Pos> evaluateInstruction(Instruction instruction, Set<Pos> state) {
        for(int z=instruction.start.z; z<=instruction.stop.z; z++) {
            for(int y=instruction.start.y; y<=instruction.stop.y; y++) {
                for (int x = instruction.start.x; x <= instruction.stop.x; x++) {
                    if(x >= -50 && x <= 50 && y>=-50 && y<=50 && z >=-50 && z<= 50) {
                        Pos p = new Pos(x, y, z);
                        if (instruction.on) {
                            state.add(p);
                        } else {
                            state.remove(p);
                        }
                    }
                }
            }
        }
        return state;
    }

    private long getCubes(Instruction i) {
        long dx = i.start.x - i.stop.x;
        long dy = i.start.y - i.stop.y;
        long dz = i.start.z - i.stop.z;
        return dx*dy*dz;
    }

    private boolean doesIntersect(Cube a, Cube b) {
//        if()
        return false;
    }

    private void eval2(List<Instruction> instructions) {
        List<Cube> cubes = new ArrayList<>();
//        long count = 0;
        for(Instruction instruction : instructions) {
            if(instruction.on) {

            }
        }

    }

    @Test
    public void testPart1() {
        assertEquals(39, evaluateAllRules(loadData("src/test/resources/2021/D22_t2.txt")));
        assertEquals(590784, evaluateAllRules(
                addConstraint(loadData("src/test/resources/2021/D22_t.txt"))));
        assertEquals(582644, evaluateAllRules(
                addConstraint(loadData("src/test/resources/2021/D22.txt"))));
    }

    /**
     * TODO
     * Keep running list of cubes which are on
     * As more instructions are applied
     *  - work out if intersect
     *  - if yes, then dissolve cube into multiple smaller cubes cutting out intersection
     */
    @Test
    public void testPart2() {
        assertEquals(2758514936282235L, evaluateAllRules(loadData("src/test/resources/2021/D22_t3.txt")));
//        assertEquals(0, evaluateAllRules(
//                loadData("src/test/resources/2021/D22.txt")));
    }

}
