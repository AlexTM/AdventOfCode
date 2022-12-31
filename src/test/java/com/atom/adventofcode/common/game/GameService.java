package com.atom.adventofcode.common.game;

import com.atom.adventofcode.common.engine.graph.Mesh;
import com.atom.adventofcode.common.engine.graph.ShaderProgram;
import com.atom.adventofcode.common.engine.scene.Scene;
import org.joml.Matrix4f;

public interface GameService {
    boolean update(Scene scene);
    Mesh getMesh();
    void cleanup();
}
