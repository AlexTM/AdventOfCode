package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * This can be solved by just reviewing the instructions
 * But wanted to try a depth search with a cache to brute force it.  Currently failing
 */
public class D24 {

    record Instruction(String type, char c, String v){ };

    private List<Instruction> loadData(String filename) {
        return FileReader.readFileObjectList(filename, line -> {
            String[] parts = line.split(" ");
            if(parts.length == 2)
                return new Instruction(parts[0], parts[1].charAt(0), null);
            return new Instruction(parts[0], parts[1].charAt(0), parts[2]);
        });
    };

    private Long getLong(Map<Character, Long> values, Instruction instruction) {
        if(Character.isAlphabetic(instruction.v.charAt(0)))
            return values.get(instruction.v.charAt(0));
        return Long.parseLong(instruction.v);
    }

    private Map<Character, Long> alu(List<Instruction> instructions, Map<Character, Long> values, String inputString) {
        char[] inputChars = inputString.toCharArray();
        int charIdx = 0;

        for (Instruction instruction : instructions) {
            switch (instruction.type) {
                case "inp" -> values.put(instruction.c, (long) (inputChars[charIdx++] - '0'));
                case "add" -> values.put(instruction.c, values.get(instruction.c) + getLong(values, instruction));
                case "mul" -> values.put(instruction.c, values.get(instruction.c) * getLong(values, instruction));
                case "div" -> values.put(instruction.c, values.get(instruction.c) / getLong(values, instruction));
                case "mod" -> values.put(instruction.c, values.get(instruction.c) % getLong(values, instruction));
                case "eql" -> values.put(instruction.c, Objects.equals(values.get(instruction.c), getLong(values, instruction)) ? 1L : 0L);
            }
        }
        return values;
    }

    public static final Map<Character, Long> template = Map.of('x', 0L, 'y', 0L, 'w', 0L, 'z', 0L);

    public Map<Character, Long> resetValues(long zValue) {
        var v = new HashMap<>(template);
        v.put('z', zValue);
        return v;
    }

    private List<List<Instruction>> splitIntoMultipleLists(List<Instruction> instructions) {
        List<List<Instruction>> multiInstructions = new ArrayList<>();
        for(Instruction i : instructions){
            if(i.type.equals("inp")) {
                multiInstructions.add(new ArrayList<>());
            }
            multiInstructions.get(multiInstructions.size()-1).add(i);
        }
        return multiInstructions;
    }



    private void solve(List<Instruction> instructions) {
        List<List<Instruction>> multiInstructionLists = splitIntoMultipleLists(instructions);

        Set<Long> zValues = new HashSet<>();
        zValues.add(0L);

        Map<CacheIdx, Long> cache = new HashMap<>();

        solveRec(multiInstructionLists, 0, zValues, cache);

    }

    record CacheIdx(int depth, long zValue, int inputNumber){};

    static long count = 0;


    private void solveRec(final List<List<Instruction>> multiInstructionLists, int depth, Set<Long> zValues, Map<CacheIdx, Long> cache) {
        List<Instruction> instructionList = multiInstructionLists.get(depth);

        Set<Long> newZValues = new HashSet<>();
        count++;
        if(count%100000==0)
            System.out.println(""+count);

        for(Long zValue : zValues) {
            var values = resetValues(zValue);
            for (int i = 9; i > 0; i--) {

                CacheIdx cidx = new CacheIdx(depth, zValue, i);
                long newZValue;

                if(!cache.containsKey(cidx)) {
                    newZValue = alu(instructionList, values, "" + i).get('z');

                    if(newZValue == 0) {
                        System.out.println("STOPPPP!!!");
                    }

                    cache.put(cidx, newZValue);
                }

                newZValues.add(cache.get(cidx));
            }
        }

        if(depth < 14) {
            solveRec(multiInstructionLists, depth+1, newZValues, cache);
        }
    }





//    @Test
//    public void testPart() {
//        Map<Character, Long> values = resetValues();
//        List<Instruction> instructions = loadData("src/test/resources/2021/D24_t.txt");
//        alu(instructions, values, "5");
//        System.out.println(values);
//    }

    @Test
    public void testPart1() {
        List<Instruction> instructions = loadData("src/test/resources/2021/D24.txt");
        solve(instructions);
    }
}
