package com.atom.adventofcode.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class GraphicsController {
    private static final Logger logger = LoggerFactory.getLogger(GraphicsController.class);

    record GraphicsData(int[] vertices, int[] colors, int[] indices) {};

    @GetMapping("/stats")
    public GraphicsData getStats() {

        var vertices = new int[]{
                -1,-1,-1, 1,-1,-1, 1, 1,-1, -1, 1,-1,
                -1,-1, 1, 1,-1, 1, 1, 1, 1, -1, 1, 1,
                -1,-1,-1, -1, 1,-1, -1, 1, 1, -1,-1, 1,
                1,-1,-1, 1, 1,-1, 1, 1, 1, 1,-1, 1,
                -1,-1,-1, -1,-1, 1, 1,-1, 1, 1,-1,-1,
                -1, 1,-1, -1, 1, 1, 1, 1, 1, 1, 1,-1};

        var colors = new int[]{
                5, 3, 7, 5, 3, 7, 5, 3, 7, 5, 3, 7,
                1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3,
                0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
                1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
                1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0,
                0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0
        };

        var indices = new int[]{
                0, 1, 2, 0, 2, 3, 4, 5, 6, 4, 6, 7,
                8, 9, 10, 8, 10, 11, 12, 13, 14, 12, 14, 15,
                16, 17, 18, 16, 18, 19, 20, 21, 22, 20, 22, 23
        };

        return new GraphicsData(vertices, colors, indices);
    }

}
