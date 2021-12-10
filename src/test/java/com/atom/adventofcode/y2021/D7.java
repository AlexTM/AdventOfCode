package com.atom.adventofcode.y2021;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *--- Day 7: The Treachery of Whales ---
 *
 * A giant whale has decided your submarine is its next meal, and it's much faster than you are. There's nowhere to run!
 *
 * Suddenly, a swarm of crabs (each in its own tiny submarine - it's too deep for them otherwise) zooms in to rescue
 * you! They seem to be preparing to blast a hole in the ocean floor; sensors indicate a massive underground cave
 * system just beyond where they're aiming!
 *
 * The crab submarines all need to be aligned before they'll have enough power to blast a large enough hole for your
 * submarine to get through. However, it doesn't look like they'll be aligned before the whale catches you! Maybe you
 * can help?
 *
 * There's one major catch - crab submarines can only move horizontally.
 *
 * You quickly make a list of the horizontal position of each crab (your puzzle input). Crab submarines have limited
 * fuel, so you need to find a way to make all of their horizontal positions match while requiring them to spend as
 * little fuel as possible.
 *
 * For example, consider the following horizontal positions:
 *
 * 16,1,2,0,4,2,7,1,2,14
 *
 * This means there's a crab with horizontal position 16, a crab with horizontal position 1, and so on.
 *
 * Each change of 1 step in horizontal position of a single crab costs 1 fuel. You could choose any horizontal position
 * to align them all on, but the one that costs the least fuel is horizontal position 2:
 *
 *     Move from 16 to 2: 14 fuel
 *     Move from 1 to 2: 1 fuel
 *     Move from 2 to 2: 0 fuel
 *     Move from 0 to 2: 2 fuel
 *     Move from 4 to 2: 2 fuel
 *     Move from 2 to 2: 0 fuel
 *     Move from 7 to 2: 5 fuel
 *     Move from 1 to 2: 1 fuel
 *     Move from 2 to 2: 0 fuel
 *     Move from 14 to 2: 12 fuel
 *
 * This costs a total of 37 fuel. This is the cheapest possible outcome; more expensive outcomes include aligning at
 * position 1 (41 fuel), position 3 (39 fuel), or position 10 (71 fuel).
 *
 * Determine the horizontal position that the crabs can align to using the least fuel possible. How much fuel must they
 * spend to align to that position?
 *
 * --- Part Two ---
 *
 * The crabs don't seem interested in your proposed solution. Perhaps you misunderstand crab engineering?
 *
 * As it turns out, crab submarine engines don't burn fuel at a constant rate. Instead, each change of 1 step in
 * horizontal position costs 1 more unit of fuel than the last: the first step costs 1, the second step costs 2, the
 * third step costs 3, and so on.
 *
 * As each crab moves, moving further becomes more expensive. This changes the best horizontal position to align them
 * all on; in the example above, this becomes 5:
 *
 *     Move from 16 to 5: 66 fuel
 *     Move from 1 to 5: 10 fuel
 *     Move from 2 to 5: 6 fuel
 *     Move from 0 to 5: 15 fuel
 *     Move from 4 to 5: 1 fuel
 *     Move from 2 to 5: 6 fuel
 *     Move from 7 to 5: 3 fuel
 *     Move from 1 to 5: 10 fuel
 *     Move from 2 to 5: 6 fuel
 *     Move from 14 to 5: 45 fuel
 *
 * This costs a total of 168 fuel. This is the new cheapest possible outcome; the old alignment position (2) now
 * costs 206 fuel instead.
 *
 * Determine the horizontal position that the crabs can align to using the least fuel possible so they can make you an
 * escape route! How much fuel must they spend to align to that position?
 *
 */
public class D7 {

    int[] testInput = new int[]{16,1,2,0,4,2,7,1,2,14};
    int[] puzzleInput = new int[]{1101,1,29,67,1102,0,1,65,1008,65,35,66,1005,66,28,1,67,65,20,4,0,1001,65,1,65,1106,0,8,99,35,67,101,99,105,32,110,39,101,115,116,32,112,97,115,32,117,110,101,32,105,110,116,99,111,100,101,32,112,114,111,103,114,97,109,10,160,1267,277,1068,422,1235,790,1391,45,252,513,1029,414,216,409,1373,1419,1176,757,64,748,835,20,436,147,347,1264,1532,240,272,430,7,85,51,12,107,1277,779,867,260,802,361,89,754,206,80,25,559,220,657,178,186,2,31,825,290,144,379,0,1682,1166,1241,180,102,464,444,122,718,25,100,1050,1358,604,546,1157,130,59,127,1351,238,97,75,821,265,23,786,116,115,93,730,1340,777,1114,263,352,115,5,69,1041,101,1222,203,1273,217,28,976,425,480,7,124,45,192,860,312,1107,1040,137,306,523,692,590,562,789,383,145,86,297,791,240,697,22,230,834,963,837,1164,1758,487,414,86,1026,1034,478,613,1,769,85,980,935,1455,16,204,170,380,324,14,699,220,50,451,738,52,437,963,718,178,508,711,1739,936,1515,246,908,126,602,295,591,22,484,752,1,1442,167,132,52,613,1172,353,36,56,468,123,393,765,1456,218,269,6,20,649,727,454,86,640,1113,836,124,405,571,882,107,75,730,346,94,35,626,1174,299,392,1449,502,854,500,128,852,248,645,159,774,155,884,1336,285,426,0,269,466,1483,93,13,17,255,295,530,694,1178,968,612,224,160,32,1154,194,494,24,845,43,274,344,301,486,43,351,581,929,168,1629,163,206,98,1242,1242,1706,1777,721,293,1621,132,199,12,66,247,1244,333,445,154,795,70,424,11,826,835,250,288,408,516,822,411,69,636,521,152,67,401,531,186,933,515,780,490,201,369,111,266,952,400,677,372,548,1325,1111,17,543,1293,20,507,74,116,656,644,872,35,80,1273,279,475,1585,1446,651,1338,285,284,23,1130,237,843,121,53,81,573,5,956,276,553,1084,544,731,35,16,53,34,405,1337,665,303,10,108,1132,233,3,834,415,161,409,1055,202,707,296,341,57,521,548,15,137,359,57,388,282,267,293,1450,28,424,819,941,1388,474,687,87,271,1462,522,33,26,841,345,104,150,573,481,297,1075,489,420,424,340,504,685,105,898,870,206,129,516,492,42,216,1829,1317,10,60,54,255,103,457,257,101,93,981,412,67,519,574,169,799,381,1509,60,409,51,151,464,1676,916,18,30,772,1566,1283,359,1260,10,405,750,160,181,541,358,213,300,1073,328,399,214,119,478,889,65,56,1077,1427,52,359,90,42,1248,336,51,1396,509,237,785,440,806,339,99,354,640,272,665,772,135,91,11,175,128,482,1244,1243,629,137,140,1003,626,433,391,731,1180,671,169,710,1561,385,1281,272,236,318,207,1323,16,233,9,720,295,34,183,362,987,1016,366,760,1244,878,600,275,1209,41,792,951,85,636,125,217,342,184,581,1300,66,165,804,285,756,96,278,598,163,655,138,869,537,141,1364,897,406,617,65,444,244,494,172,119,358,1183,310,226,98,550,634,948,985,247,1499,729,165,371,939,299,761,477,1480,840,3,319,675,492,564,3,3,80,182,69,460,341,789,742,46,1309,360,48,296,363,946,214,252,54,147,435,85,276,1072,23,71,755,572,268,1362,619,639,365,623,1560,322,535,997,1021,317,663,82,314,857,16,194,363,24,240,1596,1123,242,816,116,645,64,38,589,428,147,632,457,555,908,921,202,182,403,551,358,483,1195,1213,28,1156,725,320,16,74,931,103,145,146,1206,433,1052,158,531,699,675,379,393,475,384,1041,141,1248,521,136,326,199,725,200,465,796,724,672,569,70,663,15,150,131,1261,17,1211,66,175,608,17,81,551,627,1469,1032,342,2,972,184,798,960,22,55,462,1,151,91,119,76,1062,96,1424,567,366,831,633,205,691,50,1314,732,558,167,1624,5,147,47,110,250,935,177,445,79,306,653,47,75,626,173,104,354,573,523,46,46,757,541,431,1129,787,502,1328,1093,82,872,1876,1386,136,504,273,194,297,0,163,1025,996,354,1457,1127,52,45,1364,1128,457,1576,282,573,1648,16,28,582,768,92,92,817,1515,297,349,97,1523,634,923,76,1174,552,347,750,326,221,149,0,188,791,251,113,1,71,92,393,103,618,335,97,236,418,256,764,435,411,941,74,423,443,27,427,178,262,181,362,156,572,324,684,796,249,288,413,132,29,444,766,1135,1235,208,231,620,1481,228,174,133,918,1825,618,663,22,124,119,52,159,1318,1724,338,243,206,127,436,163,297,617,141,59,65,20,164,11,126,363,150,726,217,1282,1708,118,1055,60,603,852,170,1097,58,213,495,566,673,1607,994,539,1655};

    public int calculateCost(final int[] input, final int target, final Function<Integer, Integer> costFunction) {
        return Arrays.stream(input)
                .reduce(0, (cost, i) -> cost + costFunction.apply(Math.abs(target - i)));
    }

    public int findBest(final int[] inputs, final Function<Integer, Integer> costFunction) {
        int max = Arrays.stream(inputs).max().getAsInt();
        int min = Arrays.stream(inputs).min().getAsInt();

        // Must be a more efficient way than this, binary search?
        return IntStream.range(min, max)
                .reduce(Integer.MAX_VALUE, (bestCost, i) -> Math.min(bestCost, calculateCost(inputs, i, costFunction)));
    }

    @Test
    public void testPart1() {
        assertEquals(37, findBest(testInput, Function.identity()));
        assertEquals(344138, findBest(puzzleInput, Function.identity()));
    }

    @Test
    public void testPart2() {
        Function<Integer, Integer> costFunction = target -> IntStream.range(1, target+1).sum();
        assertEquals(168, findBest(testInput, costFunction));
        assertEquals(94862124, findBest(puzzleInput, costFunction));
    }

}