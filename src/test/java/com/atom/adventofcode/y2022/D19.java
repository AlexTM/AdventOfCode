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


    record Blueprint(int[][] cost){}

    private static Blueprint parseLine(String line) {
        Matcher m = p.matcher(line);
        if(m.find()) {
            return new Blueprint(
                    new int[][] {
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

    record State(int[] robots, int[] values, int time){
        public State incTime() {
            return new State(robots, values, time+1);
        }
    }

    private static int[] getMaxOutput(Blueprint bp) {
        int[] maxOutput = new int[4];
        for(int j=0; j<4; j++)
            for(int i=0; i<4; i++)
                maxOutput[j] = Math.max(maxOutput[j], bp.cost[i][j]);
        maxOutput[3] = Integer.MAX_VALUE;
        return maxOutput;
    }

    private static int runBlueprint(final Blueprint blueprint) {

        Queue<State> stateQueue = new LinkedList<>();
        stateQueue.add(new State(new int[]{1,0,0,0}, new int[]{0,0,0,0}, 0));

        int max = 0;
        int loops = 0;

        int[] maxOutput = getMaxOutput(blueprint);

        while (!stateQueue.isEmpty()) {

            State state = stateQueue.poll();

            if(loops % 1000000 == 0) {
                System.out.println("Queue size = "+stateQueue.size()+" level="+state.time+" max="+max);
            }
            loops++;

            if(state.time == 24) {
                max = Math.max(max, state.values[Order.GEODE.ordinal()]);
//                System.out.println("Max = "+max);
                continue;
            }

            List<Order> actions = getPossibleActions(state, blueprint, maxOutput);

            for(int p=0; p<state.robots.length; p++) {
                state.values[p] += state.robots[p];
            }
            max = Math.max(max, state.values[Order.GEODE.ordinal()]);

            state = state.incTime();

            if(actions.isEmpty()) {
                stateQueue.add(state);
            } else {
                for (Order a : actions) {
                    stateQueue.add(copyStateBuildRobot(state, a, blueprint));
                }
            }
        }

        return max;
    }

    private static State copyStateBuildRobot(final State state, final Order increment, final Blueprint b) {
        int[] r = Arrays.copyOf(state.robots, 4);
        r[increment.ordinal()]++;
        int[] c = Arrays.copyOf(state.values, 4);
        for(int i=0; i<4; i++)
            c[i] -= b.cost[increment.ordinal()][i];

        return new State(r, c, state.time);
    }

    private static List<Order> getPossibleActions(final State state, final Blueprint b, int[] maxOutput) {
        List<Order> action = new ArrayList<>();

        for(int resource=0; resource<4; resource++) {
            if (state.values[0] >= b.cost[resource][0] && state.values[1] >= b.cost[resource][1] &&
                    state.values[2] >= b.cost[resource][2] && state.values[3] >= b.cost[resource][3]) {

                if(maxOutput[resource] > state.robots[resource]) {
                    action.add(Order.values()[resource]);
                }
            }
        }

        return action;
    }

    private static int runAllBluePrints(final List<Blueprint> blueprints) {
        int sum = 0;
        for(int i=0; i<blueprints.size(); i++) {
            int geodes = runBlueprint(blueprints.get(i));
            System.out.println("Testing blueprint: "+(i+1));
            System.out.println("\tGeodes collected "+geodes);
            sum += geodes * (i+1);
        }
        return sum;
    }

    /**
     * FIXME not working at the moment.  Caching is breaking it, by either capturing too little of the state
     * FIXME and therefore storing bad cache values, or too much and cache is not being hit
     */
    @Test
    public void testBlueprint() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(9, runBlueprint(bps.get(0)));
//        assertEquals(12, runBlueprint(bps.get(1)));
    }

    @Test
    public void testBlueprints() {
        List<Blueprint> bps = FileReader.readFileObjectList("src/test/resources/2022/D19_t.txt", D19::parseLine);
        assertEquals(33, runAllBluePrints(bps));

        bps = FileReader.readFileObjectList("src/test/resources/2022/D19.txt", D19::parseLine);
        assertEquals(0, runAllBluePrints(bps));
    }

}
