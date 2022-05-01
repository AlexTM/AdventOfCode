package com.atom.adventofcode.y2019.circuit;

import java.util.Objects;

public class Candidate {
    private int x;
    private int y;
    private int steps;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }


    public Candidate(int x, int y, int steps) {
        this.x = x;
        this.y = y;
        this.steps = steps;
    }
}
