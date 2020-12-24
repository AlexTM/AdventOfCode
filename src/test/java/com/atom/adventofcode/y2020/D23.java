package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D23 {

    @Test
    public void testCups() {
        LinkedList<Integer> cups = new LinkedList<>(List.of(3, 8, 9, 1, 2, 5, 4, 6, 7));

        cups = doMove(cups, 0);
        assertEquals(List.of(3, 2, 8, 9, 1, 5, 4, 6, 7), cups);
        cups = doMove(cups, 1);
        assertEquals(List.of(3, 2, 5, 4, 6, 7, 8, 9, 1), cups);
        cups = doMove(cups, 2);
        assertEquals(List.of(7, 2, 5, 8, 9, 1, 3, 4, 6), cups);

//        for(int i=0; i<10; i++) {
//            System.out.println("move "+(i+1)+" "+cups);
//            doMove(cups, i);
//        }
    }

//    private List<Integer> getSublist(LinkedList<Integer> cups, int pos) {
//        List<Integer> tmp = new ArrayList<>(cups);
//        tmp.addAll(cups.subList(0,4));
//        return tmp.subList(pos+1, pos+4);
//    }

    private LinkedList<Integer> doMove(final LinkedList<Integer> cupsb, int pos) {

        pos = pos % 10;

        LinkedList<Integer> cups = new LinkedList<>();
        cups.addAll(cupsb.subList(pos, cupsb.size()));
        cups.addAll(cupsb.subList(0, pos));

        Integer currentCup = cups.get(0);
        List<Integer> nextThree = new ArrayList<>(cups.subList(1, 4));

        Integer destinationCup = currentCup - 1;
        // check is it a valid dest
        while(nextThree.contains(destinationCup)) {
            destinationCup--;
            if(destinationCup == 0)
                destinationCup = 9;
        }

        cups.remove(1);
        cups.remove(1);
        cups.remove(1);
        int inxOfDestCup = cups.indexOf(destinationCup);
        cups.addAll(inxOfDestCup+1, nextThree);

        LinkedList<Integer> res = new LinkedList<>();
        res.addAll(cupsb.subList(0, pos));
        res.addAll(cupsb.subList(pos, cupsb.size()));

        return res;
    }
}
