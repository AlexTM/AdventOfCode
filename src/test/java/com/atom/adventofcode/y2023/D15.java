package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D15 {
    private static final String testInput = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
    
    private static int hashAlgo(String input) {
        return input.chars().reduce(0, (a, b) -> (a + b) * 17 % 256);
    }

    @Test
    public void partOne() {
        assertEquals(52, hashAlgo("HASH"));
        assertEquals(1320, Arrays.stream(testInput.split(",")).mapToLong(s -> hashAlgo(s)).sum());
        String contents = FileReader.readFileString("src/main/resources/2023/D15.txt");
        assertEquals(516657, Arrays.stream(contents.split(",")).mapToLong(s -> hashAlgo(s)).sum());
    }

    record Instruction(String label, int focalLength) {};

    private static Instruction parseInstruction(String s) {
        String[] split = s.split("[=,-]");
        if(split.length == 1)
            return new Instruction(split[0], -1);
        return new Instruction(split[0], Integer.parseInt(split[1]));
    }

    private static List<LinkedHashMap<String, Integer>> insertLensViaHASHMAP(List<Instruction> instructions) {
        List<LinkedHashMap<String, Integer>> lens = new ArrayList<>(256);
        for(int i = 0; i < 256; i++)
            lens.add(new LinkedHashMap<>());

        for(Instruction i : instructions) {
            if(i.focalLength == -1) {
                // remove lens with given label
                int hash = hashAlgo(i.label());
                lens.get(hash).remove(i.label());

            } else {
                // add lens with given label
                int hash = hashAlgo(i.label());
                lens.get(hash).put(i.label(), i.focalLength());
            }
        }
        return lens;
    }

    private long calculateFocusPower(List<LinkedHashMap<String, Integer>> lens) {
        long power = 0;
        for(int i = 0; i < 256; i++) {
            int slot = 1;
            for(Map.Entry<String, Integer> entry : lens.get(i).entrySet()) {
                power += (i+1) * slot++ * entry.getValue();
            }
        }
        return power;
    }

    @Test
    public void partTwo() {
        List<Instruction> instructions = Arrays.stream(testInput.split(","))
            .map(s -> parseInstruction(s)).toList();
        assertEquals(145, calculateFocusPower(insertLensViaHASHMAP(instructions)));

        String contents = FileReader.readFileString("src/main/resources/2023/D15.txt");
        instructions = Arrays.stream(contents.split(","))
            .map(s -> parseInstruction(s)).toList();
        assertEquals(210906, calculateFocusPower(insertLensViaHASHMAP(instructions)));
    }

}
