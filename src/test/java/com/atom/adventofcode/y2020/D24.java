package com.atom.adventofcode.y2020;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 24: Lobby Layout ---
 * Your raft makes it to the tropical island; it turns out that the small crab was an excellent navigator. You make
 * your way to the resort.
 *
 * As you enter the lobby, you discover a small problem: the floor is being renovated. You can't even reach the
 * check-in desk until they've finished installing the new tile floor.
 *
 * The tiles are all hexagonal; they need to be arranged in a hex grid with a very specific color pattern. Not in the
 * mood to wait, you offer to help figure out the pattern.
 *
 * The tiles are all white on one side and black on the other. They start with the white side facing up. The lobby is
 * large enough to fit whatever pattern might need to appear there.
 *
 * A member of the renovation crew gives you a list of the tiles that need to be flipped over (your puzzle input).
 * Each line in the list identifies a single tile that needs to be flipped by giving a series of steps starting from
 * a reference tile in the very center of the room. (Every line starts from the same reference tile.)
 *
 * Because the tiles are hexagonal, every tile has six neighbors: east, southeast, southwest, west, northwest, and
 * northeast. These directions are given in your list, respectively, as e, se, sw, w, nw, and ne. A tile is
 * identified by a series of these directions with no delimiters; for example, esenee identifies the tile you land on
 * if you start at the reference tile and then move one tile east, one tile southeast, one tile northeast, and one
 * tile east.
 *
 * Each time a tile is identified, it flips from white to black or from black to white. Tiles might be flipped more
 * than once. For example, a line like esew flips a tile immediately adjacent to the reference tile, and a line like
 * nwwswee flips the reference tile itself.
 *
 * Here is a larger example:
 *
 * sesenwnenenewseeswwswswwnenewsewsw
 * neeenesenwnwwswnenewnwwsewnenwseswesw
 * seswneswswsenwwnwse
 * nwnwneseeswswnenewneswwnewseswneseene
 * swweswneswnenwsewnwneneseenw
 * eesenwseswswnenwswnwnwsewwnwsene
 * sewnenenenesenwsewnenwwwse
 * wenwwweseeeweswwwnwwe
 * wsweesenenewnwwnwsenewsenwwsesesenwne
 * neeswseenwwswnwswswnw
 * nenwswwsewswnenenewsenwsenwnesesenew
 * enewnwewneswsewnwswenweswnenwsenwsw
 * sweneswneswneneenwnewenewwneswswnese
 * swwesenesewenwneswnwwneseswwne
 * enesenwswwswneneswsenwnewswseenwsese
 * wnwnesenesenenwwnenwsewesewsesesew
 * nenewswnwewswnenesenwnesewesw
 * eneswnwswnwsenenwnwnwwseeswneewsenese
 * neswnwewnwnwseenwseesewsenwsweewe
 * wseweeenwnesenwwwswnew
 * In the above example, 10 tiles are flipped once (to black), and 5 more are flipped twice (to black, then back to
 * white). After all of these instructions have been followed, a total of 10 tiles are black.
 *
 * Go through the renovation crew's list and determine which tiles they need to flip. After all of the instructions
 * have been followed, how many tiles are left with the black side up?
 *
 * Your puzzle answer was 394.
 *
 * --- Part Two ---
 * The tile floor in the lobby is meant to be a living art exhibit. Every day, the tiles are all flipped according to
 * the following rules:
 *
 * Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
 * Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
 * Here, tiles immediately adjacent means the six tiles directly touching the tile in question.
 *
 * The rules are applied simultaneously to every tile; put another way, it is first determined which tiles need to be
 * flipped, then they are all flipped at the same time.
 *
 * In the above example, the number of black tiles that are facing up after the given number of days has passed is as
 * follows:
 *
 * Day 1: 15
 * Day 2: 12
 * Day 3: 25
 * Day 4: 14
 * Day 5: 23
 * Day 6: 28
 * Day 7: 41
 * Day 8: 37
 * Day 9: 49
 * Day 10: 37
 *
 * Day 20: 132
 * Day 30: 259
 * Day 40: 406
 * Day 50: 566
 * Day 60: 788
 * Day 70: 1106
 * Day 80: 1373
 * Day 90: 1844
 * Day 100: 2208
 * After executing this process a total of 100 times, there would be 2208 black tiles facing up.
 *
 * How many tiles will be black after 100 days?
 */
public class D24 {

    private List<List<CMD>> readFile(String filename) {
        return FileReader.readFileObjectList(filename, line -> {
            List<CMD> commands = new ArrayList<>();
            int pos = 0;
            while(pos < line.length()) {
                // fixme
                try {
                    commands.add(CMD.valueOf(line.substring(pos, pos+1)));
                    pos++;
                } catch(Exception e) {
                    commands.add(CMD.valueOf(line.substring(pos, pos+2)));
                    pos += 2;
                }
            }
            return commands;
        });
    }

    record Coord(int x, int y){};

    enum CMD {
        nw, ne, e, se, sw, w
    }

