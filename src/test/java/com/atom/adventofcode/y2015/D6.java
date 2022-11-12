package com.atom.adventofcode.y2015;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 6: Probably a Fire Hazard ---
 *
 * Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided to deploy one million lights in a 1000x1000 grid.
 *
 * Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display the ideal lighting configuration.
 *
 * Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at 0,0, 0,999, 999,999, and 999,0. The instructions include whether to turn on, turn off, or toggle various inclusive ranges given as coordinate pairs. Each coordinate pair represents opposite corners of a rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. The lights all start turned off.
 *
 * To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa sent you in order.
 *
 * For example:
 *
 *     turn on 0,0 through 999,999 would turn on (or leave on) every light.
 *     toggle 0,0 through 999,0 would toggle the first line of 1000 lights, turning off the ones that were on, and turning on the ones that were off.
 *     turn off 499,499 through 500,500 would turn off (or leave off) the middle four lights.
 *
 * After following the instructions, how many lights are lit?
 *
 * Your puzzle answer was 543903.
 * --- Part Two ---
 *
 * You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from Ancient Nordic Elvish.
 *
 * The light grid you bought actually has individual brightness controls; each light can have a brightness of zero or more. The lights all start at zero.
 *
 * The phrase turn on actually means that you should increase the brightness of those lights by 1.
 *
 * The phrase turn off actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.
 *
 * The phrase toggle actually means that you should increase the brightness of those lights by 2.
 *
 * What is the total brightness of all lights combined after following Santa's instructions?
 *
 * For example:
 *
 *     turn on 0,0 through 0,0 would increase the total brightness by 1.
 *     toggle 0,0 through 999,999 would increase the total brightness by 2000000.
 *
 * Your puzzle answer was 14687245.
 */
public class D6 {

    enum Action { ON, TOGGLE, OFF }
    record Instruction(Action action, int startx, int starty, int endx, int endy){};
    record Light(int x, int y){};

    private static Instruction parseString(String s) {
        Action action = Action.TOGGLE;
        String subString = s.substring(7);
        if(s.startsWith("turn on")) {
            action = Action.ON;
            subString = s.substring(8);
        } else if(s.startsWith("turn off")) {
            action = Action.OFF;
            subString = s.substring(9);
        }

        String[] split = subString.split(" ");
        String[] start = split[0].split(",");
        String[] end = split[2].split(",");

        return new Instruction(action,
                Integer.parseInt(start[0]), Integer.parseInt(start[1]),
                Integer.parseInt(end[0]), Integer.parseInt(end[1]));
    }

    private List<Instruction> readInstructions() {
        return FileReader.readFileObjectList("src/test/resources/2015/D6.txt", D6::parseString);
    }

    private static final Map<Action, BiConsumer<Set<Light>, Light>> actionMap1 = Map.of(
            Action.ON, Set::add,
            Action.OFF, Set::remove,
            Action.TOGGLE, (lightsOn, light) -> {
                if(lightsOn.contains(light))
                    lightsOn.remove(light);
                else
                    lightsOn.add(light);
            }
    );

    private static final Map<Action, BiConsumer<Map<Light, Long>, Light>> actionMap2 = Map.of(
            Action.ON, (lightsOn, light) -> {
                long intensity = lightsOn.getOrDefault(light, 0L);
                lightsOn.put(light, intensity+1);
            },
            Action.OFF, (lightsOn, light) -> {
                long intensity = lightsOn.getOrDefault(light, 0L);
                lightsOn.put(light, Math.max(0, intensity-1));
            },
            Action.TOGGLE,(lightsOn, light) -> {
                long intensity = lightsOn.getOrDefault(light, 0L);
                lightsOn.put(light, intensity+2);
            }
    );

    private <T> T runInstructions(List<Instruction> instructions,
                                        Map<Action, BiConsumer<T, Light>> actionMap,
                                        T lightsOn) {
        for (Instruction instruction : instructions) {
            for (int y = instruction.starty; y <= instruction.endy; y++) {
                for (int x = instruction.startx; x <= instruction.endx; x++) {
                    actionMap.get(instruction.action).accept(lightsOn, new Light(x, y));
                }
            }
        }
        return lightsOn;
    }

    @Test
    public void testLightsPart1() {
        System.out.println(parseString("turn on 0,0 through 999,999"));
        System.out.println(parseString("toggle 0,0 through 999,0"));
        System.out.println(parseString("turn off 499,499 through 500,500"));

        assertEquals(543903, runInstructions(
                readInstructions(),
                actionMap1,
                new HashSet<>()
                ).size()
        );
    }

    @Test
    public void testLightsPart2() {
        assertEquals(14687245, runInstructions(
                        readInstructions(),
                        actionMap2,
                        new HashMap<>()
                ).values().stream().reduce(0L, Long::sum)
        );
    }

}
