package io.innocentdream.screens;

import io.innocentdream.objects.button.Button;
import io.innocentdream.objects.texts.Text;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.utils.Identifier;
import org.lwjgl.glfw.GLFW;

public class MainMenuScreen extends Screen {

    private final Button playBtn;
    private final Button quitBtn;

    public MainMenuScreen(Identifier source) {
        super("Main Menu", new int[] { 0, 0, 0, 0 });
        this.playBtn = new Button(-150, -40, 5.0F, new Text("Play")).addOnClickListener(button -> {
            //TODO play screen
        });
        this.quitBtn = new Button(-150, 60, 5.0F, new Text("Quit")).addOnClickListener(button -> {
            GLFW.glfwMakeContextCurrent(DisplayManager.win);
            GLFW.glfwSetWindowShouldClose(DisplayManager.win, true);
        });
    }

    @Override
    public void drawScreen() {

    }

    @Override
    public void drawGUI() {
        this.playBtn.update();
        this.quitBtn.update();
        this.playBtn.draw();
        this.quitBtn.draw();
    }

    @Override
    public void tick() {

    }

}
