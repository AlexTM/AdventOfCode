package com.atom.adventofcode.test2015;


import com.atom.adventofcode.PartOne;
import com.atom.adventofcode.PartTwo;
import org.springframework.stereotype.Service;

/**
 * \--- Day 1: Not Quite Lisp ---
 * ------------------------------
 *
 */
@Service
public class Day1 implements PartOne<String, Integer>, PartTwo<String, Integer> {

    /**
     * Santa was hoping for a white Christmas, but his weather machine's "snow" function is powered by stars, and he's
     * fresh out! To save Christmas, he needs you to collect _fifty stars_ by December 25th.
     * 
     * Collect stars by helping Santa solve puzzles. Two puzzles will be made available on each day in the Advent calendar;
     * the second puzzle is unlocked when you complete the first. Each puzzle grants _one star_. Good luck!
     * 
     * Here's an easy puzzle to warm you up.
     * 
     * Santa is trying to deliver presents in a large apartment building, but he can't find the right floor - the directions
     * he got are a little confusing. He starts on the ground floor (floor `0`) and then follows the instructions one
     * character at a time.
     * 
     * An opening parenthesis, `(`, means he should go up one floor, and a closing parenthesis, `)`, means he should go
     * down one floor.
     * 
     * The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.
     * 
     * For example:
     * 
     * *   `(())` and `()()` both result in floor `0`.
     * *   `(((` and `(()(()(` both result in floor `3`.
     * *   `))(((((` also results in floor `3`.
     * *   `())` and `))(` both result in floor `-1` (the first basement level).
     * *   `)))` and `)())())` both result in floor `-3`.
     * 
     * To _what floor_ do the instructions take Santa?
     * 
     *
     * Answer is: 138
     *
     */
    @Override
    public Integer partOne(String inp) {
        return null;
    }

    /**
     * Now, given the same instructions, find the _position_ of the first character that causes him to enter the basement
     * (floor `-1`). The first character in the instructions has position `1`, the second character has position `2`,
     * and so on.
     * 
     * For example:
     * 
     * *   `)` causes him to enter the basement at character position `1`.
     * *   `()())` causes him to enter the basement at character position `5`.
     * 
     * What is the _position_ of the character that causes Santa to first enter the basement?
     * 
     *
     * Answer is: 1771
     *
     */
    @Override
    public Integer partTwo(String inp) {
        return null;
    }
}
