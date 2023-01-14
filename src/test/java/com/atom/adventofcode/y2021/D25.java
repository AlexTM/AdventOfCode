package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 25: Sea Cucumber ---
 *
 * This is it: the bottom of the ocean trench, the last place the sleigh keys could be. Your submarine's experimental
 * antenna still isn't boosted enough to detect the keys, but they must be here. All you need to do is reach the
 * seafloor and find them.
 *
 * At least, you'd touch down on the seafloor if you could; unfortunately, it's completely covered by two large herds
 * of sea cucumbers, and there isn't an open space large enough for your submarine.
 *
 * You suspect that the Elves must have done this before, because just then you discover the phone number of a deep-sea
 * marine biologist on a handwritten note taped to the wall of the submarine's cockpit.
 *
 * "Sea cucumbers? Yeah, they're probably hunting for food. But don't worry, they're predictable critters: they move in
 * perfectly straight lines, only moving forward when there's space to do so. They're actually quite polite!"
 *
 * You explain that you'd like to predict when you could land your submarine.
 *
 * "Oh that's easy, they'll eventually pile up and leave enough space for-- wait, did you say submarine? And the only
 * place with that many sea cucumbers would be at the very bottom of the Mariana--" You hang up the phone.
 *
 * There are two herds of sea cucumbers sharing the same region; one always moves east (>), while the other always
 * moves south (v). Each location can contain at most one sea cucumber; the remaining locations are empty (.). The
 * submarine helpfully generates a map of the situation (your puzzle input). For example:
 *
 * v...>>.vv>
 * .vv>>.vv..
 * >>.>v>...v
 * >>v>>.>.v.
 * v>v.vv.v..
 * >.>>..v...
 * .vv..>.>v.
 * v.v..>>v.v
 * ....v..v.>
 *
 * Every step, the sea cucumbers in the east-facing herd attempt to move forward one location, then the sea cucumbers
 * in the south-facing herd attempt to move forward one location. When a herd moves forward, every sea cucumber in the
 * herd first simultaneously considers whether there is a sea cucumber in the adjacent location it's facing (even
 * another sea cucumber facing the same direction), and then every sea cucumber facing an empty location simultaneously
 * moves into that location.
 *
 * So, in a situation like this:
 *
 * ...>>>>>...
 *
 * After one step, only the rightmost sea cucumber would have moved:
 *
 * ...>>>>.>..
 *
 * After the next step, two sea cucumbers move:
 *
 * ...>>>.>.>.
 *
 * During a single step, the east-facing herd moves first, then the south-facing herd moves. So, given this situation:
 *
 * ..........
 * .>v....v..
 * .......>..
 * ..........
 *
 * After a single step, of the sea cucumbers on the left, only the south-facing sea cucumber has moved (as it wasn't
 * out of the way in time for the east-facing cucumber on the left to move), but both sea cucumbers on the right have
 * moved (as the east-facing sea cucumber moved out of the way of the south-facing sea cucumber):
 *
 * ..........
 * .>........
 * ..v....v>.
 * ..........
 *
 * Due to strong water currents in the area, sea cucumbers that move off the right edge of the map appear on the left
 * edge, and sea cucumbers that move off the bottom edge of the map appear on the top edge. Sea cucumbers always check
 * whether their destination location is empty before moving, even if that destination is on the opposite side of the
 * map:
 *
 * Initial state:
 * ...>...
 * .......
 * ......>
 * v.....>
 * ......>
 * .......
 * ..vvv..
 *
 * After 1 step:
 * ..vv>..
 * .......
 * >......
 * v.....>
 * >......
 * .......
 * ....v..
 *
 * After 2 steps:
 * ....v>.
 * ..vv...
 * .>.....
 * ......>
 * v>.....
 * .......
 * .......
 *
 * After 3 steps:
 * ......>
 * ..v.v..
 * ..>v...
 * >......
 * ..>....
 * v......
 * .......
 *
 * After 4 steps:
 * >......
 * ..v....
 * ..>.v..
 * .>.v...
 * ...>...
 * .......
 * v......
 *
 * To find a safe place to land your submarine, the sea cucumbers need to stop moving. Again consider the first example:
 *
 * Initial state:
 * v...>>.vv>
 * .vv>>.vv..
 * >>.>v>...v
 * >>v>>.>.v.
 * v>v.vv.v..
 * >.>>..v...
 * .vv..>.>v.
 * v.v..>>v.v
 * ....v..v.>
 *
 * After 1 step:
 * ....>.>v.>
 * v.v>.>v.v.
 * >v>>..>v..
 * >>v>v>.>.v
 * .>v.v...v.
 * v>>.>vvv..
 * ..v...>>..
 * vv...>>vv.
 * >.v.v..v.v
 *
 * After 2 steps:
 * >.v.v>>..v
 * v.v.>>vv..
 * >v>.>.>.v.
 * >>v>v.>v>.
 * .>..v....v
 * .>v>>.v.v.
 * v....v>v>.
 * .vv..>>v..
 * v>.....vv.
 *
 * After 3 steps:
 * v>v.v>.>v.
 * v...>>.v.v
 * >vv>.>v>..
 * >>v>v.>.v>
 * ..>....v..
 * .>.>v>v..v
 * ..v..v>vv>
 * v.v..>>v..
 * .v>....v..
 *
 * After 4 steps:
 * v>..v.>>..
 * v.v.>.>.v.
 * >vv.>>.v>v
 * >>.>..v>.>
 * ..v>v...v.
 * ..>>.>vv..
 * >.v.vv>v.v
 * .....>>vv.
 * vvv>...v..
 *
 * After 5 steps:
 * vv>...>v>.
 * v.v.v>.>v.
 * >.v.>.>.>v
 * >v>.>..v>>
 * ..v>v.v...
 * ..>.>>vvv.
 * .>...v>v..
 * ..v.v>>v.v
 * v.v.>...v.
 *
 * ...
 *
 * After 10 steps:
 * ..>..>>vv.
 * v.....>>.v
 * ..v.v>>>v>
 * v>.>v.>>>.
 * ..v>v.vv.v
 * .v.>>>.v..
 * v.v..>v>..
 * ..v...>v.>
 * .vv..v>vv.
 *
 * ...
 *
 * After 20 steps:
 * v>.....>>.
 * >vv>.....v
 * .>v>v.vv>>
 * v>>>v.>v.>
 * ....vv>v..
 * .v.>>>vvv.
 * ..v..>>vv.
 * v.v...>>.v
 * ..v.....v>
 *
 * ...
 *
 * After 30 steps:
 * .vv.v..>>>
 * v>...v...>
 * >.v>.>vv.>
 * >v>.>.>v.>
 * .>..v.vv..
 * ..v>..>>v.
 * ....v>..>v
 * v.v...>vv>
 * v.v...>vvv
 *
 * ...
 *
 * After 40 steps:
 * >>v>v..v..
 * ..>>v..vv.
 * ..>>>v.>.v
 * ..>>>>vvv>
 * v.....>...
 * v.v...>v>>
 * >vv.....v>
 * .>v...v.>v
 * vvv.v..v.>
 *
 * ...
 *
 * After 50 steps:
 * ..>>v>vv.v
 * ..v.>>vv..
 * v.>>v>>v..
 * ..>>>>>vv.
 * vvv....>vv
 * ..v....>>>
 * v>.......>
 * .vv>....v>
 * .>v.vv.v..
 *
 * ...
 *
 * After 55 steps:
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv...>..>
 * >vv.....>.
 * .>v.vv.v..
 *
 * After 56 steps:
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv....>.>
 * >vv......>
 * .>v.vv.v..
 *
 * After 57 steps:
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv.....>>
 * >vv......>
 * .>v.vv.v..
 *
 * After 58 steps:
 * ..>>v>vv..
 * ..v.>>vv..
 * ..>>v>>vv.
 * ..>>>>>vv.
 * v......>vv
 * v>v....>>v
 * vvv.....>>
 * >vv......>
 * .>v.vv.v..
 *
 * In this example, the sea cucumbers stop moving after 58 steps.
 *
 * Find somewhere safe to land your submarine. What is the first step on which no sea cucumbers move?
 */
