package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class Renderer {

    public void prepare() {
        glfwMakeContextCurrent(DisplayManager.win);
        glClear(GL_COLOR_BUFFER_BIT);
        InnocentDream.display.loader.bindIndicesBuffer(new int[] { 0, 1, 3, 1, 2, 3 });
    }

    public void render(Model model) {
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_QUADS, 0, model.getVertexCount());
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void renderTexturedModel(Model model, int texID) {
        glfwMakeContextCurrent(DisplayManager.win);
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnable(GL_ALPHA);
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
        InnocentDream.display.loader.bindIndicesBuffer(new int[] { 0, 1, 3, 1, 2, 3 });
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisable(GL_DEPTH);
        glDisable(GL_BLEND);
        glDisable(GL_ALPHA);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void renderGuiObject(Model model, int texID) {
        glfwMakeContextCurrent(DisplayManager.win);
        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnable(GL_ALPHA);
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texID);
        InnocentDream.display.loader.bindIndicesBuffer(new int[] { 0, 1, 3, 1, 2, 3 });
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        glDisable(GL_DEPTH);
        glDisable(GL_BLEND);
        glDisable(GL_ALPHA);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

}
