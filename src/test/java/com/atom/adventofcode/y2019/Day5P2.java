package com.atom.adventofcode.y2019;


import com.atom.adventofcode.y2019.IntComputer.IntComputer2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The air conditioner comes online! Its cold air feels good for a while, but then the TEST alarms start to go off.
 * Since the air conditioner can't vent its heat anywhere but back into the spacecraft, it's actually making the air
 * inside the ship warmer.
 *
 * Instead, you'll need to use the TEST to extend the thermal radiators. Fortunately, the diagnostic program
 * (your puzzle input) is already equipped for this. Unfortunately, your Intcode computer is not.
 *
 * Your computer is only missing a few opcodes:
 *
 * Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the instruction pointer to the value from
 * the second parameter. Otherwise, it does nothing.
 *
 * Opcode 6 is jump-if-false: if the first parameter is zero, it sets the instruction pointer to the value from the
 * second parameter. Otherwise, it does nothing.
 *
 * Opcode 7 is less than: if the first parameter is less than the second parameter, it stores 1 in the position given
 * by the third parameter. Otherwise, it stores 0.
 *
 * Opcode 8 is equals: if the first parameter is equal to the second parameter, it stores 1 in the position given by
 * the third parameter. Otherwise, it stores 0.
 *
 * Like all instructions, these instructions need to support parameter modes as described above.
 *
 * Normally, after an instruction is finished, the instruction pointer increases by the number of values in that
 * instruction. However, if the instruction modifies the instruction pointer, that value is used and the instruction
 * pointer is not automatically increased.
 *
 * For example, here are several programs that take one input, compare it to the value 8, and then produce one output:
 *
 * 3,9,8,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is equal to 8; output 1 (if it is) or 0
 * (if it is not).
 * 3,9,7,9,10,9,4,9,99,-1,8 - Using position mode, consider whether the input is less than 8; output 1 (if it is) or 0
 * (if it is not).
 * 3,3,1108,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is equal to 8; output 1 (if it is) or 0
 * (if it is not).
 * 3,3,1107,-1,8,3,4,3,99 - Using immediate mode, consider whether the input is less than 8; output 1 (if it is) or 0
 * (if it is not).
 *
 * Here are some jump tests that take an input, then output 0 if the input was zero or 1 if the input was non-zero:
 *
 * 3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9 (using position mode)
 * 3,3,1105,-1,9,1101,0,0,12,4,12,99,1 (using immediate mode)
 *
 * Here's a larger example:
 *
 * 3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99
 *
 * The above example program uses an input instruction to ask for a single number. The program will then output 999 if
 * the input value is below 8, output 1000 if the input value is equal to 8, or output 1001 if the input value is
 * greater than 8.
 *
 * This time, when the TEST diagnostic program runs its input instruction to get the ID of the system to test, provide
 * it 5, the ID for the ship's thermal radiator controller. This diagnostic test suite only outputs one number, the
 * diagnostic code.
 *
 * What is the diagnostic code for system ID 5?
 */
public class Day5P2 {

    @Test
    public void testIntcode1() {
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,9,8,9,10,9,4,9,99,-1,8"), 5));
        assertEquals(1,
                IntComputer2.compute(IntComputer2.loadInstructions("3,9,8,9,10,9,4,9,99,-1,8"), 8));
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,9,8,9,10,9,4,9,99,-1,8"), 9));
    }

    @Test
    public void testIntcode2() {
        assertEquals(1,
                IntComputer2.compute(IntComputer2.loadInstructions("3,9,7,9,10,9,4,9,99,-1,8"), 5));
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,9,7,9,10,9,4,9,99,-1,8"), 8));
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,9,7,9,10,9,4,9,99,-1,8"), 9));
    }

    @Test
    public void testIntcode3() {
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,3,1108,-1,8,3,4,3,99"), 5));
        assertEquals(1,
                IntComputer2.compute(IntComputer2.loadInstructions("3,3,1108,-1,8,3,4,3,99"), 8));
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,3,1108,-1,8,3,4,3,99"), 9));
    }

    @Test
    public void testIntcode4() {
        assertEquals(1,
                IntComputer2.compute(IntComputer2.loadInstructions("3,3,1107,-1,8,3,4,3,99"), 5));
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,3,1107,-1,8,3,4,3,99"), 8));
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,3,1107,-1,8,3,4,3,99"), 9));
    }

