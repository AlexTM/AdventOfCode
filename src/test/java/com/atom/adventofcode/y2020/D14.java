package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D14 {

    record Instruction(String mask, List<Long[]> data){}

    // my eyes!
    private List<Instruction> readFile(String filename) throws FileNotFoundException {
        List<Instruction> instructions = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            String mask = "";
            List<Long[]> data = new ArrayList<>();
            while (in.hasNext()) {
                String line = in.next().trim();
                if(line.startsWith("mask")) {
                    if(!mask.isBlank()) {
                        instructions.add(new Instruction(mask, data));
                        data = new ArrayList<>();
                    }
                    mask = line.split("=")[1].trim();
                } else {
                    data.add(new Long[] {
                            Long.parseLong(line.substring(line.indexOf('[')+1, line.indexOf(']'))),
                            Long.parseLong(line.substring(line.indexOf('=')+1).trim())}
                    );
                }
            }
            if(!mask.isBlank()) {
                instructions.add(new Instruction(mask, data));
            }
            in.close();
            return instructions;
        }
    }

    static class Mask {
        private final BitSet ands = new BitSet(36);
        private final BitSet ors = new BitSet(36);

        public Mask(String inp) {
            for(int i=0; i<inp.length(); i++) {
                switch (inp.charAt(inp.length() - i - 1)) {
                    case 'X' -> {
                        ands.set(i, true);
                        ors.set(i, false);
                    }
                    case '1' -> {
                        ands.set(i, true);
                        ors.set(i, true);
                    }
                    case '0' -> {
                        ands.set(i, false);
                        ors.set(i, false);
                    }
                }
            }
        }
    }

    static class Data {
        private final BitSet bits = new BitSet(36);

        public Data(long value) {
            convert(value);
        }

        public void convert(long value) {
            int index = 0;
            while (value != 0L) {
                if (value % 2L != 0) {
                    bits.set(index);
                }
                ++index;
                value = value >>> 1;
            }
        }

        public long convert() {
            long value = 0L;
            for (int i = 0; i < bits.length(); ++i) {
                value += bits.get(i) ? (1L << i) : 0L;
            }
            return value;
        }
    }

    static class Memory {
        private final Map<Long, Data> mem = new HashMap<>();

        public void apply(Long location, String mask, long data) {
            Mask m = new Mask(mask);
            Data d = new Data(data);
            d.bits.and(m.ands);
            d.bits.or(m.ors);
            mem.put(location, d);
        }

        public void applyInstruction(Instruction i) {
            i.data.forEach(d -> apply(d[0], i.mask, d[1]));
        }

        public long getResult() {
            return mem.values().stream().map(Data::convert).reduce(Long::sum).orElse(0L);
        }

        public void applyInstructionFloating(Instruction i) {
            i.data.forEach(d -> applyFloating(d[0], i.mask, d[1]));
        }

        public void applyFloating(Long location, String mask, long data) {
            List<Long> addresses;

            Mask m = new Mask(mask);
            Data loc = new Data(location);

            loc.bits.or(m.ors);

            List<Integer> bitsToToggle = new ArrayList<>();
            for(int i=0; i<36; i++) {
                if(m.ands.get(i) && !m.ors.get(i)) {
                    bitsToToggle.add(i);
                }
            }

            addresses = toggle(loc, bitsToToggle);

            for(Long address : addresses) {
                mem.put(address, new Data(data));
            }
        }

        private List<Long> toggle(Data loc, List<Integer> bitsToToggle) {
            List<Long> res = new ArrayList<>();

            if(bitsToToggle.isEmpty()) {
                res.add(loc.convert());
                return res;
            }

            Integer pos = bitsToToggle.get(0);

            res.addAll(toggle(loc, bitsToToggle.subList(1, bitsToToggle.size())));
            loc.bits.flip(pos);
            res.addAll(toggle(loc, bitsToToggle.subList(1, bitsToToggle.size())));

            return res;
        }
    }

    @Test
    public void testReadfile() throws FileNotFoundException {
        Memory mem = new Memory();
        readFile("src/test/resources/2020/D14_t.txt")
                .forEach(mem::applyInstruction);
        assertEquals(165, mem.getResult());

        mem = new Memory();
        readFile("src/test/resources/2020/D14.txt")
                .forEach(mem::applyInstruction);
        System.out.println("Result "+mem.getResult());
        assertEquals(11501064782628L, mem.getResult());
    }


    @Test
    public void testBitmask() {
        String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X";

        Memory mem = new Memory();
        mem.apply(8L, mask, 11);
        mem.apply(7L, mask, 101);
        mem.apply(8L, mask, 0);
        assertEquals(165, mem.getResult());
    }

    @Test
    public void testFloatBits() throws FileNotFoundException {
        Memory mem = new Memory();
        readFile("src/test/resources/2020/D14_t_2.txt")
                .forEach(mem::applyInstructionFloating);
        assertEquals(208, mem.getResult());

        mem = new Memory();
        readFile("src/test/resources/2020/D14.txt")
                .forEach(mem::applyInstructionFloating);
        System.out.println("Result "+mem.getResult());
        assertEquals(5142195937660L, mem.getResult());
    }
}
