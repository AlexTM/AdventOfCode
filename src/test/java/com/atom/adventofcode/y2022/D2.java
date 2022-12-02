package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D2 {

    record Strat(char a, char b){};

    private static Strat loadData(String c) {
        return new Strat(c.charAt(0), c.charAt(2));
    };

    // A Rock, B Paper, C Scissors
    // X Rock, Y Paper, Z Scissors
    private int win(Strat s) {
        if(s.b == 'X' && s.a == 'C')
            return 6;
        if(s.b == 'Y' && s.a == 'A')
            return 6;
        if(s.b == 'Z' && s.a == 'B')
            return 6;
        if(s.b - 'X' == s.a - 'A')
            return 3;
        return 0;
    }

    // Convert to A - C from X - Z
    private char convert(char c) {
        return (char)(c - 'X' + 'A');
    }

    // Convert to 1 - 3
    private int shapeScore(char c) {
        return c - '@';
    }

    private int calculateScore(List<Strat> stratList) {
        return stratList.stream()
                .map(s -> win(s) + shapeScore(convert(s.b)))
                .reduce(0, Integer::sum);
    }

    @Test
    public void testRockPaper() {
        List<Strat> stratList = FileReader.readFileObjectList("src/test/resources/2022/D2.txt", D2::loadData);
        assertEquals(10718, calculateScore(stratList));
    }

    private char inc(char c) {
        return ++c == 'D' ? 'A' : c;
    }
    private char dec(char c) {
        return --c == '@' ? 'C' : c;
    }

    // A Rock, B Paper, C Scissors
    // X lose, Y draw, Z win
    private char win2(Strat s) {
        return switch (s.b) {
            case 'X' -> dec(s.a);
            case 'Z' -> inc(s.a);
            default -> s.a;
        };
    }

    private int winValues(char c) {
        return switch(c) {
            case 'Y' -> 3;
            case 'Z' -> 6;
            default -> 0;
        };
    }

    private int calculateScore2(List<Strat> stratList) {
        return stratList.stream()
                .map(s -> shapeScore(win2(s)) + winValues(s.b))
                .reduce(0, Integer::sum);
    }

    @Test
    public void testRockPaper2() {
        List<Strat> stratList = FileReader.readFileObjectList("src/test/resources/2022/D2.txt", D2::loadData);
        assertEquals(14652, calculateScore2(stratList));
    }

}
