package io.innocentdream.actions;

import io.innocentdream.screens.Screen;
import io.innocentdream.utils.GamePropertyManager;

public abstract class Action {

    protected final String name;
    public boolean wasPressed;

    public Action(String name) {
        this.name = name;
        this.wasPressed = false;
    }

    public abstract int getKey();
    public abstract boolean canUse(Screen currentScreen);

    public String getName() {
        return name;
    }

    public abstract void keyPressed();
    public abstract void keyReleased();

}
