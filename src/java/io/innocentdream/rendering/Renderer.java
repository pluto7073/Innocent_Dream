package io.innocentdream.rendering;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class Renderer {

    public void prepare() {
        glfwMakeContextCurrent(DisplayManager.win);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void render(Model model) {
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_QUADS, 0, model.getVertexCount());
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

}
