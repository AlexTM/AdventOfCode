package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D8 {

    private static String decode(String inp) {
        String s = inp.replaceAll("\\\\x[0-9a-f][0-9a-f]", " ");
        s = s.replace("\\\"", "\"");
        s = s.replace("\\\\", "\\");
        return s;
    }

    private static int calculateDiff(String inp) {
        int memSize = decode(inp).length();
        int codeSize = inp.length() + 2;
        return codeSize - memSize;
    }

    @Test
    public void testStringDecode() {
        assertEquals(2, calculateDiff(""));
        assertEquals(2, calculateDiff("abc"));
        assertEquals(3, calculateDiff("aaa\\\"aaa"));
        assertEquals(5, calculateDiff("\\x27"));

        List<String> inputs = FileReader.readFileStringList("src/test/resources/2015/D8.txt");
        int diffs = inputs.stream().map(D8::calculateDiff).reduce(0, Integer::sum);
        assertEquals(1350, diffs);
    }

    private static String encode(String inp) {
        String s = inp.replace("\\", "\\\\");
        s = s.replace("\"", "\\\"");
        return s;
    }

    private static int calculateDiff2(String inp) {
        String t = encode(inp);
        int memSize = encode(inp).length();
        int codeSize = inp.length();
        return memSize - codeSize + 2 ;
    }

    @Test
    public void testStringEncode() {
        assertEquals(4, calculateDiff2("\"\""));
        assertEquals(4, calculateDiff2("\"abc\""));
        assertEquals(6, calculateDiff2("\"aaa\\\"aaa\""));
        assertEquals(5, calculateDiff2("\"\\x27\""));

        List<String> inputs = FileReader.readFileStringList("src/test/resources/2015/D8.txt");
        int diffs = inputs.stream().map(D8::calculateDiff2).reduce(0, Integer::sum);
        assertEquals(2085, diffs);
    }

}
