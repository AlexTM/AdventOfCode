package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D7 {

    private static final String input = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;

    record Hand(String cards, int score, String original, HandType type) {
        public Hand(String cards, int score, String original) {
            this(cards, score, original, getHandType(cards));
        }
    }

    private static Hand parseHand(String line) {
        String[] split = line.split(" ");
        return new Hand(split[0], Integer.parseInt(split[1]), null);
    }

    private enum HandType {FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD}

    private static final List<Character> cardOrder = List.of(
            '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A');

    private static HandType getHandType(String cards) {
        Map<String, Long> freq = Arrays.stream(cards.split(""))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (freq.size() == 1) {
            return HandType.FIVE_OF_A_KIND;
        } else if (freq.size() == 2) {
            if (freq.containsValue(4L)) {
                return HandType.FOUR_OF_A_KIND;
            } else {
                return HandType.FULL_HOUSE;
            }
        } else if (freq.size() == 3) {
            if (freq.containsValue(3L)) {
                return HandType.THREE_OF_A_KIND;
            } else {
                return HandType.TWO_PAIR;
            }
        } else if (freq.size() == 4) {
            return HandType.ONE_PAIR;
        } else {
            return HandType.HIGH_CARD;
        }
    }

    private static int compareHands(Hand h1, Hand h2, List<Character> cardOrder) {

        int diff = h2.type.ordinal() - h1.type.ordinal();

        if (diff == 0) {
            char[] c1 = h1.cards.toCharArray();
            char[] c2 = h2.cards.toCharArray();
            int i = 0;
            while (diff == 0 && i<c1.length) {
                diff = cardOrder.indexOf(c1[i]) -
                        cardOrder.indexOf(c2[i]);
                i++;
            }
        }
        return diff;
    }

    private static int compareHands(Hand h1, Hand h2) {
        return compareHands(h1, h2, cardOrder);
    }

    private static long score(List<Hand> hands) {
        long score = 0;
        for (int i = 0; i < hands.size(); i++) {
            score += (long) (i + 1) * hands.get(i).score;
        }
        return score;
    }

    @Test
    public void partOne() {
        List<Hand> hands = new java.util.ArrayList<>(Arrays.stream(input.split("\n"))
                .map(D7::parseHand)
                .toList());

        hands.sort(D7::compareHands);
        assertEquals(6440, score(hands));

        List<Hand> hands2 =
                new java.util.ArrayList<>(
                        FileReader.readFileStringList("src/test/resources/2023/D7.txt").stream()
                                .map(D7::parseHand)
                                .toList());
        hands2.sort(D7::compareHands);
        assertEquals(248812215, score(hands2));
    }

    private static final List<Character> cardOrder2 = List.of(
            'J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A');

    private static int compareHands2(Hand h1, Hand h2) {
        return compareHands(h1, h2, cardOrder2);
    }

    

    @Test
    public void partTwo() {

        List<Hand> hands = new java.util.ArrayList<>(Arrays.stream(input.split("\n"))
                .map(D7::parseHand)
                .toList());

        hands.sort(D7::compareHands2);
        assertEquals(5905, score(hands));

    }
}