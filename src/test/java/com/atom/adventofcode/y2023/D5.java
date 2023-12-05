package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D5 {

    private static final String input = """
            seeds: 79 14 55 13
                        
            seed-to-soil map:
            50 98 2
            52 50 48
                        
            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15
                        
            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4
                        
            water-to-light map:
            88 18 7
            18 25 70
                        
            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13
                        
            temperature-to-humidity map:
            0 69 1
            1 0 69
                        
            humidity-to-location map:
            60 56 37
            56 93 4
            """;

    record Mapping(long destination, long source, long length) {}
    private static class Mappings {
        List<Long> seeds = new ArrayList<>();
        Map<String, List<Mapping>> mappings = new LinkedHashMap<>();
        String currentMapping = null;
        @Override
        public String toString() {
            return "Mappings{" +
                    "seeds=" + seeds +
                    ", mappings=" + mappings +
                    '}';
        }
    }

    public Mappings parseData(String input) {
        return FileReader.parseStringForObject(input, new Mappings(), (m, line, count) -> {
            if(line.startsWith("seeds:")) {
                String[] split = line.split(":");
                String[] seeds = split[1].trim().split(" ");
                for(String seed : seeds) {
                    m.seeds.add(Long.parseLong(seed));
                }
                return m;
            }
            if(line.contains("map:")) {
                String[] split = line.split(" ");
                m.currentMapping = split[0].trim();
                return m;
            }
            if(line.isEmpty())
                return m;

            String[] split = line.split(" ");
            List<Mapping> mm = m.mappings.getOrDefault(m.currentMapping, new ArrayList<>());
            mm.add(new Mapping(Long.parseLong(split[0]), Long.parseLong(split[1]), Long.parseLong(split[2])));
            m.mappings.put(m.currentMapping, mm);

            return m;
        });
    }

    private long resolveFor(Mappings mappings, long seed) {
        long value = seed;
        for(Map.Entry<String, List<Mapping>> e : mappings.mappings.entrySet()) {
            for(Mapping m : e.getValue()) {
                if(m.source <= value && m.source + m.length > value) {
                    value = m.destination + (value - m.source);
                    break;
                }
            }
        }
        return value;
    }

    @Test
    public void partOne() {
        Mappings mappings = parseData(input);
        assertEquals(82, resolveFor(mappings, 79));
        assertEquals(43, resolveFor(mappings, 14));
        assertEquals(86, resolveFor(mappings, 55));
        assertEquals(35, resolveFor(mappings, 13));

        assertEquals(35,
                mappings.seeds.stream()
                .mapToLong(i -> resolveFor(mappings, i))
                .min().orElseThrow());

        Mappings mappings2 = parseData(
                String.join("\n", FileReader.readFileStringList("src/main/resources/2023/D5.txt")));

        long res =
                mappings2.seeds.stream()
                        .mapToLong(i -> resolveFor(mappings2, i))
                        .min().orElseThrow();

        assertNotEquals(3139431799L, res);
        assertEquals(462648396, res);
    }

    private long resolveForSeedPair(Mappings mappings, long seedStart, long seedLength) {
        return LongStream.range(seedStart, seedStart + seedLength)
                .parallel()
                .map(i -> resolveFor(mappings, i))
                .min().orElseThrow();
    }

    private long getMinWithPairedSeeds(Mappings mappings) {

        List<Long[]> pairs = new ArrayList<>();
        for(int p = 0; p<mappings.seeds.size(); p+=2) {
            pairs.add(new Long[]{mappings.seeds.get(p), mappings.seeds.get(p+1)});
        }

        // brute force it, ah the power of modern laptops
        return pairs.stream().mapToLong(
                pair -> resolveForSeedPair(mappings, pair[0], pair[1])).min().orElseThrow();
    }

    @Test
    public void partTwo() {
        Mappings mappings = parseData(input);
        assertEquals(46, getMinWithPairedSeeds(mappings));

        Mappings mappings2 = parseData(
                String.join("\n", FileReader.readFileStringList("src/main/resources/2023/D5.txt")));
        assertEquals(2520479, getMinWithPairedSeeds(mappings2));
    }
}
