package com.atom.adventofcode.y2019.IntComputer;

public class Operation {
    Function func;
    int paramSize;
    public Operation(Function func, int paramSize) {
        this.func = func;
        this.paramSize = paramSize;
    }
}
