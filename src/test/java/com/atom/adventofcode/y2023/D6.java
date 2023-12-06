package com.atom.adventofcode.y2023;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D6 {

    record Race(long time, long distance) {}
    private static List<Race> testRaces =
            List.of(new Race(7, 9), new Race(15, 40), new Race(30,200));
    private static List<Race> races =
            List.of(new Race(59, 543), new Race(68, 1020),
                    new Race(82,1664), new Race(74, 1022));

    private static long countWins(Race race) {
        long speed = 0, count = 0;
        for(long i=0; i<race.time; i++) {
            long coveredDistance = speed * (race.time - i);
            if(coveredDistance > race.distance) {
                count++;
            }
            speed++;
        }
        return count;
    }

    @Test
    public void partOne() {
        assertEquals(288,
                testRaces.stream().mapToLong(D6::countWins)
                                .reduce(1, (i, j) -> i*j));

        assertEquals(275724,
                races.stream().mapToLong(D6::countWins)
                        .reduce(1, (i, j) -> i*j));
    }

    @Test
    public void partTwo() {
        assertEquals(71503, countWins(new Race(71530, 940200)));
        assertEquals(37286485L, countWins(new Race(59688274, 543102016641022L)));
    }

}
