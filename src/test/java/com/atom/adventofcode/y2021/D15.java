package com.atom.adventofcode.y2021;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * --- Day 15: Chiton ---
 *
 * You've almost reached the exit of the cave, but the walls are getting closer together. Your submarine can barely
 * still fit, though; the main problem is that the walls of the cave are covered in chitons, and it would be best not
 * to bump any of them.
 *
 * The cavern is large, but has a very low ceiling, restricting your motion to two dimensions. The shape of the cavern
 * resembles a square; a quick scan of chiton density produces a map of risk level throughout the cave (your puzzle
 * input). For example:
 *
 * 1163751742
 * 1381373672
 * 2136511328
 * 3694931569
 * 7463417111
 * 1319128137
 * 1359912421
 * 3125421639
 * 1293138521
 * 2311944581
 *
 * You start in the top left position, your destination is the bottom right position, and you cannot move diagonally.
 * The number at each position is its risk level; to determine the total risk of an entire path, add up the risk levels
 * of each position you enter (that is, don't count the risk level of your starting position unless you enter it;
 * leaving it adds no risk to your total).
 *
 * Your goal is to find a path with the lowest total risk. In this example, a path with the lowest total risk is
 * highlighted here:
 *
 * 1163751742
 * 1381373672
 * 2136511328
 * 3694931569
 * 7463417111
 * 1319128137
 * 1359912421
 * 3125421639
 * 1293138521
 * 2311944581
 *
 * The total risk of this path is 40 (the starting position is never entered, so its risk is not counted).
 *
 * What is the lowest total risk of any path from the top left to the bottom right?
 *
 * Your puzzle answer was 696.
 * --- Part Two ---
 *
 * Now that you know how to find low-risk paths in the cave, you can try to find your way out.
 *
 * The entire cave is actually five times larger in both dimensions than you thought; the area you originally scanned
 * is just one tile in a 5x5 tile area that forms the full map. Your original map tile repeats to the right and
 * downward; each time the tile repeats to the right or downward, all of its risk levels are 1 higher than the tile
 * immediately up or left of it. However, risk levels above 9 wrap back around to 1. So, if your original map had some
 * position with a risk level of 8, then that same position on each of the 25 total tiles would be as follows:
 *
 * 8 9 1 2 3
 * 9 1 2 3 4
 * 1 2 3 4 5
 * 2 3 4 5 6
 * 3 4 5 6 7
 *
 * Each single digit above corresponds to the example position with a value of 8 on the top-left tile. Because the full
 * map is actually five times larger in both dimensions, that position appears a total of 25 times, once in each
 * duplicated tile, with the values shown above.
 *
 * Here is the full five-times-as-large version of the first example above, with the original map in the top left
 * corner highlighted:
 *
 * 11637517422274862853338597396444961841755517295286
 * 13813736722492484783351359589446246169155735727126
 * 21365113283247622439435873354154698446526571955763
 * 36949315694715142671582625378269373648937148475914
 * 74634171118574528222968563933317967414442817852555
 * 13191281372421239248353234135946434524615754563572
 * 13599124212461123532357223464346833457545794456865
 * 31254216394236532741534764385264587549637569865174
 * 12931385212314249632342535174345364628545647573965
 * 23119445813422155692453326671356443778246755488935
 * 22748628533385973964449618417555172952866628316397
 * 24924847833513595894462461691557357271266846838237
 * 32476224394358733541546984465265719557637682166874
 * 47151426715826253782693736489371484759148259586125
 * 85745282229685639333179674144428178525553928963666
 * 24212392483532341359464345246157545635726865674683
 * 24611235323572234643468334575457944568656815567976
 * 42365327415347643852645875496375698651748671976285
 * 23142496323425351743453646285456475739656758684176
 * 34221556924533266713564437782467554889357866599146
 * 33859739644496184175551729528666283163977739427418
 * 35135958944624616915573572712668468382377957949348
 * 43587335415469844652657195576376821668748793277985
 * 58262537826937364893714847591482595861259361697236
 * 96856393331796741444281785255539289636664139174777
 * 35323413594643452461575456357268656746837976785794
 * 35722346434683345754579445686568155679767926678187
 * 53476438526458754963756986517486719762859782187396
 * 34253517434536462854564757396567586841767869795287
 * 45332667135644377824675548893578665991468977611257
 * 44961841755517295286662831639777394274188841538529
 * 46246169155735727126684683823779579493488168151459
 * 54698446526571955763768216687487932779859814388196
 * 69373648937148475914825958612593616972361472718347
 * 17967414442817852555392896366641391747775241285888
 * 46434524615754563572686567468379767857948187896815
 * 46833457545794456865681556797679266781878137789298
 * 64587549637569865174867197628597821873961893298417
 * 45364628545647573965675868417678697952878971816398
 * 56443778246755488935786659914689776112579188722368
 * 55172952866628316397773942741888415385299952649631
 * 57357271266846838237795794934881681514599279262561
 * 65719557637682166874879327798598143881961925499217
 * 71484759148259586125936169723614727183472583829458
 * 28178525553928963666413917477752412858886352396999
 * 57545635726865674683797678579481878968159298917926
 * 57944568656815567976792667818781377892989248891319
 * 75698651748671976285978218739618932984172914319528
 * 56475739656758684176786979528789718163989182927419
 * 67554889357866599146897761125791887223681299833479
 *
 * Equipped with the full map, you can now find a path from the top left corner to the bottom right corner with the
 * lowest total risk:
 *
 * 11637517422274862853338597396444961841755517295286
 * 13813736722492484783351359589446246169155735727126
 * 21365113283247622439435873354154698446526571955763
 * 36949315694715142671582625378269373648937148475914
 * 74634171118574528222968563933317967414442817852555
 * 13191281372421239248353234135946434524615754563572
 * 13599124212461123532357223464346833457545794456865
 * 31254216394236532741534764385264587549637569865174
 * 12931385212314249632342535174345364628545647573965
 * 23119445813422155692453326671356443778246755488935
 * 22748628533385973964449618417555172952866628316397
 * 24924847833513595894462461691557357271266846838237
 * 32476224394358733541546984465265719557637682166874
 * 47151426715826253782693736489371484759148259586125
 * 85745282229685639333179674144428178525553928963666
 * 24212392483532341359464345246157545635726865674683
 * 24611235323572234643468334575457944568656815567976
 * 42365327415347643852645875496375698651748671976285
 * 23142496323425351743453646285456475739656758684176
 * 34221556924533266713564437782467554889357866599146
 * 33859739644496184175551729528666283163977739427418
 * 35135958944624616915573572712668468382377957949348
 * 43587335415469844652657195576376821668748793277985
 * 58262537826937364893714847591482595861259361697236
 * 96856393331796741444281785255539289636664139174777
 * 35323413594643452461575456357268656746837976785794
 * 35722346434683345754579445686568155679767926678187
 * 53476438526458754963756986517486719762859782187396
 * 34253517434536462854564757396567586841767869795287
 * 45332667135644377824675548893578665991468977611257
 * 44961841755517295286662831639777394274188841538529
 * 46246169155735727126684683823779579493488168151459
 * 54698446526571955763768216687487932779859814388196
 * 69373648937148475914825958612593616972361472718347
 * 17967414442817852555392896366641391747775241285888
 * 46434524615754563572686567468379767857948187896815
 * 46833457545794456865681556797679266781878137789298
 * 64587549637569865174867197628597821873961893298417
 * 45364628545647573965675868417678697952878971816398
 * 56443778246755488935786659914689776112579188722368
 * 55172952866628316397773942741888415385299952649631
 * 57357271266846838237795794934881681514599279262561
 * 65719557637682166874879327798598143881961925499217
 * 71484759148259586125936169723614727183472583829458
 * 28178525553928963666413917477752412858886352396999
 * 57545635726865674683797678579481878968159298917926
 * 57944568656815567976792667818781377892989248891319
 * 75698651748671976285978218739618932984172914319528
 * 56475739656758684176786979528789718163989182927419
 * 67554889357866599146897761125791887223681299833479
 *
 * The total risk of this path is 315 (the starting position is still never entered, so its risk is not counted).
 *
 * Using the full map, what is the lowest total risk of any path from the top left to the bottom right?
 */
