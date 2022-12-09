package com.atom.adventofcode.y2022;

import com.atom.adventofcode.common.FileReader;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * FIXME Too ugly to fix
 */
public class D8 {

    class State {
        int r = 0;
        List<List<Integer>> d = new ArrayList<>();

        public int[][] getData() {
            int[][] data = new int[d.get(0).size()][d.size()];

            for(int c=0; c<d.get(0).size(); c++) {
                for (int r = 0; r < d.size(); r++) {
                    data[r][c] = d.get(r).get(c);
                }
            }
            return data;
        }
    }

    private static State readContent(String line, State s) {
        s.d.add(new ArrayList<>());
        for(char c : line.toCharArray()) {
            s.d.get(s.r).add(c-'0');
        }
        s.r++;
        return s;
    }

    private int checkVisibility(int[][] heights) {
        int rows = heights[0].length;
        int cols = heights.length;
        boolean[][] vis = new boolean[rows][cols];
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                vis[r][c] = false;
            }
        }

        for (int c = 0; c < cols; c++) {
            int max = -1;
            for (int r = 0; r < rows; r++) {
                if (heights[r][c] > max) {
                    vis[r][c] = true;
                    max = heights[r][c];
                }
            }

            max = -1;
            for (int r = rows - 1; r >= 0; r--) {
                if (heights[r][c] > max) {
                    vis[r][c] = true;
                    max = heights[r][c];
                }
            }
        }

        for(int r=0; r<rows; r++) {
            int max = -1;
            for (int c = 0; c < cols; c++) {
                if(heights[r][c] > max) {
                    vis[r][c] = true;
                    max = heights[r][c];
                }
            }

            max = -1;
            for (int c = cols-1; c >= 0; c--) {
                if(heights[r][c] > max) {
                    vis[r][c] = true;
                    max = heights[r][c];
                }
            }
        }

        int count = 0;
        for(int c=0; c<cols; c++) {
            for (int r = 0; r < rows; r++) {
                if(vis[r][c])
                    count++;
            }
        }

        return count;
    }

    private int calScore(int[][] heights, int c, int r) {
        int rows = heights[0].length;
        int cols = heights.length;
        int h = heights[r][c];

        int s1 = 0;
        for(int x = r+1; x<rows; x++) {
            s1++;
            if(heights[x][c] >= h) {
                break;
            }
        }

        int s2=0;
        for(int x = r-1; x>=0; x--) {
            s2++;
            if(heights[x][c] >= h) {
                break;
            }
        }

        int s3=0;
        for(int y = c+1; y<cols; y++) {
            s3++;
            if(heights[r][y] >= h) {
                break;
            }
        }

        int s4=0;
        for(int y = c-1; y>=0; y--) {
            s4++;
            if(heights[r][y] >= h) {
                break;
            }
        }
        return s1*s2*s3*s4;
    }

    private int scenicScore(int[][] heights) {
        int rows = heights[0].length;
        int cols = heights.length;

        int max = 0;
        for(int c=0; c<cols; c++) {
            for (int r = 0; r < rows; r++) {
                max = Math.max(max, calScore(heights, c, r));
            }
        }
        return max;
    }


    @Test
    public void testTree() {
        State state2 =
                FileReader.readFileForObject("src/test/resources/2022/D8.txt", new State(), D8::readContent);

        assertEquals(1782, checkVisibility(state2.getData()));
        assertEquals(474606, scenicScore(state2.getData()));
    }
}
