package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 23: Crab Cups ---
 * The small crab challenges you to a game! The crab is going to mix up some cups, and you have to predict where
 * they'll end up.
 *
 * The cups will be arranged in a circle and labeled clockwise (your puzzle input). For example, if your labeling
 * were 32415, there would be five cups in the circle; going clockwise around the circle from the first cup, the cups
 * would be labeled 3, 2, 4, 1, 5, and then back to 3 again.
 *
 * Before the crab starts, it will designate the first cup in your list as the current cup. The crab is then going to
 * do 100 moves.
 *
 * Each move, the crab does the following actions:
 *
 * The crab picks up the three cups that are immediately clockwise of the current cup. They are removed from the
 * circle; cup spacing is adjusted as necessary to maintain the circle.
 * The crab selects a destination cup: the cup with a label equal to the current cup's label minus one. If this would
 * select one of the cups that was just picked up, the crab will keep subtracting one until it finds a cup that
 * wasn't just picked up. If at any point in this process the value goes below the lowest value on any cup's label,
 * it wraps around to the highest value on any cup's label instead.
 * The crab places the cups it just picked up so that they are immediately clockwise of the destination cup. They
 * keep the same order as when they were picked up.
 * The crab selects a new current cup: the cup which is immediately clockwise of the current cup.
 * For example, suppose your cup labeling were 389125467. If the crab were to do merely 10 moves, the following
 * changes would occur:
 *
 * -- move 1 --
 * cups: (3) 8  9  1  2  5  4  6  7
 * pick up: 8, 9, 1
 * destination: 2
 *
 * -- move 2 --
 * cups:  3 (2) 8  9  1  5  4  6  7
 * pick up: 8, 9, 1
 * destination: 7
 *
 * -- move 3 --
 * cups:  3  2 (5) 4  6  7  8  9  1
 * pick up: 4, 6, 7
 * destination: 3
 *
 * -- move 4 --
 * cups:  7  2  5 (8) 9  1  3  4  6
 * pick up: 9, 1, 3
 * destination: 7
 *
 * -- move 5 --
 * cups:  3  2  5  8 (4) 6  7  9  1
 * pick up: 6, 7, 9
 * destination: 3
 *
 * -- move 6 --
 * cups:  9  2  5  8  4 (1) 3  6  7
 * pick up: 3, 6, 7
 * destination: 9
 *
 * -- move 7 --
 * cups:  7  2  5  8  4  1 (9) 3  6
 * pick up: 3, 6, 7
 * destination: 8
 *
 * -- move 8 --
 * cups:  8  3  6  7  4  1  9 (2) 5
 * pick up: 5, 8, 3
 * destination: 1
 *
 * -- move 9 --
 * cups:  7  4  1  5  8  3  9  2 (6)
 * pick up: 7, 4, 1
 * destination: 5
 *
 * -- move 10 --
 * cups: (5) 7  4  1  8  3  9  2  6
 * pick up: 7, 4, 1
 * destination: 3
 *
 * -- final --
 * cups:  5 (8) 3  7  4  1  9  2  6
 * In the above example, the cups' values are the labels as they appear moving clockwise around the circle; the
 * current cup is marked with ( ).
 *
 * After the crab is done, what order will the cups be in? Starting after the cup labeled 1, collect the other cups'
 * labels clockwise into a single string with no extra characters; each number except 1 should appear exactly once.
 * In the above example, after 10 moves, the cups clockwise from 1 are labeled 9, 2, 6, 5, and so on, producing
 * 92658374. If the crab were to complete all 100 moves, the order after cup 1 would be 67384529.
 *
 * Using your labeling, simulate 100 moves. What are the labels on the cups after cup 1?
 *
 * Your puzzle answer was 75893264.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 *
 * --- Part Two ---
 * Due to what you can only assume is a mistranslation (you're not exactly fluent in Crab), you are quite surprised
 * when the crab starts arranging many cups in a circle on your raft - one million (1000000) in total.
 *
 * Your labeling is still correct for the first few cups; after that, the remaining cups are just numbered in an
 * increasing fashion starting from the number after the highest number in your list and proceeding one by one until
 * one million is reached. (For example, if your labeling were 54321, the cups would be numbered 5, 4, 3, 2, 1, and
 * then start counting up from 6 until one million is reached.) In this way, every number from one through one
 * million is used exactly once.
 *
 * After discovering where you made the mistake in translating Crab Numbers, you realize the small crab isn't going
 * to do merely 100 moves; the crab is going to do ten million (10000000) moves!
 *
 * The crab is going to hide your stars - one each - under the two cups that will end up immediately clockwise of cup
 * 1. You can have them if you predict what the labels on those cups will be when the crab is finished.
 *
 * In the above example (389125467), this would be 934001 and then 159792; multiplying these together produces
 * 149245887792.
 *
 * Determine which two cups will end up immediately clockwise of cup 1. What do you get if you multiply their labels
 * together?
 */
public class D23 {

/*    @Test
    public void playCups2() {
        LinkedList<Integer> cups = new LinkedList<>(List.of(3, 8, 9, 1, 2, 5, 4, 6, 7));
        for(int i=10; i<1000000; i++)
            cups.add(i);

        for(int i=0; i<10000000; i++)
            cups = doMove(cups, i, 999999);

        Iterator<Integer> cup = cups.iterator();
        while(cup.hasNext()) {
            if(cup.next() == 1)
                break;
        }
        long ans = (long)cup.next()* (long)cup.next();
        assertEquals("149245887792", ans);
    }*/


    @Test
    public void playCups() {
        LinkedList<Integer> cups = new LinkedList<>(List.of(9,7,4,6,1,8,3,5,2));
        for(int i=0; i<100; i++)
            cups = doMove(cups, i, 9);
        assertEquals("75893264", getAnswer(cups));
    }

    @Test
    public void testCups() {
        LinkedList<Integer> cups = new LinkedList<>(List.of(3, 8, 9, 1, 2, 5, 4, 6, 7));

        int max = 9;

        cups = doMove(cups, 0, max);
        assertEquals(List.of(3, 2, 8, 9, 1, 5, 4, 6, 7), cups);
        cups = doMove(cups, 1, max);
        assertEquals(List.of(3, 2, 5, 4, 6, 7, 8, 9, 1), cups);
        cups = doMove(cups, 2, max);
        assertEquals(List.of(7, 2, 5, 8, 9, 1, 3, 4, 6), cups);
        cups = doMove(cups, 3, max);
        assertEquals(List.of(3,  2,  5,  8, 4, 6,  7,  9,  1), cups);
        cups = doMove(cups, 4, max);
        assertEquals(List.of(9,  2,  5,  8,  4, 1, 3,  6,  7), cups);
        cups = doMove(cups, 5, max);
        assertEquals(List.of(7,  2,  5,  8,  4,  1, 9, 3,  6), cups);
        cups = doMove(cups, 6, max);
        assertEquals(List.of(8,  3,  6,  7,  4,  1,  9,2, 5), cups);
        cups = doMove(cups, 7, max);
        assertEquals(List.of(7,  4,  1,  5,  8,  3,  9,  2, 6), cups);
        cups = doMove(cups, 8, max);
        assertEquals(List.of(5, 7,  4,  1,  8,  3,  9,  2,  6), cups);
        cups = doMove(cups, 9, max);
        assertEquals(List.of(5, 8, 3,  7,  4,  1,  9,  2,  6), cups);

        assertEquals("92658374", getAnswer(cups));

        cups = new LinkedList<>(List.of(3, 8, 9, 1, 2, 5, 4, 6, 7));
        for(int i=0; i<100; i++)
            cups = doMove(cups, i, max);
        assertEquals("67384529", getAnswer(cups));
    }

    private String getAnswer(LinkedList<Integer> cups) {
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        for(int i=0; i<cups.size(); i++) {
            if(cups.get(i) == 1) {
                pos = i;
                break;
            }
        }
        for(int i=pos+1; i<cups.size(); i++)
            sb.append(cups.get(i));
        for(int i=0; i<pos; i++)
            sb.append(cups.get(i));
        return sb.toString();
    }

    @Test
    public void testCupsGame() {
        LinkedList<Integer> cups = new LinkedList<>(List.of(3, 8, 9, 1, 2, 5, 4, 6, 7));
        for(int i=0; i<100; i++) {
            cups = doMove(cups, 0, 9);
        }
        String res = "";
        assertEquals("67384529", res);
    }


    private LinkedList<Integer> doMove(final LinkedList<Integer> cupsb, int pos, int max) {

        pos = pos % cupsb.size();

        LinkedList<Integer> cups = new LinkedList<>();
        cups.addAll(cupsb.subList(pos, cupsb.size()));
        cups.addAll(cupsb.subList(0, pos));

        Integer currentCup = cups.get(0);
        List<Integer> nextThree = new ArrayList<>(cups.subList(1, 4));

        Integer destinationCup = currentCup - 1;
        // check is it a valid dest
        while(nextThree.contains(destinationCup) || destinationCup == 0) {
            destinationCup--;
            if(destinationCup <= 0)
                destinationCup = max;
        }

        cups.remove(1);
        cups.remove(1);
        cups.remove(1);
        int inxOfDestCup = cups.indexOf(destinationCup);
        cups.addAll(inxOfDestCup+1, nextThree);

        LinkedList<Integer> res = new LinkedList<>();
        res.addAll(cups.subList(cups.size()-pos, cups.size()));
        res.addAll(cups.subList(0, cups.size()-pos));

        return res;
    }
}
