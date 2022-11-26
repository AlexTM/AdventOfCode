package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D16 {

    record Sue(String item1, int value1,
               String item2, int value2,
               String item3, int value3){}

    private static String parseKey(String str) {
        return str.replace(":", "").trim();
    }

    private static int parseValue(String str) {
        return Integer.parseInt(str.replace(",", ""));
    }

    private static Sue parseSue(String input) {
        //  0  1     2      3   4    5    6      7
        // Sue 1: children: 1, cars: 8, vizslas: 7
        String[] split = input.split(" ");
        return new Sue(parseKey(split[2]), parseValue(split[3]), parseKey(split[4]), parseValue(split[5]), parseKey(split[6]), parseValue(split[7]));
    }

    private int findSue(Map<String, Integer> mfcsamResult, List<Sue> sueList) {
        for(int i=0; i<sueList.size(); i++) {
            Sue currentSue = sueList.get(i);
            Sue tmpSue = new Sue(
                    currentSue.item1, mfcsamResult.get(currentSue.item1),
                    currentSue.item2, mfcsamResult.get(currentSue.item2),
                    currentSue.item3, mfcsamResult.get(currentSue.item3)
            );
            if(tmpSue.equals(currentSue))
                return i+1;
        }
        return -1;
    }

    private boolean checkTrue(String item1, int sueValue, int mfcsamValue) {
        return switch (item1) {
            case "cats", "trees" -> sueValue > mfcsamValue;
            case "pomeranians", "goldfish" -> sueValue < mfcsamValue;
            default -> sueValue == mfcsamValue;
        };
    }

    private int findSueWithOutdateRetroencabulator(Map<String, Integer> mfcsamResult, List<Sue> sueList) {
        for(int i=0; i<sueList.size(); i++) {
            Sue currentSue = sueList.get(i);
            if(checkTrue(currentSue.item1, currentSue.value1, mfcsamResult.get(currentSue.item1)) &&
                    checkTrue(currentSue.item2, currentSue.value2, mfcsamResult.get(currentSue.item2)) &&
                    checkTrue(currentSue.item3, currentSue.value3, mfcsamResult.get(currentSue.item3))
            ) {
                return i+1;
            }
        }
        return -1;
    }

    @Test
    public void testPresent() {

        Map<String, Integer> mfcsamResult = Map.of(
                "children", 3,
                "cats", 7,
                "samoyeds", 2,
                "pomeranians", 3,
                "akitas", 0,
                "vizslas", 0,
                "goldfish", 5,
                "trees", 3,
                "cars", 2,
                "perfumes", 1
        );

        List<Sue> sueList = FileReader.readFileObjectList("src/test/resources/2015/D16.txt", D16::parseSue);
        assertEquals(213, findSue(mfcsamResult, sueList));
        assertEquals(323, findSueWithOutdateRetroencabulator(mfcsamResult, sueList));
    }
}
