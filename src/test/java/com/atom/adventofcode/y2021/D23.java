package com.atom.adventofcode.y2021;

import org.junit.jupiter.api.Test;

import java.util.*;

public class D23 {

    class Node {
        final String id;
        final List<Node> nodes = new ArrayList<>();
        public Node(String id) {
            this.id = id;
        }
    }

    class Amphipod {
        final int id;
        final Character type;
        public Amphipod(Character type, int id) {
            this.type = type;
            this.id = id;
        }

        @Override
        public String toString() {
            return "Amphipod{" + type +
                    '}';
        }
    }

    record House(List<Node> hallway, List<List<Node>> rooms){};
    record Move(Amphipod amphipod, Node node){};

    private List<Node> generateStringOfNodes(int size, String prefix) {
        List<Node> nodeList = new ArrayList<>();
        Node last = null;
        for(int i=0; i<size; i++) {
            Node current = new Node(prefix+i);
            if(last != null) {
                current.nodes.add(last);
                last.nodes.add(current);
            }
            last = current;
            nodeList.add(current);
        }
        return nodeList;
    }

    // Build a graph to represent the house
    public House buildHouse() {

        List<Node> hallway = generateStringOfNodes(11, "H");
        List<List<Node>> rooms = new ArrayList<>();

        for(int j=0; j<4; j++) {
            rooms.add(generateStringOfNodes(2, "R"+j+"_"));
        }

        // Connect up rooms and hallway
        hallway.get(2).nodes.add(rooms.get(0).get(1));
        rooms.get(0).get(1).nodes.add(hallway.get(2));

        hallway.get(4).nodes.add(rooms.get(1).get(1));
        rooms.get(1).get(1).nodes.add(hallway.get(4));

        hallway.get(6).nodes.add(rooms.get(2).get(1));
        rooms.get(2).get(1).nodes.add(hallway.get(6));

        hallway.get(8).nodes.add(rooms.get(3).get(1));
        rooms.get(3).get(1).nodes.add(hallway.get(8));

        return new House(hallway, rooms);
    }

    public HashMap<Amphipod, Node> populate(House house, Character[][] am) {
        HashMap<Amphipod, Node> locations = new HashMap<>();
        int c = 0;
        for(int room=0; room<am.length; room++) {
            for(int i=0; i<am[0].length; i++) {
                locations.put(new Amphipod(am[room][i], c++), house.rooms.get(room).get(i));
            }
        }
        return locations;
    }

    private Map<Node, Amphipod> reverseMap(Map<Amphipod, Node> locations) {
        Map<Node, Amphipod> rev = new HashMap<>();
        for(Map.Entry<Amphipod, Node> e : locations.entrySet()) {
            rev.put(e.getValue(), e.getKey());
        }
        return rev;
    }

    private List<Move> getAllMoves(House house, Map<Amphipod, Node> locations) {

        // Reverse Map
        Map<Node, Amphipod> rev = reverseMap(locations);

        List<Move> moves = new ArrayList<>();
        for(Map.Entry<Amphipod, Node> e : locations.entrySet()) {
            for(Node possibleMove : e.getValue().nodes) {
                if(!rev.containsKey(possibleMove)) {
                    moves.add(new Move(e.getKey(), possibleMove));
                }
            }
        }
        return moves;
    }

    public void go(Character[][] initialSetup) {
        House house = buildHouse();
        HashMap<Amphipod, Node> locations = populate(house, initialSetup);

        Set<String> cache = new HashSet<>();
        rec(house, locations, cache, 0, new ArrayList<>());

        System.out.println("Min cost: "+minCost);
        System.out.println(bestMoveList);
        System.out.println("Searches: "+searches);
        System.out.println("Caches size: "+cache.size());
        System.out.println("Caches hits: "+cacheHits);
    }

    static int minCost = Integer.MAX_VALUE;
    static List<Move> bestMoveList;
    static long searches = 0;
    static long cacheHits = 0;

    public void rec(House house, Map<Amphipod, Node> locations, Set<String> cache, int currentCost, List<Move> moveList) {

        // Don't waste time if already worst than best known
        if(currentCost > minCost)
            return;

        // Cache so we don't repeat or get stuck in loops
        String cacheIndex = representation(locations);
        if(cache.contains(cacheIndex)) {
            cacheHits++;
            return;
        }
        cache.add(cacheIndex);

        if(isSolved(house, locations)) {
            System.out.println("SOLVED!!!!!!!!!!!!!!!!!!!!!! "+currentCost);
            minCost = Math.min(minCost, currentCost);
            bestMoveList = moveList;
            print(house, locations);
            return;
        }

        // Don't do more than 50 moves
        if(moveList.size() > 100)
            return;
        searches++;

        //print(house, locations);

        List<Move> moves = getAllMoves(house, locations);

        for(Move move : moves) {
            Map<Amphipod, Node> newLocations = new HashMap<>(locations);
            List<Move> newMoveList = new ArrayList<>(moveList);
            // do move
            newLocations.put(move.amphipod, move.node);
            newMoveList.add(move);
            int cost=0;
            switch(move.amphipod.type) {
                case 'A' -> cost = 1;
                case 'B' -> cost = 10;
                case 'C' -> cost = 100;
                case 'D' -> cost = 1000;
            }

            // check is solved
            rec(house, newLocations, cache, cost+currentCost, newMoveList);
        }
    }

    private boolean isSolved(House house, Map<Amphipod, Node> locations) {
        Map<Node, Amphipod> rev = reverseMap(locations);
        for(List<Node> room : house.rooms) {
            Character c = null;
            for(Node n : room) {
                if(!rev.containsKey(n))
                    return false;
                if(c == null)
                    c = rev.get(n).type;
                if(c != rev.get(n).type)
                    return false;
            }
        }
        return true;
    }

    public String representation(Map<Amphipod, Node> locations) {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Amphipod, Node> e : locations.entrySet()) {
            sb.append("[A=").append(e.getKey().id).append(", N=").append(e.getValue().id).append("]");
        }
        return sb.toString();
    }

    public void print(House house, Map<Amphipod, Node> locations) {
        Map<Node, Amphipod> rev = reverseMap(locations);

        for(Node n : house.hallway) {
            if(!rev.containsKey(n))
                System.out.print(".");
            else
                System.out.print(rev.get(n).type);
        }
        System.out.println("");

        for(List<Node> room : house.rooms) {
            System.out.print("Room : ");
            for(Node n : room) {
                if(rev.containsKey(n))
                    System.out.print(rev.get(n).type);
                else
                    System.out.print(".");
            }
            System.out.println("");
        }
    }

    @Test
    public void testPart1() {
        //go(new Character[][]{{'A', 'B'}, {'D', 'C'}, {'C', 'B'}, {'A', 'D'}});
        go(new Character[][]{{'A', 'B'}, {'B', 'A'}});
    }
}
