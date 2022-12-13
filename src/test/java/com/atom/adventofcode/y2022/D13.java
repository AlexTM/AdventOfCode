package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class D13 {

    record Pair(List<Object> a, List<Object> b) {};

    static ObjectMapper om = new ObjectMapper();

    private static List<Object> readLine(String input) {
        try {
            return om.readValue(input, List.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    private static Pair readPair(Scanner in) {
        List<Object> a = readLine(in.next());
        List<Object> b = readLine(in.next());
        if(in.hasNext())
            in.next();
        return new Pair(a, b);
    }

    private static int comparePair(Object a, Object b) {
        int res = 0;
        if (a instanceof List && b instanceof List) {
            List<Object> arrA = (List) a;
            List<Object> arrB = (List) b;
            for (int i = 0; i < arrA.size(); i++) {
                if (i == arrB.size()) {
                    res = 1;
                    break;
                }

                int tmpRes = comparePair(arrA.get(i), arrB.get(i));
                if (tmpRes != 0) {
                    res = tmpRes;
                    break;
                }
            }
            if (res == 0 && arrA.size() < arrB.size())
                res = -1;
        } else if (a instanceof List) {
            res = comparePair(a, List.of(b));
        } else if (b instanceof List) {
            res = comparePair(List.of(a), b);
        } else {
            int aa = (int) a;
            int bb = (int) b;
            if (aa < bb)
                res = -1;
            else if (bb < aa)
                res = 1;
        }
        return res;
    }

    private int score(List<Pair> pairs) {
        int score = 0;
        for (int i = 0; i < pairs.size(); i++) {
            if (comparePair(pairs.get(i).a, pairs.get(i).b) == -1) {
                score += 1 + i;
            }
        }
        return score;
    }

    @Test
    public void testPackets() {
        // Pair 1
        assertEquals(-1, comparePair(readLine("[1,1,3,1,1]"), readLine("[1,1,5,1,1]")));
        // Pair 2
        assertEquals(-1, comparePair(readLine("[[1],[2,3,4]]"), readLine("[[1],4]")));
        // Pair 3
        assertEquals(1, comparePair(readLine("[9]"), readLine("[[8,7,6]]")));
        // Pair 4
        assertEquals(-1, comparePair(readLine("[[4,4],4,4]"), readLine("[[4,4],4,4,4]")));
        // Pair 5
        assertEquals(1, comparePair(readLine("[7,7,7,7]"), readLine("[7,7,7]")));
        // Pair 6
        assertEquals(-1, comparePair(readLine("[]"), readLine("[3]")));
        // Pair 7
        assertEquals(1, comparePair(readLine("[[[]]]"), readLine("[[]]")));
        // Pair 8
        assertEquals(1, comparePair(readLine("[1,[2,[3,[4,[5,6,7]]]],8,9]"), readLine("[1,[2,[3,[4,[5,6,0]]]],8,9]")));

        assertEquals(13, score(
                FileReader.scanFileObjectList("src/test/resources/2022/D13_t.txt", D13::readPair)));

        assertEquals(6072, score(
                FileReader.scanFileObjectList("src/test/resources/2022/D13.txt", D13::readPair)));
    }

    private int getDecoderKey(List<Pair> pairs) {
        List<List<Object>> inp = pairs.stream()
                .flatMap(m -> Stream.of(m.a, m.b))
                .collect(Collectors.toCollection(ArrayList::new));

        inp.add(readLine("[[2]]"));
        inp.add(readLine("[[6]]"));
        inp.sort(D13::comparePair);

        return (1+inp.indexOf(readLine("[[2]]"))) * (1+inp.indexOf(readLine("[[6]]")));
    }

    @Test
    public void testPackets2() {
        assertEquals(140, getDecoderKey(
                FileReader.scanFileObjectList("src/test/resources/2022/D13_t.txt",
                        D13::readPair)));
        assertEquals(22184, getDecoderKey(
                FileReader.scanFileObjectList("src/test/resources/2022/D13.txt",
                        D13::readPair)));
    }

}