package com.atom.adventofcode.y2020;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Before you leave, the Elves in accounting just need you to fix your expense report (your puzzle input); apparently,
 * something isn't quite adding up.
 *
 * Specifically, they need you to find the two entries that sum to 2020 and then multiply those two numbers together.
 *
 * For example, suppose your expense report contained the following:
 *
 * 1721
 * 979
 * 366
 * 299
 * 675
 * 1456
 * In this list, the two entries that sum to 2020 are 1721 and 299. Multiplying them together produces
 * 1721 * 299 = 514579, so the correct answer is 514579.
 *
 *
 * --- Part Two ---
 * The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they had left over
 * from a past vacation. They offer you a second one if you can find three numbers in your expense report that meet the
 * same criteria.
 *
 * Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them together
 * produces the answer, 241861950.
 *
 * In your expense report, what is the product of the three entries that sum to 2020?
 *
 */
public class D1 {

    private static Integer values[] = new Integer[] {
            1721,
            979,
            366,
            299,
            675,
            1456
    };

    @Test
    public void testDayOnePart1() {
        assertEquals(514579, findTwoValuesIn(values, 2020));

        System.out.println("Result: "+
                findTwoValuesIn(FileReader.readFileIntegerArray("src/test/resources/2020/D1.txt"), 2020));
    }

    @Test
    public void testDayOnePart2() {
        assertEquals(241861950, findThreeValuesIn(values, 2020));

        System.out.println("Result: "+
                findThreeValuesIn(FileReader.readFileIntegerArray("src/test/resources/2020/D1.txt"), 2020));
    }

    private int findTwoValuesIn(Integer[] values, int target) {
        Arrays.sort(values);
        int i=0, j=values.length-1;
        while(i < j) {
            int sum = values[i] + values[j];
            if(sum == target) {
                return values[i]*values[j];
            }
            if(sum > target) {
                j--;
            } else {
                i++;
            }
        }
        return -1;
    }

    private int findThreeValuesIn(Integer[] values, int target) {
        Arrays.sort(values);
        for (int k = 0; k < values.length - 2; k++) {
            int i = 1, j = values.length - 1;
            while (i < j) {
                int sum = values[i] + values[j] + values[k];
                if (sum == target) {
                    return values[i] * values[j] * values[k];
                }
                if (sum > target) {
                    j--;
                } else {
                    i++;
                }
            }
        }
        return -1;
    }

}
