package com.atom.advent2020;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 11: Seating System ---
 * Your plane lands with plenty of time to spare. The final leg of your journey is a ferry that goes directly to the
 * tropical island where you can finally start your vacation. As you reach the waiting area to board the ferry, you
 * realize you're so early, nobody else has even arrived yet!
 *
 * By modeling the process people use to choose (or abandon) their seat in the waiting area, you're pretty sure you
 * can predict the best place to sit. You make a quick map of the seat layout (your puzzle input).
 *
 * The seat layout fits neatly on a grid. Each position is either floor (.), an empty seat (L), or an occupied seat
 * (#). For example, the initial seat layout might look like this:
 *
 * L.LL.LL.LL
 * LLLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLLL
 * L.LLLLLL.L
 * L.LLLLL.LL
 * Now, you just need to model the people who will be arriving shortly. Fortunately, people are entirely predictable
 * and always follow a simple set of rules. All decisions are based on the number of occupied seats adjacent to a
 * given seat (one of the eight positions immediately up, down, left, right, or diagonal from the seat). The
 * following rules are applied to every seat simultaneously:
 *
 * If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
 * If a seat is occupied (#) and four or more seats adjacent to it are also occupied, the seat becomes empty.
 * Otherwise, the seat's state does not change.
 * Floor (.) never changes; seats don't move, and nobody sits on the floor.
 *
 * After one round of these rules, every seat in the example layout becomes occupied:
 *
 * #.##.##.##
 * #######.##
 * #.#.#..#..
 * ####.##.##
 * #.##.##.##
 * #.#####.##
 * ..#.#.....
 * ##########
 * #.######.#
 * #.#####.##
 * After a second round, the seats with four or more occupied adjacent seats become empty again:
 *
 * #.LL.L#.##
 * #LLLLLL.L#
 * L.L.L..L..
 * #LLL.LL.L#
 * #.LL.LL.LL
 * #.LLLL#.##
 * ..L.L.....
 * #LLLLLLLL#
 * #.LLLLLL.L
 * #.#LLLL.##
 * This process continues for three more rounds:
 *
 * #.##.L#.##
 * #L###LL.L#
 * L.#.#..#..
 * #L##.##.L#
 * #.##.LL.LL
 * #.###L#.##
 * ..#.#.....
 * #L######L#
 * #.LL###L.L
 * #.#L###.##
 *
 * #.#L.L#.##
 * #LLL#LL.L#
 * L.L.L..#..
 * #LLL.##.L#
 * #.LL.LL.LL
 * #.LL#L#.##
 * ..L.L.....
 * #L#LLLL#L#
 * #.LLLLLL.L
 * #.#L#L#.##
 *
 * #.#L.L#.##
 * #LLL#LL.L#
 * L.#.L..#..
 * #L##.##.L#
 * #.#L.LL.LL
 * #.#L#L#.##
 * ..L.L.....
 * #L#L##L#L#
 * #.LLLLLL.L
 * #.#L#L#.##
 *
 * At this point, something interesting happens: the chaos stabilizes and further applications of these rules cause
 * no seats to change state! Once people stop moving around, you count 37 occupied seats.
 *
 * Simulate your seating area by applying the seating rules repeatedly until no seats change state. How many seats
 * end up occupied?
 *
 * Your puzzle answer was 2263.
 *
 * --- Part Two ---
 * As soon as people start to arrive, you realize your mistake. People don't just care about adjacent seats - they
 * care about the first seat they can see in each of those eight directions!
 *
 * Now, instead of considering just the eight immediately adjacent seats, consider the first seat in each of those
 * eight directions. For example, the empty seat below would see eight occupied seats:
 *
 * .......#.
 * ...#.....
 * .#.......
 * .........
 * ..#L....#
 * ....#....
 * .........
 * #........
 * ...#.....
 * The leftmost empty seat below would only see one empty seat, but cannot see any of the occupied ones:
 *
 * .............
 * .L.L.#.#.#.#.
 * .............
 * The empty seat below would see no occupied seats:
 *
 * .##.##.
 * #.#.#.#
 * ##...##
 * ...L...
 * ##...##
 * #.#.#.#
 * .##.##.
 * Also, people seem to be more tolerant than you expected: it now takes five or more visible occupied seats for an
 * occupied seat to become empty (rather than four or more from the previous rules). The other rules still apply:
 * empty seats that see no occupied seats become occupied, seats matching no rule don't change, and floor never changes.
 *
 * Given the same starting layout as above, these new rules cause the seating area to shift around as follows:
 *
 * L.LL.LL.LL
 * LLLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLLL
 * L.LLLLLL.L
 * L.LLLLL.LL
 *
 * #.##.##.##
 * #######.##
 * #.#.#..#..
 * ####.##.##
 * #.##.##.##
 * #.#####.##
 * ..#.#.....
 * ##########
 * #.######.#
 * #.#####.##
 *
 * #.LL.LL.L#
 * #LLLLLL.LL
 * L.L.L..L..
 * LLLL.LL.LL
 * L.LL.LL.LL
 * L.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLL#
 * #.LLLLLL.L
 * #.LLLLL.L#
 * #.L#.##.L#
 *
 * #L#####.LL
 * L.#.#..#..
 * ##L#.##.##
 * #.##.#L.##
 * #.#####.#L
 * ..#.#.....
 * LLL####LL#
 * #.L#####.L
 * #.L####.L#
 * #.L#.L#.L#
 *
 * #LLLLLL.LL
 * L.L.L..#..
 * ##LL.LL.L#
 * L.LL.LL.L#
 * #.LLLLL.LL
 * ..L.L.....
 * LLLLLLLLL#
 * #.LLLLL#.L
 * #.L#LL#.L#
 *
 * #.L#.L#.L#
 * #LLLLLL.LL
 * L.L.L..#..
 * ##L#.#L.L#
 * L.L#.#L.L#
 * #.L####.LL
 * ..#.#.....
 * LLL###LLL#
 * #.LLLLL#.L
 * #.L#LL#.L#
 *
 * #.L#.L#.L#
 * #LLLLLL.LL
 * L.L.L..#..
 * ##L#.#L.L#
 * L.L#.LL.L#
 * #.LLLL#.LL
 * ..#.L.....
 * LLL###LLL#
 * #.LLLLL#.L
 * #.L#LL#.L#
 *
 * Again, at this point, people stop shifting around and the seating area reaches equilibrium. Once this occurs, you
 * count 26 occupied seats.
 *
 * Given the new visibility method and the rule change for occupied seats becoming empty, once equilibrium is
 * reached, how many seats end up occupied?
 */
public class D11 {

    private char[][] readFile(String filename) throws FileNotFoundException {
        List<char[]> values = new ArrayList<>();
        try(Scanner in = new Scanner(new FileReader(filename))) {
            in.useDelimiter(System.getProperty("line.separator"));
            while (in.hasNext()) {
                String line = in.next().trim();
                values.add(line.toCharArray());
            }
            in.close();
            return values.toArray(new char[values.size()][]);
        }
    }

    private int countSeats(char[][] inp) {
        int c = 0;
        for(int i=0; i<inp.length; i++) {
            for(int j=0; j<inp[0].length; j++) {
                if(inp[i][j] == '#')
                    c++;
            }
        }
        return c;
    }

    @FunctionalInterface
    interface NeighbourStrategy {
        int countNeighbours(char[][] inp, int i, int j);
    }

    @Test
    public void test() throws FileNotFoundException {
        char[][]  inp = readFile("src/test/resources/day11_test.txt");
        inp = engine(inp, 4, NeighboursNextTo::countNeighbours);
        assertEquals(37, countSeats(inp));

        inp = readFile("src/test/resources/day11.txt");
        inp = engine(inp, 4, NeighboursNextTo::countNeighbours);
        System.out.println("Result "+countSeats(inp));

    }

    abstract static class NeighboursNextTo {
        public static int countNeighbours(char[][] inp, int i, int j) {
            int count = 0;
            for(int a = i -1; a<=i+1; a++) {
                for (int b = j - 1; b <= j + 1; b++) {
                    // checkout bounds
                    if (a < 0)
                        continue;
                    if (a >= inp.length)
                        continue;
                    if (b < 0)
                        continue;
                    if (b >= inp[0].length)
                        continue;

                    if (a == i && b == j)
                        continue;

                    if (inp[a][b] == '#') {
                        count++;
                    }
                }
            }
            return count;
        }
    }



    @Test
    public void test2() throws FileNotFoundException {
        char[][]  inp = readFile("src/test/resources/day11_test.txt");
        inp = engine(inp, 5, NeighboursViewable::countNeighbours);
        assertEquals(26, countSeats(inp));

        inp = readFile("src/test/resources/day11.txt");
        inp = engine(inp, 5, NeighboursViewable::countNeighbours);
        System.out.println("Result "+countSeats(inp));
        assertEquals(2002, countSeats(inp));
    }

    private char[][] engine(char[][] inp, int tolerance, NeighbourStrategy strategy) {
        boolean change = true;
        while(change) {

            char[][] nextRound = new char[inp.length][inp[0].length];
            // Iterate and make changes
            change = false;
            for(int i=0; i<inp.length; i++) {
                for(int j=0; j<inp[0].length; j++) {
                    nextRound[i][j] = inp[i][j];
                    switch(inp[i][j]) {
                        case '#':
                            if(strategy.countNeighbours(inp, i, j) >= tolerance) {
                                nextRound[i][j] = 'L';
                                change = true;
                            }

                            break;
                        case 'L':
                            if(strategy.countNeighbours(inp, i, j) == 0) {
                                nextRound[i][j] = '#';
                                change = true;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            inp = nextRound;
        }
        return inp;
    }

    abstract static class NeighboursViewable {

        public static int countNeighbours(char[][] inp, int i, int j) {
            int count = 0;

            count += countViewable(inp, i - 1, j,
                    pos -> pos[0]>=0,
                    pos -> {
                        pos[0]--;
                        return pos;
                    });

            count += countViewable(inp, i + 1, j,
                    pos -> pos[0]< inp.length,
                    pos -> {
                        pos[0]++;
                        return pos;
                    });

            count += countViewable(inp, i, j - 1,
                    pos -> pos[1]>=0,
                    pos -> {
                        pos[1]--;
                        return pos;
                    });

            count += countViewable(inp, i, j + 1,
                    pos -> pos[1]< inp[0].length,
                    pos -> {
                        pos[1]++;
                        return pos;
                    });

            // diagonal
            count += countViewable(inp, i-1, j-1,
                    pos -> pos[0] >= 0 && pos[1] >= 0,
                    pos -> {
                        pos[0]--;
                        pos[1]--;
                        return pos;
                    });

            count += countViewable(inp, i+1, j+1,
                    pos -> pos[0] < inp.length && pos[1] < inp[0].length,
                    pos -> {
                        pos[0]++;
                        pos[1]++;
                        return pos;
                    });

            count += countViewable(inp, i-1, j+1,
                    pos -> pos[0] >= 0 && pos[1] < inp[0].length,
                    pos -> {
                        pos[0]--;
                        pos[1]++;
                        return pos;
                    });

            count += countViewable(inp, i+1, j-1,
                    pos -> pos[0] < inp.length && pos[1] >= 0,
                    pos -> {
                        pos[0]++;
                        pos[1]--;
                        return pos;
                    });

            return count;
        }

        private static int countViewable(char[][] inp, int i, int j, Function<int[], Boolean> cond, Function<int[], int[]> func) {
            int count = 0;
            int[] pos = new int[] {i, j};
            while(cond.apply(pos)) {
                if(inp[pos[0]][pos[1]] == 'L')
                    break;
                if(inp[pos[0]][pos[1]] == '#') {
                    count++;
                    break;
                }
                pos = func.apply(pos);
            }
            return count;
        }
    }
}