public class D25 {

    record Pos(int x, int y){};
    record MaxXY(int maxX, int maxY){};

    private List<Set<Pos>> loadData(String filename) {
        return FileReader.readFileForObject(filename, new ArrayList<>(), (state, line, lineCount) -> {
            if(state.size() == 0) {
                state.add(new HashSet<>());
                state.add(new HashSet<>());
            }
            char[] cs = line.toCharArray();
            for(int i=0; i<cs.length; i++) {
                switch (cs[i]) {
                    case '>' -> state.get(0).add(new Pos(i, lineCount));
                    case 'v' -> state.get(1).add(new Pos(i, lineCount));
                }
            }
            return state;
        });
    }

    private MaxXY getMaxXandMaxY(List<Set<Pos>> data) {
        int mx = data.stream().flatMap(Collection::stream).map(p -> p.x).mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
        int my = data.stream().flatMap(Collection::stream).map(p -> p.y).mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
        return new MaxXY(mx+1, my+1);
    }

    private boolean listsEqual(List<Set<Pos>> lastStep, List<Set<Pos>> thisStep) {
        if(lastStep == null)
            return false;
        if(lastStep.get(0).size() != thisStep.get(0).size())
            return false;
        if(lastStep.get(1).size() != thisStep.get(1).size())
            return false;
        if(!lastStep.get(0).containsAll(thisStep.get(0)))
            return false;
        if(!lastStep.get(1).containsAll(thisStep.get(1)))
            return false;

        return true;
    }