public class D15 {

    record Pos(int x, int y) {};
    class Data {
        int[][] map;
        int sizex;
        int sizey;
    }

    public Data loadData(String filename) {
        return loadData(filename, null);
    }

    public Data loadData(String filename, Integer mult) {
        List<List<Integer>> list = FileReader.readFileObjectList(filename, line -> line.chars()
                .boxed().map(c -> c - '0').collect(Collectors.toList()));

        int[][] m = new int[list.size()][list.get(0).size()];
        for(int i=0; i<list.size(); i++) {
            for(int j=0; j<list.get(0).size(); j++) {
                m[i][j] = list.get(i).get(j);
            }
        }
        Data data = new Data();
        data.map = m;
        data.sizex = mult == null ? m.length : m.length*mult;
        data.sizey = mult == null ? m[0].length : m[0].length*mult;
        return data;
    }

    public List<Pos> getCandidates(Data data, Pos p) {
        List<Pos> can = new ArrayList<>();
        if(p.x > 0)
            can.add(new Pos(p.x-1, p.y));
        if(p.x < data.sizex-1)
            can.add(new Pos(p.x+1, p.y));
        if(p.y > 0)
            can.add(new Pos(p.x, p.y-1));
        if(p.y < data.sizey-1)
            can.add(new Pos(p.x, p.y+1));
        return can;
    }

