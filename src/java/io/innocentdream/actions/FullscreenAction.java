package io.innocentdream.actions;

import io.innocentdream.InnocentDream;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.screens.Screen;
import io.innocentdream.utils.GamePropertyManager;

public class FullscreenAction extends Action {

    private boolean pressed = false;

    public FullscreenAction() {
        super("key_fullscreen");
    }

    @Override
    public int getKey() {
        return GamePropertyManager.getFullscreenKey();
    }

    @Override
    public boolean canUse(Screen currentScreen) {
        return true;
    }

    @Override
    public void keyPressed() {
        if (!pressed) {
            pressed = true;
            boolean fullscreen = GamePropertyManager.getFullscreenProperty();
            if (fullscreen) {
                InnocentDream.display.goWindowed();
            } else {
                InnocentDream.display.goFullscreen();
            }
            GamePropertyManager.saveProperty("fullscreen", String.valueOf(!fullscreen));
        }
    }

    @Override
    public void keyReleased() {
        pressed = false;
    }
}
