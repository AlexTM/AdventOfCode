package com.atom.adventofcode.y2019.circuit;

import java.util.*;
import java.util.function.Function;

public class Circuit {

    public static class PointStep {
        public int steps;
        public int marker;
        public PointStep(int steps, int marker) {
            this.steps = steps;
            this.marker = marker;
        }
    }

    public static int getNearest(List<Candidate> points, Function<Candidate, Integer> fn) {
        Optional<Candidate> opt =
                points.stream().min(Comparator.comparingInt(fn::apply));
        return fn.apply(opt.get());
    }

    public static int distance(Candidate c) {
        return Math.abs(c.getX()) + Math.abs(c.getY());
    }
    public static int steps(Candidate c) { return Math.abs(c.getSteps()); }

    public static List<Candidate> mapToBoard(String... inputs) {
        HashMap<Point, PointStep> points = new HashMap<>();
        int marker = 1;
        List<Candidate> candidates = new ArrayList<>();
        for(String input : inputs) {
            candidates.addAll(mapToBoard2(points, marker, input));
            marker++;
        }
        return candidates;
    }


    private static List<Candidate> mapToBoard2(HashMap<Point, PointStep> points, int marker, String input) {
        List<Candidate> candidates = new ArrayList<>();
        String parts[] = input.split(",");
        int x = 0;
        int y = 0;
        int steps = 0;
        for (String part : parts) {
            final int i1 = Integer.parseInt(part.substring(1, part.length()));
            final char d = part.charAt(0);
            for (int i = 0; i < i1; i++) {
                switch (d) {
                    case 'U':
                        y--;
                        break;
                    case 'D':
                        y++;
                        break;
                    case 'L':
                        x--;
                        break;
                    case 'R':
                        x++;
                        break;
                    default:
                        System.out.println("Error");
                }

                steps++;
                Point nCan = new Point(x, y);
                if (points.containsKey(nCan) && points.get(nCan).marker != marker) {
                    candidates.add(new Candidate(x, y, steps+points.get(nCan).steps));
                } else {
                    points.put(nCan, new PointStep(steps, marker));
                }
            }
        }
        return candidates;
    }


}
