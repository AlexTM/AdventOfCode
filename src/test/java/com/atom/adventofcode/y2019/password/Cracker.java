package com.atom.adventofcode.y2019.password;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You arrive at the Venus fuel depot only to discover it's protected by a password. The Elves had written the password
 * on a sticky note, but someone threw it out.
 *
 * However, they do remember a few key facts about the password:
 *
 * It is a six-digit number.
 * The value is within the range given in your puzzle input.
 * Two adjacent digits are the same (like 22 in 122345).
 * Going from left to right, the digits never decrease; they only ever increase or stay the same (like 111123 or 135679).
 * Other than the range rule, the following are true:
 *
 * 111111 meets these criteria (double 11, never decreases).
 * 223450 does not meet these criteria (decreasing pair of digits 50).
 * 123789 does not meet these criteria (no double).
 * How many different passwords within the range given in your puzzle input meet these criteria?
 */
public class Cracker {

    public boolean isValid(int i) {
        String s = Integer.toString(i);

        if(s.length() != 6)
            return false;

        int[] password = new int[7];

        int index = 1;
        password[0] = -1;
        for(char c : s.toCharArray()) {
            password[index++] = Integer.valueOf(c)-48;
        }

        // Check rules
        int repeatedCount = 1;
        boolean goodToGo = false;
        for(int j=1; j<password.length; j++) {

            if(password[j-1] > password[j])
                return false;

            if(password[j-1] == password[j]) {
                repeatedCount++;
            } else {
                if(repeatedCount == 2) {
                    goodToGo = true;
                }
                repeatedCount = 1;
            }
        }

        if(repeatedCount == 2)
            goodToGo = true;

        if(!goodToGo)
            return false;

        return true;
    }

    @Test
    public void testIsValid() {
        assertTrue(isValid(112233));
        assertFalse(isValid(123444));
        assertTrue(isValid(111122));
    }


    @Test
    public void testCracker() {

        int c=0;
        for(int i=347312; i<=805915; i++) {
            if(isValid(i))
                c++;
        }
        System.out.println("Valid passwords "+c);
        assertEquals(364, c);
    }

}
