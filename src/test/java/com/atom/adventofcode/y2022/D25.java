package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D25 {

    // FIXME This is super ugly
    private static String DecToSNAFU(long dec) {
        StringBuilder sb = new StringBuilder();

        // Convert to base 5
        int max = 20;
        long r = dec;
        for(int i=0; i<=max; i++) {
            long mx = (long)Math.pow(5, max-i);
            int t = (int)(r / mx);
            r = r % mx;
            sb.append(t);
        }

        // Modify base five to work with SNAFU numbers
        String p = sb.toString();
        List<Integer> arr = new ArrayList<>(p.chars().boxed().map(c -> c - '0').toList());
        Collections.reverse(arr);
        for(int i=0; i<arr.size(); i++) {
            if(arr.get(i) > 2) {
                arr.set(i, arr.get(i) - 5);
                if (arr.size() == i + 1) {
                    arr.add(1);
                } else {
                    arr.set(i + 1, arr.get(i + 1) + 1);
                }
            }
        }
        Collections.reverse(arr);

        sb = new StringBuilder();
        for (int t : arr) {
            switch (t) {
                case 2, 1, 0 -> sb.append(t);
                case -1 -> sb.append("-");
                case -2 -> sb.append("=");
                default -> throw new RuntimeException("Not a recognised number format");
            }
        }
        return sb.toString().replaceFirst("^0+(?!$)", "");
    }

    private static long SNAFUToDec(String snafu) {
        long total = 0;
        for(int i=0; i<snafu.length(); i++) {
            char c = snafu.charAt(snafu.length() - i - 1);
            total += switch (c){
                case '2' -> 2*Math.pow(5, i);
                case '1' -> 1*Math.pow(5, i);
                case '0' -> 0;
                case '-' -> -1*Math.pow(5, i);
                case '=' -> -2*Math.pow(5, i);
                default -> throw new RuntimeException("Not a recognised number format");
            };
        }
        return total;
    }

    private long sumNumbers(List<String> allNums) {
        return allNums.stream().mapToLong(D25::SNAFUToDec).sum();
    }

    @Test
    public void testSum() {
        assertEquals("2=-1=0",
                DecToSNAFU(
                    sumNumbers(
                            FileReader.readFileStringList("src/test/resources/2022/D25_t.txt"))));

        assertEquals("2-02===-21---2002==0",
                DecToSNAFU(
                        sumNumbers(
                                FileReader.readFileStringList("src/test/resources/2022/D25.txt"))));
    }

    @Test
    public void testSNAFUToDec() {
        assertEquals(1, SNAFUToDec("1"));
        assertEquals(2, SNAFUToDec("2"));
        assertEquals(3, SNAFUToDec("1="));
        assertEquals(4, SNAFUToDec("1-"));
        assertEquals(5, SNAFUToDec("10"));
        assertEquals(6, SNAFUToDec("11"));
        assertEquals(7, SNAFUToDec("12"));
        assertEquals(8, SNAFUToDec("2="));
        assertEquals(9, SNAFUToDec("2-"));
        assertEquals(10, SNAFUToDec("20"));
        assertEquals(15, SNAFUToDec("1=0"));
        assertEquals(20, SNAFUToDec("1-0"));
        assertEquals(2022, SNAFUToDec("1=11-2"));
        assertEquals(12345, SNAFUToDec("1-0---0"));
        assertEquals(314159265, SNAFUToDec("1121-1110-1=0"));
    }

    @Test
    public void testDecToSNAFU() {
        assertEquals("1", DecToSNAFU(1));
        assertEquals("2", DecToSNAFU(2));
        assertEquals("1=", DecToSNAFU(3));
        assertEquals("1-", DecToSNAFU(4));
        assertEquals("10", DecToSNAFU(5));
        assertEquals("11", DecToSNAFU(6));
        assertEquals("12", DecToSNAFU(7));
        assertEquals("2=", DecToSNAFU(8));
        assertEquals("2-", DecToSNAFU(9));
        assertEquals("20", DecToSNAFU(10));
        assertEquals("1=0", DecToSNAFU(15));
        assertEquals("1-0", DecToSNAFU(20));
        assertEquals("1=11-2", DecToSNAFU(2022));
        assertEquals("1-0---0", DecToSNAFU(12345));
        assertEquals("2=-1=0", DecToSNAFU(4890));
        assertEquals("1121-1110-1=0", DecToSNAFU(314159265));
    }

}
