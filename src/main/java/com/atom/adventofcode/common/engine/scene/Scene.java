package com.atom.adventofcode.common.engine.scene;

import com.atom.adventofcode.common.engine.graph.Mesh;

import java.util.HashMap;
import java.util.Map;

public class Scene {

    private Map<String, Mesh> meshMap;

    public Scene() {
        meshMap = new HashMap<>();
    }

    public void addMesh(String meshId, Mesh mesh) {
        if(meshMap.containsKey(meshId)) {
            meshMap.get(meshId).cleanup();
        }
        meshMap.put(meshId, mesh);
    }

    public void cleanup() {
        meshMap.values().forEach(Mesh::cleanup);
    }

    public Map<String, Mesh> getMeshMap() {
        return meshMap;
    }
}
