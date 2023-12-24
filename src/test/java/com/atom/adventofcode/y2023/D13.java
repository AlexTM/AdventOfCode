package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D13 {

    private static final String input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
            """;

    FileReader.TriFunction<List<List<String>>, String, Integer, List<List<String>>> tri = (map, line, yy) -> {
        if(map.isEmpty())
            map.add(new ArrayList<>());

        if(line.isEmpty())
            map.add(new ArrayList<>());
        else
            map.get(map.size()-1).add(line);
        return map;
    };

    private static final BiFunction<List<String>, Integer, String> getRow = List::get;
    private static final BiFunction<List<String>, Integer, String> getColumn = (map, i) -> {
        StringBuilder sb = new StringBuilder();
        for(String row : map) {
            sb.append(row.charAt(i));
        }
        return sb.toString();
    };

    private static long checkMirror(List<String> s, BiFunction<List<String>, Integer, String> getString,
                                    BiFunction<List<String>, Integer, String> getLength) {
        int stringLength = getLength.apply(s, 0).length();

        for(int i=0; i<stringLength-1; i++) {
            String thisRow = getString.apply(s, i);
            String nextRow = getString.apply(s, i+1);

            if(thisRow.equals(nextRow)) {
                // found 2 matching rows, need current match backwards current confirm it's a mirror
                boolean mirror = true;
                for(int j=1; j<=i; j++) {
                    if(i+j+1 >= stringLength)
                        break;

                    String thisRow2 = getString.apply(s, i-j);
                    String nextRow2 = getString.apply(s, i+j+1);
                    if(!thisRow2.equals(nextRow2)) {
                        // not a match
                        mirror = false;
                        break;
                    }
                }
                if(mirror)
                    return i+1;
            }
        }
        return -1;
    }

    private static Solution findMirror(List<String> m) {
        long tmp = checkMirror(m, getColumn, getRow);
        if(tmp != -1)
            return new Solution('C', (int) tmp);

        tmp = checkMirror(m, getRow, getColumn);
        return new Solution('R', (int) tmp);
    }

    private static List<Solution> findMirrors(List<List<String>> maps) {
        List<Solution> solutionMap = new ArrayList<>();
        for(List<String> m : maps) {
            Solution tmp = findMirror(m);
            solutionMap.add(tmp);
        }
        return solutionMap;
    }

    record Solution(char c, int p){};

    private long getSolutionValue(List<Solution> solutionMap) {
        long res = 0;
        for(Solution s : solutionMap) {
            if(s.c == 'R')
                res += s.p * 100L;
            else
                res += s.p;
        }
        return res;
    }

    @Test
    public void partOne() {
        List<List<String>> map = FileReader.parseStringForObject(input, new ArrayList<>(), tri);
        assertEquals(405, getSolutionValue(findMirrors(map)));

        List<List<String>> map2 = FileReader.readFileForObject("src/main/resources/2023/D13.txt", new ArrayList<>(), tri);

        assertEquals(27300, getSolutionValue(findMirrors(map2)));
    }

//    private List<Solution> newSolutions(List<List<String>> maps, List<Solution> knownSolutions) {
//
//    }

    private static List<Solution> findMirrorsWithSmudge(List<List<String>> maps) {
        return null;
    }


    @Test
    public void partTwo() {
        String tmp = """
            ..##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            """;
        List<List<String>> map = FileReader.parseStringForObject(input, new ArrayList<>(), tri);
        List<Solution> solutions = findMirrorsWithSmudge(map);
//        newSolutions(tmp, solutions);


        assertEquals(400, getSolutionValue(findMirrorsWithSmudge(map)));
    }
}
