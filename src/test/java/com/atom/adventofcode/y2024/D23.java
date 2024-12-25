package com.atom.adventofcode.y2024;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D23 {

    record Table(int[][] matrix, List<String> indexes) { }
    private Table parseInput(String input) {
        List<String[]> data = input.lines()
                .map(line -> line.split("-")).toList();

        List<String> index = new ArrayList<>();
        for(String[] pair : data) {
            if(!index.contains(pair[0])) {
                index.add(pair[0]);
            }
            if(!index.contains(pair[1])) {
                index.add(pair[1]);
            }
        }
        Collections.sort(index);

        int[][] matrix = new int[index.size()][index.size()];
        for(String[] pair : data) {
            int a = index.indexOf(pair[0]);
            int b = index.indexOf(pair[1]);
            matrix[a][b] = 1;
            matrix[b][a] = 1;
        }
        return new Table(matrix, index);
    }

    private List<Set<String>> getAllGroups(Table data) {
        List<Set<String>> allGroups = new ArrayList<>();

        // Find groups where at least 3 computers are connected to 2 other computers
        for(int i = 0; i < data.matrix.length; i++) {
            Set<String> group = new HashSet<>();
            for(int j = 0; j < data.matrix[i].length; j++) {
                if(data.matrix[i][j] != 0) {
                    group.add(data.indexes.get(j));
                }
            }
            allGroups.add(group);
        }
        return allGroups;
    }

    private boolean groupHasNConnections(Table data, Set<String> group, int n) {
        int[] connections = new int[data.indexes.size()];

        for(String id : group) {
            int idx = data.indexes.indexOf(id);
            for(int j=0; j<data.matrix[idx].length; j++) {
                connections[j] += data.matrix[idx][j];
            }
        }

        System.out.println("Connections: "+Arrays.toString(connections));
        Set<String> groupWithNConnections = new HashSet<>();
        for(int i = 0; i < connections.length; i++) {
            if(connections[i] >= n) {
                groupWithNConnections.add(data.indexes.get(i));
            }
        }
        System.out.println("Group with "+n+" connections: "+groupWithNConnections);
        return true;
    }

    @Test
    public void testPartOne() {

        Table data = parseInput(FileReader.readFileString("src/test/resources/2024/D23_t.txt"));
        print(data);

        List<Set<String>> groups = getAllGroups(data);
//        List<Set<String>> groups = getAllGroups(data).stream().filter(g -> {
//            return g.stream().filter(s -> s.startsWith("t")).findFirst().orElse(null) != null;
//                }).toList();

//        System.out.println(groups);
        groups.stream().forEach(System.out::println);

//        int[] connections = new int[groups.get(0).size()];
//        for(String id : groups.get(0)) {
//            int idx = data.indexes.indexOf(id);
//            System.out.print(id+" : ");
//            for(int j=0; j<data.matrix[idx].length; j++) {
//                System.out.print(data.matrix[idx][j]+",");
//            }
//            System.out.println();
//        }

        groupHasNConnections(data, groups.get(0), 2);

        List<Set<String>> grps = groups.stream().filter(g -> groupHasNConnections(data, g, 2)).toList();
        System.out.println(grps);

        assertEquals(7, grps.size());
    }

    private void print(Table data) {
        System.out.println(data.indexes);
        for(int i = 0; i < data.matrix.length; i++) {
            for(int j = 0; j < data.matrix[i].length; j++) {
                System.out.print(data.matrix[i][j]);
            }
            System.out.println();
        }
    }
}
