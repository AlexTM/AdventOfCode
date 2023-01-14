package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 12: Passage Pathing ---
 *
 * With your submarine's subterranean subsystems subsisting suboptimally, the only way you're getting out of this cave
 * anytime soon is by finding a path yourself. Not just a path - the only way to know if you've found the best path is
 * to find all of them.
 *
 * Fortunately, the sensors are still mostly working, and so you build a rough map of the remaining caves (your puzzle
 * input). For example:
 *
 * start-A
 * start-b
 * A-c
 * A-b
 * b-d
 * A-end
 * b-end
 *
 * This is a list of how all of the caves are connected. You start in the cave named start, and your destination is the
 * cave named end. An entry like b-d means that cave b is connected to cave d - that is, you can move between them.
 *
 * So, the above cave system looks roughly like this:
 *
 *     start
 *     /   \
 * c--A-----b--d
 *     \   /
 *      end
 *
 * Your goal is to find the number of distinct paths that start at start, end at end, and don't visit small caves more
 * than once. There are two types of caves: big caves (written in uppercase, like A) and small caves (written in
 * lowercase, like b). It would be a waste of time to visit any small cave more than once, but big caves are large
 * enough that it might be worth visiting them multiple times. So, all paths you find should visit small caves at most
 * once, and can visit big caves any number of times.
 *
 * Given these rules, there are 10 paths through this example cave system:
 *
 * start,A,b,A,c,A,end
 * start,A,b,A,end
 * start,A,b,end
 * start,A,c,A,b,A,end
 * start,A,c,A,b,end
 * start,A,c,A,end
 * start,A,end
 * start,b,A,c,A,end
 * start,b,A,end
 * start,b,end
 *
 * (Each line in the above list corresponds to a single path; the caves visited by that path are listed in the order
 * they are visited and separated by commas.)
 *
 * Note that in this cave system, cave d is never visited by any path: to do so, cave b would need to be visited twice
 * (once on the way to cave d and a second time when returning from cave d), and since cave b is small, this is not
 * allowed.
 *
 * Here is a slightly larger example:
 *
 * dc-end
 * HN-start
 * start-kj
 * dc-start
 * dc-HN
 * LN-dc
 * HN-end
 * kj-sa
 * kj-HN
 * kj-dc
 *
 * The 19 paths through it are as follows:
 *
 * start,HN,dc,HN,end
 * start,HN,dc,HN,kj,HN,end
 * start,HN,dc,end
 * start,HN,dc,kj,HN,end
 * start,HN,end
 * start,HN,kj,HN,dc,HN,end
 * start,HN,kj,HN,dc,end
 * start,HN,kj,HN,end
 * start,HN,kj,dc,HN,end
 * start,HN,kj,dc,end
 * start,dc,HN,end
 * start,dc,HN,kj,HN,end
 * start,dc,end
 * start,dc,kj,HN,end
 * start,kj,HN,dc,HN,end
 * start,kj,HN,dc,end
 * start,kj,HN,end
 * start,kj,dc,HN,end
 * start,kj,dc,end
 *
 * Finally, this even larger example has 226 paths through it:
 *
 * fs-end
 * he-DX
 * fs-he
 * start-DX
 * pj-DX
 * end-zg
 * zg-sl
 * zg-pj
 * pj-he
 * RW-he
 * fs-DX
 * pj-RW
 * zg-RW
 * start-pj
 * he-WI
 * zg-he
 * pj-fs
 * start-RW
 *
 * How many paths through this cave system are there that visit small caves at most once?
 *
 * --- Part Two ---
 *
 * After reviewing the available paths, you realize you might have time to visit a single small cave twice.
 * Specifically, big caves can be visited any number of times, a single small cave can be visited at most twice, and
 * the remaining small caves can be visited at most once. However, the caves named start and end can only be visited
 * exactly once each: once you leave the start cave, you may not return to it, and once you reach the end cave, the
 * path must end immediately.
 *
 * Now, the 36 possible paths through the first example above are:
 *
 * start,A,b,A,b,A,c,A,end
 * start,A,b,A,b,A,end
 * start,A,b,A,b,end
 * start,A,b,A,c,A,b,A,end
 * start,A,b,A,c,A,b,end
 * start,A,b,A,c,A,c,A,end
 * start,A,b,A,c,A,end
 * start,A,b,A,end
 * start,A,b,d,b,A,c,A,end
 * start,A,b,d,b,A,end
 * start,A,b,d,b,end
 * start,A,b,end
 * start,A,c,A,b,A,b,A,end
 * start,A,c,A,b,A,b,end
 * start,A,c,A,b,A,c,A,end
 * start,A,c,A,b,A,end
 * start,A,c,A,b,d,b,A,end
 * start,A,c,A,b,d,b,end
 * start,A,c,A,b,end
 * start,A,c,A,c,A,b,A,end
 * start,A,c,A,c,A,b,end
 * start,A,c,A,c,A,end
 * start,A,c,A,end
 * start,A,end
 * start,b,A,b,A,c,A,end
 * start,b,A,b,A,end
 * start,b,A,b,end
 * start,b,A,c,A,b,A,end
 * start,b,A,c,A,b,end
 * start,b,A,c,A,c,A,end
 * start,b,A,c,A,end
 * start,b,A,end
 * start,b,d,b,A,c,A,end
 * start,b,d,b,A,end
 * start,b,d,b,end
 * start,b,end
 *
 * The slightly larger example above now has 103 paths through it, and the even larger example now has 3509 paths
 * through it.
 *
 * Given these new rules, how many paths through this cave system are there?
 */
