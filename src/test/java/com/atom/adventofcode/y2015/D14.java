package com.atom.adventofcode.y2015;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D14 {

    record Reindeer(String name, int speed, int time, int rest){};

    private final List<Reindeer> reindeerList = List.of(
            new Reindeer("Rudolph", 22, 8, 165),
            new Reindeer("Cupid", 8, 17, 114),
            new Reindeer("Prancer", 18, 6, 103),
            new Reindeer("Donner", 25, 6, 145),
            new Reindeer("Dasher", 11, 12, 125),
            new Reindeer("Comet", 21, 6, 121),
            new Reindeer("Blitzen", 18, 3, 50),
            new Reindeer("Vixen", 20, 4, 75),
            new Reindeer("Dancer", 7, 20, 119)
    );

    private int distanceTraveled(Reindeer reindeer, int time) {
        int cycleTime = reindeer.time + reindeer.rest;
        int cycles = time/cycleTime;
        int remainderTime = time%cycleTime;
        remainderTime = Math.min(remainderTime, reindeer.time);
        return (cycles * reindeer.speed * reindeer.time) +
                reindeer.speed*remainderTime;
    }

    @Test
    public void testSpeed() {
        Reindeer comet = new Reindeer("comet", 14, 10, 127);
        Reindeer dancer = new Reindeer("dancer", 16, 11, 162);

        assertEquals(1120, distanceTraveled(comet, 1000));
        assertEquals(1056, distanceTraveled(dancer, 1000));

        int max =
                reindeerList.stream().map(r -> distanceTraveled(r, 2503))
                        .max(Comparator.naturalOrder()).get();

        assertEquals(2696, max);
    }

    // TODO this function needs a redo
    private Map<Reindeer, Integer> distanceTraveled2(List<Reindeer> reindeerList, int time) {
        LinkedHashMap<Reindeer, Integer> distances = new LinkedHashMap<>();
        Map<Reindeer, Integer> scores = new HashMap<>();
        for(int i=0; i<time; i++) {
            for(Reindeer reindeer : reindeerList) {
                int cycleTime = reindeer.time + reindeer.rest;
                int remainder = i % cycleTime;
                if (remainder < reindeer.time) {
                    int distance = distances.getOrDefault(reindeer, 0);
                    distances.put(reindeer, distance + reindeer.speed);
                }
            }

            // order map by value
            var res = distances.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (x,y)-> {throw new AssertionError();},
                            LinkedHashMap::new
                    ));

            int dist = -1;
            for(Map.Entry<Reindeer, Integer> e : res.entrySet()) {
                if(dist==-1)
                    dist = e.getValue();
                else if(dist != e.getValue())
                    break;
                scores.put(e.getKey(), scores.getOrDefault(e.getKey(), 0)+1);
            }
        }
        return scores;
    }


    @Test
    public void testSpeed2() {
        List<Reindeer> reindeerListtTmp = List.of(
                new Reindeer("comet", 14, 10, 127),
                new Reindeer("dancer", 16, 11, 162));

        Map<Reindeer, Integer> scores = distanceTraveled2(reindeerListtTmp, 1000);
        assertEquals(312, scores.get(reindeerListtTmp.get(0)));
        assertEquals(689, scores.get(reindeerListtTmp.get(1)));

        Map<Reindeer, Integer> scores2 = distanceTraveled2(reindeerList, 2503);
        int max = scores2.values().stream().max(Comparator.naturalOrder()).get();
        assertEquals(1084, max);
    }

}
