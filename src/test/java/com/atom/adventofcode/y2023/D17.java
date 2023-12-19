package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D17 {

    private static final String testInput = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """;

    int[][] parseInput(String input) {
        String[] split = input.split("\n");
        int ysize = split.length;
        int xsize = split[0].length();

        int[][] map = new int[xsize][ysize];
        for (int y = 0; y < split.length; y++) {
            char[] chars = split[y].toCharArray();
            for (int x = 0; x < chars.length; x++) {
                map[x][y] = chars[x] - '0';
            }
        }
        return map;
    }

    private enum Direction {NORTH, EAST, SOUTH, WEST}

    record Pos(int x, int y) {}
    record Edge(Pos to, int distance) {}
    record Cart(Pos pos, Direction dir, int count) {}

    private static final Map<Direction, Function<Pos, Pos>> fns = Map.of(
            Direction.NORTH, p -> new Pos(p.x, p.y-1),
            Direction.SOUTH, p -> new Pos(p.x, p.y+1),
            Direction.EAST, p -> new Pos(p.x+1, p.y),
            Direction.WEST, p -> new Pos(p.x-1, p.y)
    );

    private static Stream<Pos> getOptions(Pos p) {
        return Arrays.stream(Direction.values())
                .map(d -> fns.get(d).apply(p));
    }

    private static List<Edge> getOptions(int[][] edges, Pos p, Cart cart) {
        return Arrays.stream(Direction.values())
                .filter(d -> cart.count < 3 || cart.dir != d)
                .map(d -> fns.get(d).apply(p))
                .filter(e -> e.x >= 0 && e.x < edges.length && e.y >= 0 && e.y < edges[0].length)
                .map(e -> new Edge(e, edges[e.x][e.y]))
                .toList();
    }

    private static Cart updateCart(Cart cart, Pos to) {
        // if cart is going in the same direction, increment count else reset count
        Direction d = null;
        if(cart.pos.x > to.x)
            d = Direction.WEST;
        if(cart.pos.x < to.x)
            d = Direction.EAST;
        if(cart.pos.y > to.y)
            d = Direction.NORTH;
        if(cart.pos.y < to.y)
            d = Direction.SOUTH;

        if(d == cart.dir)
            return new Cart(to, d, cart.count+1);
        return new Cart(to, d, 0);
    }

    private static Integer doDijkstra(int[][] edges, Pos start, Pos end) {

        Set<Pos> visited = new HashSet<>();
        Map<Pos, Cart> mappedCarts = new HashMap<>();

        // Set all distance to MAX with exception of start
        HashMap<Pos, Integer> distanceFromStart = new HashMap<>();
        for(int x=0; x < edges.length; x++) {
            for(int y=0; y < edges[0].length; y++) {
                distanceFromStart.put(new Pos(x, y), Integer.MAX_VALUE);
            }
        }

        distanceFromStart.put(start, 0);

        // Visit start location
        Queue<Pos> toVisit = new PriorityQueue<>(Comparator.comparing(distanceFromStart::get));
        toVisit.add(start);

        mappedCarts.put(start, new Cart(start, Direction.NORTH, 0));

        while(!toVisit.isEmpty()) {

            Pos currentNode = toVisit.poll();

            // check if we have already visited
            if(visited.contains(currentNode))
                continue;
            visited.add(currentNode);

            if(currentNode.equals(end)) {
                print(edges, distanceFromStart);
                return distanceFromStart.get(currentNode);
            }

            Cart currentCart = mappedCarts.get(currentNode);

            // get all edges
            List<Edge> eList = getOptions(edges, currentNode, currentCart);
            if(eList != null) {
                for (Edge e : eList) {
                    int newDistance = e.distance + distanceFromStart.get(currentNode);
                    if (newDistance < distanceFromStart.get(e.to)) {
                        distanceFromStart.put(e.to, newDistance);
                        toVisit.add(e.to);
                        mappedCarts.put(e.to, updateCart(currentCart, e.to));
                    }
                }
            }
        }

        distanceFromStart.forEach((key, value) -> System.out.println(key + ": " + value));
        return -1;
    }


    private static void print(int[][] edges, Map<Pos, Integer> distanceFromStart) {
        for(int y=0; y < edges.length; y++) {
            for(int x=0; x < edges[0].length; x++) {
                System.out.print(distanceFromStart.get(new Pos(x, y)) + "\t");
            }
            System.out.println();
        }
        System.out.println();

        List<Pos> path = new ArrayList<>();
        Pos p = new Pos(edges[0].length-1, edges.length-1);
        while(!p.equals(new Pos(0,0))) {
            Pos minPos = p;
            int minValue = Integer.MAX_VALUE;
            for(Pos e : getOptions(p).toList()) {
                if(distanceFromStart.get(e) != null && distanceFromStart.get(e) < minValue) {
                    minValue = distanceFromStart.get(e);
                    minPos = e;
                }
            }
            path.add(minPos);
            p = minPos;
        }

        for(int y=0; y < edges.length; y++) {
            for(int x=0; x < edges[0].length; x++) {
                if(path.contains(new Pos(x, y))) {
                    System.out.print("X\t");
                } else {
                    System.out.print("-\t");
                }
            }
            System.out.println();
        }


    }

    @Test
    public void partOne() {
        int[][] map = parseInput(testInput);
        System.out.println(Arrays.deepToString(map));
        System.out.println(map[0][3]);

        int res = doDijkstra(map, new Pos(0, 0), new Pos(map[0].length-1, map.length-1));
        assertEquals(102, res);
    }

    /*
2>>34^>>>1323
32v>>>35v5623
32552456v>>54
3446585845v52
4546657867v>6
14385987984v4
44578769877v6
36378779796v>
465496798688v
456467998645v
12246868655<v
25465488877v5
43226746555v>
    * */

}