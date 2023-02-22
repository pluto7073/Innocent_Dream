package io.innocentdream.utils;

import java.io.File;
import java.util.Properties;

public class GamePropertyManager {

    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
    }

    public static void start() {
        File settings = Utils.newFileInRunDir("game.properties");
    }

}
