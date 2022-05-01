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
 * Now, you just need to figure out how many orbital transfers you (YOU) need to take to get to Santa (SAN).
 *
 * You start at the object YOU are orbiting; your destination is the object SAN is orbiting. An orbital transfer
 * lets you move from any object to an object orbiting or orbited by that object.
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
 * K)YOU
 * I)SAN
 * Visually, the above map of orbits looks like this:
 *
 *                           YOU
 *                          /
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *                \
 *                 I - SAN
 * In this example, YOU are in orbit around K, and SAN is in orbit around I. To move from K to I, a minimum of 4
 * orbital transfers are required:
 *
 * K to J
 * J to E
 * E to D
 * D to I
 * Afterward, the map of orbits looks like this:
 *
 *         G - H       J - K - L
 *        /           /
 * COM - B - C - D - E - F
 *                \
 *                 I - SAN
 *                  \
 *                   YOU
 * What is the minimum number of orbital transfers required to move from the object YOU are orbiting to the object
 * SAN is orbiting? (Between the objects they are orbiting - not between YOU and SAN.)
 */
public class Day6P2 {

    @Test
    public void testA() throws IOException, URISyntaxException {

        String fileName = "orbits2";

        Path path = Paths.get(getClass().getClassLoader()
                .getResource(fileName).toURI());

        Stream<String> stream = Files.lines(path);
        List<String> input = stream.collect(Collectors.toList());

        int steps = calculateMinDistance(input, "YOU", "SAN");
        assertEquals(4, steps);
    }

    @Test
    public void testPuzzle() throws IOException, URISyntaxException {

        String fileName = "orbits_puzzle";

        Path path = Paths.get(getClass().getClassLoader()
                .getResource(fileName).toURI());

        Stream<String> stream = Files.lines(path);
        List<String> input = stream.collect(Collectors.toList());

        int steps = calculateMinDistance(input, "YOU", "SAN");
        assertEquals(514, steps);
    }

    private List<Node> walkUpTree(Map<String, Node> planetMapUp, String start) {
        List<Node> fromStart = new ArrayList<>();
        Node n = planetMapUp.get(start);
        while(n != null) {
            fromStart.add(n);
            if(n.getNodes().size() > 1) {
                throw new RuntimeException("This should not happen, as every child only has 1 parent");
            }
            if(n.getNodes().size() == 1)
                n = n.getNodes().get(0);
            else
                n = null;
        }
        return fromStart;
    }

    public int calculateMinDistance(List<String> input, String start, String end) {

        Map<String, Node> planetMapUp = Orbit.buildMap(input, Orbit.Direction.UP);

        // Walk up tree to get order list of parents
        List<Node> fromStart = walkUpTree(planetMapUp, start);
        List<Node> fromEnd = walkUpTree(planetMapUp, end);

        // Reverse the list and work out the common ancestor
        Collections.reverse(fromStart);
        Collections.reverse(fromEnd);
        Iterator<Node> startIt = fromStart.iterator();
        Iterator<Node> endIt = fromEnd.iterator();

        Node lastConnected = null;
        while(startIt.hasNext() && endIt.hasNext()) {
            Node s = startIt.next();
            Node e = endIt.next();
            if(s.equals(e)) {
                lastConnected = s;
                continue;
            }
            break;
        }

        if(lastConnected == null) {
            return -1;
        }

        // Using common ancestor add the orbits required to reach it
        // minus 2 as that is the way it works!
        // eg. only need to count common ancestor orbit once
        //     start point is not really an orbit
        Map<String, Integer> orbitDepths = new HashMap<>();
        Map<String, Node> planetMapDown = Orbit.buildMap(input, Orbit.Direction.DOWN);
        Orbit.updateDepth(planetMapDown.get(lastConnected.getName()), 0, orbitDepths);
        int orbitStart = orbitDepths.get(start);
        int orbitEnd = orbitDepths.get(end);

        return (orbitStart) + (orbitEnd) - 2;
    }

}
