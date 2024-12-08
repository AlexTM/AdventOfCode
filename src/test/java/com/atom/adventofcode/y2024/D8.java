package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D8 {

    private static final String TEST_INPUT = """
            ......#....#
            ...#....0...
            ....#0....#.
            ..#....0....
            ....0....#..
            .#....A.....
            ...#........
            #......#....
            ........A...
            .........A..
            ..........#.
            ..........#.""";

    record Antenna(int x, int y, char freq) { }
    record Pos(int x, int y){}
    record Data(Set<Antenna> antennas, int xLimit, int yLimit) { }

    private Data parseInput(String input) {
        Set<Antenna> antennas = new HashSet<>();
        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            String line = lines[y];
            for (int x = 0; x < line.length(); x++) {
                if (line.charAt(x) != '.' && line.charAt(x) != '#') {
                    antennas.add(new Antenna(x, y, line.charAt(x)));
                }
            }
        }
        return new Data(antennas, lines[0].length(),lines.length);
    }

    private boolean addAntiNode(Set<Pos> antiNodes, int x, int y, int xLimit, int yLimit) {
        if(x >= 0 && y >= 0 && x < xLimit && y < yLimit) {
            antiNodes.add(new Pos(x, y));
            return true;
        }
        return false;
    }

    private Set<Pos> computeAntiNodes(Set<Antenna> antennas, char freq, int xLimit, int yLimit, boolean includeResonantHarmonics) {

        Set<Antenna> freqStations = antennas.stream().filter(antenna -> antenna.freq == freq).collect(Collectors.toSet());
        Set<Pos> antiNodes = new HashSet<>();

        for (Antenna station : freqStations) {
            for(Antenna otherStation :  freqStations) {
                if(otherStation == station) {
                    continue;
                }
                int difX = station.x - otherStation.x;
                int difY = station.y - otherStation.y;

                boolean addMore;
                int i=1;
                do {
                    addMore = addAntiNode(antiNodes, station.x + i*difX, station.y + i*difY, xLimit, yLimit);
                    addMore |= addAntiNode(antiNodes, otherStation.x - i*difX, otherStation.y - i*difY, xLimit, yLimit);
                    i++;
                } while(addMore && includeResonantHarmonics);
            }
        }
        return antiNodes;
    }

    private Set<Pos> getAllAntiNodes(Data data, boolean includeResonantHarmonics) {
        Set<Character> frequencies = data.antennas.stream().map(antenna -> antenna.freq).collect(Collectors.toSet());
        Set<Pos> antiNodes = new HashSet<>();
        for (char f : frequencies) {
            antiNodes.addAll(computeAntiNodes(data.antennas, f, data.xLimit, data.yLimit, includeResonantHarmonics));
        }
        return antiNodes;
    }

    @Test
    public void testPart1() {
        Data data = parseInput(TEST_INPUT);
        assertEquals(14, getAllAntiNodes(data, false).size());

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D8.txt"));
        assertEquals(413, getAllAntiNodes(data, false).size());
    }

    private long sumResonantHarmonics(Data data) {
        Set<Pos> antiNodes = getAllAntiNodes(data, true);
        Set<Pos> positions = data.antennas.stream().map(a -> new Pos(a.x, a.y)).collect(Collectors.toSet());
        antiNodes.addAll(positions);
        return antiNodes.size();
    }

    @Test
    public void testPart2() {
        Data data = parseInput("""
                T....#....
                ...T......
                .T....#...
                .........#
                ..#.......
                ..........
                ...#......
                ..........
                ....#.....
                ..........""");
        assertEquals(9, sumResonantHarmonics(data));

        data = parseInput(TEST_INPUT);
        assertEquals(34, sumResonantHarmonics(data));

        data = parseInput(FileReader.readFileString("src/test/resources/2024/D8.txt"));
        assertEquals(1417, sumResonantHarmonics(data));
    }
}
