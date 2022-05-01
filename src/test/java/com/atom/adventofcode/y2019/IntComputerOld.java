package com.atom.adventofcode.y2019;

import java.util.Arrays;

public class IntComputerOld {

    public static int[] loadInstructions(String input) {
        return loadInstructions(input, 0);
    }

    public static int[] loadInstructions(String input, int size) {
        String[] parts = input.split(",");
        int[] codes = new int[Math.max(size, parts.length)];
        Arrays.fill(codes, 0);
        int c=0;
        for(String part : parts) {
            codes[c++] = Integer.valueOf(part);
        }
        return codes;
    }

    public static void compute(int[] codes) {
        compute(codes, null);
    }

    public static Integer[] compute(int[] codes, Integer inRegister) {

        Integer[] outRegister = new Integer[1];
        boolean finish = false;

        for(int pos = 0; pos < codes.length; pos++) {
            if(finish)
                break;

            int code = codes[pos];
            switch (code) {
                case 0:
                    break;
                case 1:
                    add(pos, codes);
                    pos += 3;
                    break;
                case 2:
                    mult(pos, codes);
                    pos += 3;
                    break;
                case 3:
                    input(pos, codes, inRegister);
                    pos += 2;
                case 4:
                    outRegister[0] = output(pos, codes);
                    System.out.println("Outputting: "+outRegister);
                    pos += 2;
                case 99:
                    finish = true;
                    break;
                default:
                    System.out.println("System error");
                    break;
            }
        }
        return outRegister;
    }

    private static void input(int addressA, int[] data, int input) {
        data[data[addressA+1]] = input;
    }

    private static int output(int addressA, int[] data) {
        return data[data[addressA+1]];
    }

    private static void mult(int addressA, int[] data) {
        data[data[addressA+3]] = data[data[addressA+1]] * data[data[addressA+2]];
    }

    private static void add(int addressA, int[] data) {
        data[data[addressA+3]] = data[data[addressA+1]] + data[data[addressA+2]];
    }

}
