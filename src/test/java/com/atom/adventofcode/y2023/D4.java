package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D4 {

    private static final String input1 = """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
            """;

    record Card(int num, List<Integer> winning, List<Integer> numbers) {}

    public static Card parseLine(String line) {
        String[] split = line.split(":");
        String[] cards = split[1].split("\\|");
        String[] card1 = cards[0].split(" ");
        String[] card2 = cards[1].split(" ");
        String[] num = split[0].split(" ");

        List<Integer> winning = Arrays.stream(card1).map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt).toList();
        List<Integer> numbers = Arrays.stream(card2).map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt).toList();

        return new Card(Integer.parseInt(num[num.length-1]), winning, numbers);
    }

    private int scoreCard(Card card) {
        Set<Integer> n = new HashSet<>(card.numbers);
        Set<Integer> w = new HashSet<>(card.winning);
        w.retainAll(n);
        return (int)Math.pow(2, w.size()-1);
    }

    private int processCards(List<Card> cards) {
        Map<Integer, Integer> score = new HashMap<>();

        for(Card card : cards) {
            Set<Integer> n = new HashSet<>(card.numbers);
            Set<Integer> w = new HashSet<>(card.winning);
            w.retainAll(n);

            if(!score.containsKey(card.num))
                score.put(card.num, 1);

            for(int i=1; i<=w.size(); i++) {
                int thisCardScore = score.get(card.num);
                int x = score.getOrDefault(i+card.num, 1);
                score.put(i+card.num, x+thisCardScore);
            }
        }

        return score.values().stream().mapToInt(Integer::intValue).sum();
    }

    @Test
    public void partOne() {
        int score = FileReader.readFileStringList("src/main/resources/2023/D4.txt")
                .stream().map(D4::parseLine)
                .mapToInt(this::scoreCard).sum();
        assertEquals(20667, score);
    }

    @Test
    public void partTwo() {
        List<Card> cards = Arrays.stream(input1.split("\n"))
                .map(D4::parseLine).toList();
        assertEquals(30, processCards(cards));

        cards = FileReader.readFileStringList("src/main/resources/2023/D4.txt")
                .stream().map(D4::parseLine).toList();
        assertEquals(5833065, processCards(cards));
    }
}