    @Test
    public void testIntcode5() {
        assertEquals(0,
                IntComputer2.compute(IntComputer2.loadInstructions("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), 0));
        assertEquals(1,
                IntComputer2.compute(IntComputer2.loadInstructions("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"), 1));
    }


    @Test
    public void testLargeExample() {
        assertEquals(999,
                IntComputer2.compute(IntComputer2.loadInstructions(
                        "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"
                ), 4));
        assertEquals(1000,
                IntComputer2.compute(IntComputer2.loadInstructions(
                        "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"
                ), 8));
        assertEquals(1001,
                IntComputer2.compute(IntComputer2.loadInstructions(
                        "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"
                ), 9));
    }

    @Test
    public void testPuzzle() {
        assertEquals(11460760,
                IntComputer2.compute(IntComputer2.loadInstructions(
                        "3,225,1,225,6,6,1100,1,238,225,104,0,1101,37,34,224,101,-71,224,224,4,224,1002,223,8,223,101,6,224,224,1,224,223,223,1002,113,50,224,1001,224,-2550,224,4,224,1002,223,8,223,101,2,224,224,1,223,224,223,1101,13,50,225,102,7,187,224,1001,224,-224,224,4,224,1002,223,8,223,1001,224,5,224,1,224,223,223,1101,79,72,225,1101,42,42,225,1102,46,76,224,101,-3496,224,224,4,224,102,8,223,223,101,5,224,224,1,223,224,223,1102,51,90,225,1101,11,91,225,1001,118,49,224,1001,224,-140,224,4,224,102,8,223,223,101,5,224,224,1,224,223,223,2,191,87,224,1001,224,-1218,224,4,224,1002,223,8,223,101,4,224,224,1,224,223,223,1,217,83,224,1001,224,-124,224,4,224,1002,223,8,223,101,5,224,224,1,223,224,223,1101,32,77,225,1101,29,80,225,101,93,58,224,1001,224,-143,224,4,224,102,8,223,223,1001,224,4,224,1,223,224,223,1101,45,69,225,4,223,99,0,0,0,677,0,0,0,0,0,0,0,0,0,0,0,1105,0,99999,1105,227,247,1105,1,99999,1005,227,99999,1005,0,256,1105,1,99999,1106,227,99999,1106,0,265,1105,1,99999,1006,0,99999,1006,227,274,1105,1,99999,1105,1,280,1105,1,99999,1,225,225,225,1101,294,0,0,105,1,0,1105,1,99999,1106,0,300,1105,1,99999,1,225,225,225,1101,314,0,0,106,0,0,1105,1,99999,7,226,226,224,102,2,223,223,1005,224,329,101,1,223,223,108,677,226,224,102,2,223,223,1005,224,344,1001,223,1,223,1108,226,677,224,102,2,223,223,1005,224,359,1001,223,1,223,8,677,226,224,102,2,223,223,1006,224,374,1001,223,1,223,107,226,226,224,102,2,223,223,1006,224,389,101,1,223,223,1108,677,226,224,1002,223,2,223,1005,224,404,1001,223,1,223,108,677,677,224,102,2,223,223,1005,224,419,101,1,223,223,7,226,677,224,1002,223,2,223,1006,224,434,1001,223,1,223,107,226,677,224,102,2,223,223,1005,224,449,101,1,223,223,1108,677,677,224,1002,223,2,223,1006,224,464,101,1,223,223,7,677,226,224,102,2,223,223,1006,224,479,101,1,223,223,1007,677,677,224,1002,223,2,223,1005,224,494,101,1,223,223,1008,226,226,224,102,2,223,223,1006,224,509,1001,223,1,223,107,677,677,224,102,2,223,223,1006,224,524,1001,223,1,223,8,226,226,224,1002,223,2,223,1005,224,539,1001,223,1,223,1007,677,226,224,102,2,223,223,1006,224,554,1001,223,1,223,1007,226,226,224,1002,223,2,223,1005,224,569,1001,223,1,223,8,226,677,224,1002,223,2,223,1006,224,584,101,1,223,223,108,226,226,224,1002,223,2,223,1006,224,599,101,1,223,223,1107,677,226,224,1002,223,2,223,1005,224,614,1001,223,1,223,1107,226,677,224,102,2,223,223,1006,224,629,1001,223,1,223,1008,226,677,224,102,2,223,223,1005,224,644,101,1,223,223,1107,226,226,224,102,2,223,223,1006,224,659,1001,223,1,223,1008,677,677,224,102,2,223,223,1006,224,674,1001,223,1,223,4,223,99,226"
                ), 5));
    }

}