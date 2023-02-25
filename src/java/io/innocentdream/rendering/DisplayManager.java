package io.innocentdream.rendering;

import io.innocentdream.InnocentDream;
import io.innocentdream.actions.Actions;
import io.innocentdream.objects.texts.CharacterManager;
import io.innocentdream.rendering.shaders.GUIShader;
import io.innocentdream.rendering.shaders.MainShader;
import io.innocentdream.screens.LoadScreen;
import io.innocentdream.screens.MainMenuScreen;
import io.innocentdream.screens.Screen;
import io.innocentdream.utils.GamePropertyManager;
import io.innocentdream.utils.Identifier;
import io.innocentdream.utils.Utils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Stack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL11.*;

public class DisplayManager implements Runnable {

    public static long win;
    public static int WIDTH;
    public static int HEIGHT;

    public ModelLoader loader;
    public Renderer renderer;
    public Stack<Screen> activeScreens = new Stack<>();
    public MainShader mainShader;
    public GUIShader guiShader;

    public static DisplayManager create() {
        DisplayManager display = new DisplayManager();
        display.activeScreens.add(new LoadScreen(MainMenuScreen::new));
        Thread thread = new Thread(display);
        thread.setName("RenderThread");
        thread.start();
        return display;
    }

    @SuppressWarnings("resource")
    @Override
    public void run() {
        if (!glfwInit()) {
            InnocentDream.logger.error("Failed to initialize GLFW");
            System.exit(1);
        }

        loader = new ModelLoader();
        renderer = new Renderer();

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);

        if (GamePropertyManager.getFullscreenProperty()) {
            glfwWindowHint(GLFW_RED_BITS, mode.redBits());
            glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
            glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
            glfwWindowHint(GLFW_REFRESH_RATE, mode.refreshRate());
            WIDTH = mode.width();
            HEIGHT = mode.height();
            win = glfwCreateWindow(WIDTH, HEIGHT, "Window", monitor, 0);
        } else {
            WIDTH = 1024;
            HEIGHT = 678;
            win = glfwCreateWindow(WIDTH, HEIGHT, "Window", 0, 0);
        }

        ByteBuffer icon16;
        ByteBuffer icon32;
        try {
            icon16 = Utils.inputStreamToByteBuffer(getClass().getClassLoader().getResourceAsStream("assets/gui/icon/icon-16.png"));
            icon32 = Utils.inputStreamToByteBuffer(getClass().getClassLoader().getResourceAsStream("assets/gui/icon/icon-32.png"));
        } catch (Exception e) {
            InnocentDream.logger.error("An error occurred in loading the window icon", e);
            System.exit(e.hashCode());
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

        glfwSetWindowSizeCallback(win, (win, width, height) -> {
            glViewport(0, 0, width, height);
            WIDTH = width;
            HEIGHT = height;
        });

        glfwMakeContextCurrent(win);
        GL.createCapabilities();
        glfwMakeContextCurrent(win);
        glfwShowWindow(win);
        mainShader = new MainShader();
        guiShader = new GUIShader();
        loop();
    }

    public void goFullscreen() {
        glfwMakeContextCurrent(win);
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        WIDTH = mode.width();
        HEIGHT = mode.height();
        glfwSetWindowMonitor(win, monitor, 0, 0, WIDTH, HEIGHT, mode.refreshRate());
    }

    public void goWindowed() {
        glfwMakeContextCurrent(win);
        long monitor = glfwGetWindowMonitor(win);
        GLFWVidMode mode = glfwGetVideoMode(monitor);
        double scale = GamePropertyManager.getScaleProperty();
        Dimension dim = new Dimension(mode.width(), mode.height());
        WIDTH = (int) (1024 * scale);
        HEIGHT = (int) (678 * scale);
        int x = dim.width / 2 - WIDTH / 2;
        int y = dim.height / 2 - HEIGHT / 2;
        glfwSetWindowMonitor(win, 0, x, y, WIDTH, HEIGHT, 1);
    }

    private void loop() {
        glfwMakeContextCurrent(win);
        glClearColor(0, 0, 0, 0);
        glClear(GL_COLOR_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        ResourceManager.init();
        CharacterManager.loadCharacters();
        while (!glfwWindowShouldClose(win)) {
            glfwMakeContextCurrent(win);

            activeScreens.lastElement().drawBackground();

            glfwPollEvents();

            renderer.prepare();
            mainShader.start();

            try {
                activeScreens.lastElement().drawScreen();
            } catch (Exception e) {
                InnocentDream.logger.error("Exception in drawing screen %s".formatted(activeScreens.lastElement().name), e);
            }
            Actions.pollActions(activeScreens.lastElement());

            mainShader.stop();
            guiShader.start();

            try {
                activeScreens.lastElement().drawGUI();
            } catch (Exception e) {
                InnocentDream.logger.error(
                        "Exception in drawing screen GUI %s".formatted(activeScreens.lastElement().name), e);
            }

            guiShader.stop();


            glfwSwapBuffers(win);

            InnocentDream.timer.updateTime();
        }
        cleanUp();
        InnocentDream.isRunning = false;
    }

    public static float[] getWindowMousePosition() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwMakeContextCurrent(DisplayManager.win);
        glfwGetCursorPos(DisplayManager.win, x, y);
        glfwGetWindowSize(win, width, height);
        int w = width.get(0);
        int h = height.get(0);
        float _x = ((float) x.get(0)) - w / 2f;
        float _y = ((float) y.get(0)) - h / 2f;
        return new float[] { _x, -_y };
    }

    public static boolean isMouseDown() {
        glfwMakeContextCurrent(win);
        return glfwGetMouseButton(win, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;
    }

    public void cleanUp() {
        InnocentDream.logger.debug("Unloading models");
        loader.cleanUp();
        InnocentDream.logger.debug("Done!");
        InnocentDream.logger.debug("Unloading textures");
        TextureHelper.cleanUp();
        InnocentDream.logger.debug("Done!");
    }

}
