package com.atom.adventofcode.y2016;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class D4 {

    record Room(String name, Integer sector, String checksum){}

    private final static Pattern p = Pattern.compile("(\\S*)-(\\d*)\\[(\\S*)\\]");

    private static Room parseRoom(String line) {
        Matcher m = p.matcher(line);
        return m.find() ? new Room(m.group(1), Integer.parseInt(m.group(2)), m.group(3)) : null;
    }

    private static boolean isValid(Room room) {
        LinkedHashMap<Character, Long> freqMap = room.name.chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c!='-').collect(Collectors.groupingBy(
                    Function.identity(),
                    LinkedHashMap::new,
                    Collectors.counting()));

        List<Map.Entry<Character, Long>> l = new ArrayList<>(freqMap.entrySet().stream().toList());
        l.sort((o1, o2) -> {
            int v = o2.getValue().compareTo(o1.getValue());
            if (v != 0)
                return v;
            return o1.getKey().compareTo(o2.getKey());
        });

        String hash = l.stream().map(e -> String.valueOf(e.getKey())).reduce("", (a, b) -> a+b).substring(0,5);
        return hash.equals(room.checksum);
    }

    @Test
    public void testRooms() {
        List<Room> testRooms = List.of(
                new Room("aaaaa-bbb-z-y-x", 123, "abxyz"),
                new Room("a-b-c-d-e-f-g-h", 987, "abcde"),
                new Room("not-a-real-room", 404, "oarel"),
                new Room("totally-real-room", 200, "decoy"));

        assertEquals(1514, testRooms.stream().filter(D4::isValid).mapToInt(r -> r.sector).sum());

        assertEquals(185371,
                FileReader.readFileObjectList("src/test/resources/2016/D4.txt", D4::parseRoom)
                .stream().filter(D4::isValid).mapToInt(r -> r.sector).sum());
    }

    private static String deCrypt(Room room) {
        int shift = room.sector % 26;
        StringBuilder name = new StringBuilder();
        for(int i=0; i< room.name.length(); i++) {
            char c = (char)('a' + ((room.name.charAt(i) - 'a' + shift) % 26));
            if(c == 'L')
                c = ' ';
            name.append(c);
        }
        return name.toString();
    }

    @Test
    public void testCipher() {
        assertEquals("very encrypted name", deCrypt(new Room("qzmt-zixmtkozy-ivhz", 343, null)));
        Room room =
                FileReader.readFileObjectList("src/test/resources/2016/D4.txt", D4::parseRoom)
                        .stream()
                        .filter(D4::isValid)
                        .filter(r -> deCrypt(r).startsWith("north")).findFirst().orElseThrow();
        assertEquals(984, room.sector);
    }
}
