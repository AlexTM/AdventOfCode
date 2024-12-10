package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D9 {

    record Data(int value, int size, int free){}

    private List<Data> parseInput(String input) {
        List<Data> blocks = new ArrayList<>();
        int pos = 0;
        while(pos < input.length()) {
            int size = Character.getNumericValue(input.charAt(pos++));
            int free = 0;
            if(pos < input.length())
                free = Character.getNumericValue(input.charAt(pos++));
            blocks.add(new Data(blocks.size(), size, free));
        }
        return blocks;
    }

    private List<Integer> expandFormat(List<Data> input) {
        List<Integer> idxs = new ArrayList<>();
        for (Data block : input) {
            for (int j = 0; j < block.size; j++)
                idxs.add(block.value);
            for (int j = 0; j < block.free; j++)
                idxs.add(null);
        }
        return idxs;
    }

    private List<Integer> compactFileWithFragmentation(List<Integer> input) {
        List<Integer> output = new ArrayList<>();
        int start = 0;
        int end = input.size()-1;
        while(start <= end) {
            Integer c = input.get(start);
            while (c == null && start <= end) {
                c = input.get(end--);
            }
            output.add(c);
            start++;
        }
        return output;
    }

    private long computeCheckSum(List<Integer> input) {
        long sum = 0;
        for(int i=0; i<input.size(); i++) {
            if(input.get(i) != null)
                sum += input.get(i) * i;
        }
        return sum;
    }

    private static final String TEST_INPUT = "2333133121414131402";

    @Test
    public void testPartOne() {
        assertEquals(1928, computeCheckSum(compactFileWithFragmentation(expandFormat(parseInput(TEST_INPUT)))));

        long res = computeCheckSum(compactFileWithFragmentation(expandFormat(parseInput(
                FileReader.readFileString("src/test/resources/2024/D9.txt")))));
        assertNotEquals(91111740342L, res);
        assertEquals(6421128769094L, res);
    }

    private static void print(List<Integer> ns) {
        ns.stream().forEach(n -> {
            if(n == null)
                System.out.print(".");
            else
                System.out.print(n);
        });
    }

    /**
     * 00...111...2...333.44.5555.6666.777.888899
     * 0099.111...2...333.44.5555.6666.777.8888..
     * 0099.1117772...333.44.5555.6666.....8888..
     * 0099.111777244.333....5555.6666.....8888..
     * 00992111777.44.333....5555.6666.....8888..
     *
     * This takes for ever and produces the wrong result
     */
    private List<Data> compactFileWithoutFragmentation(List<Data> input) {
        List<Data> linkedList = new LinkedList<>(input);
        Set<Integer> moved = new HashSet<>();

        int idxRHS = linkedList.size()-1;
        while(idxRHS > 0) {
            Data rhs = linkedList.get(idxRHS);

            if(!moved.contains(rhs.value)) {
                moved.add(rhs.value);

                System.out.println(rhs.value);

                // find free space for this block
                for (int idxLHS = 0; idxLHS < idxRHS; idxLHS++) {
                    Data lhs = linkedList.get(idxLHS);

                    if (lhs.free >= rhs.size) {
                        // Can move here
                        Data rhsPrev = linkedList.get(idxRHS-1);

                        linkedList.set(idxLHS, new Data(lhs.value, lhs.size, 0));
                        linkedList.set(idxRHS - 1, new Data(rhsPrev.value, rhsPrev.size, rhsPrev.free + rhs.size + rhs.free));
                        linkedList.add(idxLHS + 1, new Data(rhs.value, rhs.size, lhs.free - rhs.size));
                        linkedList.remove(idxRHS+1);
                        idxRHS++;
                        break;
                    }
                }
            }
            idxRHS--;
        }

        return linkedList;
    }

    @Test
    public void testPartTwo() {
        assertEquals(2858, computeCheckSum(expandFormat(compactFileWithoutFragmentation(parseInput(TEST_INPUT)))));

        long res = computeCheckSum(expandFormat(compactFileWithoutFragmentation(parseInput(
                FileReader.readFileString("src/test/resources/2024/D9.txt")))));
        assertNotEquals(6448192553234L, res);
        assertEquals(0, res);
    }
}
