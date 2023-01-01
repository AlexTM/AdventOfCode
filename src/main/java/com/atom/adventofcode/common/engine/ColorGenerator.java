package com.atom.adventofcode.common.engine;

import org.joml.Vector3f;

public interface ColorGenerator {
    Vector3f getColor(int x, int y);
}
