package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D15 {

    record Pos(int x, int y){};
    record Range(int min, int max){};

    private static Pattern p = Pattern.compile("Sensor at x=(-?\\d*), y=(-?\\d*): closest beacon is at x=(-?\\d*), y=(-?\\d*)");

    private static Map<Pos, Pos> parseLine(String line, Map<Pos, Pos> sensors) {
        Matcher m = p.matcher(line);
        while(m.find()) {
            sensors.put(
                    new Pos(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))),
                    new Pos(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
        }
        return sensors;
    }

    private Range getRangeForLine(Pos sensor, Pos beacon, int line) {
        int dist = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y);
        int tmp = Math.abs(line - sensor.y);

        if(tmp <= dist) {
            int width = dist-tmp;
            return new Range(sensor.x - width, sensor.x + width);
        }
        return null;
    }

    private List<Range> mapSensors(Map<Pos, Pos> sensors, int line) {
        List<Pos> orderList = new ArrayList<>(sensors.keySet());
        orderList.sort(Comparator.comparingInt(o -> o.y));

        List<Range> rangeList = new ArrayList<>();
        for(Map.Entry<Pos, Pos> e : sensors.entrySet()) {
            Pos sensor = e.getKey();
            Pos beacon = e.getValue();
            Range r = getRangeForLine(sensor, beacon, line);
            if(r != null)
                rangeList.add(r);
        }

        return mergeRanges(rangeList);
    }

    private List<Range> mergeRanges(List<Range> rangeList) {
        rangeList.sort(Comparator.comparingInt(o -> o.min));
        Queue<Range> r = new LinkedList<>(rangeList);
        List<Range> result = new ArrayList<>();

        Range r1 = r.poll();
        Range r2 = r.poll();
        while(r2 != null) {
            if(r1.max >= r2.min) {
                r1 = new Range(r1.min, Math.max(r1.max, r2.max));
            } else {
                result.add(r1);
                r1 = r2;
            }
            r2 = r.poll();
        }
        result.add(r1);
        return result;
    }

    @Test
    public void testBeacon() {
        List<Range> rangeList = mapSensors(FileReader.readFileForObject("src/test/resources/2022/D15_t.txt", new HashMap<>(), D15::parseLine), 10);
        assertEquals(26, rangeList.stream().map(m -> m.max - m.min).reduce(0, Integer::sum));

        rangeList = mapSensors(FileReader.readFileForObject("src/test/resources/2022/D15.txt", new HashMap<>(), D15::parseLine), 2000000);
        assertEquals(5240818, rangeList.stream().map(m -> m.max - m.min).reduce(0, Integer::sum));
    }

    private long findDistress(Map<Pos, Pos> sensors, int limit) {
        for(int y=0; y<limit; y++) {
            List<Range> rangeList = mapSensors(sensors, y);
            long x = checkX(rangeList, limit);
            if(x != -1) {
                return (x*4000000)+(long)y;
            }
        }
        return -1;
    }

    private int checkX(List<Range> rangeList, int limit) {
        int x = 0;
        for(Range r : rangeList) {
            if(r.min <= x && r.max > x)
                x = r.max+1;
        }
        return x < limit ? x : -1;
    }

    @Test
    public void testBeacon2() {
        Map<Pos, Pos> sensors = FileReader.readFileForObject("src/test/resources/2022/D15_t.txt", new HashMap<>(), D15::parseLine);
        assertEquals(56000011, findDistress(sensors, 20));

        sensors = FileReader.readFileForObject("src/test/resources/2022/D15.txt", new HashMap<>(), D15::parseLine);
        assertEquals(13213086906101L, findDistress(sensors, 4000000));
    }

}