    @Test
    public void testRun() {
        assertEquals(new Coord(-1,0), run(List.of(CMD.w)));
        assertEquals(new Coord(1,0), run(List.of(CMD.e)));
        assertEquals(new Coord(-1,-1), run(List.of(CMD.nw)));
        assertEquals(new Coord(0,-1), run(List.of(CMD.ne)));
        assertEquals(new Coord(-1,1), run(List.of(CMD.sw)));
        assertEquals(new Coord(0,1), run(List.of(CMD.se)));

        assertEquals(new Coord(0,0), run(List.of(CMD.e, CMD.w)));
        assertEquals(new Coord(2,0), run(List.of(CMD.e, CMD.e)));
        assertEquals(new Coord(1,-1), run(List.of(CMD.e, CMD.ne)));
        assertEquals(new Coord(0,-1), run(List.of(CMD.e, CMD.nw)));
        assertEquals(new Coord(0,1), run(List.of(CMD.e, CMD.sw)));
        assertEquals(new Coord(1,1), run(List.of(CMD.e, CMD.se)));

        assertEquals(new Coord(-1,1), run(List.of(CMD.se, CMD.w)));
        assertEquals(new Coord(1,1), run(List.of(CMD.se, CMD.e)));
        assertEquals(new Coord(1,0), run(List.of(CMD.se, CMD.ne)));
        assertEquals(new Coord(0,0), run(List.of(CMD.se, CMD.nw)));
        assertEquals(new Coord(0,2), run(List.of(CMD.se, CMD.sw)));
        assertEquals(new Coord(1,2), run(List.of(CMD.se, CMD.se)));

        assertEquals(new Coord(0,0), run(List.of(CMD.nw, CMD.w, CMD.sw, CMD.e, CMD.e)));
        assertEquals(new Coord(0,1), run(List.of(CMD.e,CMD.se,CMD.w)));

        List<List<CMD>> cmds = readFile("src/test/resources/2020/D24_t2.txt");
        System.out.println(cmds);
        assertEquals(new Coord(0,0), run(cmds.get(0)));
    }


    @Test
    public void testTiles() {
        List<List<CMD>> cmds = readFile("src/test/resources/2020/D24_t.txt");
        assertEquals(10, runAll(cmds).size());

        cmds = readFile("src/test/resources/2020/D24.txt");
        assertEquals(394, runAll(cmds).size());
    }

    private Set<Coord> runAll(List<List<CMD>> cmds) {
        Set<Coord> blackTiles = new HashSet<>();
        for(List<CMD> c : cmds) {
            Coord coord = run(c);
            if(blackTiles.contains(coord))
                blackTiles.remove(coord);
            else
                blackTiles.add(coord);
        }
        return blackTiles;
    }

    private static final Map<CMD, Function<Coord, Coord>> actions;
    static {
        actions = new HashMap<>();
        actions.put(CMD.e, c -> new Coord(c.x+1, c.y));
        actions.put(CMD.w, c -> new Coord(c.x-1, c.y));
        actions.put(CMD.ne, c -> new Coord(c.y % 2 != 0 ? c.x + 1 : c.x, c.y-1));
        actions.put(CMD.nw, c -> new Coord(c.y % 2 == 0 ? c.x - 1 : c.x, c.y-1));
        actions.put(CMD.se, c -> new Coord(c.y % 2 != 0 ? c.x + 1 : c.x, c.y+1));
        actions.put(CMD.sw, c -> new Coord(c.y % 2 == 0 ? c.x - 1 : c.x, c.y+1));
    }

    private Coord run(List<CMD> cmds) {
        return run(cmds, new Coord(0,0));
    }

    private Coord run(List<CMD> cmds, Coord startPos) {
        AtomicReference<Coord> end = new AtomicReference<>();
        end.set(startPos);
        cmds.forEach(c -> end.set(actions.get(c).apply(end.get())));
        return end.get();
    }

    private Set<Coord> getNeighbours(Coord pos) {
        Set<Coord> n = new HashSet<>();
        for(CMD c : CMD.values())
            n.add(run(List.of(c), pos));
        return n;
    }

    @Test
    public void testArt() {
        List<List<CMD>> cmds = readFile("src/test/resources/2020/D24_t.txt");
        Set<Coord> blackTiles = runAll(cmds);
        for(int i=0; i<100; i++)
            blackTiles = artIterate(blackTiles);
        assertEquals(2208, blackTiles.size());

        cmds = readFile("src/test/resources/2020/D24.txt");
        blackTiles = runAll(cmds);
        for(int i=0; i<100; i++)
            blackTiles = artIterate(blackTiles);
        assertEquals(4036, blackTiles.size());
    }

    public Set<Coord> artIterate(Set<Coord> blackTiles) {

        // Generate coords to be counted
        Set<Coord> allHexs = new HashSet<>(blackTiles);
        for(Coord c: blackTiles) {
            allHexs.addAll(getNeighbours(c));
        }

        // Count
        Map<Coord, Integer> count = new HashMap<>();
        allHexs.forEach(h -> {
            Set<Coord> c = getNeighbours(h);
            c.retainAll(blackTiles);
            count.put(h, c.size());
        });


        // Apply rules for next generation
        Set<Coord> res = new HashSet<>();
        for(Map.Entry<Coord, Integer> e : count.entrySet()) {
            if(blackTiles.contains(e.getKey())) {
                if(e.getValue() != 0 && e.getValue() <= 2) {
                    res.add(e.getKey());
                }
            } else {
                if (e.getValue() == 2) {
                    res.add(e.getKey());
                }
            }
        }

        return res;
    }

}
