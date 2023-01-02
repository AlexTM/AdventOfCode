package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D19 {

    private static Pattern p =
            Pattern.compile("Blueprint (\\d+): " +
                    "Each ore robot costs (\\d+) ore. " +
                    "Each clay robot costs (\\d+) ore. " +
                    "Each obsidian robot costs (\\d+) ore and (\\d+) clay. " +
                    "Each geode robot costs (\\d+) ore and (\\d+) obsidian.");

    record Blueprint(int[][] cost) {}

    private static Blueprint parseLine(String line) {
        Matcher m = p.matcher(line);
        if (m.find()) {
            return new Blueprint(
                    new int[][]{
                            {Integer.parseInt(m.group(2)), 0, 0, 0},
                            {Integer.parseInt(m.group(3)), 0, 0, 0},
                            {Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), 0, 0},
                            {Integer.parseInt(m.group(6)), 0, Integer.parseInt(m.group(7)), 0}
                    }
            );
        }
        return null;
    }

    enum Order {ORE, CLAY, OBSIDIAN, GEODE}
    enum Actions {ORE, CLAY, OBSIDIAN, GEODE, NONE}

    record State(int[] robot, int[] resource, int time) {
        public State incTime() {
            return new State(robot, resource, time + 1);
        }
    }

    private static int[] calculateMaxOutputNeeded(Blueprint bp) {
        int[] maxOutput = new int[4];
        for (int resource = 0; resource < 3; resource++)
            for (int robot = 0; robot < 4; robot++)
                maxOutput[resource] = Math.max(maxOutput[resource], bp.cost[robot][resource]);
        maxOutput[Order.GEODE.ordinal()] = Integer.MAX_VALUE;
        return maxOutput;
    }

    private static int runBlueprint(
            final Blueprint blueprint, final int maxTime, final float pruneFraction,
            final Function<State, Integer> scoreFunction) {

        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(new State(new int[]{1, 0, 0, 0}, new int[]{0, 0, 0, 0}, 0));

        int max = 0;
        int maxScore = 0;
        int[] maxOutput = calculateMaxOutputNeeded(blueprint);

        while (!stateQueue.isEmpty()) {

            State state = stateQueue.poll();

            if (state.time == maxTime) {
                max = Math.max(max, state.resource[Order.GEODE.ordinal()]);
                continue;
            }

            List<Actions> actions = getPossibleActions(state, blueprint, maxOutput, maxTime);

            for (int p = 0; p < state.robot.length; p++)
                state.resource[p] += state.robot[p];

            // start pruning the bottom pruneFraction after 15 time loops
            int score = scoreFunction.apply(state);
            if (state.time > 15 && score < maxScore * pruneFraction)
                continue;

            maxScore = Math.max(maxScore, score);
            state = state.incTime();

            for (Actions action : actions)
                stateQueue.add(copyStateBuildRobot(state, action, blueprint));
        }

        return max;
    }

    private static int scoreFnOne(State state) {
        return state.robot[0] + state.robot[1] * 10 + state.robot[2] * 100 + state.robot[3] * 1000;
    }

    private static int scoreFnTwo(State state) {
        return state.robot[0] + state.robot[1] * 2 + state.robot[2] * 4 + state.robot[3] * 8;
    }

    private static State copyStateBuildRobot(final State state, final Actions increment, final Blueprint b) {
        if (increment != Actions.NONE) {
            int[] r = Arrays.copyOf(state.robot, 4);
            r[increment.ordinal()]++;
            int[] c = Arrays.copyOf(state.resource, 4);
            for (int i = 0; i < 4; i++)
                c[i] -= b.cost[increment.ordinal()][i];

            return new State(r, c, state.time);
        }
        return state;
    }

    private static List<Actions> getPossibleActions(
            final State state, final Blueprint b, final int[] maxRequiredOutput, final int maxTime) {
        List<Actions> action = new ArrayList<>();

        action.add(Actions.NONE);
        // Only add action is we are NOT hitting the maxRequiredOutput limit
        for (int robot = 0; robot < 4; robot++) {
            if (state.resource[0] >= b.cost[robot][0] && state.resource[1] >= b.cost[robot][1] &&
                    state.resource[2] >= b.cost[robot][2] && state.resource[3] >= b.cost[robot][3]) {
                if (maxRequiredOutput[robot] > state.robot[robot]) {
                    action.add(Actions.values()[robot]);
                }
            }
        }

        // If can build geode, then only build geode
        if (action.contains(Actions.GEODE))
            return List.of(Actions.GEODE);

        // Check for each robot (except geode) that we still have time to use any extra resources
        int remainingTime = maxTime - state.time;
        for (int resource = 0; resource < 3; resource++) {
            int maxUsage = remainingTime * maxRequiredOutput[resource];
            if(state.resource[resource] >= maxUsage) {
                action.remove(Actions.values()[resource]);
            }
        }

        return action;
    }

    private static int runAllBluePrints(final List<Blueprint> blueprints, final float pruneFraction) {
        int sum = 0;
        for(int i=0; i<blueprints.size(); i++) {
            int geodes = runBlueprint(blueprints.get(i), 24, pruneFraction, D19::scoreFnOne);
            sum += geodes * (i+1);
        }
        return sum;
    }

    @Test
    public void testBlueprint() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(9, runBlueprint(bps.get(0), 24, 0.6f, D19::scoreFnOne));
        assertEquals(12, runBlueprint(bps.get(1), 24, 0.6f, D19::scoreFnOne));
    }

    @Test
    public void testBlueprints() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(33, runAllBluePrints(bps, 0.6f));

        bps = FileReader.readFileObjectList("src/test/resources/2022/D19.txt", D19::parseLine);
        assertEquals(1725, runAllBluePrints(bps, 0.6f));
    }

    @Test
    public void testBlueprint2() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(56, runBlueprint(bps.get(0), 32, 0.8f, D19::scoreFnTwo));
        assertEquals(62, runBlueprint(bps.get(1), 32, 0.8f, D19::scoreFnTwo));

    }

    @Test
    public void testBlueprint3() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19.txt", D19::parseLine);
        assertEquals(15510,
                IntStream.range(0, 3).map(i -> runBlueprint(bps.get(i), 32, 0.8f, D19::scoreFnTwo))
                        .reduce(1, (a, b) -> a*b));
    }
}
