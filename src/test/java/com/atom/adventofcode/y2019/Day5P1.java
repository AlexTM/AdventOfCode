package com.atom.adventofcode.y2019;


import com.atom.adventofcode.y2019.IntComputer.IntComputer2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * You're starting to sweat as the ship makes its way toward Mercury. The Elves suggest that you get the air
 * conditioner working by upgrading your ship computer to support the Thermal Environment Supervision Terminal.
 *
 * The Thermal Environment Supervision Terminal (TEST) starts by running a diagnostic program (your puzzle input).
 * The TEST diagnostic program will run on your existing Intcode computer after a few modifications:
 *
 * First, you'll need to add two new instructions:
 *
 * Opcode 3 takes a single integer as input and saves it to the position given by its only parameter. For example,
 * the instruction 3,50 would take an input value and store it at address 50.
 * Opcode 4 outputs the value of its only parameter. For example, the instruction 4,50 would output the value at
 * address 50.
 * Programs that use these instructions will come with documentation that explains what should be connected to the
 * input and output. The program 3,0,4,0,99 outputs whatever it gets as input, then halts.
 *
 * Second, you'll need to add support for parameter modes:
 *
 * Each parameter of an instruction is handled based on its parameter mode. Right now, your ship computer already
 * understands parameter mode 0, position mode, which causes the parameter to be interpreted as a position - if the
 * parameter is 50, its value is the value stored at address 50 in memory. Until now, all parameters have been in
 * position mode.
 *
 * Now, your ship computer will also need to handle parameters in mode 1, immediate mode. In immediate mode, a
 * parameter is interpreted as a value - if the parameter is 50, its value is simply 50.
 *
 * Parameter modes are stored in the same value as the instruction's opcode. The opcode is a two-digit number based
 * only on the ones and tens digit of the value, that is, the opcode is the rightmost two digits of the first value in
 * an instruction. Parameter modes are single digits, one per parameter, read right-to-left from the opcode: the first
 * parameter's mode is in the hundreds digit, the second parameter's mode is in the thousands digit, the third
 * parameter's mode is in the ten-thousands digit, and so on. Any missing modes are 0.
 *
 * For example, consider the program 1002,4,3,4,33.
 *
 * The first instruction, 1002,4,3,4, is a multiply instruction - the rightmost two digits of the first value, 02,
 * indicate opcode 2, multiplication. Then, going right to left, the parameter modes are 0 (hundreds digit),
 * 1 (thousands digit), and 0 (ten-thousands digit, not present and therefore zero):
 *
 * ABCDE
 *  1002
 *
 * DE - two-digit opcode,      02 == opcode 2
 *  C - mode of 1st parameter,  0 == position mode
 *  B - mode of 2nd parameter,  1 == immediate mode
 *  A - mode of 3rd parameter,  0 == position mode,
 *                                   omitted due to being a leading zero
 * This instruction multiplies its first two parameters. The first parameter, 4 in position mode, works like it did
 * before - its value is the value stored at address 4 (33). The second parameter, 3 in immediate mode, simply has
 * value 3. The result of this operation, 33 * 3 = 99, is written according to the third parameter, 4 in position mode,
 * which also works like it did before - 99 is written to address 4.
 *
 * Parameters that an instruction writes to will never be in immediate mode.
 *
 * Finally, some notes:
 *
 * It is important to remember that the instruction pointer should increase by the number of values in the instruction
 * after the instruction finishes. Because of the new instructions, this amount is no longer always 4.
 * Integers can be negative: 1101,100,-1,4,0 is a valid program (find 100 + -1, store the result in position 4).
 * The TEST diagnostic program will start by requesting from the user the ID of the system to test by running an input
 * instruction - provide it 1, the ID for the ship's air conditioner unit.
 *
 * It will then perform a series of diagnostic tests confirming that various parts of the Intcode computer, like
 * parameter modes, function correctly. For each test, it will run an output instruction indicating how far the result
 * of the test was from the expected value, where 0 means the test was successful. Non-zero outputs mean that a function
 * is not working correctly; check the instructions that were run before the output instruction to see which one failed.
 *
 * Finally, the program will output a diagnostic code and immediately halt. This final output isn't an error; an output
 * followed immediately by a halt means the program finished. If all outputs were zero except the diagnostic code, the
 * diagnostic program ran successfully.
 *
 * After providing 1 to the only input instruction and passing all the tests, what diagnostic code does the program
 * produce?
 */
public class Day5P1 {

