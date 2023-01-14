package com.atom.adventofcode.common.game;

import com.atom.adventofcode.common.engine.ColorGenerator;
import com.atom.adventofcode.common.engine.graph.Mesh;
import org.joml.Vector3f;

/**
 * Simple grid
 */
public class PlaneGeneratorSimple {

    private final int xSize, ySize;
    private final int x1,y1;
    private int[] idx = null;
    private float[] shapes = null;
    private final ColorGenerator colorGenerator;
    private final float xDelta, yDelta;

    public record Pos(int x, int y){}
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
        return (x) + ((y) * (xSize + 1));
    }

    public PlaneGeneratorSimple(int x1, int y1, int x2, int y2, ColorGenerator colorGenerator) {
        this.x1 = x1;
        this.y1 = y1;
        this.xSize = x2-x1;
        this.ySize = y2-y1;
        this.colorGenerator = colorGenerator;
        this.xDelta = 1f/xSize;
        this.yDelta = 1f/ySize;
    }

    private int getNumberOfVertex() {
        return (ySize+1)*(xSize+1);
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

    protected int[] generateIndices() {
        if(idx != null)
            return idx;

        idx = new int[xSize * ySize * 6];
        int c = 0;

        for(int y=0; y<ySize; y++) {
            for(int x=0; x<xSize; x++) {
                SquareIdx vm = getMapping(x, y);
                vm.getTriangleA().copy(idx, c);
                vm.getTriangleB().copy(idx, c+3);
                c = c + 6;
            }
        }
        return idx;
    }

    protected float[] generateShape() {
        if(shapes != null)
            return shapes;

        shapes = new float[getNumberOfVertex()*3];

        for(int y=0; y<=ySize; y++) {
            for(int x=0; x<=xSize; x++) {
                int i = coordsToIndex(x, y);
                shapes[i*3] = (float)x * xDelta;
                shapes[(i*3)+1] = (float)y * yDelta;
                shapes[(i*3)+2] = 0f;
            }
        }
        return shapes;
    }

    protected float[] generateColors() {
        float[] colors = new float[getNumberOfVertex()*3];

        for(int y=0; y<=ySize; y++) {
            for(int x=0; x<=xSize; x++) {
                int i = coordsToIndex(x, y);
                Vector3f vec = colorGenerator.getColor(x-x1, y-y1);
                colors[i*3] = vec.x;
                colors[(i*3)+1] = vec.y;
                colors[(i*3)+2] = vec.z;
            }
        }
        return colors;
    }
}
