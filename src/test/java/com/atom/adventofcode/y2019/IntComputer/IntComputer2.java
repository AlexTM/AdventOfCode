package com.atom.adventofcode.y2019.IntComputer;

import java.util.*;

public class IntComputer2 {

    public static int[] loadInstructions(String input) {
        return loadInstructions(input, 0);
    }

    private static int[] loadInstructions(String input, int size) {
        String[] parts = input.split(",");
        int[] codes = new int[Math.max(size, parts.length)];
        Arrays.fill(codes, 0);
        int c=0;
        for(String part : parts) {
            codes[c++] = Integer.valueOf(part);
        }
        return codes;
    }

    private final static Map<Integer, Operation> funcs;

    static {
        funcs = new HashMap<>();

        // Nothing
        funcs.put(0,
                new Operation(
                        (pos, codes, paramPositions, inRegister, outRegister) -> {
                            System.out.println("Nothing");
                            return pos + 1;
                        }, 0)
        );
        // Add
        funcs.put(1,
                new Operation(
                        (pos, codes, paramPositions, inRegister, outRegister) -> {
                            codes[paramPositions[2]] = codes[paramPositions[0]] + codes[paramPositions[1]];
                            return pos + 4;
                        }, 3)
        );
        // Multiply
        funcs.put(2, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    codes[paramPositions[2]] = codes[paramPositions[0]] * codes[paramPositions[1]];
                    return pos + 4;
                }, 3)
        );
        // Input
        funcs.put(3, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    codes[paramPositions[0]] = inRegister[0];
                    return pos + 2;
                }, 1)
        );
        // Output
        funcs.put(4, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    outRegister[0] = codes[paramPositions[0]];
                    return pos + 2;
                }, 1)
        );
        // Jump if true
        funcs.put(5, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    if(codes[paramPositions[0]] != 0)
                        return codes[paramPositions[1]];
                    return pos + 3;
                }, 2)
        );
        // Jump if false
        funcs.put(6, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    if(codes[paramPositions[0]] == 0)
                        return codes[paramPositions[1]];
                    return pos + 3;
                }, 2)
        );
        // If less than
        funcs.put(7, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    if(codes[paramPositions[0]] < codes[paramPositions[1]])
                        codes[paramPositions[2]] = 1;
                    else
                        codes[paramPositions[2]] = 0;
                    return pos + 4;
                }, 3)
        );
        // If greater than
        funcs.put(8, new Operation(
                (pos, codes, paramPositions, inRegister, outRegister) -> {
                    if(codes[paramPositions[0]] == codes[paramPositions[1]])
                        codes[paramPositions[2]] = 1;
                    else
                        codes[paramPositions[2]] = 0;
                    return pos + 4;
                }, 3)
        );
    }

    private static int[] resolvePositions(Instruction i, int pos, int[] codes) {
        int[] positions = new int[i.getModes().length];
        int c = 0;
        for(Mode m : i.getModes()) {
            if(Mode.IMMEDIATE.equals(m)) {
                positions[c] = pos + c + 1;
            } else {
                // Copy positions
                positions[c] = codes[pos + c + 1];
            }
            c++;
        }
        return positions;
    }

    public static int compute(int[] codes, int input) {
        int[] inRegister = new int[1];
        int[] outRegister = new int[1];
        inRegister[0] = input;
        compute(codes, inRegister, outRegister);
        return outRegister[0];
    }


    public static void compute(int[] codes, int[] inRegister, int[] outRegister) {

        int pos = 0;
        while(pos < codes.length) {

            // FIXME this does not need to be an object
            Instruction i = new Instruction(String.valueOf(codes[pos]));

            if(i.getOpcode() == 99)
                break;

            i.setModes(funcs.get(i.getOpcode()).paramSize);

            int[] paramPositions = resolvePositions(i, pos, codes);
            Operation op = funcs.get(i.getOpcode());
            pos = op.func.apply(pos, codes, paramPositions, inRegister, outRegister);


//            if(!Arrays.equals(new int[]{0}, outRegister)) {
//                System.out.print("Outputting: ");
//                Arrays.stream(outRegister).forEach(s -> System.out.print(s + ", "));
//                System.out.println("");
//                // This might not be what we want to do?
//                //Arrays.fill(outRegister, 0);
//            }
        }
    }


}
