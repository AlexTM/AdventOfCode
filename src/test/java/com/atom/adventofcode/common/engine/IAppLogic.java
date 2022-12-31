package com.atom.adventofcode.common.engine;

import com.atom.adventofcode.common.engine.graph.Render;
import com.atom.adventofcode.common.engine.scene.Scene;

public interface IAppLogic {
    void cleanup();
    void init(Window window, Scene scene, Render render);
    void input(Window window, Scene scene, long diffTimeMillis);
    boolean update(Window window, Scene scene, long diffTimeMillis);
}