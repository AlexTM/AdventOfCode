package com.atom.adventofcode.y2019;

import com.atom.adventofcode.y2019.orbits.Node;
import com.atom.adventofcode.y2019.orbits.Orbit;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * You've landed at the Universal Orbit Map facility on Mercury. Because navigation in space often involves transferring
 * between orbits, the orbit maps here are useful for finding efficient routes between, for example, you and Santa.
 * You download a map of the local orbits (your puzzle input).
 *
 * Except for the universal Center of Mass (COM), every object in space is in orbit around exactly one other object.
 * An orbit looks roughly like this:
 *
 *                   \
 *                    \
 *                     |
 *                     |
 * AAA--> o            o <--BBB
 *                     |
 *                     |
 *                    /
 *                   /
 * In this diagram, the object BBB is in orbit around AAA. The path that BBB takes around AAA (drawn with lines) is
 * only partly shown. In the map data, this orbital relationship is written AAA)BBB, which means "BBB is in orbit
 * around AAA".
 *
 * Before you use your map data to plot a course, you need to make sure it wasn't corrupted during the download. To
 * verify maps, the Universal Orbit Map facility uses orbit count checksums - the total number of direct orbits (like
 * the one shown above) and indirect orbits.
 *
 * Whenever A orbits B and B orbits C, then A indirectly orbits C. This chain can be any number of objects long: if
 * A orbits B, B orbits C, and C orbits D, then A indirectly orbits D.
 *
 * For example, suppose you have the following map:
 *
 * COM)B
 * B)C
 * C)D
 * D)E
 * E)F
 * B)G
 * G)H
 * D)I
 * E)J
 * J)K
 * K)L
 * Visually, the above map of orbits looks like this:
 *
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *                \
 *                 I
 * In this visual representation, when two objects are connected by a line, the one on the right directly orbits the
 * one on the left.
 *
 * Here, we can count the total number of orbits as follows:
 *
 * D directly orbits C and indirectly orbits B and COM, a total of 3 orbits.
 * L directly orbits K and indirectly orbits J, E, D, C, B, and COM, a total of 7 orbits.
 * COM orbits nothing.
 * The total number of direct and indirect orbits in this example is 42.
 *
 * What is the total number of direct and indirect orbits in your map data?
 */
public class Day6P1 {


    @Test
    public void testA() throws IOException, URISyntaxException {

        String fileName = "planetMapTest";

        Path path = Paths.get(getClass().getClassLoader()
                .getResource(fileName).toURI());


        //read file into stream, try-with-resources
        Stream<String> stream = Files.lines(path);
        List<String> input = stream.collect(Collectors.toList());
        Map<String, Node> planetMap = Orbit.buildMap(input, Orbit.Direction.DOWN);
        planetMap.values().stream().forEach(System.out::println);

        Map<String, Integer> planetOrbits = new HashMap<>();
        Orbit.updateDepth(planetMap.get("COM"), 0, planetOrbits);

        assertEquals(Integer.valueOf(42), (Integer) planetOrbits.values().stream().mapToInt(Integer::intValue).sum());

    }

    @Test
    public void testPuzzle() throws IOException, URISyntaxException {

        String fileName = "planetMap";

        Path path = Paths.get(getClass().getClassLoader()
                .getResource(fileName).toURI());


        Stream<String> stream = Files.lines(path);
        List<String> input = stream.collect(Collectors.toList());
        Map<String, Node> planetMap = Orbit.buildMap(input, Orbit.Direction.DOWN);

        Map<String, Integer> planetOrbits = new HashMap<>();
        Orbit.updateDepth(planetMap.get("COM"), 0, planetOrbits);

        assertEquals(Integer.valueOf(314247), (Integer) planetOrbits.values().stream().mapToInt(Integer::intValue).sum());

    }



}