    private Pos boundsCheck(MaxXY m, Pos pos) {
        int x = pos.x;
        int y = pos.y;
        if(pos.x == m.maxX)
            x -= m.maxX;
        if(pos.y == m.maxY)
            y -= m.maxY;
        return new Pos(x, y);
    }

    private Set<Pos> herdMove(Set<Pos> herdPositions, Set<Pos> herd, Function<Pos, Pos> fn) {
        Set<Pos> newHerd = new HashSet<>();
        for (Pos p : herd) {
            Pos newPos = fn.apply(p);
            if (!herdPositions.contains(newPos)) {
                newHerd.add(newPos);
            } else {
                newHerd.add(p);
            }
        }
        return newHerd;
    }

    private List<Set<Pos>> step(List<Set<Pos>> herd, MaxXY m) {

        List<Set<Pos>> newHerds = new ArrayList<>();
        Set<Pos>  all = new HashSet<>();
        all.addAll(herd.get(0));
        all.addAll(herd.get(1));

        newHerds.add(
                herdMove(all, herd.get(0), p -> boundsCheck(m, new Pos(p.x + 1, p.y))));

        all.removeAll(herd.get(0));
        all.addAll(newHerds.get(0));
        newHerds.add(
                herdMove(all, herd.get(1), p -> boundsCheck(m, new Pos(p.x, p.y + 1))));

        return newHerds;
    }

    private int findStopStep(List<Set<Pos>> data) {
        MaxXY m = getMaxXandMaxY(data);

        List<Set<Pos>> lastStep = null;
        int steps = 0;
        while(!listsEqual(lastStep, data)) {
            lastStep = data;
            data = step(lastStep, m);
            steps++;
        }
        return steps;
    }

    @Test
    public void testPart1() {
        assertEquals(58, findStopStep(loadData("src/test/resources/2021/D25_t.txt")));
        assertEquals(337, findStopStep(loadData("src/test/resources/2021/D25.txt")));
    }

}

