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

    record Hand(String cards, int score, String original) { }

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

    private static int compareHands(Hand h1, Hand h2) {
        return compareHands(h1.cards, h2.cards);
    }

    private static int compareHands(String h1, String h2) {
        HandType ht1 = getHandType(h1);
        HandType ht2 = getHandType(h2);

        int diff = ht2.ordinal() - ht1.ordinal();

        if (diff == 0) {
            char[] c1 = h1.toCharArray();
            char[] c2 = h2.toCharArray();
            int i = 0;
            while (diff == 0 && i<c1.length) {
                diff = cardOrder.indexOf(c1[i]) -
                        cardOrder.indexOf(c2[i]);
                i++;
            }
        }
        return diff;
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
                        FileReader.readFileStringList("src/main/resources/2023/D7.txt").stream()
                                .map(D7::parseHand)
                                .toList());
        hands2.sort(D7::compareHands);
        assertEquals(248812215, score(hands2));
    }

    private static Hand modifyHand(Hand hand) {
        return new Hand(modifyHand(hand.cards), hand.score, hand.cards);
    }

    private static String modifyHand(String hand) {

        Set<String> freq = Arrays.stream(hand.split("")).collect(Collectors.toSet());
        freq.add(cardOrder.get(cardOrder.size()-1).toString());

        if(!freq.contains("J")) {
            return hand;
        }

        String stripJokers = hand.replaceAll("J", "");
        int jokerCount = hand.length() - stripJokers.length();

        // try replacing the jokers with each of the new cards and check if that results in a better hand
        for(String e : freq) {
            String newCards = stripJokers + e.repeat(jokerCount);
            if(compareHands(hand, newCards) < 0){
                hand = newCards;
            }
        }

        return hand;
    }

    private static Set<String> generateCandidates(String hand) {
        Set<String> freq = Arrays.stream(hand.split("")).collect(Collectors.toSet());
        freq.add(cardOrder.get(cardOrder.size()-1).toString());

        if(!freq.contains("J")) {
            return Set.of(hand);
        }

        String stripJokers = hand.replaceAll("J", "");
        int jokerCount = hand.length() - stripJokers.length();

        // try replacing the jokers with each of the new cards and check if that results in a better hand
        Set<String> candidates = new HashSet<>();
        for(String e : freq) {
            candidates.add(stripJokers + e.repeat(jokerCount));
        }

        return candidates;
    }

    private static Set<String> genCan(String hand, Set<String> freq, int jokerCount) {

        if(jokerCount == 0) {
            return Set.of(hand);
        }

        String stripJokers = hand.replaceAll("J", "");

        // try replacing the jokers with each of the new cards and check if that results in a better hand
        Set<String> candidates = new HashSet<>();
        for(String e : freq) {
            candidates.add(stripJokers + e.repeat(jokerCount));
        }

        return candidates;
    }

    @Test
    public void partTwo() {
//        assertEquals("QQQQ2", modifyHand("QJJQ2"));


/*
        List<Hand> hands = new java.util.ArrayList<>(Arrays.stream(input.split("\n"))
                .map(D7::parseHand)
                .map(D7::modifyHand)
                .toList());
        hands.sort(D7::compareHands);
        assertEquals(5905, score(hands));


        List<Hand> hands2 =
                new java.util.ArrayList<>(FileReader.readFileStringList("src/main/resources/2023/D7.txt")
                        .stream()
                        .map(D7::parseHand)
                        .map(D7::modifyHand)
                        .toList());
        hands2.sort(D7::compareHands);

        assertNotEquals(250024765, score(hands2));
        assertEquals(0, score(hands2));
*/

    }
}