package io.innocentdream;

import io.innocentdream.objects.GameObject;
import io.innocentdream.rendering.DisplayManager;
import io.innocentdream.utils.DiscordManager;
import io.innocentdream.utils.LibraryManager;
import io.innocentdream.utils.NetworkManager;
import io.innocentdream.utils.Timer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.awt.*;

public class InnocentDream implements Runnable {

    public static final String version = "LT-19102021";
    public static volatile boolean isRunning = false;

    public static final Logger logger = LogManager.getLogger("InnocentDream");
    public static DiscordManager discord;
    public static DisplayManager display;
    public static Timer timer;

    public static GameObject test = new GameObject(0, -1000, 10, 10, new Color(0x00FFAA)) {
        @Override
        public void update() {
            this.aABB.x += 1 * InnocentDream.timer.getTimeDifference();
        }
    };

    @Override
    public void run() {
        isRunning = true;
        display = DisplayManager.create();
        while (isRunning) {
            test.update();
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        logger.setLevel(Level.ALL);
        logger.info("Starting Innocent Dream version " + version + "...");
        NetworkManager.init();
        LibraryManager.lwjgl();
        discord = new DiscordManager();
        timer = new Timer();
        Thread thread = new Thread(new InnocentDream());
        thread.setName("MAIN");
        thread.start();
    }

}
