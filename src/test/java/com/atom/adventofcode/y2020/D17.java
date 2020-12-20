package com.atom.adventofcode.y2020;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class D17 {

    record Active(int x, int y, int z, int w){}

    private Set<Active> readFile(String filename) throws FileNotFoundException {
        Set<Active> active = new HashSet<>();
        try(Scanner in = new Scanner(new File(filename))) {
            int row = 0;
            while (in.hasNext()) {

                int col = 0;
                for(char c : in.next().toCharArray()) {
                    if(c == '#') {
                        active.add(new Active(col, row, 0, 0));
                    }
                    col++;
                }
                row++;
            }
            in.close();
            return active;
        }
    }

    private Set<Active> initUniverse() {
        Set<Active> active = new HashSet<>();
        active.add(new Active(1, 2,0,0));
        active.add(new Active(2, 1,0,0));
        active.add(new Active(0, 0,0,0));
        active.add(new Active(1, 0,0,0));
        active.add(new Active(2, 0,0,0));
        return active;
    }

    @Test
    public void testPowerSource() throws FileNotFoundException {
        assertEquals(112, activeCubes(initUniverse(), 6, false));
        assertEquals(336, activeCubes(
                readFile("src/test/resources/2020/D17.txt"), 6, false));

        assertEquals(848, activeCubes(initUniverse(), 6, true));
        assertEquals(2620, activeCubes(
                readFile("src/test/resources/2020/D17.txt"), 6, true));

    }

    private int activeCubes(Set<Active> active, int loops, boolean fourD) {
        for(int i=0; i<loops; i++) {
            active = iterateUniverse(active, fourD);
        }
        return active.size();
    }

    private Set<Active> generateCandidates(Active active, boolean fourD) {
        Set<Active> as = new HashSet<>();
        for(int i=-1; i<2; i++) {
            for(int j=-1; j<2; j++) {
                for (int k = -1; k < 2; k++) {
                    for (int m = -1; m < 2; m++) {
                        as.add(new Active(
                                active.x + i, active.y + j, active.z + k, active.w + m
                        ));
                    }
                }
            }
        }
        // Remove center
        as.remove(active);
        // Quick hack, Remove all 4th dimension candidates
        if(!fourD)
            return as.stream().filter(a -> a.w == 0).collect(Collectors.toSet());
        return as;
    }

    private Set<Active> iterateUniverse(Set<Active> actives, boolean fourD) {
        Map<Active, Integer> neighbourCount = new HashMap<>();
        for(Active a: actives) {
            for(Active c : generateCandidates(a, fourD)) {
                int count = neighbourCount.getOrDefault(c, 0);
                neighbourCount.put(c, count + 1);
            }
        }

        Set<Active> nextRound = new HashSet<>();
        neighbourCount.entrySet().stream().forEach(e -> {
            if(actives.contains(e.getKey())) {
                if(e.getValue() == 2 || e.getValue() == 3) {
                    nextRound.add(e.getKey());
                }
            } else {
                if(e.getValue() == 3) {
                    nextRound.add(e.getKey());
                }
            }
        });

        return nextRound;
    }
}
