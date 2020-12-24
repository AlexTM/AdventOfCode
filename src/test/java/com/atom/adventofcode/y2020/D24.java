package com.atom.adventofcode.y2020;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    record Coords (int x, int y){};

    enum CMD {
        nw, ne, e, se, sw, w
    }

    @Test
    public void testRun() {
        assertEquals(new Coords(-1,0), run(List.of(CMD.w)));
        assertEquals(new Coords(1,0), run(List.of(CMD.e)));
        assertEquals(new Coords(-1,-1), run(List.of(CMD.nw)));
        assertEquals(new Coords(0,-1), run(List.of(CMD.ne)));
        assertEquals(new Coords(-1,1), run(List.of(CMD.sw)));
        assertEquals(new Coords(0,1), run(List.of(CMD.se)));

        assertEquals(new Coords(0,0), run(List.of(CMD.e, CMD.w)));
        assertEquals(new Coords(2,0), run(List.of(CMD.e, CMD.e)));
        assertEquals(new Coords(1,-1), run(List.of(CMD.e, CMD.ne)));
        assertEquals(new Coords(0,-1), run(List.of(CMD.e, CMD.nw)));
        assertEquals(new Coords(0,1), run(List.of(CMD.e, CMD.sw)));
        assertEquals(new Coords(1,1), run(List.of(CMD.e, CMD.se)));

        assertEquals(new Coords(-1,1), run(List.of(CMD.se, CMD.w)));
        assertEquals(new Coords(1,1), run(List.of(CMD.se, CMD.e)));
        assertEquals(new Coords(1,0), run(List.of(CMD.se, CMD.ne)));
        assertEquals(new Coords(0,0), run(List.of(CMD.se, CMD.nw)));
        assertEquals(new Coords(0,2), run(List.of(CMD.se, CMD.sw)));
        assertEquals(new Coords(1,2), run(List.of(CMD.se, CMD.se)));

        assertEquals(new Coords(0,0), run(List.of(CMD.nw, CMD.w, CMD.sw, CMD.e, CMD.e)));
        assertEquals(new Coords(0,1), run(List.of(CMD.e,CMD.se,CMD.w)));

        List<List<CMD>> cmds = readFile("src/test/resources/2020/D24_t2.txt");
        System.out.println(cmds);
        assertEquals(new Coords(0,0), run(cmds.get(0)));
    }


    @Test
    public void testTiles() {
        List<List<CMD>> cmds = readFile("src/test/resources/2020/D24_t.txt");
        assertEquals(10, runAll(cmds).size());

        cmds = readFile("src/test/resources/2020/D24.txt");
        assertEquals(394, runAll(cmds).size());
    }

    private Set<Coords> runAll(List<List<CMD>> cmds) {
        Set<Coords> blackTiles = new HashSet<>();
        for(List<CMD> c : cmds) {
            Coords coord = run(c);
            if(blackTiles.contains(coord))
                blackTiles.remove(coord);
            else
                blackTiles.add(coord);
        }
        return blackTiles;
    }

    private static final Map<CMD, Function<Coords, Coords>> actions;
    static {
        actions = new HashMap<>();
        actions.put(CMD.e, c -> new Coords(c.x+1, c.y));
        actions.put(CMD.w, c -> new Coords(c.x-1, c.y));
        actions.put(CMD.ne, c -> new Coords(c.y % 2 != 0 ? c.x + 1 : c.x, c.y-1));
        actions.put(CMD.nw, c -> new Coords(c.y % 2 == 0 ? c.x - 1 : c.x, c.y-1));
        actions.put(CMD.se, c -> new Coords(c.y % 2 != 0 ? c.x + 1 : c.x, c.y+1));
        actions.put(CMD.sw, c -> new Coords(c.y % 2 == 0 ? c.x - 1 : c.x, c.y+1));
    }

    private Coords run(List<CMD> cmds) {
        return run(cmds, new Coords(0,0));
    }

    private Coords run(List<CMD> cmds, Coords startPos) {
        AtomicReference<Coords> end = new AtomicReference<>();
        end.set(startPos);
        cmds.forEach(c -> end.set(actions.get(c).apply(end.get())));
        return end.get();
    }

    private Set<Coords> getNeighbours(Coords pos) {
        Set<Coords> n = new HashSet<>();
        for(CMD c : CMD.values())
            n.add(run(List.of(c), pos));
        return n;
    }

    @Test
    public void testArt() {
        List<List<CMD>> cmds = readFile("src/test/resources/2020/D24_t.txt");
        Set<Coords> blackTiles = runAll(cmds);
        for(int i=0; i<100; i++)
            blackTiles = artIterate(blackTiles);
        assertEquals(2208, blackTiles.size());

        cmds = readFile("src/test/resources/2020/D24.txt");
        blackTiles = runAll(cmds);
        for(int i=0; i<100; i++)
            blackTiles = artIterate(blackTiles);
        assertEquals(4036, blackTiles.size());
    }

    public Set<Coords> artIterate(Set<Coords> blackTiles) {

        Set<Coords> allHexs = new HashSet<>(blackTiles);
        for(Coords c: blackTiles) {
            allHexs.addAll(getNeighbours(c));
        }

        Map<Coords, Integer> count = new HashMap<>();
        allHexs.forEach(h -> {
            Set<Coords> c = getNeighbours(h);
            c.retainAll(blackTiles);
            count.put(h, c.size());
        });


        Set<Coords> res = new HashSet<>();
        for(Map.Entry<Coords, Integer> e : count.entrySet()) {
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
