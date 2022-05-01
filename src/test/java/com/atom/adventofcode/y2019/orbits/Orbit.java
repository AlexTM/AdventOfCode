package com.atom.adventofcode.y2019.orbits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orbit {

    public static void updateDepth(Node com, int depth, Map<String, Integer> planetOrbits) {
        planetOrbits.put(com.getName(), depth);
        for(Node n : com.getNodes())
            updateDepth(n, depth+1, planetOrbits);
    }

    public enum Direction {
        UP, DOWN
    }

    public static Map<String, Node> buildMap(List<String> parts, Direction direction) {
        // TODO redo with stream API
        Map<String, Node> planetMap = new HashMap<>();

        for(String part : parts) {
            String[] planets = part.split("\\)");
            planets[0] = planets[0].trim();
            planets[1] = planets[1].trim();
            if(!planetMap.containsKey(planets[0])) {
                planetMap.put(planets[0], new Node(planets[0]));
            }
            if(!planetMap.containsKey(planets[1])) {
                planetMap.put(planets[1], new Node(planets[1]));
            }
            if(direction.equals(Direction.DOWN))
                planetMap.get(planets[0]).getNodes().add(planetMap.get(planets[1]));
            else
                planetMap.get(planets[1]).getNodes().add(planetMap.get(planets[0]));
        }
        return planetMap;
    }

}
