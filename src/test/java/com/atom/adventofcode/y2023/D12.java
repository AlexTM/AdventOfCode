package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class D12 {

    private static final String input = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """;

    record WaterStream(String s, List<Integer> numbers) {}

    private final FileReader.TriFunction<List<WaterStream>, String, Integer, List<WaterStream>> tri = (map, line, i) -> {
        String []split = line.split(" ");
        String []split2 = split[1].split(",");
        List<Integer> nums = Arrays.stream(split2).mapToInt(Integer::parseInt).boxed().toList();
        map.add(new WaterStream(split[0], nums));
        return map;
    };

    private static int resolveSpring(WaterStream ws) {
        return resolveSpring(ws, ws.s, 0);
    }

    private static boolean validate(String s, List<Integer> numbers) {
        // parse s
        int count = 0;
        List<Integer> counts = new ArrayList<>();
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if(c == '#') {
                count++;
            } else if (count != 0){
                counts.add(count);
                count = 0;
            }
        }
        if(count != 0) {
            counts.add(count);
        }

        return counts.equals(numbers);
    }

    private static int resolveSpring(WaterStream ws, String s, int stringPos) {
        while(stringPos < s.length()) {
            char c = ws.s.charAt(stringPos);
            if (c == '?') {
                // could be # or .
                int res = 0;
                res += resolveSpring(ws, s.substring(0, stringPos) + "#" + s.substring(stringPos + 1), stringPos + 1);
                res += resolveSpring(ws, s.substring(0, stringPos) + "." + s.substring(stringPos + 1), stringPos + 1);
                return res;
            }
            stringPos++;
        }
        return validate(s, ws.numbers) ? 1 : 0;
    }

    @Test
    public void partOne() {
        assertTrue(validate("#.#.###", List.of(1, 1, 3)));

        List<WaterStream> streams = FileReader.parseStringForObject(input, new ArrayList<>(), tri);
        assertEquals(1, resolveSpring(streams.get(0)));
        assertEquals(4, resolveSpring(streams.get(1)));
        assertEquals(1, resolveSpring(streams.get(2)));
        assertEquals(1, resolveSpring(streams.get(3)));
        assertEquals(4, resolveSpring(streams.get(4)));
        assertEquals(10, resolveSpring(streams.get(5)));

        assertEquals(21, streams.stream().mapToInt(D12::resolveSpring).sum());

        List<WaterStream> streams2 = FileReader.readFileForObject("src/main/resources/2023/D12.txt", new ArrayList<>(), tri);
        assertEquals(7032, streams2.stream().mapToInt(D12::resolveSpring).sum());
    }

    private static List<WaterStream> unFoldPaper(List<WaterStream> streams) {
        List<WaterStream> newList =  new ArrayList<>();
        for(WaterStream ws : streams) {
            String s = "";
            List<Integer> n = new ArrayList<>();
            for(int i=0; i<5; i++) {
                if(!s.isEmpty())
                    s += "?";
                s += ws.s;
                n.addAll(ws.numbers);
            }
            newList.add(new WaterStream(s, n));
        }
        return newList;
    }

    @Test
    public void partTwo() {
        List<WaterStream> streams = FileReader.parseStringForObject(input, new ArrayList<>(), tri);
        streams = unFoldPaper(streams);


        assertEquals(1, resolveSpring(streams.get(0)));
        System.out.println("1");
        assertEquals(16384, resolveSpring(streams.get(1)));
        System.out.println("1");
        assertEquals(1, resolveSpring(streams.get(2)));
        System.out.println("1");
        assertEquals(16, resolveSpring(streams.get(3)));
        System.out.println("1");
        assertEquals(2500, resolveSpring(streams.get(4)));
        System.out.println("1");
        assertEquals(506250, resolveSpring(streams.get(5)));
        System.out.println("1");

        System.out.println(streams.get(0));

//        assertEquals(525152, streams.stream().mapToInt(D12::resolveSpring).sum());

    }
}
