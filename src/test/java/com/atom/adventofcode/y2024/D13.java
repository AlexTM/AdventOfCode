package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D13 {
    private static final String TEST_INPUT = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
            
            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
            
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
            
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """;

    record Problem(long x1, long y1, long x2, long y2, long r1, long r2) {}

    private List<Problem> parseInput(String input) {
        List<Problem> problems = new ArrayList<>();

        Pattern pattern = Pattern.compile("[XY][+=](\\d*)");
        Matcher matcher = pattern.matcher(input);

        long [] values = new long[6];
        int count = 0;
        while (matcher.find()) {
            values[count++] = Long.parseLong(matcher.group(1));
            if (count == 6) {
                problems.add(new Problem(values[0], values[1], values[2], values[3], values[4], values[5]));
                count = 0;
            }
        }
        return problems;
    }

    private long getTokenNumber(Problem p) {
        long top = p.x1*p.r2 - p.y1*p.r1;
        long bot = p.x1*p.y2 - p.x2*p.y1;
        double b = (double)top/(double)bot;

        double a = (p.r1 - p.x2*b)/(double)p.x1;

        if(b % 1 != 0 || a % 1 != 0)
            return 0;

        return (long)(a*3+b);
    }

    private long solveAll(List<Problem> problems) {
        return problems.stream().mapToLong(this::getTokenNumber).sum();
    }

    @Test
    public void testPart1() {
        assertEquals(480, solveAll(parseInput(TEST_INPUT)));
        assertEquals(29522, solveAll(parseInput(FileReader.readFileString("src/test/resources/2024/D13.txt"))));
    }

    @Test
    public void testPart2() {
        List<Problem> problems = parseInput(FileReader.readFileString("src/test/resources/2024/D13.txt")).stream()
                .map(p -> new Problem(p.x1, p.y1, p.x2, p.y2, p.r1+10_000_000_000_000L, p.r2+10000000000000L)).toList();
        assertEquals(101214869433312L, solveAll(problems));
    }
}
