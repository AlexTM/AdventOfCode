package com.atom.adventofcode.y2019.IntComputer;

public interface Function {
    int apply(int pos, int[] codes, int[]paramPositions, int[] inRegister, int[] outRegister);
}
