package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D25 {

    record Puzzle(List<int[]> locks, List<int[]> keys) { }

    private Puzzle parseInput(String input) {
        String[] parts = input.split("\n");
        List<int[]> locks = new ArrayList<>();
        List<int[]> keys = new ArrayList<>();

        int c = 0;
        while(c < parts.length) {
            int[] arr = new int[5];
            for(int i = 0; i < 7; i++) {
                String line = parts[c + i];
                for(int j = 0; j < 5; j++) {
                    if(line.charAt(j) == '#') {
                        arr[j] += 1;
                    }
                }
            }
            IntStream.range(0, 5).forEach(i -> arr[i] = arr[i] -1);
            if(parts[c].equals("#####")) {
                locks.add(arr);
            } else {
                keys.add(arr);
            }
            c += 8;
        }
        return new Puzzle(locks, keys);
    }

    private boolean canUnlock(int[] lock, int[] key) {
        for(int i = 0; i < 5; i++) {
            if (lock[i] + key[i] > 5) {
                return false;
            }
        }
        return true;
    }

    private int fullSearch(List<int[]> locks, List<int[]> keys) {
        int count = 0;
        for(int[] lock : locks) {
            for(int[] key : keys) {
                if(canUnlock(lock, key)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Test
    public void testPartOne() {
        Puzzle puzzle = parseInput(FileReader.readFileString("src/test/resources/2024/D25.txt"));
        assertEquals(3344, fullSearch(puzzle.locks, puzzle.keys));
    }
}
