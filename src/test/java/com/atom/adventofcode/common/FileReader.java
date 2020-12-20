package com.atom.adventofcode.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Helper class
 */
public abstract class FileReader {

    public static Integer[] readFileIntegerArray(String filename) {
        return readFileIntegerList(filename).toArray(new Integer[0]);
    }

    public static List<Integer> readFileIntegerList(String filename) {
        List<Integer> values = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            while (in.hasNext()) {
                values.add(Integer.parseInt(in.next()));
            }
            in.close();
            return values;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>List<T> scanFileObjectList(String filename, Function<Scanner, T> function) {
        List<T> values = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                T obj = function.apply(in);
                if(obj != null)
                    values.add(obj);
            }
            in.close();
            return values;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>List<T> readFileObjectList(String filename, Function<String, T> function) {
        List<T> values = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                String line = in.next();
                T obj = function.apply(line);
                if(obj != null)
                    values.add(obj);
            }
            in.close();
            return values;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
