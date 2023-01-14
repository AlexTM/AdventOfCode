package com.atom.adventofcode.y2021;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 6: Lanternfish ---
 * The sea floor is getting steeper. Maybe the sleigh keys got carried this way?
 *
 * A massive school of glowing lanternfish swims past. They must spawn quickly to reach such large numbers - maybe
 * exponentially quickly? You should model their growth rate to be sure.
 *
 * Although you know nothing about this specific species of lanternfish, you make some guesses about their attributes
 * . Surely, each lanternfish creates a new lanternfish once every 7 days.
 *
 * However, this process isn't necessarily synchronized between every lanternfish - one lanternfish might have 2 days
 * left until it creates another lanternfish, while another might have 4. So, you can model each fish as a single
 * number that represents the number of days until it creates a new lanternfish.
 *
 * Furthermore, you reason, a new lanternfish would surely need slightly longer before it's capable of producing more
 * lanternfish: two more days for its first cycle.
 *
 * So, suppose you have a lanternfish with an internal timer value of 3:
 *
 * After one day, its internal timer would become 2.
 * After another day, its internal timer would become 1.
 * After another day, its internal timer would become 0.
 * After another day, its internal timer would reset to 6, and it would create a new lanternfish with an internal
 * timer of 8.
 * After another day, the first lanternfish would have an internal timer of 5, and the second lanternfish would have
 * an internal timer of 7.
 * A lanternfish that creates a new fish resets its timer to 6, not 7 (because 0 is included as a valid timer value).
 * The new lanternfish starts with an internal timer of 8 and does not start counting down until the next day.
 *
 * Realizing what you're trying to do, the submarine automatically produces a list of the ages of several hundred
 * nearby lanternfish (your puzzle input). For example, suppose you were given the following list:
 *
 * 3,4,3,1,2
 * This list means that the first fish has an internal timer of 3, the second fish has an internal timer of 4, and so
 * on until the fifth fish, which has an internal timer of 2. Simulating these fish over several days would proceed
 * as follows:
 *
 * Initial state: 3,4,3,1,2
 * After  1 day:  2,3,2,0,1
 * After  2 days: 1,2,1,6,0,8
 * After  3 days: 0,1,0,5,6,7,8
 * After  4 days: 6,0,6,4,5,6,7,8,8
 * After  5 days: 5,6,5,3,4,5,6,7,7,8
 * After  6 days: 4,5,4,2,3,4,5,6,6,7
 * After  7 days: 3,4,3,1,2,3,4,5,5,6
 * After  8 days: 2,3,2,0,1,2,3,4,4,5
 * After  9 days: 1,2,1,6,0,1,2,3,3,4,8
 * After 10 days: 0,1,0,5,6,0,1,2,2,3,7,8
 * After 11 days: 6,0,6,4,5,6,0,1,1,2,6,7,8,8,8
 * After 12 days: 5,6,5,3,4,5,6,0,0,1,5,6,7,7,7,8,8
 * After 13 days: 4,5,4,2,3,4,5,6,6,0,4,5,6,6,6,7,7,8,8
 * After 14 days: 3,4,3,1,2,3,4,5,5,6,3,4,5,5,5,6,6,7,7,8
 * After 15 days: 2,3,2,0,1,2,3,4,4,5,2,3,4,4,4,5,5,6,6,7
 * After 16 days: 1,2,1,6,0,1,2,3,3,4,1,2,3,3,3,4,4,5,5,6,8
 * After 17 days: 0,1,0,5,6,0,1,2,2,3,0,1,2,2,2,3,3,4,4,5,7,8
 * After 18 days: 6,0,6,4,5,6,0,1,1,2,6,0,1,1,1,2,2,3,3,4,6,7,8,8,8,8
 * Each day, a 0 becomes a 6 and adds a new 8 to the end of the list, while each other number decreases by 1 if it
 * was present at the start of the day.
 *
 * In this example, after 18 days, there are a total of 26 fish. After 80 days, there would be a total of 5934.
 *
 * Find a way to simulate lanternfish. How many lanternfish would there be after 80 days?
 *
 * --- Part Two ---
 * Suppose the lanternfish live forever and have unlimited food and space. Would they take over the entire ocean?
 *
 * After 256 days in the example above, there would be a total of 26984457539 lanternfish!
 *
 * How many lanternfish would there be after 256 days?
 *
 */
public class D6 {

