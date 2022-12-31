package com.atom.adventofcode.common.game;

import com.atom.adventofcode.common.engine.ColorGenerator;
import com.atom.adventofcode.common.engine.graph.Mesh;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class PlaneGeneratorSimple {

    private int xsize;
    private int zsize;
    private int[] idx = null;
    private float[] shapes = null;
    private final ColorGenerator colorGenerator;

    public record Pos(float xpos, float zpos){};
    record Triangle(int p1, int p2, int p3){
        public void copy(int[] indexes, int p) {
            indexes[p++] = p1;
            indexes[p++] = p2;
            indexes[p] = p3;
        }
    }
    record SquareIdx(int topLeft, int topRight, int bottomLeft, int bottomRight){
        public Triangle getTriangleA() {
            return new Triangle(topLeft, bottomLeft, bottomRight);
        }
        public Triangle getTriangleB() {
            return new Triangle(topLeft, bottomRight, topRight);
        }
    }

    private final BiFunction<Integer, Integer, Integer> coordsToIndex = (x, z) -> x + (z * (xsize+1));
    private final Function<Integer, Pos> indexToCoords = new Function<>() {
        @Override
        public Pos apply(Integer c) {
            int z = c / (xsize + 1);
            int x = c % (xsize + 1);
            return new Pos(x, z);
        }
    };

    public PlaneGeneratorSimple(int xsize, int zsize, ColorGenerator colorGenerator) {
        this.xsize = xsize;
        this.zsize = zsize;
        this.colorGenerator = colorGenerator;
    }

    private Vector3f getVector(Pos p) {
        return new Vector3f(p.xpos/xsize, p.zpos/zsize, -0f);
    }

    private int getNumberOfVertex() {
        return (zsize+1)*(xsize+1);
    }

    public Mesh createMesh() {

        int[] indices = generateIndices();
        float[] shape = generateShape();
        float[] colours = generateColors();

        return new Mesh(
                shape,
                colours,
                indices);
    }

    private SquareIdx getMapping(int x, int z) {
        int topLeft = coordsToIndex.apply(x, z);
        int topRight = topLeft + 1;
        int bottomLeft = coordsToIndex.apply(x, z+1);
        int bottomRight = bottomLeft + 1;

        return new SquareIdx(
                topLeft, topRight, bottomLeft, bottomRight
        );
    }

    private int[] generateIndices() {
        if(idx != null)
            return idx;

        idx = new int[xsize*zsize*6];
        int c = 0;
        for(int j=0; j<zsize; j++) {
            for(int i=0; i<xsize; i++) {
                SquareIdx vm = getMapping(i, j);
                vm.getTriangleA().copy(idx, c);
                vm.getTriangleB().copy(idx, c+3);
                c = c + 6;
            }
        }
        return idx;
    }

    private float[] generateShape() {
        if(shapes != null)
            return shapes;

        shapes = new float[getNumberOfVertex()*3];
        for(int i=0; i<shapes.length/3; i++) {
            Vector3f vec = getVector(indexToCoords.apply(i));
            shapes[i*3] = vec.x;
            shapes[(i*3)+1] = vec.y;
            shapes[(i*3)+2] = vec.z;
        }
        return shapes;
    }

    private float[] generateColors() {
        float[] colors = new float[getNumberOfVertex()*3];

        IntStream.range(0, colors.length/3).forEach(i -> {
            Vector3f vec = colorGenerator.getColor(indexToCoords.apply(i));
            colors[i*3] = vec.x;
            colors[(i*3)+1] = vec.y;
            colors[(i*3)+2] = vec.z;
        });

        return colors;
    }
}
