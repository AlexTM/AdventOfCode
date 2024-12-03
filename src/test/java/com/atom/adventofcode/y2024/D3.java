package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D3 {

    private long getMultipliedValue(String input) {
        String regex = "mul\\((\\d*),(\\d*)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        long sum = 0;
        while (matcher.find()) {
            sum += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
        }

        return sum;
    }

    @Test
    public void testPart1() {
        assertEquals(161, getMultipliedValue("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"));
        assertEquals(168539636, getMultipliedValue(FileReader.readFileString("src/test/resources/2024/D3.txt")));
    }

    private long getMultipliedValue2(String input) {

        String regex = "(do\\(\\)|don't\\(\\)|mul\\((\\d*),(\\d*)\\))";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        boolean enabled = true;
        long sum = 0;
        while (matcher.find()) {
            if(matcher.group(1).equalsIgnoreCase("do()")) {
                enabled = true;
            } else if(matcher.group(1).equalsIgnoreCase("don't()")) {
                enabled = false;
            } else if(enabled) {
                sum += Long.parseLong(matcher.group(2)) * Long.parseLong(matcher.group(3));
            }
        }

        return sum;
    }

    @Test
    public void testPart2() {
        assertEquals(48, getMultipliedValue2("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"));
        assertEquals(97529391, getMultipliedValue2(FileReader.readFileString("src/test/resources/2024/D3.txt")));
    }
}
