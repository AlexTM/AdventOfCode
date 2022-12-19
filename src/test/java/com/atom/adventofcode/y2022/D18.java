package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D18 {

    enum Direction {LEFT, RIGHT, FRONT, BACK, UP, DOWN}

    record Cube(int x, int y, int z){}
    record Face(int x, int y, int z, Direction direction){}

    private static Cube parseString(String line) {
        String[] split = line.split(",");
        return new Cube(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()), Integer.parseInt(split[2].trim()));
    }

    private static Face createOpposite(Face s) {
        return switch (s.direction) {
            case UP -> new Face(s.x, s.y - 1, s.z, Direction.DOWN);
            case DOWN -> new Face(s.x, s.y + 1, s.z, Direction.UP);
            case LEFT -> new Face(s.x - 1, s.y, s.z, Direction.RIGHT);
            case RIGHT -> new Face(s.x + 1, s.y, s.z, Direction.LEFT);
            case FRONT -> new Face(s.x, s.y, s.z - 1, Direction.BACK);
            case BACK -> new Face(s.x, s.y, s.z + 1, Direction.FRONT);
        };
    }

    private static Set<Face> mergeFaces(Set<Face> sidesa, Set<Face> sidesb) {
        for(Face s : sidesb) {
            Face opp = createOpposite(s);
            if(sidesa.contains(opp)) {
                sidesa.remove(opp);
            } else {
                sidesa.add(s);
            }
        }
        return sidesa;
    }

    private static Set<Face> resolveToFaces(final Cube cube) {
        return Arrays.stream(Direction.values())
                .map(s -> new Face(cube.x, cube.y, cube.z, s))
                .collect(Collectors.toSet());
    }

    private static Set<Face> loopAllCubes(final List<Cube> cubes) {
        return cubes.stream()
                .map(D18::resolveToFaces)
                .reduce(new HashSet<>(), D18::mergeFaces);
    }

    @Test
    public void testLava() {
        assertEquals(64, loopAllCubes(FileReader.readFileObjectList("src/test/resources/2022/D18_t.txt", D18::parseString)).size());
        assertEquals(4310, loopAllCubes(FileReader.readFileObjectList("src/test/resources/2022/D18.txt", D18::parseString)).size());
    }

    private Set<Face> fillBucket(Set<Face> faces, int bucketSize) {
        Set<Face> facesInContactWithWater = new HashSet<>();
        Set<Cube> visited = new HashSet<>();
        Queue<Cube> water = new LinkedList<>();
        water.add(new Cube(bucketSize,bucketSize,bucketSize));
        while(!water.isEmpty()) {
            Cube cube = water.poll();
            if(visited.contains(cube))
                continue;
            visited.add(cube);
            Set<Face> waterFaces = resolveToFaces(cube);
            for(Face wf : waterFaces) {
                Face oppFace = createOpposite(wf);
                if(oppFace.x > bucketSize || oppFace.y > bucketSize || oppFace.z > bucketSize ||
                        oppFace.x < -1 || oppFace.y < -1 || oppFace.z < -1)
                    continue;
                if(faces.contains(oppFace)) {
                    facesInContactWithWater.add(oppFace);
                } else {
                    water.add(new Cube(oppFace.x, oppFace.y, oppFace.z));
                }
            }
        }

        return facesInContactWithWater;
    }

    @Test
    public void testLava2() {
        assertEquals(58, fillBucket(
                loopAllCubes(FileReader.readFileObjectList("src/test/resources/2022/D18_t.txt", D18::parseString)), 10).size());

        assertEquals(2466, fillBucket(
                loopAllCubes(FileReader.readFileObjectList("src/test/resources/2022/D18.txt", D18::parseString)), 30).size());
    }

}
