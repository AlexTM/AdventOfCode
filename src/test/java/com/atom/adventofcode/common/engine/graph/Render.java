package com.atom.adventofcode.common.engine.graph;

import com.atom.adventofcode.common.engine.Window;
import com.atom.adventofcode.common.engine.scene.Scene;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL32;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private SceneRender sceneRender;

    public Render() {
        GL.createCapabilities();
        GL32.glProvokingVertex(GL32.GL_FIRST_VERTEX_CONVENTION);
        sceneRender = new SceneRender();
    }

    public void cleanup() {
        sceneRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        sceneRender.render(scene);
    }
}