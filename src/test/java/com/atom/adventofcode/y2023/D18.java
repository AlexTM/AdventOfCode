package com.atom.adventofcode.y2023;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class D18 {

    private static final String testInput = """
            R 6 (#70c710)
            D 5 (#0dc571)
            L 2 (#5713f0)
            D 2 (#d2c081)
            R 2 (#59c680)
            D 2 (#411b91)
            L 5 (#8ceee2)
            U 2 (#caa173)
            L 1 (#1b58a2)
            U 2 (#caa171)
            R 2 (#7807d2)
            U 3 (#a77fa3)
            L 2 (#015232)
            U 2 (#7a21e3)
            """;

    enum Direction {U, D, L, R}
    record Instruction(Direction dir, long steps, String  color) {}
    record Pos(long x, long y) {}

    private static Instruction parseLine(String line) {
        String[] split = line.split(" ");
        return new Instruction(Direction.valueOf(split[0]), Integer.parseInt(split[1]), split[2].substring(2, 8));
    }

    private static Pos applyInstruction(Pos pos, Direction dir) {
        return switch (dir) {
            case R -> new Pos(pos.x+1, pos.y);
            case L -> new Pos(pos.x-1, pos.y);
            case U -> new Pos(pos.x, pos.y-1);
            case D -> new Pos(pos.x, pos.y+1);
        };
    }

    private Set<Pos> draw(List<Instruction> instructionList) {
        Set<Pos> edges = new HashSet<>();
        Pos pos = new Pos(0,0);
        for(Instruction i : instructionList) {
            for(int steps = 0; steps < i.steps; steps++) {
                pos = applyInstruction(new Pos(pos.x, pos.y), i.dir);
                edges.add(pos);
            }
        }
        return edges;
    }

    private void print(Set<Pos> edges) {
        long minx = edges.stream().mapToLong(p -> p.x).min().getAsLong();
        long maxx = edges.stream().mapToLong(p -> p.x).max().getAsLong();
        long miny = edges.stream().mapToLong(p -> p.y).min().getAsLong();
        long maxy = edges.stream().mapToLong(p -> p.y).max().getAsLong();

        for(long y = miny; y <= maxy; y++) {
            for(long x = minx; x <= maxx; x++) {
                Pos p = new Pos(x, y);
                if(edges.contains(p))
                    System.out.print("#");
                else
                    System.out.print(".");
            }
            System.out.println();
        }
        System.out.println();
    }

    private Set<Pos> fillEnclosedArea(Set<Pos> edges) {
        long minx = edges.stream().mapToLong(p -> p.x).min().getAsLong();
        long maxx = edges.stream().mapToLong(p -> p.x).max().getAsLong();
        long miny = edges.stream().mapToLong(p -> p.y).min().getAsLong();
        long maxy = edges.stream().mapToLong(p -> p.y).max().getAsLong();

        // find first enclosed pos
        boolean started = false;
        Queue<Pos> queue = new LinkedList<>();
        for(long x = minx; x <= maxx; x++) {
            Pos p = new Pos(x, miny+1);
            if(edges.contains(p) && !started) {
                started = true;
            }
            if(!edges.contains(p) && started) {
                queue.add(new Pos(x, miny+1));
                break;
            }
        }

        while(!queue.isEmpty()) {
            Pos p = queue.poll();
            Arrays.stream(Direction.values())
                    .map(d -> applyInstruction(p, d))
                    .filter(p2 -> p2.x >= minx && p2.x <= maxx && p2.y >= miny && p2.y <= maxy)
                    .filter(p2 -> !edges.contains(p2))
                    .forEach(p2 -> {
                        edges.add(p2);
                        queue.add(p2);
                    });
        }

        return edges;
    }

    private long fillEnclosedArea2(Set<Pos> edges) {
        List<Pos> sortedByYthenX = edges.stream().sorted((a, b) -> {
            int c = Long.compare(a.y, b.y);
            if(c == 0)
                return Long.compare(a.x, b.x);
            return c;
        }).toList();

        Pos startOfRow = sortedByYthenX.get(0);
        long area = 0;
        while(true) {
            List<Pos> sortedX = sortedByYthenX.stream().filter(p -> startOfRow.y == p.y).toList();
//            area += getAreaAcrossRow(sortedX);
        }
    }

/*    private long getAreaAcrossRow(List<Pos> sortedX) {

        boolean started = false;
        for(Pos p : sortedX) {

        }

    }*/


    @Test
    public void partOne() {
        List<Instruction> instructionList = testInput.lines().map(D18::parseLine).toList();
        Set<Pos> edge = draw(instructionList);
        print(edge);
        edge = fillEnclosedArea(edge);
        print(edge);
        assertEquals(62, edge.stream().distinct().count());

        List<Instruction> instructionList2 =
                FileReader.readFileString("src/test/resources/2023/D18.txt").lines().map(D18::parseLine).toList();
        Set<Pos> edge2 = draw(instructionList2);
        print(edge2);
        edge2 = fillEnclosedArea(edge2);
        print(edge2);

        assertEquals(35244, edge2.stream().distinct().count());

    }

    private static Instruction convert(String input) {
        Direction d = switch (input.charAt(5)) {
            case '0' -> Direction.R;
            case '1' -> Direction.D;
            case '2' -> Direction.L;
            case '3' -> Direction.U;
            default -> throw new RuntimeException();
        };
        return new Instruction(d, Long.parseLong(input.substring(0, 5), 16), "");
    }

    private Set<Pos> draw2(List<Instruction> instructionList) {
        Set<Pos> edges = new HashSet<>();
        Pos pos = new Pos(0,0);
        edges.add(pos);
        for(Instruction i : instructionList) {
            pos = switch(i.dir) {
                case R -> new Pos(pos.x+i.steps, pos.y);
                case L -> new Pos(pos.x-i.steps, pos.y);
                case U -> new Pos(pos.x, pos.y-i.steps);
                case D -> new Pos(pos.x, pos.y+i.steps);
            };
            edges.add(pos);
        }
        return edges;
    }


    @Test
    public void partTwo() {
        List<Instruction> instructionList = testInput.lines().map(D18::parseLine).toList();
//                .map(i -> convert(i.color)).toList();
        Set<Pos> edge = draw2(instructionList);
        print(edge);
//        edge = fillEnclosedArea(edge);
//        assertEquals(952408144115L, edge.stream().distinct().count());
    }
}
