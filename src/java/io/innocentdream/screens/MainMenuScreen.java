package io.innocentdream.screens;

import io.innocentdream.objects.button.Button;
import io.innocentdream.objects.texts.Text;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.utils.Identifier;
import org.lwjgl.glfw.GLFW;

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Identifier source) {
        super("Main Menu", new int[] { 0, 0, 0, 0 });
    }

    @Override
    public void drawScreen() {

    }

    @Override
    public void addGUIObjects() {
        this.addDrawableChild(new Button(-256, 32, 512, 128, Text.translatable("button.play")).addOnClickListener(button -> {
            //TODO play screen
        }));
        this.addDrawableChild(new Button(-256, -160, 512, 128, Text.translatable("button.quit")).addOnClickListener(button -> {
            GLFW.glfwMakeContextCurrent(DisplayManager.win);
            GLFW.glfwSetWindowShouldClose(DisplayManager.win, true);
        }));
    }

}