    public HashMap<Pos, Integer> doDijkstra(Data data, Pos start, BiFunction<Pos, int[][], Integer> costFunction) {

        Set<Pos> visited = new HashSet<>();
        HashMap<Pos, Integer> distanceFromStart = new HashMap<>();
        distanceFromStart.put(start, 0);

        Queue<Pos> toVisit = new PriorityQueue<>(Comparator.comparing(distanceFromStart::get));
        toVisit.add(start);

        while(!toVisit.isEmpty()) {

            Pos currentNode = toVisit.poll();

            // check if we have already visited
            if(visited.contains(currentNode))
                continue;

            visited.add(currentNode);

            // get all edges
            List<Pos> eList = getCandidates(data, currentNode);
            for (Pos p : eList) {
                int newDistance = costFunction.apply(p, data.map) + distanceFromStart.get(currentNode);
                if (newDistance < distanceFromStart.getOrDefault(p, Integer.MAX_VALUE)) {
                    distanceFromStart.put(p, newDistance);
                    toVisit.add(p);
                }
            }
        }

        return distanceFromStart;
    }

    public int getRisk(Data data, Pos start, BiFunction<Pos, int[][], Integer> fn) {
        return doDijkstra(data, start, fn).get(new Pos(data.sizex-1, data.sizey-1));
    }

    @Test
    public void testPart1() {
        assertEquals(40, getRisk(
                loadData("src/test/resources/2021/D15_t.txt"), new Pos(0,0),
                (p, m) -> m[p.x][p.y]
        ));
        assertEquals(393, getRisk(
                loadData("src/test/resources/2021/D15.txt"), new Pos(0,0),
                (p, m) -> m[p.x][p.y]
        ));
    }

    @Test
    public void testPart2() {
        BiFunction<Pos, int[][], Integer> costFunction = (p, m) -> {
            int xsize = m.length;
            int ysize = m[0].length;
            int add = (p.x/xsize) + (p.y/ysize);
            int v = m[(p.x%xsize)][(p.y%ysize)] + add;
            if(v > 9)
                v -= 9;
            return v;
        };

        assertEquals(315, getRisk(
                loadData("src/test/resources/2021/D15_t.txt", 5), new Pos(0,0), costFunction
        ));

        assertEquals(2823, getRisk(
                loadData("src/test/resources/2021/D15.txt", 5), new Pos(0,0), costFunction
        ));
    }

}
