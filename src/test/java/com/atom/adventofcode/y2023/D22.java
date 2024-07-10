/*
package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D22 {

    private static final String testInput = """
            1,0,1~1,2,1
            0,0,2~2,0,2
            0,2,3~2,2,3
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
            """;

    record Brick(int x1, int y1, int z1, int x2, int y2, int z2, char name) {
        @Override
        public String toString() {
            return "Brick{" +
                    "name=" + name +
                    '}';
        }
    }

    private static char name = 'a';

    private static Brick parseBrick(String line) {
        String[] split = line.split("~");
        String[] split1 = split[0].split(",");
        String[] split2 = split[1].split(",");
        return new Brick(
                Integer.parseInt(split1[0]), Integer.parseInt(split1[1]), Integer.parseInt(split1[2]),
                Integer.parseInt(split2[0]), Integer.parseInt(split2[1]), Integer.parseInt(split2[2]),
                name++);
    }

    record Pos(int x, int y, int z){};

    private static Set<Pos> getShape(Brick brick) {
        int d = 0;
        if(brick.x1 != brick.x2)
            d++;
        if(brick.y1 != brick.y2)
            d++;
        if(brick.z1 != brick.z2)
            d++;
        if(d > 1)
            throw new RuntimeException("");

        if(brick.x1 != brick.x2)
            return IntStream.range(brick.x1, brick.x2+1).mapToObj(i -> new Pos(i, brick.y1, brick.z1)).collect(Collectors.toSet());
        if(brick.y1 != brick.y2)
            return IntStream.range(brick.y1, brick.y2+1).mapToObj(i -> new Pos(brick.x1, i, brick.z1)).collect(Collectors.toSet());
        if(brick.z1 != brick.z2)
            return IntStream.range(brick.z1, brick.z2+1).mapToObj(i -> new Pos(brick.x1, brick.y1, i)).collect(Collectors.toSet());
        return Set.of(new Pos(brick.x1, brick.y1, brick.z1));
    }

    private static void applyGravity(Map<Pos, Brick> height, Brick brick) {
        // given a brick, lower it to until it makes contact with something
        Set<Pos> shape = getShape(brick);
        Set<Pos> oldShape = shape;

        while(true) {
            // change to stream the lot
            Set<Pos> tmp = new HashSet<>(shape);
            tmp.retainAll(height.keySet());
            if(!tmp.isEmpty())
                break;
            // check if any z is 1
            if(shape.stream().anyMatch(p -> p.z == 1))
                break;
            oldShape = shape;
            shape = shape.stream().map(p -> new Pos(p.x, p.y, p.z-1)).collect(Collectors.toSet());
        }
        height.putAll(oldShape.stream().collect(Collectors.toMap(p -> p, p -> brick)));
    }

    private static long stack(List<Brick> bricks) {
        Map<Pos, Brick> height = new LinkedHashMap<>();

        for(Brick b : bricks)
            applyGravity(height, b);

//        height.forEach((k, v) -> System.out.println(k + " -> " + v));

        // foreach brick check if it has any bricks resting on it
        // Reverse the Map<Pos, Brick> to Map<Brick, Set<Pos>>
        Map<Brick, Set<Pos>> reverse = height.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue,
                Collectors.mapping(Map.Entry::getKey, Collectors.toSet())));

//        reverse.forEach((k, v) -> System.out.println(k + " -> " + v));

        Map<Brick, Set<Brick>> dependenciesUp = new HashMap<>();
        Map<Brick, Set<Brick>> dependenciesDown = new HashMap<>();
        bricks.forEach(b -> dependenciesUp.put(b, new HashSet<>()));
        bricks.forEach(b -> dependenciesDown.put(b, new HashSet<>()));

        // for each brick, check if it has any bricks resting on it
        for(Brick brick : bricks) {
        //for(Map.Entry<Brick, Set<Pos>> entry : reverse.entrySet()) {
            //Map.Entry<Brick, Set<Pos>> entry = reverse.get(brick);
            if(!reverse.containsKey(brick))
                continue;
            Set<Pos> positions = reverse.get(brick);

            // for each position, check if there is a brick above it by checking if the position above it is in the map
            for(Pos p : positions) {
                Pos above = new Pos(p.x, p.y, p.z+1);
                if(height.containsKey(above)) {
                    Brick brickAbove = height.get(above);
                    dependenciesUp.get(brick).add(brickAbove);
                    dependenciesDown.get(brickAbove).add(brick);
                }
            }
        }

//        System.out.println();
//        dependenciesUp.forEach((k, v) -> System.out.println(k + " -> " + v));
//        System.out.println();
//        dependenciesDown.forEach((k, v) -> System.out.println(k + " -> " + v));

        int ss = bricks.size() - reverse.size();

        return dependenciesDown.values().stream().filter(s -> s.size() != 1).count()+ss;
    }

    @Test
    public void partOne() {
        List<Brick> bricks = testInput.lines().map(D22::parseBrick).toList();
        assertEquals(5, stack(bricks));

        List<Brick> bricks2 = FileReader.readFileString("src/test/resources/2023/D22.txt")
                .lines().map(D22::parseBrick).toList();

        long result = stack(bricks2);
        assertNotEquals(545, result);
        assertNotEquals(428, result);
        assertNotEquals(443, result);
        assertEquals(0, result);

    }
}
*/
