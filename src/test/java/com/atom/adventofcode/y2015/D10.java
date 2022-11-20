package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D10 {

    private String lookAndSay(String start, int loops) {

        for(int i=0; i<loops; i++) {
            char currentC = 0;
            int count = 0;
            for(char c : start.toCharArray()) {
                if(c == currentC) {
                    count++;
                } else if() {

                }
            }
        }

    }

    @Test
    public void testGame() {
        assertEquals("11", lookAndSay("1", 1));
        assertEquals("21", lookAndSay("1", 2));
    }
}
