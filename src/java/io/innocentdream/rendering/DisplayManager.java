package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;
import io.innocentdream.utils.Utils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;

public class DisplayManager implements Runnable {

    public static long win;
    public static int WIDTH;
    public static int HEIGHT;

    public ModelLoader loader;
    public Renderer renderer;

    public static DisplayManager create() {
        DisplayManager display = new DisplayManager();
        Thread thread = new Thread(display);
        thread.setName("RenderThread");
        thread.start();
        return display;
    }

    @Override
    public void run() {
        if (!glfwInit()) {
            InnocentDream.logger.error("Failed to initialize GLFW");
            System.exit(1);
        }

        loader = new ModelLoader();
        renderer = new Renderer();

        WIDTH = 1024;
        HEIGHT = 678;

        win = glfwCreateWindow(WIDTH, HEIGHT, "Window", 0, 0);

        ByteBuffer icon16;
        ByteBuffer icon32;
        try {
            icon16 = Utils.ioResourceToByteBuffer("icon-16.png", 2048);
            icon32 = Utils.ioResourceToByteBuffer("icon-32.png", 4096);
        } catch (Exception e) {
            InnocentDream.logger.error("An error occurred in loading the window icon", e);
            return;
        }
        IntBuffer w = MemoryUtil.memAllocInt(1);
        IntBuffer h = MemoryUtil.memAllocInt(1);
        IntBuffer comp = MemoryUtil.memAllocInt(1);
        try (GLFWImage.Buffer icons = GLFWImage.malloc(2)) {
            ByteBuffer pixels16 = stbi_load_from_memory(icon16, w, h, comp, 4);
            icons.position(0)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(pixels16);
            ByteBuffer pixels32 = stbi_load_from_memory(icon32, w, h, comp, 4);
            icons.position(1)
                    .width(w.get(0))
                    .height(h.get(0))
                    .pixels(pixels32);
            icons.position(0);
            glfwSetWindowIcon(win, icons);
            stbi_image_free(pixels32);
            stbi_image_free(pixels16);
        }

        glfwSetWindowTitle(win, "Innocent Dream - " + InnocentDream.version);
        glfwMakeContextCurrent(win);
        GL.createCapabilities();
        glfwMakeContextCurrent(win);
        glfwShowWindow(win);
        loop();
    }

    private void loop() {
        glfwMakeContextCurrent(win);
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        Model model = loader.loadToVAO(new float[] {
                -0.5f, -0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f, -0.5f
        });
        while (!glfwWindowShouldClose(win)) {
            glfwMakeContextCurrent(win);
            glfwPollEvents();

            renderer.prepare();
            renderer.render(model);

            glfwSwapBuffers(win);

            InnocentDream.timer.updateTime();
        }
        InnocentDream.isRunning = false;
    }

    public void cleanUp() {
        loader.cleanUp();
    }

}
