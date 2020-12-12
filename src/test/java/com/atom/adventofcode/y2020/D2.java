package com.atom.adventofcode.y2020;

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
public class D2 {

    record Password(int min, int max, char letter, String password) {}

    @Test
    public void testPasswordPolicy() {
        assertTrue(validatePasswordPolicy(new Password(1,3,'a',"abcde")));
        assertTrue(validatePasswordPolicy(new Password(2,9,'c',"ccccccccc")));
        assertFalse(validatePasswordPolicy(new Password(1,3,'b',"cdefg")));
    }

    @Test
    public void testPasswordList() throws FileNotFoundException {
        System.out.println("Valid passwords: "+
                readFile("src/test/resources/2020/D2.txt").stream().filter(this::validatePasswordPolicy).count()
        );
    }

    private boolean validatePasswordPolicy(Password p) {
        Map<Character, Long> freq =
                p.password.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        if(!freq.containsKey(p.letter))
            return false;
        return freq.get(p.letter) >= p.min && freq.get(p.letter) <= p.max;
    }

    private List<Password> readFile(String filename) throws FileNotFoundException {
        List<Password> values = new ArrayList<>();
        try(Scanner in = new Scanner(new FileReader(filename))) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                String line = in.next();
                String[] parts = line.split(" ");
                String[] minMax = parts[0].split("-");
                values.add(new Password(
                        Integer.parseInt(minMax[0]),
                        Integer.parseInt(minMax[1]),
                        parts[1].charAt(0),
                        parts[2])
                );
            }
            in.close();
            return values;
        }
    }

    @Test
    public void testPasswordPolicy2() {
        assertTrue(validatePasswordPolicy2(new Password(1,3,'a',"abcde")));
        assertFalse(validatePasswordPolicy2(new Password(2,9,'c',"ccccccccc")));
        assertFalse(validatePasswordPolicy2(new Password(1,3,'b',"cdefg")));
    }


    @Test
    public void testPasswordList2() throws FileNotFoundException {
        System.out.println("Valid passwords: "+
                readFile("src/test/resources/2020/D2.txt").stream().filter(this::validatePasswordPolicy2).count()
        );
    }

    private boolean validatePasswordPolicy2(Password p) {
        char[] chars = p.password.toCharArray();
        return chars[p.min-1] == p.letter ^ chars[p.max-1] == p.letter;
    }
}
