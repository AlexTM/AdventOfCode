package com.atom.adventofcode.y2020;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D18 {

    @Test
    public void testEval() {
        assertEquals(51, eval("1 + (2 * 3) + (4 * (5 + 6))".toCharArray(), 0).value);
        assertEquals(26, eval("2 * 3 + (4 * 5)".toCharArray(), 0).value);
        assertEquals(437, eval("5 + (8 * 3 + 9 + 3 * 4 * 3)".toCharArray(), 0).value);
        assertEquals(12240, eval("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))".toCharArray(), 0).value);
        assertEquals(13632, eval("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2".toCharArray(), 0).value);
    }

    @Test
    public void testHomework() {
        List<String> lines = FileReader.readFileObjectList("src/test/resources/2020/D18.txt", line -> line);
        long sum = lines.stream().map(line -> eval(line.toCharArray(), 0).value).mapToLong(Long::longValue).sum();
        assertEquals(1, sum);
    }


    record Answer(int pos, Long value){};


    private Answer eval(char[] chars, int pos) {
        Long working = null;
        Long wb = null;
        Character action = null;

        while(pos < chars.length) {
            switch (chars[pos]) {
                case ' ':
                    pos++;
                    break;
                case '(':
                    Answer a = eval(chars, pos + 1);
                    pos = a.pos;
                    if (working == null)
                        working = a.value;
                    else
                        wb = a.value;
                    break;
                case ')':
                    return new Answer(pos+1, working);
                case '+':
                    action = '+';
                    pos++;
                    break;
                case '*':
                    action = '*';
                    pos++;
                    break;
                default:
                    // must be number
                    if (working == null)
                        working = Long.parseLong(Character.toString(chars[pos]));
                    else
                        wb = Long.parseLong(Character.toString(chars[pos]));
                    pos++;
                    break;
            }
            if(working != null && wb != null && action != null) {
                if(action=='+')
                    working += wb;
                if(action=='*')
                    working *= wb;
                wb = null;
                action = null;
            }
        }
        return new Answer(pos, working);
    }

    private String preProcessString(String str) {
        return str.replaceAll("(\\d \\+ \\d)", "($1)");
    }

    @Test
    public void testpreProcessString() {
        assertEquals("(1 + 2) * (3 + 4) * (5 + 6)", preProcessString("1 + 2 * 3 + 4 * 5 + 6"));
        assertEquals("(1 + (2 * 3)) + (4 * (5 + 6))", preProcessString("1 + (2 * 3) + (4 * (5 + 6))"));
    }

    private Answer eval2(char[] chars, int pos) {
        return eval(preProcessString("1 + 2 * 3 + 4 * 5 + 6").toCharArray(), 0);
    }


    @Test
    public void testEval2() {
        assertEquals(231, eval2("1 + 2 * 3 + 4 * 5 + 6".toCharArray(), 0).value);
        assertEquals(51, eval2("1 + (2 * 3) + (4 * (5 + 6))".toCharArray(), 0).value);
        assertEquals(46, eval2("2 * 3 + (4 * 5)".toCharArray(), 0).value);
        assertEquals(1445, eval2("5 + (8 * 3 + 9 + 3 * 4 * 3)".toCharArray(), 0).value);
        assertEquals(669060, eval2("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))".toCharArray(), 0).value);
        assertEquals(23340, eval2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2".toCharArray(), 0).value);
    }

    @Test
    public void testHomework2() {
        List<String> lines = FileReader.readFileObjectList("src/test/resources/2020/D18.txt", line -> line);
        long sum = lines.stream().map(line -> eval2(line.toCharArray(), 0).value).mapToLong(Long::longValue).sum();
        assertEquals(1, sum);
    }
}