    int[] puzzleInput =
            new int[]{3,5,1,2,5,4,1,5,1,2,5,5,1,3,1,5,1,3,2,1,5,1,1,1,2,3,1,3,1,2,1,1,5,1,5,4,5,5,
                    3,3,1,5,1,1,5,5,1,3,5,5,3,2,2,4,1,5,3,4,2,5,4,1,2,2,5,1,1,2,4,4,1,3,1,3,1,1,2,
                    2,1,1,5,1,1,4,4,5,5,1,2,1,4,1,1,4,4,3,4,2,2,3,3,2,1,3,3,2,1,1,1,2,1,4,2,2,1,5,
                    5,3,4,5,5,2,5,2,2,5,3,3,1,2,4,2,1,5,1,1,2,3,5,5,1,1,5,5,1,4,5,3,5,2,3,2,4,3,1,
                    4,2,5,1,3,2,1,1,3,4,2,1,1,1,1,2,1,4,3,1,3,1,2,4,1,2,4,3,2,3,5,5,3,3,1,2,3,4,5,
                    2,4,5,1,1,1,4,5,3,5,3,5,1,1,5,1,5,3,1,2,3,4,1,1,4,1,2,4,1,5,4,1,5,4,2,1,5,2,1,
                    3,5,5,4,5,5,1,1,4,1,2,3,5,3,3,1,1,1,4,3,1,1,4,1,5,3,5,1,4,2,5,1,1,4,4,4,2,5,1,
                    2,5,2,1,3,1,5,1,2,1,1,5,2,4,2,1,3,5,5,4,1,1,1,5,5,2,1,1
            };

    @Test
    public void testBothParts() {
        assertEquals(26, thirdSolution(new int[]{3, 4, 3, 1, 2}, 18));
        assertEquals(5934, thirdSolution(new int[]{3, 4, 3, 1, 2}, 80));
        assertEquals(26984457539L, thirdSolution(new int[]{3, 4, 3, 1, 2}, 256));

        assertEquals(354564, thirdSolution(puzzleInput, 80));
        assertEquals(1609058859115L, thirdSolution(puzzleInput, 256));

    }

    public Long[] convertFishAges(int[] fishAges) {
        Long[] hist = new Long[]{0L,0L,0L,0L,0L,0L,0L};
        for(int i=0; i<fishAges.length; i++) {
            hist[fishAges[i]]++;
        }
        return hist;
    }

    /**
     * Quick
     */
    public long thirdSolution(int[] fishAges, int days) {
        Queue<Long> lifeCycle = new ArrayBlockingQueue<>(7);
        Queue<Long> babyCycle = new ArrayBlockingQueue<>(2);
        Collections.addAll(babyCycle, 0L, 0L);
        Collections.addAll(lifeCycle, convertFishAges(fishAges));

        for(int day=0; day<days; day++) {
            Long nextFish = lifeCycle.poll();
            lifeCycle.add(nextFish + babyCycle.poll());
            babyCycle.add(nextFish);
        }

        return lifeCycle.stream().reduce(0L, Long::sum) + babyCycle.stream().reduce(0L, Long::sum);
    }


    /////////////////////

    /**
     * Tried to be clever and couldn't get this to work and ran out of time.
     *
     * Use 1 array and with a pointer to indicate dayZero, each index contains
     * number of fish
     */
    public long[] secondSolution(long[] fishAge, int days) {

        for(int day=0; day<days; day++) {
            int dayZeroIs = day % 7;
            int daySevenIs = (day-1) % 7;

            long newFish = fishAge[dayZeroIs];
            int firstDayFirstCycle = day % 2;
            if(day>0) {
                int lastDayFirstCycle = (day - 1) % 2;
                fishAge[7+firstDayFirstCycle] = newFish;
                fishAge[daySevenIs] += fishAge[7+lastDayFirstCycle];
                fishAge[7+lastDayFirstCycle] = 0;
            }
        }

        return fishAge;
    }


    /**
     * Didn't scale
     *
     * @param fish
     * @param days
     * @return
     */
    public long firstSolution(List<Integer> fish, int days) {
        List<Integer> fishList = new ArrayList<>(fish);

        for(int i=0; i<days; i++) {
            for(int j=0; j<fishList.size(); j++) {
                fishList.set(j, fishList.get(j)-1);
                if(fishList.get(j) == -1) {
                    fishList.set(j, 6);
                    fishList.add(9);
                }
            }
        }

        return fishList.size();
    }
}
