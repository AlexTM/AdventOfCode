package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D22 {

    @Test
    public void testCards() {
        Queue<Integer> player1 = new ArrayDeque<>();
        Queue<Integer> player2 = new ArrayDeque<>();
        for(int i : new int[]{ 9, 2, 6, 3, 1 })
            player1.add(i);
        for(int i : new int[]{ 5, 8, 4, 7, 10 })
            player2.add(i);

        while(!player1.isEmpty() && !player2.isEmpty()) {
            playRound(player1, player2);
        }
        assertEquals(306, calculateWinnings(player2));
    }

    @Test
    public void playCards() {
        Queue<Integer> player1 = new ArrayDeque<>();
        Queue<Integer> player2 = new ArrayDeque<>();
        for(int i : new int[]{ 31,
                24,
                5,
                33,
                7,
                12,
                30,
                22,
                48,
                14,
                16,
                26,
                18,
                45,
                4,
                42,
                25,
                20,
                46,
                21,
                40,
                38,
                34,
                17,
                50 })
            player1.add(i);
        for(int i : new int[]{ 1,
                3,
                41,
                8,
                37,
                35,
                28,
                39,
                43,
                29,
                10,
                27,
                11,
                36,
                49,
                32,
                2,
                23,
                19,
                9,
                13,
                15,
                47,
                6,
                44 })
            player2.add(i);

        while(!player1.isEmpty() && !player2.isEmpty()) {
            playRound(player1, player2);
        }
        assertEquals(36257, calculateWinnings(player1));
        assertEquals(0, calculateWinnings(player2));
    }


    private long calculateWinnings(Queue<Integer> winningDeck) {
        long total = 0;
        long mult = winningDeck.size();
        while(!winningDeck.isEmpty()) {
            total += mult*winningDeck.poll();
            mult--;
        }
        return total;
    }

    private void playRound(Queue<Integer> player1, Queue<Integer> player2) {

        int c1 = player1.poll();
        int c2 = player2.poll();

        if(c1 > c2) {
            player1.add(c1);
            player1.add(c2);
        } else if(c1 < c2) {
            player2.add(c2);
            player2.add(c1);
        } else {

        }

    }
}
