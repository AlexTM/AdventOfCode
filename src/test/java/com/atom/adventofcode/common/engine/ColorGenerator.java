package com.atom.adventofcode.common.engine;

import com.atom.adventofcode.common.game.PlaneGeneratorSimple;
import org.joml.Vector3f;

public interface ColorGenerator {
    Vector3f getColor(PlaneGeneratorSimple.Pos pos);
}
