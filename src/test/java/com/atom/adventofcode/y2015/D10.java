package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D10 {

    private String lookAndSay(String start, int loops) {

        for(int i=0; i<loops; i++) {
            char currentC = start.charAt(0);
            int count = 0, pos = 0;
            StringBuilder sb = new StringBuilder();

            // Over run by 1 but check and make char 0 in that case, triggers the appending logic
            while(pos < start.length()+1) {
                char c = pos == start.length() ? 0 : start.charAt(pos);
                if(c == currentC) {
                    count++;
                } else {
                    sb.append(count);
                    sb.append(currentC);
                    currentC = c;
                    count = 1;
                }
                pos++;
            }

            start = sb.toString();
        }
        return start;
    }

    @Test
    public void testLookAndSee() {
        assertEquals("11", lookAndSay("1", 1));
        assertEquals("21", lookAndSay("1", 2));
        assertEquals("1211", lookAndSay("1", 3));
        assertEquals("111221", lookAndSay("1", 4));
        assertEquals("312211", lookAndSay("1", 5));

        assertEquals(492982, lookAndSay("1321131112", 40).length());
        assertEquals(6989950, lookAndSay("1321131112", 50).length());
    }
}
