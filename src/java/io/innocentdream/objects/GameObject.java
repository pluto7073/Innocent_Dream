package io.innocentdream.objects;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.rendering.Model;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class GameObject {

    public final AABB aABB;
    private Color colour;
    protected Model model;
    protected float[] texCoords;

    public GameObject(int x, int y, int w, int h, Color colour) {
        this.aABB = new AABB(x, y, w, h);
        this.colour = colour;
        texCoords = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };
    }

    public GameObject(int x, int y, int w, int h) {
        this(x, y, w, h, new Color(0));
    }

    public Color getColour() { return colour; }
    public void setColour(Color colour) { this.colour = colour; }

    public abstract void update();

    private void prepareModel() {
        if (model == null) {
            model = InnocentDream.display.loader.loadToVAO(new float[] {aABB.x, aABB.y,
                aABB.x, aABB.y + aABB.h,
                aABB.x + aABB.w, aABB.y + aABB.h,
                aABB.x + aABB.w, aABB.y});
        }
    }

    public void draw() {
        prepareModel();

        InnocentDream.display.renderer.render(model);
    }

}
