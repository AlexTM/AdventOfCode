package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D3 {

    private static final String input1 = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """;

    record Point(int x, int y) {}
    record Number(int value, int length) {}
    record Data(Map<Point, Number> numberMap, Map<Point, Character> symbolMap) {}

    private static  Data parseInput(String input1) {
        Map<Point, Number> points = new HashMap<>();
        Map<Point, Character> symbols = new HashMap<>();
        String[] lines = input1.split("\n");
        for(int i=0; i<lines.length; i++) {
            String line = lines[i];
            for(int j=0; j<line.length(); j++) {
                char c = line.charAt(j);

                if(Character.isDigit(c)) {
                    int start = j;
                    while (true) {

                        if(j >= line.length())
                            break;

                        c = line.charAt(j);
                        if (!Character.isDigit(c))
                            break;

                        j++;
                    }
                    String s = line.substring(start, j);
                    points.put(
                            new Point(j - s.length(), i),
                            new Number(Integer.parseInt(s), s.length()));
                    j--;
                } else {
                    if(c != '.') {
                        symbols.put(new Point(j, i), c);
                    }
                }
            }
        }
        return new Data(points, symbols);
    }


    private static Set<Point> getCandidates(Map<Point, Number> numberMap, Point p) {
        Number number = numberMap.get(p);
        Set<Point> candidates = new HashSet<>();

        for(int i=-1; i<number.length()+1; i++) {
            candidates.add(new Point(p.x()+i, p.y()));
            candidates.add(new Point(p.x()+i, p.y()+1));
            candidates.add(new Point(p.x()+i, p.y()-1));
        }
        return candidates;
    }


    private static int sumOfPartNumbers(Data p) {
        return p.numberMap.entrySet().stream()
                .filter(e -> {
                    Set<Point> candidates = getCandidates(p.numberMap, e.getKey());
                    return candidates.stream().anyMatch(p.symbolMap::containsKey);
                })
                .mapToInt(e -> e.getValue().value())
                .sum();
    }

    @Test
    public void partOne() {
        Data p = parseInput(input1);
        assertEquals(4361, sumOfPartNumbers(p));
        String file = String.join("\n",
                FileReader.readFileStringList("src/main/resources/2023/D3.txt"));
        Data p2 = parseInput(file);
        assertEquals(543867, sumOfPartNumbers(p2));
    }

    private static int sumOfGearRatios(Data p) {
        // Filter out all non * symbols
        Set<Point> asterisk = p.symbolMap.entrySet().stream()
                .filter(e -> e.getValue() == '*')
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // For each number, find all candidates
        // For each candidate, if there is also an asterisk, check for previous
        // get gear ratio add the number current the sum
        int sum = 0;
        Map<Point, Integer> candidateMap = new HashMap<>();
        for(Map.Entry<Point, Number> e : p.numberMap.entrySet()) {
            Set<Point> candidates = getCandidates(p.numberMap, e.getKey());
            for(Point candidate : candidates) {
                if(asterisk.contains(candidate)) {
                    if(candidateMap.containsKey(candidate)) {
                        sum += candidateMap.get(candidate) * e.getValue().value();
                    }
                    candidateMap.put(candidate, e.getValue().value());
                }
            }
        }

        return sum;
    }

    @Test
    public void partTwo() {
        assertEquals(467835, sumOfGearRatios(parseInput(input1)));
        String file = String.join("\n",
                FileReader.readFileStringList("src/main/resources/2023/D3.txt"));
        Data p2 = parseInput(file);
        assertEquals(79613331, sumOfGearRatios(p2));
    }
}
