package io.innocentdream.screens;

import io.innocentdream.rendering.DisplayManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public abstract class Screen {

    public final String name;
    public int[] backgroundColor;

    public Screen(String name, int[] backgroundColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
    }

    public abstract void drawScreen();
    public abstract void drawGUI();
    public abstract void tick();

    public void drawBackground() {
        GLFW.glfwMakeContextCurrent(DisplayManager.win);
        GL11.glClearColor(backgroundColor[0] / 255F, backgroundColor[1] / 255f, backgroundColor[2] / 255f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

}
