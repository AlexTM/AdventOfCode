package com.atom.adventofcode.common.game;

import com.atom.adventofcode.common.engine.ColorGenerator;
import org.joml.Vector3f;

public class GridColorGenerator implements ColorGenerator {
    @Override
    public Vector3f getColor(int x, int y) {
        if(x%2 == y%2)
            return new Vector3f(0.6f, 0.6f, 0.6f);
        return new Vector3f(1f, 1f, 1f);
    }
}