public class D12 {

    public Map<String, List<String>> loadData(String filename) {
        Map<String, List<String>> connections = FileReader.readFileForObject(filename, new HashMap<>(), (line, state) -> {
            String[] parts = line.split("-");
            if(!state.containsKey(parts[0]))
                state.put(parts[0], new ArrayList<>());
            state.get(parts[0]).add(parts[1]);

            // start and end are never bi-directional
            if(!parts[0].equals("start") && !parts[1].equals("end")) {
                if (!state.containsKey(parts[1]))
                    state.put(parts[1], new ArrayList<>());
                state.get(parts[1]).add(parts[0]);
            }
            return state;
        });
        // Remove connections for start and end as we can't revisit these nodes
        connections.remove("end");
        connections.forEach((key, value) -> value.remove("start"));
        return connections;
    }

    public List<String> mapAllRoutes(Map<String, List<String>> connections, int visitLimit) {
        List<String> routes = new ArrayList<>();
        recursivelyFindAllRoutes(connections, "start", new HashMap<>(), "start", routes, visitLimit);
        return routes;
    }

    public void recursivelyFindAllRoutes(
            Map<String, List<String>> connections, String node,
            Map<String, Integer> visited, String path, List<String> routes, int visitLimit) {

        if(node.equals("end")) {
            routes.add(path);
            return;
        }

        if(!node.toUpperCase().equals(node)) {
            visited.put(node, visited.getOrDefault(node, 0)+1);
        }

        List<String> cons = connections.get(node);
        for(String con : cons) {
            int timesVisited = visited.getOrDefault(con, 0);
            boolean reachedVisitLimit = visited.containsValue(visitLimit);
            if(timesVisited == 0) {
                recursivelyFindAllRoutes(connections, con, new HashMap<>(visited), path+"->"+con, routes, visitLimit);
            } else if(timesVisited == 1 && !reachedVisitLimit) {
                recursivelyFindAllRoutes(connections, con, new HashMap<>(visited), path+"->"+con, routes, visitLimit);
            }
        }
    }

    @Test
    public void testPart1() {
        assertEquals(10, mapAllRoutes(loadData("src/test/resources/2021/D12_t1.txt"), 1).size());
        assertEquals(19, mapAllRoutes(loadData("src/test/resources/2021/D12_t2.txt"), 1).size());
        assertEquals(226, mapAllRoutes(loadData("src/test/resources/2021/D12_t3.txt"), 1).size());
        assertEquals(5157, mapAllRoutes(loadData("src/test/resources/2021/D12.txt"), 1).size());
    }

    @Test
    public void testPart2() {
        assertEquals(36, mapAllRoutes(loadData("src/test/resources/2021/D12_t1.txt"), 2).size());
        assertEquals(103, mapAllRoutes(loadData("src/test/resources/2021/D12_t2.txt"), 2).size());
        assertEquals(144309, mapAllRoutes(loadData("src/test/resources/2021/D12.txt"), 2).size());
    }
}
