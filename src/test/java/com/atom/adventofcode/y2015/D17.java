package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D17 {

    @Test
    public void testEggnog() {
        int[] testInts = new int[]{5, 5, 10, 15, 20};
        int[] containerSizes = new int[]{11, 30, 47, 31, 32, 36, 3, 1, 5, 3, 32, 36, 15, 11, 46, 26, 28, 1, 19, 3};
        assertEquals(4, findCombinations(testInts, 25, false));
        assertEquals(4372, findCombinations(containerSizes,150, false));
        assertEquals(3, findCombinations(testInts, 25, true));
        assertEquals(4, findCombinations(containerSizes,150, true));
    }

    private int findCombinations(int[] containers, int target, boolean smallestSolution) {
        int solutions = 0;
        for(int i=0; i<containers.length; i++) {
            int[] selectedContainers = new int[i+1];
            solutions += solutionsWithNContainers(containers, selectedContainers, target, 0);

            if(smallestSolution && solutions > 0)
                break;
        }
        return solutions;
    }

    private int solve(int[] containers, int[] selectedContainers) {
        return Arrays.stream(selectedContainers).reduce(0, (x, y) -> x + containers[y]);
    }

    private int solutionsWithNContainers(
            final int[] availableContainers,
            final int[] selectedContainers,
            int target, int depth) {

        if(depth == selectedContainers.length)
            return solve(availableContainers, selectedContainers) == target ? 1 : 0;

        int start = depth == 0 ? 0 : selectedContainers[depth-1]+1;

        int solutions = 0;
        for(int i=start; i<availableContainers.length; i++) {
            selectedContainers[depth] = i;
            solutions += solutionsWithNContainers(availableContainers, selectedContainers, target, depth+1);
        }
        return solutions;
    }
}
