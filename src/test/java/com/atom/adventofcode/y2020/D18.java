package com.atom.adventofcode.y2020;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 18: Operation Order ---
 * As you look out the window and notice a heavily-forested continent slowly appear over the horizon, you are
 * interrupted by the child sitting next to you. They're curious if you could help them with their math homework.
 *
 * Unfortunately, it seems like this "math" follows different rules than you remember.
 *
 * The homework (your puzzle input) consists of a series of expressions that consist of addition (+), multiplication
 * (*), and parentheses ((...)). Just like normal math, parentheses indicate that the expression inside must be
 * evaluated before it can be used by the surrounding expression. Addition still finds the sum of the numbers on both
 * sides of the operator, and multiplication still finds the product.
 *
 * However, the rules of operator precedence have changed. Rather than evaluating multiplication before addition, the
 * operators have the same precedence, and are evaluated left-to-right regardless of the order in which they appear.
 *
 * For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are as follows:
 *
 * 1 + 2 * 3 + 4 * 5 + 6
 *   3   * 3 + 4 * 5 + 6
 *       9   + 4 * 5 + 6
 *          13   * 5 + 6
 *              65   + 6
 *                  71
 * Parentheses can override this order; for example, here is what happens if parentheses are added to form 1 + (2 *
 * 3) + (4 * (5 + 6)):
 *
 * 1 + (2 * 3) + (4 * (5 + 6))
 * 1 +    6    + (4 * (5 + 6))
 *      7      + (4 * (5 + 6))
 *      7      + (4 *   11   )
 *      7      +     44
 *             51
 * Here are a few more examples:
 *
 * 2 * 3 + (4 * 5) becomes 26.
 * 5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 437.
 * 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 12240.
 * ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 13632.
 * Before you can help with the homework, you need to understand it yourself. Evaluate the expression on each line of
 * the homework; what is the sum of the resulting values?
 *
 * Your puzzle answer was 53660285675207.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 *
 * --- Part Two ---
 * You manage to answer the child's questions and they finish part 1 of their homework, but get stuck when they reach
 * the next section: advanced math.
 *
 * Now, addition and multiplication have different precedence levels, but they're not the ones you're familiar with.
 * Instead, addition is evaluated before multiplication.
 *
 * For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are now as follows:
 *
 * 1 + 2 * 3 + 4 * 5 + 6
 *   3   * 3 + 4 * 5 + 6
 *   3   *   7   * 5 + 6
 *   3   *   7   *  11
 *      21       *  11
 *          231
 * Here are the other examples from above:
 *
 * 1 + (2 * 3) + (4 * (5 + 6)) still becomes 51.
 * 2 * 3 + (4 * 5) becomes 46.
 * 5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 1445.
 * 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 669060.
 * ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 23340.
 * What do you get if you add up the results of evaluating the homework problems using these new rules?
 */
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
