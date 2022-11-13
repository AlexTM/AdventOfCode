package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D8 {

    private String parse(String inp) {
        String s = inp.replace("\\x\d\d", " ");
        s = s.replace("\\", "");
        return s;
    }

    private int diff(String inp) {
        int memSize = parse(inp).length();
        int codeSize = inp.length() + 2;
        return codeSize - memSize;
    }

    @Test
    public void testStringParsing() {
//        assertEquals(2, diff(""));
//        assertEquals(2, diff("abc"));
//        assertEquals(3, diff("aaa\\\"aaa"));
        assertEquals(5, diff("\\x27"));

        List<String> inputs = FileReader.readFileStringList("src/test/resources/2015/D8.txt");
        assertEquals(10, inputs.size());
    }
}
