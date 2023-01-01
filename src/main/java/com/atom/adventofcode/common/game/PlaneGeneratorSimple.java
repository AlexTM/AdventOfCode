package com.atom.adventofcode.common.game;

import com.atom.adventofcode.common.engine.ColorGenerator;
import com.atom.adventofcode.common.engine.graph.Mesh;
import org.joml.Vector3f;

import java.util.stream.IntStream;

public class PlaneGeneratorSimple {

    private final int xSize, ySize;
    private final int x1,y1,x2,y2;
    private int[] idx = null;
    private float[] shapes = null;
    private final ColorGenerator colorGenerator;

    public record Pos(int x, int y){};
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

    private int coordsToIndex(int x, int y) {
        return (x - x1) + ((y - y1) * (xSize + 1));
    }

    private Pos indexToPos(int c) {
        return new Pos(x1 + c % (xSize + 1), y1 + c / (xSize + 1));
    }

    public PlaneGeneratorSimple(int x1, int y1, int x2, int y2, ColorGenerator colorGenerator) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.xSize = x2-x1;
        this.ySize = y2-y1;
        this.colorGenerator = colorGenerator;
    }

    private int getNumberOfVertex() {
        return (ySize +1)*(xSize +1);
    }

    public Mesh createMesh() {
        return new Mesh(generateShape(), generateColors(), generateIndices());
    }

    private SquareIdx getMapping(int x, int z) {
        int topLeft = coordsToIndex(x, z);
        int topRight = topLeft + 1;
        int bottomLeft = coordsToIndex(x, z+1);
        int bottomRight = bottomLeft + 1;
        return new SquareIdx(topLeft, topRight, bottomLeft, bottomRight);
    }

    private int[] generateIndices() {
        if(idx != null)
            return idx;

        idx = new int[xSize * ySize * 6];
        int c = 0;
        for(int j=y1; j<y2; j++) {
            for(int i=x1; i<x2; i++) {
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
            Pos p = indexToPos(i);
            shapes[i*3] = (float)p.x/xSize - 0.1f;
            shapes[(i*3)+1] = (float)p.y/ySize - 0.1f;
            shapes[(i*3)+2] = 0f;
        }
        return shapes;
    }

    private float[] generateColors() {
        float[] colors = new float[getNumberOfVertex()*3];

        // FIXME this is unnecessarily complicated
        IntStream.range(0, getNumberOfVertex()).forEach(i -> {
            Pos p = indexToPos(i);
            Vector3f vec = colorGenerator.getColor(p.x, ySize - p.y);
            colors[i*3] = vec.x;
            colors[(i*3)+1] = vec.y;
            colors[(i*3)+2] = vec.z;
        });

        return colors;
    }
}
