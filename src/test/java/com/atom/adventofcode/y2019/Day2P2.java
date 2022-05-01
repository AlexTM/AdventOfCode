package com.atom.adventofcode.y2019;

import org.junit.jupiter.api.Test;

/**
 * "Good, the new computer seems to be working correctly! Keep it nearby during this mission - you'll probably use it
 * again. Real Intcode computers support many more features than your new one, but we'll let you know what they are as
 * you need them."
 *
 * "However, your current priority should be to complete your gravity assist around the Moon. For this mission to
 * succeed, we should settle on some terminology for the parts you've already built."
 *
 * Intcode programs are given as a list of integers; these values are used as the initial state for the computer's
 * memory. When you run an Intcode program, make sure to start by initializing memory to the program's values. A
 * position in memory is called an address (for example, the first value in memory is at "address 0").
 *
 * Opcodes (like 1, 2, or 99) mark the beginning of an instruction. The values used immediately after an opcode, if
 * any, are called the instruction's parameters. For example, in the instruction 1,2,3,4, 1 is the opcode; 2, 3, and
 * 4 are the parameters. The instruction 99 contains only an opcode and has no parameters.
 *
 * The address of the current instruction is called the instruction pointer; it starts at 0. After an instruction
 * finishes, the instruction pointer increases by the number of values in the instruction; until you add more
 * instructions to the computer, this is always 4 (1 opcode + 3 parameters) for the add and multiply instructions.
 * (The halt instruction would increase the instruction pointer by 1, but it halts the program instead.)
 *
 * "With terminology out of the way, we're ready to proceed. To complete the gravity assist, you need to determine
 * what pair of inputs produces the output 19690720."
 *
 * The inputs should still be provided to the program by replacing the values at addresses 1 and 2, just like before.
 * In this program, the value placed in address 1 is called the noun, and the value placed in address 2 is called the
 * verb. Each of the two input values will be between 0 and 99, inclusive.
 *
 * Once the program has halted, its output is available at address 0, also just like before. Each time you try a pair
 * of inputs, make sure you first reset the computer's memory to the values in the program (your puzzle input) - in
 * other words, don't reuse memory from a previous attempt.
 *
 * Find the input noun and verb that cause the program to produce the output 19690720. What is 100 * noun + verb? (For
 * example, if noun=12 and verb=2, the answer would be 1202.)
 */
public class Day2P2 {

    @Test
    public void testIntcodeBasic() {

        int result = 0;

        // try new values
        for(int i=0; i<99; i++) {
            for (int j = 0; j < 99; j++) {

                int[] codes = IntComputerOld.loadInstructions(
                        "1,12,2,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,6,19,1,19,6,23,2,23,6,27,2,6,27,31,2,13,31,35,1,9,35,39,2,10,39,43,1,6,43,47,1,13,47,51,2,6,51,55,2,55,6,59,1,59,5,63,2,9,63,67,1,5,67,71,2,10,71,75,1,6,75,79,1,79,5,83,2,83,10,87,1,9,87,91,1,5,91,95,1,95,6,99,2,10,99,103,1,5,103,107,1,107,6,111,1,5,111,115,2,115,6,119,1,119,6,123,1,123,10,127,1,127,13,131,1,131,2,135,1,135,5,0,99,2,14,0,0");

                codes[1] = i;
                codes[2] = j;
                IntComputerOld.compute(codes);

                if(codes[0] == 19690720) {
                    System.out.println("i = "+i);
                    System.out.println("j = "+j);
                    return;
                }

            }
        }

    }

}

