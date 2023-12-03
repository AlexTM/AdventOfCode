package com.atom.adventofcode.common.engine;

import com.atom.adventofcode.common.engine.graph.Render;
import com.atom.adventofcode.common.engine.scene.Scene;

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
        if(appLogic != null)
            appLogic.cleanup();
        if(render != null)
           render.cleanup();
        if(scene != null)
            scene.cleanup();
        if(window != null)
            window.cleanup();
    }

    private void resize() {
        // Nothing to be done yet
    }

    private void run(Supplier<Boolean> fn) {
        switch(opts.gui) {
            case NONE -> runWithoutGUI(fn);
            case GUI -> runWithGUI(fn);
            case WEB -> runWithWeb(fn);
        }
    }

    private void runWithoutGUI(Supplier<Boolean> fn) {
        while(!fn.get()) {
            appLogic.update(null, null, 0);
        }
    }

    public void start(Supplier<Boolean> endCondition) {
        running = true;
        run(endCondition);
    }

    private void runWithGUI(Supplier<Boolean> fn) {
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
                appLogic.update(window, scene, diffTimeMillis);
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

    private void initWeb() {
//        window = new Window(windowTitle, opts, () -> {
//            resize();
//            return null;
//        });
        targetFps = opts.fps;
        targetUps = opts.ups;
//        render = new Render();
        scene = new Scene();
        appLogic.init(window, scene, render);
        running = true;
    }


    private void runWithWeb(Supplier<Boolean> fn) {
        initWeb();

        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaFps = 0;

        long updateTime = initialTime;
        while (fn.get()) {
//            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaFps += (now - initialTime) / timeR;

//            if (targetFps <= 0 || deltaFps >= 1) {
//                appLogic.input(window, scene, now - initialTime);
//            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                appLogic.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaFps >= 1) {
                render.render(window, scene);
                deltaFps--;
//                window.update();
            }
            initialTime = now;

//            if() {
//                running = false;
//            }

        }

        cleanup();
    }

}
