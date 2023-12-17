package com.atom.adventofcode.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Helper class
 */
public abstract class FileReader {

    public static Integer[] readFileIntegerArray(String filename) {
        return readFileIntegerList(filename).toArray(new Integer[0]);
    }

    public static String readFileString(String filename) {
        return String.join("\n", readFileStringList(filename));
    }

    public static List<String> readFileStringList(String filename) {
        List<String> values = new ArrayList<>();
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                values.add(in.next());
            }
            in.close();
            return values;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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

    public static <T> T scanFileObject(String filename, Function<Scanner, T> function) {
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            return function.apply(in);
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

    public static <T>List<T> readObjectList(String contents, Function<String, T> function) {
        List<T> values = new ArrayList<>();
        try(Scanner in = new Scanner(contents)) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                String line = in.next();
                T obj = function.apply(line);
                if(obj != null)
                    values.add(obj);
            }
            in.close();
            return values;
        }
    }

    public static <R> R readFileForObject(String filename, R state, BiFunction<String, R, R> function) {
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            while (in.hasNext()) {
                state = function.apply(in.next(), state);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return state;
    }

    public interface TriFunction<T, U, I, R> {
        R apply(T a, U b, I c);
    }

    public static <R> R readFileForObject(String filename, R state, TriFunction<R, String, Integer, R> function) {
        try(Scanner in = new Scanner(new File(filename))) {
            in.useDelimiter("\n");
            int count = 0;
            while (in.hasNext()) {
                state = function.apply(state, in.next(), count++);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return state;
    }

    public static <R> R parseStringForObject(String content, R state, TriFunction<R, String, Integer, R> function) {
        try(Scanner in = new Scanner(content)) {
            in.useDelimiter("\n");
            int count = 0;
            while (in.hasNext()) {
                state = function.apply(state, in.next(), count++);
            }
        }
        return state;
    }
}
