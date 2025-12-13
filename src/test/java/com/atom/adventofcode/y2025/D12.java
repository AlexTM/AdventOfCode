package com.atom.adventofcode.y2025;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class D12 {
    private static final String TEST_INPUT = """
            0:
            ###
            ##.
            ##.
            
            1:
            ###
            ##.
            .##
            
            2:
            .##
            ###
            ##.
            
            3:
            ##.
            ###
            ##.
            
            4:
            ###
            #..
            ###
            
            5:
            ###
            .#.
            ###
            
            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2
            """;

    record Data(Map<Integer, int[][]> shapes){}

    private void parseShape(String[] input) {
        String[] split = input[0].split(":");
        int id = Integer.parseInt(split[0]);
        for(int i=1; i<4; i++) {
            String line = input[i];
            for(int j=0; j<3; j++) {

            }
        }
    }

    private List<String> parseInput(String input) {
        String[] split = input.split("\n");

        return null;
    }

    @Test
    public void testPart1() {

    }

}