    @Test
    public void testIntcode() {

        int[] codesB = IntComputer2.loadInstructions("1,0,0,0,99");
        IntComputer2.compute(codesB, -1);
        assertEquals(2, codesB[0]);

        int[] codesC = IntComputer2.loadInstructions("2,3,0,3,99");
        IntComputer2.compute(codesC, -1);
        assertEquals(6, codesC[3]);

        int[] codesD = IntComputer2.loadInstructions("2,4,4,5,99,0");
        IntComputer2.compute(codesD, -1);
        assertEquals(9801, codesD[5]);

        int[] codesE = IntComputer2.loadInstructions("1,1,1,4,99,5,6,0,99");
        IntComputer2.compute(codesE, -1);
        assertEquals(30, codesE[0]);

        int[] codesA = IntComputer2.loadInstructions("1,9,10,3,2,3,11,0,99,30,40,50");
        IntComputer2.compute(codesA, -1);
        assertEquals(3500, codesA[0]);

    }

    @Test
    public void testInputOutput() {
        int[] codes = IntComputer2.loadInstructions("3,0,4,0,99");
        int output = IntComputer2.compute(codes, 8);
        assertEquals(8, output);
    }


    @Test
    public void testIntComputer() {
        int[] codes = IntComputer2.loadInstructions("1002,4,3,4,33");
        IntComputer2.compute(codes, -1);
        assertEquals(99, codes[4]);

        codes = IntComputer2.loadInstructions("1101,100,-1,4,0");
        IntComputer2.compute(codes, -1);
        assertEquals(99, codes[4]);
    }

    @Test
    public void puzzle() {
        int[] codes = IntComputer2.loadInstructions("3,225,1,225,6,6,1100,1,238,225,104,0,1101,37,34,224,101,-71,224,224,4,224,1002,223,8,223,101,6,224,224,1,224,223,223,1002,113,50,224,1001,224,-2550,224,4,224,1002,223,8,223,101,2,224,224,1,223,224,223,1101,13,50,225,102,7,187,224,1001,224,-224,224,4,224,1002,223,8,223,1001,224,5,224,1,224,223,223,1101,79,72,225,1101,42,42,225,1102,46,76,224,101,-3496,224,224,4,224,102,8,223,223,101,5,224,224,1,223,224,223,1102,51,90,225,1101,11,91,225,1001,118,49,224,1001,224,-140,224,4,224,102,8,223,223,101,5,224,224,1,224,223,223,2,191,87,224,1001,224,-1218,224,4,224,1002,223,8,223,101,4,224,224,1,224,223,223,1,217,83,224,1001,224,-124,224,4,224,1002,223,8,223,101,5,224,224,1,223,224,223,1101,32,77,225,1101,29,80,225,101,93,58,224,1001,224,-143,224,4,224,102,8,223,223,1001,224,4,224,1,223,224,223,1101,45,69,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,7,226,226,224,102,2,223,223,1005,224,329,101,1,223,223,108,677,226,224,102,2,223,223,1005,224,344,1001,223,1,223,1108,226,677,224,102,2,223,223,1005,224,359,1001,223,1,223,8,677,226,224,102,2,223,223,1006,224,374,1001,223,1,223,107,226,226,224,102,2,223,223,1006,224,389,101,1,223,223,1108,677,226,224,1002,223,2,223,1005,224,404,1001,223,1,223,108,677,677,224,102,2,223,223,1005,224,419,101,1,223,223,7,226,677,224,1002,223,2,223,1006,224,434,1001,223,1,223,107,226,677,224,102,2,223,223,1005,224,449,101,1,223,223,1108,677,677,224,1002,223,2,223,1006,224,464,101,1,223,223,7,677,226,224,102,2,223,223,1006,224,479,101,1,223,223,1007,677,677,224,1002,223,2,223,1005,224,494,101,1,223,223,1008,226,226,224,102,2,223,223,1006,224,509,1001,223,1,223,107,677,677,224,102,2,223,223,1006,224,524,1001,223,1,223,8,226,226,224,1002,223,2,223,1005,224,539,1001,223,1,223,1007,677,226,224,102,2,223,223,1006,224,554,1001,223,1,223,1007,226,226,224,1002,223,2,223,1005,224,569,1001,223,1,223,8,226,677,224,1002,223,2,223,1006,224,584,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,599,101,1,223,223,1107,677,226,224,1002,223,2,223,1005,224,614,1001,223,1,223,1107,226,677,224,102,2,223,223,1006,224,629,1001,223,1,223,1008,226,677,224,102,2,223,223,1005,224,644,101,1,223,223,1107,226,226,224,102,2,223,223,1006,224,659,1001,223,1,223,1008,677,677,224,102,2,223,223,1006,224,674,1001,223,1,223,4,223,99,226");
        int output = IntComputer2.compute(codes, 1);
        assertEquals(13294380, output);
    }
}