package io.innocentdream.screens;

import io.innocentdream.objects.GUIObject;
import io.innocentdream.rendering.DisplayManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public abstract class Screen {

    public final String name;
    public int[] backgroundColor;
    private final ArrayList<GUIObject> drawables;

    public Screen(String name, int[] backgroundColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.drawables = new ArrayList<>();
        this.addGUIObjects();
    }

    public abstract void drawScreen();
    public void drawGUI() {
        for (GUIObject drawable : this.drawables) {
            drawable.draw();
        }
    }
    public void tick() {
        for (GUIObject drawable : drawables) {
            drawable.update();
        }
    }
    public abstract void addGUIObjects();

    protected void addDrawableChild(GUIObject object) {
        this.drawables.add(object);
    }

    public void drawBackground() {
        GLFW.glfwMakeContextCurrent(DisplayManager.win);
        GL11.glClearColor(backgroundColor[0] / 255F, backgroundColor[1] / 255f, backgroundColor[2] / 255f, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

}
