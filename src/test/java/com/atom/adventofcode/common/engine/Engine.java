package com.atom.adventofcode.common.engine;

import com.atom.adventofcode.common.engine.graph.Render;
import com.atom.adventofcode.common.engine.scene.Scene;

import java.util.function.Function;
import java.util.function.Supplier;

public class Engine {

    public static final int TARGET_UPS = 30;
    private final IAppLogic appLogic;
    private Window window;
    private Render render;
    private boolean running;
    private Scene scene;
    private int targetFps;
    private int targetUps;
    private Window.WindowOptions opts;
    private String windowTitle;

    public Engine(String windowTitle, Window.WindowOptions opts, IAppLogic appLogic) {
        this.appLogic = appLogic;
        this.opts = opts;
        this.windowTitle = windowTitle;
    }

    private void init() {
        window = new Window(windowTitle, opts, () -> {
            resize();
            return null;
        });
        targetFps = opts.fps;
        targetUps = opts.ups;
        render = new Render();
        scene = new Scene();
        appLogic.init(window, scene, render);
        running = true;
    }

    private void cleanup() {
        appLogic.cleanup();
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void resize() {
        // Nothing to be done yet
    }

    private void run() {
        if(opts.gui)
            runWithGUI();
        else
            runWithoutGUI();
    }

    private long runWithoutGUI() {
        long updates = 0;
        while(!appLogic.update(null, null, 0))
            updates++;
        return updates;
    }

    private void runWithGUI() {
        init();

        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                if(appLogic.update(window, scene, diffTimeMillis)) {
                    running = false;
                }
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }
            initialTime = now;
        }

        cleanup();
    }

    public void start() {
        running = true;
        run();
    }

    public void start(Supplier<Boolean> fn) {
        running = true;
        runWithGUITmp(fn);
    }


    public void stop() {
        running = false;
    }


    private void runWithGUITmp(Supplier<Boolean> fn) {
        init();

        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaFps >= 1) {
                appLogic.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                // FIXME use endCondition instead
                if(appLogic.update(window, scene, diffTimeMillis)) {
                    running = false;
                }
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
                window.update();
            }
            initialTime = now;

            if(fn.get()) {
                running = false;
            }

        }

        cleanup();
    }

}
