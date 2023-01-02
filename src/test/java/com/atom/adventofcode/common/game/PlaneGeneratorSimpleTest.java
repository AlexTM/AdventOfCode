package com.atom.adventofcode.common.game;

import com.atom.adventofcode.common.engine.ColorGenerator;
import com.atom.adventofcode.common.engine.graph.Mesh;
import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class PlaneGeneratorSimpleTest {

    @Test
    public void testSomething() {
//        PlaneGeneratorSimple pgs1 = new PlaneGeneratorSimple(
//                -1, -1, 1, 1, (x, y) -> new Vector3f(0.1f, 0.1f, 0.1f));
//        int[] indx1 = pgs1.generateIndices();

        PlaneGeneratorSimple pgs2 = new PlaneGeneratorSimple(
                0, 0, 1, 1, (x, y) -> new Vector3f(0.1f, 0.1f, 0.1f));
        int[] indx2 = pgs2.generateIndices();

//        Arrays.stream(indx1).forEach(i -> System.out.print(i+", "));
//        System.out.println("");
        Arrays.stream(indx2).forEach(i -> System.out.print(i+", "));
        System.out.println("");

//        assertArrayEquals(indx1, indx2);

//        float[] shape1 = pgs1.generateShape();
        float[] shape2 = pgs2.generateShape();

//        IntStream.range(0, shape1.length)
//                .mapToDouble(i -> shape1[i]).forEach(i -> System.out.print(i+", "));
//        System.out.println("");
        IntStream.range(0, shape2.length)
                .mapToDouble(i -> shape2[i]).forEach(i -> System.out.print(i+", "));
        System.out.println("");

//        assertArrayEquals(shape1, shape2);

//        Arrays.stream(shape1).forEach(i -> System.out.print(i+", "));
//        System.out.println("");
    }
}
