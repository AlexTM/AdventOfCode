package com.atom.advent2020;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Your flight departs in a few days from the coastal airport; the easiest way down to the coast from here is via
 * toboggan.
 *
 * The shopkeeper at the North Pole Toboggan Rental Shop is having a bad day. "Something's wrong with our computers;
 * we can't log in!" You ask if you can take a look.
 *
 * Their password database seems to be a little corrupted: some of the passwords wouldn't have been allowed by the
 * Official Toboggan Corporate Policy that was in effect when they were chosen.
 *
 * To try to debug the problem, they have created a list (your puzzle input) of passwords (according to the corrupted
 * database) and the corporate policy when that password was set.
 *
 * For example, suppose you have the following list:
 *
 * 1-3 a: abcde
 * 1-3 b: cdefg
 * 2-9 c: ccccccccc
 * Each line gives the password policy and then the password. The password policy indicates the lowest and highest
 * number of times a given letter must appear for the password to be valid. For example, 1-3 a means that the password
 * must contain a at least 1 time and at most 3 times.
 *
 * In the above example, 2 passwords are valid. The middle password, cdefg, is not; it contains no instances of b,
 * but needs at least 1. The first and third passwords are valid: they contain one a or nine c, both within the limits
 * of their respective policies.
 *
 * How many passwords are valid according to their policies?
 *
 * --- Part Two ---
 * While it appears you validated the passwords correctly, they don't seem to be what the Official Toboggan Corporate
 * Authentication System is expecting.
 *
 * The shopkeeper suddenly realizes that he just accidentally explained the password policy rules from his old job at
 * the sled rental place down the street! The Official Toboggan Corporate Policy actually works a little differently.
 *
 * Each policy actually describes two positions in the password, where 1 means the first character, 2 means the second
 * character, and so on. (Be careful; Toboggan Corporate Policies have no concept of "index zero"!) Exactly one of
 * these positions must contain the given letter. Other occurrences of the letter are irrelevant for the purposes of
 * policy enforcement.
 *
 * Given the same example list from above:
 *
 * 1-3 a: abcde is valid: position 1 contains a and position 3 does not.
 * 1-3 b: cdefg is invalid: neither position 1 nor position 3 contains b.
 * 2-9 c: ccccccccc is invalid: both position 2 and position 9 contain c.
 * How many passwords are valid according to the new interpretation of the policies?
 *
 */
public class DayTwo {

    class Password {
        int min;
        int max;
        char letter;
        String password;

        public int getMin() {
            return min;
        }

        public Password setMin(int min) {
            this.min = min;
            return this;
        }

        public int getMax() {
            return max;
        }

        public Password setMax(int max) {
            this.max = max;
            return this;
        }

        public char getLetter() {
            return letter;
        }

        public Password setLetter(char letter) {
            this.letter = letter;
            return this;
        }

        public String getPassword() {
            return password;
        }

        public Password setPassword(String password) {
            this.password = password;
            return this;
        }
    }

    @Test
    public void testPasswordPolicy() {
        assertTrue(validatePasswordPolicy(new Password().setMin(1).setMax(3).setLetter('a').setPassword("abcde")));
        assertTrue(validatePasswordPolicy(new Password().setMin(2).setMax(9).setLetter('c').setPassword("ccccccccc")));
        assertFalse(validatePasswordPolicy(new Password().setMin(1).setMax(3).setLetter('b').setPassword("cdefg")));
    }

    @Test
    public void testPasswordList() throws FileNotFoundException {
        System.out.println("Valid passwords: "+
                readFile("src/test/resources/daytwo.txt").stream().filter(this::validatePasswordPolicy).count()
        );
    }

    private boolean validatePasswordPolicy(Password p) {
        Map<Character, Long> freq =
                p.getPassword().chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if(!freq.containsKey(p.getLetter()))
            return false;
        return freq.get(p.getLetter()) >= p.getMin() && freq.get(p.getLetter()) <= p.getMax();
    }

    private List<Password> readFile(String filename) throws FileNotFoundException {
        List<Password> values = new ArrayList<>();
        Scanner in = new Scanner(new FileReader(filename));
        in.useDelimiter("\n");
        while(in.hasNext()) {
            String line = in.next();
            String[] parts = line.split(" ");
            String[] minMax = parts[0].split("-");
            values.add(new Password()
                    .setMin(Integer.parseInt(minMax[0]))
                    .setMax(Integer.parseInt(minMax[1]))
                    .setPassword(parts[2])
                    .setLetter(parts[1].charAt(0))
            );
        }
        in.close();
        return values;
    }

    @Test
    public void testPasswordPolicy2() {
        assertTrue(validatePasswordPolicy2(new Password().setMin(1).setMax(3).setLetter('a').setPassword("abcde")));
        assertFalse(validatePasswordPolicy2(new Password().setMin(2).setMax(9).setLetter('c').setPassword("ccccccccc")));
        assertFalse(validatePasswordPolicy2(new Password().setMin(1).setMax(3).setLetter('b').setPassword("cdefg")));
    }


    @Test
    public void testPasswordList2() throws FileNotFoundException {
        System.out.println("Valid passwords: "+
                readFile("src/test/resources/daytwo.txt").stream().filter(this::validatePasswordPolicy2).count()
        );
    }

    private boolean validatePasswordPolicy2(Password p) {
        char[] chars = p.getPassword().toCharArray();
        return chars[p.getMin()-1] == p.getLetter() ^ chars[p.getMax()-1] == p.getLetter();
    }
}
