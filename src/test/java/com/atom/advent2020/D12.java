package com.atom.advent2020;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 12: Rain Risk ---
 * Your ferry made decent progress toward the island, but the storm came in faster than anyone expected. The ferry
 * needs to take evasive actions!
 *
 * Unfortunately, the ship's navigation computer seems to be malfunctioning; rather than giving a route directly to
 * safety, it produced extremely circuitous instructions. When the captain uses the PA system to ask if anyone can
 * help, you quickly volunteer.
 *
 * The navigation instructions (your puzzle input) consists of a sequence of single-character actions paired with
 * integer input values. After staring at them for a few minutes, you work out what they probably mean:
 *
 * Action N means to move north by the given value.
 * Action S means to move south by the given value.
 * Action E means to move east by the given value.
 * Action W means to move west by the given value.
 * Action L means to turn left the given number of degrees.
 * Action R means to turn right the given number of degrees.
 * Action F means to move forward by the given value in the direction the ship is currently facing.
 * The ship starts by facing east. Only the L and R actions change the direction the ship is facing. (That is, if the
 * ship is facing east and the next instruction is N10, the ship would move north 10 units, but would still move east
 * if the following action were F.)
 *
 * For example:
 *
 * F10
 * N3
 * F7
 * R90
 * F11
 * These instructions would be handled as follows:
 *
 * F10 would move the ship 10 units east (because the ship starts by facing east) to east 10, north 0.
 * N3 would move the ship 3 units north to east 10, north 3.
 * F7 would move the ship another 7 units east (because the ship is still facing east) to east 17, north 3.
 * R90 would cause the ship to turn right by 90 degrees and face south; it remains at east 17, north 3.
 * F11 would move the ship 11 units south to east 17, south 8.
 * At the end of these instructions, the ship's Manhattan distance (sum of the absolute values of its east/west
 * position and its north/south position) from its starting position is 17 + 8 = 25.
 *
 * Figure out where the navigation instructions lead. What is the Manhattan distance between that location and the
 * ship's starting position?
 *
 * Your puzzle answer was 441.
 *
 * The first half of this puzzle is complete! It provides one gold star: *
 *
 * --- Part Two ---
 * Before you can give the destination to the captain, you realize that the actual action meanings were printed on
 * the back of the instructions the whole time.
 *
 * Almost all of the actions indicate how to move a waypoint which is relative to the ship's position:
 *
 * Action N means to move the waypoint north by the given value.
 * Action S means to move the waypoint south by the given value.
 * Action E means to move the waypoint east by the given value.
 * Action W means to move the waypoint west by the given value.
 * Action L means to rotate the waypoint around the ship left (counter-clockwise) the given number of degrees.
 * Action R means to rotate the waypoint around the ship right (clockwise) the given number of degrees.
 * Action F means to move forward to the waypoint a number of times equal to the given value.
 * The waypoint starts 10 units east and 1 unit north relative to the ship. The waypoint is relative to the ship;
 * that is, if the ship moves, the waypoint moves with it.
 *
 * For example, using the same instructions as above:
 *
 * F10 moves the ship to the waypoint 10 times (a total of 100 units east and 10 units north), leaving the ship at
 * east 100, north 10. The waypoint stays 10 units east and 1 unit north of the ship.
 * N3 moves the waypoint 3 units north to 10 units east and 4 units north of the ship. The ship remains at east 100,
 * north 10.
 * F7 moves the ship to the waypoint 7 times (a total of 70 units east and 28 units north), leaving the ship at east
 * 170, north 38. The waypoint stays 10 units east and 4 units north of the ship.
 * R90 rotates the waypoint around the ship clockwise 90 degrees, moving it to 4 units east and 10 units south of the
 * ship. The ship remains at east 170, north 38.
 * F11 moves the ship to the waypoint 11 times (a total of 44 units east and 110 units south), leaving the ship at
 * east 214, south 72. The waypoint stays 4 units east and 10 units south of the ship.
 * After these operations, the ship's Manhattan distance from its starting position is 214 + 72 = 286.
 *
 * Figure out where the navigation instructions actually lead. What is the Manhattan distance between that location
 * and the ship's starting position?
 */
public class D12 {

    class Ship {
        private int x, y;
        private int angle;
        private int wx, wy;

        public int getWx() {
            return wx;
        }

        public Ship setWx(int wx) {
            this.wx = wx;
            return this;
        }

        public int getWy() {
            return wy;
        }

        public Ship setWy(int wy) {
            this.wy = wy;
            return this;
        }

        public int getX() {
            return x;
        }

        public Ship setX(int x) {
            this.x = x;
            return this;
        }

        public int getY() {
            return y;
        }

        public Ship setY(int y) {
            this.y = y;
            return this;
        }

