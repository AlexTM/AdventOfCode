package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D22 {

    private long calculateWinnings(Queue<Integer> winningDeck) {
        long total = 0;
        long mult = winningDeck.size();
        while(!winningDeck.isEmpty()) {
            total += mult*winningDeck.poll();
            mult--;
        }
        return total;
    }


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

    int[] player1Cards = new int[]{ 31, 24, 5, 33, 7, 12, 30, 22, 48, 14, 16, 26, 18, 45, 4, 42, 25, 20, 46, 21, 40, 38, 34, 17, 50 };
    int[] player2Cards = new int[] {1, 3, 41, 8, 37, 35, 28, 39, 43, 29, 10, 27, 11, 36, 49, 32, 2, 23, 19, 9, 13, 15, 47, 6, 44 };

    @Test
    public void playCards() {
        Queue<Integer> player1 = new ArrayDeque<>();
        Queue<Integer> player2 = new ArrayDeque<>();
        for(int i : player1Cards)
            player1.add(i);
        for(int i : player2Cards)
            player2.add(i);

        while(!player1.isEmpty() && !player2.isEmpty()) {
            playRound(player1, player2);
        }
        assertEquals(36257, calculateWinnings(player1));
        assertEquals(0, calculateWinnings(player2));
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
        }
    }


    @Test
    public void testCardsRecursive() {
        // Would recurse forever
        Queue<Integer> player1 = new ArrayDeque<>();
        Queue<Integer> player2 = new ArrayDeque<>();
        for(int i : new int[]{ 43, 19 })
            player1.add(i);
        for(int i : new int[]{ 2, 29, 14 })
            player2.add(i);

        while(!player1.isEmpty() && !player2.isEmpty()) {
            playRound(player1, player2);
        }
        assertEquals(306, calculateWinnings(player2));
    }

    private String generateUniqueStateString(Queue<Integer> player1, Queue<Integer> player2) {
        StringBuilder sb = new StringBuilder();
        for(Integer i : player1)
            sb.append(i);
        sb.append(";");
        for(Integer i : player2)
            sb.append(i);
        return sb.toString();
    }

    @Test
    public void testCards2() {
        Queue<Integer> player1 = new ArrayDeque<>();
        Queue<Integer> player2 = new ArrayDeque<>();

        for(int i : new int[]{ 9, 2, 6, 3, 1 })
            player1.add(i);
        for(int i : new int[]{ 5, 8, 4, 7, 10 })
            player2.add(i);

        playGameRecursive(player1, player2);
        assertEquals(291, calculateWinnings(player2));
    }

    @Test
    public void playCards2() {
        Queue<Integer> player1 = new ArrayDeque<>();
        Queue<Integer> player2 = new ArrayDeque<>();
        for(int i : player1Cards)
            player1.add(i);
        for(int i : player2Cards)
            player2.add(i);

        playGameRecursive(player1, player2);
        assertEquals(33304, calculateWinnings(player1));
    }


    private int playGameRecursive(Queue<Integer> player1, Queue<Integer> player2) {
        Set<String> states = new HashSet<>();
        while(!player1.isEmpty() && !player2.isEmpty()) {
            String state = generateUniqueStateString(player1, player2);
            if(states.contains(state))
                return 1;
            states.add(state);

            playRoundRecursive(player1, player2);
        }
        return player1.isEmpty() ? 2 : 1;
    }

    private int playRoundRecursive(Queue<Integer> player1, Queue<Integer> player2) {

        int c1 = player1.poll();
        int c2 = player2.poll();

        int winner = -1;

        if(player1.size() >= c1 && player2.size() >= c2) {
            winner = playGameRecursive(
                    new ArrayDeque<>(new ArrayList<>(player1).subList(0, c1)),
                    new ArrayDeque<>(new ArrayList<>(player2).subList(0, c2)));
        } else {
            if (c1 > c2) {
                winner = 1;
            } else if (c1 < c2) {
                winner = 2;
            }
        }

        if(winner == 1) {
            player1.add(c1);
            player1.add(c2);
        } else if(winner == 2) {
            player2.add(c2);
            player2.add(c1);
        } else {

        }
        return winner;
    }
}
