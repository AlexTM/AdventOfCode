package com.atom.adventofcode.common.engine.graph;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int numVertices;
    private int vaoId;
    private List<Integer> vboIdList;

    public Mesh(float[] positions, float[] colours, int[] indices) {
//        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.numVertices = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Positions VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            //FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
            FloatBuffer positionsBuffer = MemoryUtil.memAllocFloat(positions.length);
            positionsBuffer.put(0, positions);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Colours VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            //FloatBuffer coloursBuffer = stack.callocFloat(colours.length);
            FloatBuffer coloursBuffer = MemoryUtil.memAllocFloat(positions.length);
            coloursBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, coloursBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            //IntBuffer indicesBuffer = stack.callocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

            MemoryUtil.memFree(indicesBuffer);
            MemoryUtil.memFree(coloursBuffer);
            MemoryUtil.memFree(positionsBuffer);
//        }
    }

    public void cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public final int getVaoId() {
        return vaoId;
    }
}
