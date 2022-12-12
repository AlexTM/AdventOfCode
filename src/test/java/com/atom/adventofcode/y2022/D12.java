package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D12 {

    record Pos(int c, int r){};

    static class LoadingState {
        List<Integer[]> map = new ArrayList<>();
        public int[][] toMatrix() {
            int m[][] = new int[map.size()][map.get(0).length];
            for (int x = 0; x < map.size(); x++) {
                for(int y=0; y<map.get(0).length; y++) {
                    m[x][y] = map.get(x)[y];
                }
            }
            return m;
        }
    }

    private static LoadingState loadFile(String line, LoadingState state) {
        Integer[] l = new Integer[line.length()];
        for(int i=0; i<line.length(); i++) {
            l[i] = line.charAt(i) - 'a';
        }
        state.map.add(l);
        return state;
    }

    private void addCandidate(int[][]map, List<Pos> candidate, Pos pos, Pos newPos) {
        if(newPos.c >= 0 && newPos.r >= 0 && newPos.c < map.length && newPos.r < map[0].length) {
            if(map[newPos.c][newPos.r] - map[pos.c][pos.r] <= 1)
                candidate.add(newPos);
        }
    }

    private Pos findPoint(int[][] map, char c, char to) {
        for(int y=0; y<map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                if(map[x][y] == c - 'a') {
                    map[x][y] = to - 'a';
                    return new Pos(x, y);
                }
            }
        }
        return null;
    }

    private List<Pos> getCandidates(int[][]map, Pos pos) {
        List<Pos> c = new ArrayList<>();
        addCandidate(map, c, pos, new Pos(pos.c+1, pos.r));
        addCandidate(map, c, pos, new Pos(pos.c-1, pos.r));
        addCandidate(map, c, pos, new Pos(pos.c, pos.r+1));
        addCandidate(map, c, pos, new Pos(pos.c, pos.r-1));
        return c;
    }

    public HashMap<Pos, Integer> doDijkstra(int[][] map, Pos start, Pos end) {

        HashMap<Pos, Integer> distanceFromStart = new HashMap<>();
        distanceFromStart.put(start, 0);

        Queue<Pos> toVisit = new PriorityQueue<>(
                Comparator.comparing(o -> distanceFromStart.getOrDefault(o, Integer.MAX_VALUE)));
        toVisit.add(start);

        while(!toVisit.isEmpty()) {

            Pos currentNode = toVisit.poll();

            if(currentNode == end)
                break;

            List<Pos> candidates = getCandidates(map, currentNode);
            for (Pos e : candidates) {
                int newDistance = 1 + distanceFromStart.getOrDefault(currentNode, Integer.MAX_VALUE);
                if (newDistance < distanceFromStart.getOrDefault(e, Integer.MAX_VALUE)) {
                    distanceFromStart.put(e, newDistance);
                    toVisit.add(e);
                }
            }
        }

        return distanceFromStart;
    }

    @Test
    public void testSomething() {
        int[][]map = FileReader.readFileForObject(
                "src/test/resources/2022/D12.txt", new LoadingState(), D12::loadFile).toMatrix();

        Pos start = findPoint(map, 'S', 'a');
        Pos end = findPoint(map, 'E', 'z');

        assertEquals(370, doDijkstra(map, start, end).get(end));
    }

    private List<Pos> getAllStarts(int[][] map) {
        List<Pos> allStarts = new ArrayList<>();
        for(int y=0; y<map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                if(map[x][y] == 0) {
                    allStarts.add(new Pos(x, y));
                }
            }
        }
        return allStarts;
    }

    private int findMinFromAllStartPlaces(int[][] map) {
        findPoint(map, 'S', 'a'); // to remove the S
        final Pos end = findPoint(map, 'E', 'z');
        final List<Pos> allStarts = getAllStarts(map);

        return allStarts.parallelStream()
                .map(p -> doDijkstra(map, p, end))
                .filter(m -> m.containsKey(end))
                .map(m -> m.get(end))
                .reduce(Integer.MAX_VALUE, Math::min);
    }

    @Test
    public void testSomething2() {
        assertEquals(29, findMinFromAllStartPlaces(
                FileReader.readFileForObject(
                        "src/test/resources/2022/D12_t.txt", new LoadingState(), D12::loadFile).toMatrix()));
        assertEquals(363, findMinFromAllStartPlaces(
                FileReader.readFileForObject(
                        "src/test/resources/2022/D12.txt", new LoadingState(), D12::loadFile).toMatrix()));
    }

}
