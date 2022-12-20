package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D19 {

    private static Pattern p =
            Pattern.compile("Blueprint (\\d+): " +
                    "Each ore robot costs (\\d+) ore. " +
                    "Each clay robot costs (\\d+) ore. " +
                    "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
                    "Each geode robot costs (\\d+) ore and (\\d+) obsidian.");

    record Blueprint(int oreRobotOre,
                     int clayRobotOre,
                     int obsidianRobotOre, int obsidianRobotClay,
                     int geodeRobotOre, int geodeRobotObsidian){}

    private static Blueprint parseLine(String line) {
        Matcher m = p.matcher(line);
        if(m.find()) {
            return new Blueprint(
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    Integer.parseInt(m.group(5)),
                    Integer.parseInt(m.group(6)),
                    Integer.parseInt(m.group(7))
            );
        }
        return null;
    }

    enum Robot {ORE, CLAY, OBSIDIAN, GEODE}
    enum Action {DO_NOTHING, BUILD_ORE, BUILD_CLAY, BUILD_OBSIDIAN, BUILD_GEODE}

    record State(List<Robot> robots, int ore, int clay, int obsidian, int geode, Set<Action> ignored, int time){}
    record CacheObject(int ore, int clay, int obsidian, int time) {}

    private static int runBlueprint(final Blueprint blueprint) {
        return runBlueprintRec(
                blueprint,
                new State(List.of(Robot.ORE), 0, 0, 0, 0, null, 0),
                new HashMap<>());
    }

    private static int runBlueprintRec(final Blueprint blueprint, final State state, final Map<CacheObject, Integer> maxGeods) {

        if(state.time == 24) {
            return state.geode;
        }

        CacheObject co = new CacheObject(state.ore, state.clay, state.obsidian, state.time);
        if(maxGeods.containsKey(co)) {
            return maxGeods.get(co);
        }

        Set<Action> actions = getPossibleActions(state, blueprint);

        State s = state;
        for(Robot r : state.robots) {
            s = processRobot(r, s);
        }

        // Once decided not to perform an action then don't reconsider it
        int max = 0;
        for(Action a : actions) {
            State newState = performAction(s, a, blueprint, actions);
            max = Math.max(max, runBlueprintRec(blueprint, newState, maxGeods));
        }

        maxGeods.put(co, max);
        return max;
    }

    private static List<Robot> moreRobots(final List<Robot> robots, final Robot newRobot) {
        List<Robot> newRobotList = new ArrayList<>(robots);
        newRobotList.add(newRobot);
        return newRobotList;
    }

    private static State performAction(final State s, final Action a, final Blueprint b, final Set<Action> actions) {
        return switch(a) {
            case DO_NOTHING -> new State(s.robots, s.ore, s.clay, s.obsidian, s.geode, actions, s.time+1);
            case BUILD_ORE -> new State(moreRobots(s.robots, Robot.ORE), s.ore-b.oreRobotOre, s.clay, s.obsidian, s.geode, null, s.time+1);
            case BUILD_CLAY -> new State(moreRobots(s.robots, Robot.CLAY), s.ore-b.clayRobotOre, s.clay, s.obsidian, s.geode, null, s.time+1);
            case BUILD_OBSIDIAN -> new State(moreRobots(s.robots, Robot.OBSIDIAN), s.ore-b.obsidianRobotOre, s.clay-b.obsidianRobotClay, s.obsidian, s.geode, null, s.time+1);
            case BUILD_GEODE -> new State(moreRobots(s.robots, Robot.GEODE), s.ore-b.geodeRobotOre, s.clay, s.obsidian-b.geodeRobotObsidian, s.geode, null, s.time+1);
        };
    }

    private static Set<Action> getPossibleActions(final State state, final Blueprint blueprint) {
        Set<Action> possible = new HashSet<>();
        if(state.ore >= blueprint.oreRobotOre)
            possible.add(Action.BUILD_ORE);
        if(state.ore >= blueprint.clayRobotOre)
            possible.add(Action.BUILD_CLAY);
        if(state.ore >= blueprint.obsidianRobotOre && state.clay >= blueprint.obsidianRobotClay)
            possible.add(Action.BUILD_OBSIDIAN);
        if(state.ore >= blueprint.geodeRobotOre && state.obsidian >= blueprint.geodeRobotObsidian)
            possible.add(Action.BUILD_GEODE);

        // If we have ignored certain actions before, lets ignore again
//        if(state.ignored != null)
//            possible.removeAll(state.ignored);

        possible.add(Action.DO_NOTHING);
        return possible;
    }

    private static State processRobot(final Robot r, final State s){
        return switch(r) {
            case ORE -> new State(s.robots, s.ore+1, s.clay, s.obsidian, s.geode, s.ignored, s.time);
            case CLAY -> new State(s.robots, s.ore, s.clay+1, s.obsidian, s.geode, s.ignored, s.time);
            case OBSIDIAN -> new State(s.robots, s.ore, s.clay, s.obsidian+1, s.geode, s.ignored, s.time);
            case GEODE -> new State(s.robots, s.ore, s.clay, s.obsidian, s.geode+1, s.ignored, s.time);
        };
    }

    private static int runAllBluePrints(final List<Blueprint> blueprints) {
        int sum = 0;
        for(int i=0; i<blueprints.size(); i++) {
            int geods = runBlueprint(blueprints.get(i));
            System.out.println("Testing blueprint: "+(i+1));
            System.out.println("\tGeods collected "+geods);
            sum += geods * (i+1);
        }
        return sum;
    }

    @Test
    public void testBlueprint() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(9, runBlueprint(bps.get(0)));
        assertEquals(12, runBlueprint(bps.get(1)));
    }

    @Test
    public void testBlueprints() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(33, runAllBluePrints(bps));

        bps = FileReader.readFileObjectList("src/test/resources/2022/D19.txt", D19::parseLine);
        assertEquals(1417, runAllBluePrints(bps));
    }

}
