package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D1 {
    private static final Map<String, Integer> words =
            Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5,
                    "six", 6, "seven", 7, "eight", 8, "nine", 9);

    private final String input = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """;

    private static int twoPointerApproach(String input) {
        int i = 0, j = input.length() - 1;
        while (i < j) {
            char c1 = input.charAt(i);
            char c2 = input.charAt(j);
            if (Character.isDigit(c1) && Character.isDigit(c2)) {
                return Integer.parseInt(c1 + "" + c2);
            } else if (Character.isDigit(c1)) {
                j--;
            } else if (Character.isDigit(c2)) {
                i++;
            } else {
                i++;
                j--;
            }
        }
        if (i == j && Character.isDigit(input.charAt(i))) {
            return Integer.parseInt(input.charAt(i) + ""+input.charAt(i));
        }
        return 0;
    }

    private static int getNumberFromString(String input) {
        char first = 0, last = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (Character.isDigit(c)) {
                if (first == 0) {
                    first = last = c;
                } else {
                    last = c;
                }
            }
        }
        String s = first + "" + last;
        return Integer.parseInt(s);
    }


    private static String replaceWords(String input) {
        TreeMap<Integer, String> indexes = new TreeMap<>();
        for(Map.Entry<String, Integer> entry : words.entrySet()) {
            if(input.contains(entry.getKey())) {
                int idx = 0;
                while(true) {
                    idx = input.indexOf(entry.getKey(), idx);
                    if(idx == -1) break;
                    indexes.put(idx, entry.getKey());
                    idx++;
                }
            }
        }

        List<Integer> list = new ArrayList<>(indexes.keySet());
        if(!list.isEmpty()) {
            String first = indexes.get(list.get(0));
            input = input.replaceFirst(first, words.get(first).toString());
            if(list.size() > 1) {
                String last = indexes.get(list.get(list.size() - 1));
//                input = input.replaceAll(last, words.get(last).toString());
                 input = new StringBuilder(input).reverse().toString();
                 String revlast = new StringBuilder(last).reverse().toString();
                input = input.replaceFirst(revlast, words.get(last).toString());
                input = new StringBuilder(input).reverse().toString();

            }
        }
//        for(Map.Entry<Integer, String> entry : indexes.entrySet()) {
//            input = input.replaceFirst(entry.getValue(), words.get(entry.getValue()).toString());
//        }

//        List<Integer> list = new ArrayList<>(indexes.keySet());//.forEach(System.out::println);
//        System.out.println("");
//
////        String front = input;
////        String back = input;
//        System.out.println(input);
//        if(!list.isEmpty()) {
//            int first = list.get(0);
//            input = replace(input, indexes.get(first), first);
//            System.out.println(input);
//
//            if (list.size() > 1) {
//                int last = list.get(list.size() - 1);
//                input = replace(input, indexes.get(last), last - indexes.get(first).length() + 1);
//                System.out.println(input);
//            }
//        }
////        input = replace(input, indexes.get(last), first, last+indexes.get(last).length());

        return input;
    }

    @Test
    public void simpleTests() {
        System.out.println(replaceWords("two1nine"));
    }

    private static String replace(String input, String value, int start) {
        return input.substring(0, start) + words.get(value) + input.substring(start+value.length());
    }

    /**
     * --- Day 1: Trebuchet?! ---
     *
     * Something is wrong with global snow production, and you've been selected to take a look. The Elves have even
     * given you a map; on it, they've used stars to mark the top fifty locations that are likely to be having problems.
     *
     * You've been doing this long enough to know that to restore snow operations, you need to check all fifty stars by
     * December 25th.
     *
     * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar; the second
     * puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
     *
     * You try to ask why they can't just use a weather machine ("not powerful enough") and where they're even sending you
     * ("the sky") and why your map looks mostly blank ("you sure ask a lot of questions") and hang on did you just say the
     * sky ("of course, where do you think snow comes from") when you realize that the Elves are already loading you into a
     * trebuchet ("please hold still, we need to strap you in").
     *
     * As they're making the final adjustments, they discover that their calibration document (your puzzle input) has been
     * amended by a very young Elf who was apparently just excited to show off her art skills. Consequently, the Elves are
     * having trouble reading the values on the document.
     *
     * The newly-improved calibration document consists of lines of text; each line originally contained a specific
     * calibration value that the Elves now need to recover. On each line, the calibration value can be found by combining
     * the first digit and the last digit (in that order) to form a single two-digit number.
     *
     * For example:
     *
     * 1abc2
     * pqr3stu8vwx
     * a1b2c3d4e5f
     * treb7uchet
     *
     * In this example, the calibration values of these four lines are 12, 38, 15, and 77. Adding these together
     * produces 142.
     *
     * Consider your entire calibration document. What is the sum of all of the calibration values?
     */
    @Test
    public void partOne() {
        int res = Arrays.stream(input.split("\n"))
                .mapToInt(D1::twoPointerApproach)
                .sum();
        assertEquals(142, res);

        res = FileReader.readFileStringList("src/main/resources/2023/D1.txt")
                .stream()
                .mapToInt(D1::twoPointerApproach)
                .sum();
        assertEquals(55002, res);
    }

    private static final String input2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """;

    /**
     * --- Part Two ---
     *
     * Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters:
     * one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".
     *
     * Equipped with this new information, you now need to find the real first and last digit on each line. For example:
     *
     * two1nine
     * eightwothree
     * abcone2threexyz
     * xtwone3four
     * 4nineeightseven2
     * zoneight234
     * 7pqrstsixteen
     *
     * In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.
     *
     * What is the sum of all of the calibration values?
     */
    @Test
    public void partTwo() {
        assertEquals(29, twoPointerApproach(replaceWords("two1nine")));
        assertEquals(83, twoPointerApproach(replaceWords("eightwothree")));
        assertEquals(13, twoPointerApproach(replaceWords("abcone2threexyz")));
        assertEquals(24, twoPointerApproach(replaceWords("xtwone3four")));
        assertEquals(42, twoPointerApproach(replaceWords("4nineeightseven2")));
        assertEquals(14, twoPointerApproach(replaceWords("zoneight234")));
        assertEquals(76, twoPointerApproach(replaceWords("7pqrstsixteen")));

        int res = Arrays.stream(input2.split("\n"))
                .map(D1::replaceWords)
                .mapToInt(D1::twoPointerApproach)
                .sum();
        assertEquals(281, res);

        res = FileReader.readFileStringList("src/main/resources/2023/D1.txt")
                .stream()
                .map(D1::replaceWords)
                .mapToInt(D1::twoPointerApproach)
                .sum();
        assertNotEquals(55086, res);
        assertNotEquals(55061, res);
        assertNotEquals(51491, res);
        assertNotEquals(55095, res);


        assertEquals(0, res);
    }

}
