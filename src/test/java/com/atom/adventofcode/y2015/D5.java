package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class D5 {

    static final Set<String> naughtySet = Set.of("ab", "cd", "pq", "xy");
    static final Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u');

    private boolean isNiceString(String input) {
        int vowelCount = 0;
        char lastChar = 0;
        boolean doubleLetter = false;

        for(String naughty : naughtySet) {
            if (input.contains(naughty))
                return false;
        }

        for(char c : input.toCharArray()) {
            if(vowels.contains(c))
                vowelCount++;
            if(c == lastChar)
                doubleLetter = true;
            lastChar = c;
        }
        return doubleLetter && vowelCount >= 3;
    }

    @Test
    public void testStrings() {
        assertTrue(isNiceString("ugknbfddgicrmopn"));
        assertTrue(isNiceString("aaa"));
        assertFalse(isNiceString("jchzalrnumimnmhp"));
        assertFalse(isNiceString("haegwjzuvuyypxyu"));
        assertFalse(isNiceString("dvszwmarrgswjxmb"));

        assertEquals(258, FileReader.readFileStringList("src/test/resources/2015/D5.txt")
                        .stream().filter(this::isNiceString).count());
    }

    private boolean isNiceString2(String input) {
        char lastChar = 0;
        char lastCharButOne = 0;
        boolean repeatWithOneLetter = false;
        boolean containsPair = false;
        for(int i=0; i<input.length(); i++) {
            char c = input.charAt(i);

            if(i+1 < input.length() && !containsPair) {
                String subString = input.substring(i+1);
                containsPair = subString.contains(""+lastChar+c);
            }

            if(lastCharButOne == c)
                repeatWithOneLetter = true;

            lastCharButOne = lastChar;
            lastChar = c;
        }
        return repeatWithOneLetter && containsPair;
    }

    @Test
    public void testStringsPart2() {
        assertTrue(isNiceString2("qjhvhtzxzqqjkmpb"));
        assertTrue(isNiceString2("xxyxx"));
        assertFalse(isNiceString2("uurcxstgmygtbstg"));
        assertFalse(isNiceString2("ieodomkazucvgmuy"));

        assertEquals(53, FileReader.readFileStringList("src/test/resources/2015/D5.txt")
                .stream().filter(this::isNiceString2).count());
    }

}
