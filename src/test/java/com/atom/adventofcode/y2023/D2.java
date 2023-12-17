package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D2 {

    private static final String input1 = """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
            """;

    record Handful(int blue, int green, int red) {}
    record Game(int number, List<Handful> handfuls) {}

    private static Game parseLine(String line) {
        List<Handful> handfuls = new ArrayList<>();
        String[] partsa = line.split(":");

        String[] partsb = partsa[1].split(";");
        for(String h : partsb) {
            int red=0, green=0, blue=0;
            String[] cubes = h.split(",");
            for(String c : cubes) {
                String[] bb = c.trim().split(" ");
                switch (bb[1]) {
                    case "blue" -> blue = Integer.parseInt(bb[0]);
                    case "green" -> green = Integer.parseInt(bb[0]);
                    case "red" -> red = Integer.parseInt(bb[0]);
                }
            }
            handfuls.add(new Handful(blue, green, red));
        }
        return new Game(Integer.parseInt(partsa[0].split(" ")[1].trim()), handfuls);
    }

    private static boolean filterGames(Game game, int limitBlue, int limitGreen, int limitRed) {
        for(Handful hh : game.handfuls) {
            if(hh.blue() > limitBlue || hh.green() > limitGreen || hh.red() > limitRed) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void partOne() {

        int limitRed = 12;
        int limitGreen = 13;
        int limitBlue = 14;

        int sum = Arrays.stream(input1.split("\n"))
                .map(D2::parseLine)
                .filter(g -> filterGames(g, limitBlue, limitGreen, limitRed))
                .mapToInt(Game::number).sum();
        assertEquals(8, sum);

        int res = FileReader.readFileStringList("src/main/resources/2023/D2.txt").stream()
                .map(D2::parseLine)
                .filter(g -> filterGames(g, limitBlue, limitGreen, limitRed))
                .mapToInt(Game::number).sum();
        assertEquals(3099, res);
    }

    private static int getPowerOfCubes(Game game) {
        int red = 0, green = 0, blue = 0;
        for(Handful hh : game.handfuls) {
            red = Math.max(red, hh.red());
            green = Math.max(green, hh.green());
            blue = Math.max(blue, hh.blue());
        }
        return red*green*blue;
    }

    @Test
    public void partTwo() {
        int sum = Arrays.stream(input1.split("\n"))
                .map(D2::parseLine)
                .mapToInt(D2::getPowerOfCubes)
                .sum();
        assertEquals(2286, sum);

        int res = FileReader.readFileStringList("src/main/resources/2023/D2.txt").stream()
                .map(D2::parseLine)
                .mapToInt(D2::getPowerOfCubes)
                .sum();
        assertEquals(72970, res);
    }
}
