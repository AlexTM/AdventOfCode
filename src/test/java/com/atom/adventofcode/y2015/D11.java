package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class D11 {

    // Convert to long, base 26
    private long convertStringToLong(String str) {
        int pos = 0;
        long num = 0;
        for(int i=str.length()-1; i>=0; i--) {
            char c = str.charAt(i);
            int v = c - 'a'+1;
            num += v*Math.pow(26, pos);
            pos++;
        }
        return num;
    }

    private String convertLongToString(long num) {
        // 9,223,372,036,854,775,808 = 2^63-1 max Long size 9e+18
        // 26 ^12 = 9e+16, there can store 16 digit passwords in a long

        StringBuilder ret = new StringBuilder();
        for(int i=16; i>=0; i--) {
            long d = (long)Math.pow(26, i);
            long div = num / d;
            if(div > 0) {
                num = num % (div*d);
                ret.append(Character.toString('a' + (int) div - 1));
            }
        }

        return ret.toString();
    }

    private boolean passwordCheckOne(String password) {
        char lastC = 0;
        int count = 0;
        for(char c : password.toCharArray()){
            if(c == lastC+1) {
                count++;
                if(count == 3)
                    return true;
            } else {
                count = 0;
            }
            lastC = c;
        }
        return false;
    }
    private boolean passwordCheckTwo(String password) {
        for(char c : password.toCharArray()){
            if(c == 'i' || c == 'o' || c == 'l')
                return false;
        }
        return true;
    }
    private boolean passwordCheckThree(String password) {
        char lastC = 0;
        boolean firstPair = false;
        for(char c : password.toCharArray()){
            if(c == lastC) {
                if(firstPair)
                    return true;
                else {
                    firstPair = true;
                    c = 0;
                }
            }
            lastC = c;
        }
        return false;
    }

    private String preCheck(String password) {
        // look for characters that should not be allowed
        int i = password.indexOf("i");
        if(i != -1) {
            long num = convertStringToLong(password);
            num += Math.pow(26, password.length()-i);
            password = convertLongToString(num);
        }
        return password;
    }

    private String getNextPassword(String password) {
        while(true) {
            password = preCheck(password);

            password = convertLongToString(convertStringToLong(password)+1);
            if(passwordCheckOne(password) &&
            passwordCheckTwo(password) &&
            passwordCheckThree(password))
                return password;
        }
    }

    @Test
    public void testPassword() {
        assertEquals(1L, convertStringToLong("a"));
        assertEquals(2L, convertStringToLong("b"));
        assertEquals(3L, convertStringToLong("c"));
        assertEquals(26L, convertStringToLong("z"));
        assertEquals(27L, convertStringToLong("aa"));
        assertEquals(53L, convertStringToLong("ba"));
        assertEquals(54L, convertStringToLong("bb"));

        assertEquals("a", convertLongToString(1L));
        assertEquals("b", convertLongToString(2L));
        assertEquals("aa", convertLongToString(27L));
        assertEquals("ba", convertLongToString(53L));
        assertEquals("bb", convertLongToString(54L));

        assertEquals("hxbxwxba", convertLongToString(convertStringToLong("hxbxwxba")));

        assertTrue(passwordCheckOne("hijklmmn"));
        assertFalse(passwordCheckTwo("hijklmmn"));
        assertTrue(passwordCheckThree("abbceffg"));
        assertFalse(passwordCheckThree("abbcegjk"));

//
//        assertEquals("abcdffaa", getNextPassword("abcdefgh"));
        assertEquals("ghjaabcc", getNextPassword("ghijklmn"));

    }
}
