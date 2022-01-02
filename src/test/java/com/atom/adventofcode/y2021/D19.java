package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

public class D19 {

    record Pos(int x, int y, int z){};
    class Scanner {
        Set<Pos> beacons = new HashSet<>();
        Pos centriod;

        @Override
        public String toString() {
            return "Scanner{" +
                    "beacons=" + beacons +
                    ", centriod=" + centriod +
                    '}';
        }
    }
//    class Data {
//        Map<Scanner, Pos>
//    }

    public List<Scanner> loadData(String filename) {
        //        Data
        final List<Scanner> scannwers = new ArrayList<>();
        return FileReader.readFileForObject(filename, scannwers, (line, state) -> {
            if(line.isEmpty()) return state;
            if(line.startsWith("--- scanner")) {
                state.add(new Scanner());
                return state;
            }
            String[] parts = line.split(",");
            state.get(state.size()-1).beacons.add(new Pos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            return state;
        });
    };

    public Pos getAveragePositions(Collection<Pos> pos) {
        int x = 0, y = 0, z = 0;
        for(var p : pos) {
            x += p.x;
            y += p.y;
            z += p.z;
        }
        return new Pos(x/pos.size(), y/pos.size(), z/ pos.size());
    }

    @Test
    public void testPart1() {
        List<Scanner> scanners = loadData("src/test/resources/2021/D19_t.txt");
        for(Scanner s : scanners)
            s.centriod = getAveragePositions(s.beacons);
        System.out.println(scanners);
    }
}
