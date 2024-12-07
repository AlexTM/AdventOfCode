package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D7 {
    private static final String TEST_INPUT = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20""";


    record Equation(long value, long[] values) { }

    private List<Equation> parseEquations(String input) {
        List<Equation> equations = new ArrayList<>();
        String[] lines = input.split("\n");
        for (String line : lines) {
            String[] parts = line.split(": ");
            long id = Long.parseLong(parts[0]);
            long[] values = Arrays.stream(parts[1].split(" ")).mapToLong(Long::parseLong).toArray();
            equations.add(new Equation(id, values));
        }
        return equations;
    }

    private static final List<BiFunction<Long, Long, Long>> OPERATORS_PART_ONE = List.of(
            (a, b) -> a + b,
            (a, b) -> a * b
    );

    private long validateEquation(Equation equation, int pos, long sum, List<BiFunction<Long, Long, Long>> operators) {

        if (pos == equation.values.length) {
            if (sum == equation.value) {
                return equation.value;
            }
            return 0;
        }

        for (BiFunction<Long, Long, Long> operator : operators) {
            long result = validateEquation(equation, pos + 1, operator.apply(sum, equation.values[pos]), operators);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    @Test
    public void testPart1() {
        List<Equation> equations = parseEquations(TEST_INPUT);
        long sum = equations.stream().mapToLong(equation ->
                validateEquation(equation, 1, equation.values[0], OPERATORS_PART_ONE))
                .sum();
        assertEquals(3749, sum);

        equations = parseEquations(FileReader.readFileString("src/test/resources/2024/D7.txt"));
        sum = equations.stream().mapToLong(equation ->
                validateEquation(equation, 1, equation.values[0], OPERATORS_PART_ONE))
                .sum();
        assertEquals(3351424677624L, sum);
    }

    private static final List<BiFunction<Long, Long, Long>> OPERATORS_PART_TWO = List.of(
            (a, b) -> a + b,
            (a, b) -> a * b,
            (a, b) -> Long.parseLong(""+a + b)
    );

    @Test
    public void testPart2() {
        List<Equation> equations = parseEquations(TEST_INPUT);
        long sum = equations.stream().mapToLong(equation ->
                validateEquation(equation, 1, equation.values[0], OPERATORS_PART_TWO))
                .sum();
        assertEquals(11387, sum);

        equations = parseEquations(FileReader.readFileString("src/test/resources/2024/D7.txt"));
        sum = equations.stream().mapToLong(equation ->
                validateEquation(equation, 1, equation.values[0], OPERATORS_PART_TWO))
                .sum();
        assertEquals(204976636995111L, sum);
    }

}