        public int getAngle() {
            return angle;
        }

        public Ship setAngle(int angle) {
            this.angle = angle;
            return this;
        }

        @Override
        public String toString() {
            return "Ship{" +
                    "x=" + x +
                    ", y=" + y +
                    ", angle=" + angle +
                    ", wx=" + wx +
                    ", wy=" + wy +
                    '}';
        }
    }

    static class Instruction {
        final int value;
        final char inst;
        public Instruction(String s) {
            inst = s.charAt(0);
            value = Integer.parseInt(s.substring(1));
        }

        public int getValue() {
            return value;
        }

        public char getInst() {
            return inst;
        }
    }

    private List<Instruction> readFile(String filename) throws FileNotFoundException {
        List<Instruction> instructions = new ArrayList<>();
        try(Scanner in = new Scanner(new FileReader(filename))) {
            in.useDelimiter(System.getProperty("line.separator"));
            while (in.hasNext()) {
                String line = in.next().trim();
                instructions.add(new Instruction(line));
            }
            in.close();
            return instructions;
        }
    }

    private int distance(Ship s) {
        return Math.abs(s.x)+Math.abs(s.y);
    }

    @FunctionalInterface
    interface ShipControls {
        void apply(Instruction i, Ship s);
    }

    static Map<Character, ShipControls> controlMapOne = Map.of(
            'N', (i, s) -> s.setY(s.getY()-i.value),
            'S', (i, s) -> s.setY(s.getY()+i.value),
            'E', (i, s) -> s.setX(s.getX()+i.value),
            'W', (i, s) -> s.setX(s.getX()-i.value),
            'R', (i, s) -> {
                s.setAngle(s.getAngle()+i.value);
                if(s.getAngle() >= 360)
                    s.setAngle(s.getAngle()-360);
            },
            'L', (i, s) -> {
                s.setAngle(s.getAngle()-i.value);
                if(s.getAngle() < 0)
                    s.setAngle(s.getAngle()+360);
            },
            'F', (i, s) -> {
                if(s.getAngle() == 0)
                    s.setY(s.getY()-i.value);
                if(s.getAngle() == 90)
                    s.setX(s.getX()+i.value);
                if(s.getAngle() == 180)
                    s.setY(s.getY()+i.value);
                if(s.getAngle() == 270)
                    s.setX(s.getX()-i.value);
            });

    @Test
    public void testShipControls1() throws FileNotFoundException {
        Ship s = new Ship().setAngle(90).setX(0).setY(0);
        for(Instruction i : readFile("src/test/resources/D12_t.txt")) {
            controlMapOne.get(i.inst).apply(i, s);
        }
        assertEquals(25, distance(s));

        s = new Ship().setAngle(90).setX(0).setY(0);
        for(Instruction i : readFile("src/test/resources/D12.txt")) {
            controlMapOne.get(i.inst).apply(i, s);
        }
        System.out.println("Result :"+distance(s));
    }

    static Map<Character, ShipControls> controlMapTwo = Map.of(
            'N', (i, s) -> s.setWy(s.getWy()-i.value),
            'S', (i, s) -> s.setWy(s.getWy()+i.value),
            'E', (i, s) -> s.setWx(s.getWx()+i.value),
            'W', (i, s) -> s.setWx(s.getWx()-i.value),
            'R', (i, s) -> {
                int rightAngleRotations = i.value / 90;
                for(int j=0; j<rightAngleRotations; j++) {
                    int tmp = s.getWy();
                    s.setWy(s.getWx());
                    s.setWx(tmp*-1);
                }
            },
            'L', (i, s) -> {
                int rightAngleRotations = i.value / 90;
                for(int j=0; j<rightAngleRotations; j++) {
                    int tmp = s.getWx();
                    s.setWx(s.getWy());
                    s.setWy(tmp*-1);
                }
            },
            'F', (i, s) -> {
                s.setY(s.getY()+(s.getWy()*i.value));
                s.setX(s.getX()+(s.getWx()*i.value));
            });

    @Test
    public void testShipControls2() throws FileNotFoundException {
        Ship s = new Ship().setAngle(0).setX(0).setY(0).setWx(10).setWy(-1);
        for(Instruction i : readFile("src/test/resources/D12_t.txt")) {
            controlMapTwo.get(i.inst).apply(i, s);
        }
        assertEquals(286, distance(s));

        s = new Ship().setAngle(90).setX(0).setY(0).setWx(10).setWy(-1);
        for(Instruction i : readFile("src/test/resources/D12.txt")) {
            controlMapTwo.get(i.inst).apply(i, s);
        }
        System.out.println("Result :"+distance(s));
        assertEquals(40014, distance(s));
    }

}
