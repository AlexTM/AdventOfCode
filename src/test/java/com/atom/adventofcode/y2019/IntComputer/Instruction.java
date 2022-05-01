package com.atom.adventofcode.y2019.IntComputer;

import java.util.Arrays;

public class Instruction {
    private final String strInstruction;
    private final Integer opcode;
    private Mode[] modes;

    // For backward compatibility
    public Instruction(String strInstruction){
        this.strInstruction = strInstruction;
        int l = strInstruction.length();
        if(l > 1) {
            this.opcode = Integer.valueOf(strInstruction.substring(l - 2, l));
        } else {
            this.opcode = Integer.valueOf(strInstruction);
        }
    }

    public void setModes(int paramSize) {
        modes = new Mode[paramSize];

        // default is positional
        Arrays.fill(modes, Mode.POSITION);

        int l = strInstruction.length();
        if (l >= 3)
            modes[0] = Mode.values()[Integer.valueOf(strInstruction.charAt(l - 3) - 48)];
        if (l >= 4)
            modes[1] = Mode.values()[Integer.valueOf(strInstruction.charAt(l - 4) - 48)];
        if (l >= 5)
            modes[2] = Mode.values()[Integer.valueOf(strInstruction.charAt(l - 5) - 48)];

    }

    @Override
    public String toString() {
        return "Instruction{" +
                "opcode=" + opcode +
                ", modes=" + Arrays.toString(modes) +
                '}';
    }

    public Integer getOpcode() {
        return opcode;
    }

    public Mode[] getModes() {
        return modes;
    }

}
