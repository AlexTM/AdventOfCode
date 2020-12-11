package com.atom.advent2020;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
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

    private void print(char[][] inp) {
        for(int i=0; i<inp.length; i++) {
            for(int j=0; j<inp[0].length; j++) {
                System.out.print(inp[i][j]);
            }
            System.out.println("");
        }
        System.out.println("");
    }

    @Test
    public void test() throws FileNotFoundException {
        char[][]  inp = readFile("src/test/resources/day11_test.txt");
        inp = engine(inp);
        assertEquals(37, countSeats(inp));

        inp = readFile("src/test/resources/day11.txt");
        inp = engine(inp);
        System.out.println("Result "+countSeats(inp));

    }


    private int countNeighbours(char[][] inp, int i, int j) {
        int count = 0;
        for(int a = i -1; a<=i+1; a++) {
            for(int b = j-1; b<=j+1; b++) {

                // checkout bounds
                if(a<0)
                    continue;
                if(a>=inp.length)
                    continue;
                if(b<0)
                    continue;
                if(b>=inp[0].length)
                    continue;

                if(a == i && b == j)
                    continue;

                if(inp[a][b] == '#') {
                    count++;
                }
            }
        }
        return count;
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

    private char[][] engine(char[][] inp) {
        boolean change = true;
        int loops = 0;
        while(change) {

            char[][] nextRound = new char[inp.length][inp[0].length];


            // Iterate and make changes
            change = false;
            for(int i=0; i<inp.length; i++) {
                for(int j=0; j<inp[0].length; j++) {
//                    System.out.print(inp[i][j]);
                    switch(inp[i][j]) {
                        case '#':
                            nextRound[i][j] = inp[i][j];
                            if(countNeighbours(inp, i, j) >= 4) {
                                nextRound[i][j] = 'L';
                                change = true;
                            }

                            break;
                        case 'L':
                            nextRound[i][j] = inp[i][j];
                            if(countNeighbours(inp, i, j) == 0) {
                                nextRound[i][j] = '#';
                                change = true;
                            }
                            break;
                        default:
                            nextRound[i][j] = inp[i][j];
                            break;
                    }
                }
            }

            inp = nextRound;

            print(inp);
            loops++;
//            if(loops == 2)
//                break;
        }
        return inp;
    }


    @Test
    public void test2() throws FileNotFoundException {
        char[][]  inp = readFile("src/test/resources/day11_test.txt");
        inp = engine2(inp, 5);
        assertEquals(26, countSeats(inp));

        inp = readFile("src/test/resources/day11.txt");
        inp = engine2(inp, 5);
        System.out.println("Result "+countSeats(inp));
        assertEquals(2002, countSeats(inp));
    }

    // fixme merge with engine, parse in neighbour count function
    private char[][] engine2(char[][] inp, int tolerance) {
        boolean change = true;
        while(change) {

            char[][] nextRound = new char[inp.length][inp[0].length];
            // Iterate and make changes
            change = false;
            for(int i=0; i<inp.length; i++) {
                for(int j=0; j<inp[0].length; j++) {
                    switch(inp[i][j]) {
                        case '#':
                            nextRound[i][j] = inp[i][j];
                            if(countNeighbours2(inp, i, j) >= tolerance) {
                                nextRound[i][j] = 'L';
                                change = true;
                            }

                            break;
                        case 'L':
                            nextRound[i][j] = inp[i][j];
                            if(countNeighbours2(inp, i, j) == 0) {
                                nextRound[i][j] = '#';
                                change = true;
                            }
                            break;
                        default:
                            nextRound[i][j] = inp[i][j];
                            break;
                    }
                }
            }

            inp = nextRound;

        }
        return inp;
    }


    // fixme use lambdas to simplify code
    private int countNeighbours2(char[][] inp, int i, int j) {
        int tmpi, tmpj;
        int count = 0;

        tmpi = i-1;
        tmpj = j;
        while(tmpi>=0) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpi--;
        }

        tmpi = i+1;
        tmpj = j;
        while(tmpi<inp.length) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpi++;
        }


        tmpi = i;
        tmpj = j-1;
        while(tmpj>=0) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpj--;
        }

        tmpi = i;
        tmpj = j+1;
        while(tmpj<inp[0].length) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpj++;
        }

        // diaagonal

        tmpi = i-1;
        tmpj = j-1;
        while(tmpi >= 0 && tmpj >= 0) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpi--;
            tmpj--;
        }

        tmpi = i+1;
        tmpj = j+1;
        while(tmpi < inp.length && tmpj < inp[0].length) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpi++;
            tmpj++;
        }

        tmpi = i-1;
        tmpj = j+1;
        while(tmpi >= 0 && tmpj < inp[0].length) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpi--;
            tmpj++;
        }

        tmpi = i+1;
        tmpj = j-1;
        while(tmpi < inp.length && tmpj >= 0) {
            if(inp[tmpi][tmpj] == 'L')
                break;
            if(inp[tmpi][tmpj] == '#') {
                count++;
                break;
            }
            tmpi++;
            tmpj--;
        }


        return count;
    }

}
