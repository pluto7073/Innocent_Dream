package io.innocentdream.actions;

import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.screens.Screen;
import io.innocentdream.utils.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class Actions {

    private static final Map<Identifier, Action> ACTION_REGISTRY = new HashMap<>();

    public static final Action TOGGLE_FULLSCREEN;

    public static Action register(Identifier id, Action action) {
        ACTION_REGISTRY.put(id, action);
        return action;
    }

    private static Action register(String id, Action action) {
        return register(new Identifier("innocent_dream", id), action);
    }

    public static void pollActions(Screen currentScreen) {
        ACTION_REGISTRY.forEach((id, action) -> {
            if (GLFW.glfwGetKey(DisplayManager.win, action.getKey()) == GLFW.GLFW_TRUE) {
                if (!action.canUse(currentScreen)) {
                    return;
                }
                action.wasPressed = true;
                action.keyPressed();
            } else {
                if (action.wasPressed) {
                    action.keyReleased();
                }
                action.wasPressed = false;
            }
        });
    }

    static {
        TOGGLE_FULLSCREEN = register("key_fullscreen", new FullscreenAction());
    }

}
